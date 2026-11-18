package com.bluemoon.bfms.service;

import com.bluemoon.bfms.model.AuditLog;
import com.bluemoon.bfms.model.Person;
import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.HouseholdWriteLock;
import com.bluemoon.bfms.repository.PersonRepository;
import com.bluemoon.bfms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Thực hiện nghiệp vụ quản trị tài khoản theo US-SEC-01 / NFR-02.
 *
 * <p>Service là lớp điều phối giữa User, Person và các Repository; việc tạo/cập nhật
 * tài khoản, hồ sơ liên kết và audit được đặt trong cùng transaction.</p>
 */
@Service
public class UserService {
    private final UserRepository users;
    private final PersonRepository persons;
    private final AuditLogRepository audits;
    private final HouseholdWriteLock writeLock;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor Spring sử dụng; bộ mã hóa BCrypt có cấu hình 10 rounds theo AC.
     */
    @Autowired
    public UserService(
            UserRepository users, PersonRepository persons, AuditLogRepository audits,
            HouseholdWriteLock writeLock) {
        this(users, persons, audits, writeLock, new BCryptPasswordEncoder(10));
    }

    /** Constructor package-private để test có thể thay PasswordEncoder bằng bản giả lập. */
    UserService(
            UserRepository users, PersonRepository persons, AuditLogRepository audits,
            HouseholdWriteLock writeLock, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.persons = persons;
        this.audits = audits;
        this.writeLock = writeLock;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Tạo hồ sơ Person và tài khoản User liên kết như một đơn vị nghiệp vụ.
     * Nếu bất kỳ thao tác lưu nào thất bại, transaction rollback để không để lại
     * hồ sơ hoặc tài khoản được tạo dở dang.
     *
     * @param username tên đăng nhập; Service trim và chuyển thành chữ thường
     * @param fullName họ tên người gắn với tài khoản
     * @param email email người gắn với tài khoản
     * @param phone số điện thoại gồm đúng 10 chữ số
     * @param role vai trò hệ thống cần cấp
     * @param initialPassword mật khẩu khởi tạo dạng thô, được BCrypt băm trước khi lưu
     * @param actorId ID quản trị viên lấy từ principal phía server
     * @param ipAddress địa chỉ IP client tùy chọn
     * @return bản tóm tắt tài khoản, không chứa password hash
     * @throws IllegalArgumentException nếu dữ liệu trùng/không hợp lệ hoặc người thực hiện không phải quản trị viên đang hoạt động
     */
    // @Transactional tạo ranh giới BEGIN/COMMIT; rollbackFor bảo đảm exception cũng rollback như lỗi runtime.
    @Transactional(rollbackFor = Exception.class)
    public UserSummary createUser(
            String username, String fullName, String email, String phone, UserRole role,
            String initialPassword, Long actorId, String ipAddress) {
        AccountDetails details = validateDetails(username, fullName, email, phone, role);
        validatePassword(initialPassword);
        validateIpAddress(ipAddress);
        // Tuân cùng thứ tự advisory lock trước khi đọc actor/kiểm tra trùng và ghi Person/User.
        writeLock.acquire();
        User actor = requireAdministrator(actorId);
        if (users.existsByUsername(details.username())) {
            throw new IllegalArgumentException("Username is already in use.");
        }
        if (users.existsByPerson_EmailIgnoreCase(details.email())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        // Person là hồ sơ dùng chung của hệ thống; User giữ khóa ngoại tới hồ sơ này thay vì lặp thông tin.
        Person person = persons.save(new Person(
                details.fullName(), null, details.phone(), details.email()));
        // Chỉ lưu hash BCrypt, tuyệt đối không gán mật khẩu thô vào Entity hoặc audit.
        User user = users.save(new User(
                details.username(), passwordEncoder.encode(initialPassword), person, details.role()));
        // Audit cùng transaction với dữ liệu tài khoản để không có thay đổi nghiệp vụ thiếu lịch sử.
        audits.save(new AuditLog(actor, "CREATE_USER", "User", user.getId(), null, ipAddress));
        return toSummary(user);
    }

    /**
     * Cập nhật username, thông tin liên hệ của Person, vai trò và trạng thái tài khoản.
     *
     * @param userId ID tài khoản cần cập nhật
     * @param username username mới; được chuẩn hóa trước khi kiểm tra duy nhất
     * @param fullName họ tên mới trên hồ sơ cá nhân liên kết
     * @param email email mới trên hồ sơ cá nhân liên kết
     * @param phone số điện thoại mới gồm đúng 10 chữ số
     * @param role vai trò mới
     * @param active true nếu cho phép tài khoản đăng nhập
     * @param actorId ID Admin lấy từ principal phía server
     * @param ipAddress địa chỉ IP client tùy chọn
     * @return bản tóm tắt sau cập nhật, không chứa password hash
     * @throws IllegalArgumentException nếu dữ liệu trùng/không hợp lệ hoặc người thực hiện/tài khoản đích không hợp lệ
     */
    @Transactional(rollbackFor = Exception.class)
    public UserSummary updateUser(
            Long userId, String username, String fullName, String email, String phone,
            UserRole role, boolean active, Long actorId, String ipAddress) {
        AccountDetails details = validateDetails(username, fullName, email, phone, role);
        validateIpAddress(ipAddress);
        writeLock.acquire();
        User actor = requireAdministrator(actorId);
        User user = findUser(userId);
        if (!details.username().equals(user.getUsername()) && users.existsByUsername(details.username())) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        // Vì Person dùng chung giữa các chức năng, cập nhật hồ sơ này cũng cập nhật nguồn dữ liệu cá nhân;
        // CCCD, ngày sinh và giới tính được giữ nguyên vì thao tác quản trị tài khoản không sửa các trường đó.
        Person person = user.getPerson();
        if (!details.email().equalsIgnoreCase(person.getEmail())
                && users.existsByPerson_EmailIgnoreCase(details.email())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        person.updatePersonalDetails(details.fullName(), person.getNationalId(), details.phone(),
                details.email(), person.getDateOfBirth(), person.getGender());
        user.updateAccount(details.username(), details.role(), active);
        audits.save(new AuditLog(actor, "UPDATE_USER", "User", user.getId(), null, ipAddress));
        return toSummary(user);
    }

    /**
     * Kích hoạt hoặc vô hiệu hóa tài khoản mà không thay đổi vai trò/hồ sơ cá nhân.
     *
     * @param userId ID tài khoản cần đổi trạng thái
     * @param active trạng thái mới; false sẽ chặn đăng nhập
     * @param actorId ID Admin lấy từ principal phía server
     * @param ipAddress địa chỉ IP client tùy chọn
     * @return bản tóm tắt trạng thái mới
     * @throws IllegalArgumentException nếu người thực hiện hoặc tài khoản đích không hợp lệ
     */
    @Transactional(rollbackFor = Exception.class)
    public UserSummary toggleUserStatus(Long userId, boolean active, Long actorId, String ipAddress) {
        validateIpAddress(ipAddress);
        writeLock.acquire();
        User actor = requireAdministrator(actorId);
        User user = findUser(userId);
        user.updateAccount(user.getUsername(), user.getRole(), active);
        audits.save(new AuditLog(actor, "TOGGLE_USER_STATUS", "User", user.getId(), null, ipAddress));
        return toSummary(user);
    }

    /**
     * Trả danh sách tài khoản cho Admin đang hoạt động; không bao giờ trả password hash.
     *
     * @param actorId ID Admin lấy từ principal phía server
     * @return danh sách theo ID tăng dần
     * @throws IllegalArgumentException nếu người thực hiện không phải quản trị viên đang hoạt động
     */
    @Transactional(readOnly = true)
    public List<UserSummary> getAllUsers(Long actorId) {
        requireAdministrator(actorId);
        return users.findAllByOrderByIdAsc().stream()
                .map(this::toSummary)
                .toList();
    }

    /**
     * Cấp lại mật khẩu, lưu hash BCrypt mới và xóa trạng thái khóa đăng nhập cũ.
     *
     * @param userId ID tài khoản cần cấp lại mật khẩu
     * @param newPassword mật khẩu mới dạng thô; được kiểm tra và băm trước khi lưu
     * @param actorId ID Admin lấy từ principal phía server
     * @param ipAddress địa chỉ IP client tùy chọn
     * @throws IllegalArgumentException nếu người thực hiện, tài khoản đích hoặc mật khẩu không hợp lệ
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String newPassword, Long actorId, String ipAddress) {
        validatePassword(newPassword);
        validateIpAddress(ipAddress);
        writeLock.acquire();
        User actor = requireAdministrator(actorId);
        User user = findUser(userId);
        user.changePasswordHash(passwordEncoder.encode(newPassword));
        // Mật khẩu mới thay thế thông tin xác thực cũ nên xóa cả bộ đếm và thời điểm khóa.
        user.resetLoginFailureState();
        audits.save(new AuditLog(actor, "RESET_USER_PASSWORD", "User", user.getId(), null, ipAddress));
    }

    private User requireAdministrator(Long actorId) {
        if (actorId == null || actorId <= 0) {
            throw new IllegalArgumentException("An authenticated administrator is required.");
        }
        User actor = users.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException("Administrator account does not exist."));
        if (!actor.isActive() || actor.getRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("Only an active administrator may manage accounts.");
        }
        // Kiểm tra quyền tại Service; Controller vẫn phải bảo vệ route và lấy ID người thực hiện từ phiên.
        return actor;
    }

    private User findUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("A valid account ID is required.");
        }
        return users.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account does not exist."));
    }

    private AccountDetails validateDetails(
            String username, String fullName, String email, String phone, UserRole role) {
        if (username == null || username.isBlank() || username.strip().length() > 50) {
            throw new IllegalArgumentException("Username must contain between 1 and 50 characters.");
        }
        if (fullName == null || fullName.isBlank() || fullName.strip().length() > 100) {
            throw new IllegalArgumentException("Full name must contain between 1 and 100 characters.");
        }
        if (email == null || email.isBlank() || email.strip().length() > 255
                || !email.strip().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        if (phone == null || !phone.matches("[0-9]{10}")) {
            throw new IllegalArgumentException("Phone number must contain exactly 10 digits.");
        }
        if (role == null) {
            throw new IllegalArgumentException("A user role is required.");
        }
        // Chuẩn hóa trước mọi truy vấn để khớp quy ước username/email lowercase trong schema.
        return new AccountDetails(
                username.strip().toLowerCase(Locale.ROOT),
                fullName.strip(),
                email.strip().toLowerCase(Locale.ROOT),
                phone,
                role);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            // BCrypt giới hạn đầu vào theo byte, không phải số ký tự Unicode.
            throw new IllegalArgumentException("Password exceeds the BCrypt input limit.");
        }
    }

    private void validateIpAddress(String ipAddress) {
        if (ipAddress != null && ipAddress.length() > 50) {
            throw new IllegalArgumentException("Client IP address must contain at most 50 characters.");
        }
    }

    private UserSummary toSummary(User user) {
        Person person = user.getPerson();
        return new UserSummary(user.getId(), user.getUsername(), person.getFullName(),
                person.getEmail(), person.getPhone(), user.getRole(), user.isActive());
    }

    private record AccountDetails(String username, String fullName, String email, String phone, UserRole role) {
    }

    /** Read model dành cho quản trị; cố ý không có trường passwordHash. */
    public record UserSummary(
            Long id, String username, String fullName, String email, String phone, UserRole role, boolean active) {
    }
}
