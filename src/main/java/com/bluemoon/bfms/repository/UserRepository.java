package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * Cung cấp truy vấn lưu trữ tài khoản dựa trên Spring Data JPA.
 *
 * <p>Ví dụ {@code findByUsername(username)} được JPA chuyển thành truy vấn
 * tương đương {@code SELECT ... FROM users WHERE username = ?}. Tìm email
 * đi qua quan hệ hồ sơ cá nhân, tương đương JOIN {@code users.person_id}
 * với {@code persons.id}; không sao chép email vào bảng tài khoản.</p>
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Tìm tài khoản theo tên đăng nhập đã được Service chuẩn hóa.
     *
     * @param username tên đăng nhập
     * @return tài khoản nếu tồn tại
     */
    Optional<User> findByUsername(String username);

    /**
     * Tìm và khóa bản ghi tài khoản tới khi transaction kết thúc để các lần
     * đăng nhập sai đồng thời không ghi đè bộ đếm của nhau.
     *
     * @param username tên đăng nhập đã chuẩn hóa
     * @return tài khoản nếu tồn tại
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from User account where account.username = :username")
    Optional<User> findByUsernameForUpdate(@Param("username") String username);

    /**
     * Tải danh sách tài khoản kèm hồ sơ cá nhân trong cùng truy vấn để tránh
     * phát sinh một truy vấn bổ sung cho mỗi hồ sơ khi tạo read model.
     *
     * @return tài khoản sắp xếp theo ID tăng dần
     */
    @EntityGraph(attributePaths = "person")
    List<User> findAllByOrderByIdAsc();

    /**
     * Kiểm tra tên đăng nhập đã được sử dụng hay chưa.
     *
     * @param username tên đăng nhập cần kiểm tra
     * @return true nếu đã có bản ghi khớp
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email hồ sơ có gắn với một tài khoản hay chưa, không phân biệt
     * chữ hoa/thường; database vẫn là nơi bảo đảm tính duy nhất khi ghi đồng thời.
     *
     * @param email email cần kiểm tra
     * @return true nếu có tài khoản liên kết với email
     */
    boolean existsByPerson_EmailIgnoreCase(String email);

    /**
     * Kiểm tra một hồ sơ cá nhân đã được gắn với tài khoản khác hay chưa.
     *
     * @param personId khóa chính hồ sơ cá nhân
     * @return true nếu đã có tài khoản tham chiếu hồ sơ đó
     */
    boolean existsByPerson_Id(Long personId);
}
