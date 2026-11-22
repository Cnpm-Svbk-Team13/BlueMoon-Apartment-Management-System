package com.bluemoon.bfms.service;

import com.bluemoon.bfms.repository.ApartmentActorRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kiểm tra quyền thao tác căn hộ theo BE-02, US-001 và NFR-02.
 *
 * <p>Username phải lấy từ principal đã xác thực ở phía server; không dùng username
 * do trình duyệt tự gửi để xác định tài khoản thực hiện thao tác.</p>
 */
@Service
public class ApartmentAccessService {
    private final ApartmentActorRepository actorRepository;

    /** Spring tiêm Repository có truy vấn kiểm tra tài khoản và vai trò được phép. */
    public ApartmentAccessService(ApartmentActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    /**
     * Tìm ID tài khoản đang hoạt động được phép sửa thông tin căn hộ.
     *
     * @param username username lấy từ principal phía server
     * @return ID tài khoản để Service nghiệp vụ ghi vào audit
     * @throws AccessDeniedException nếu tài khoản không tồn tại, không hoạt động hoặc không có quyền
     */
    // Giao việc lọc trạng thái/vai trò cho Repository; nếu không có kết quả thì từ chối ngay tại server.
    @Transactional(readOnly = true)
    public Long requireEditorId(String username) {
        return actorRepository.findActiveEditorId(username)
                .orElseThrow(() -> new AccessDeniedException("Apartment editor access is required."));
    }

    /** Tìm ID tài khoản Admin đang hoạt động cho Controller quản trị tài khoản. */
    @Transactional(readOnly = true)
    public Long requireAdminId(String username) {
        return actorRepository.findActiveAdminId(username)
                .orElseThrow(() -> new AccessDeniedException("Administrator access is required."));
    }

    /** Tìm tài khoản đang hoạt động cho thao tác đổi mật khẩu cá nhân. */
    @Transactional(readOnly = true)
    public Long requireActiveAccountId(String username) {
        return actorRepository.findActiveAccountId(username)
                .orElseThrow(() -> new AccessDeniedException("An active account is required."));
    }
}
