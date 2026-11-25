package com.bluemoon.bfms.service;

import com.bluemoon.bfms.repository.ApartmentActorRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kiểm tra quyền thao tác căn hộ theo BE-02, US-001 và NFR-02.
 *
 * <p>ID tài khoản lấy từ principal đã xác thực ở phía server; trạng thái và vai trò
 * được kiểm tra lại trong database cho từng request.</p>
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
     * @param userId ID lấy từ principal phía server
     * @return ID tài khoản để Service nghiệp vụ ghi vào audit
     * @throws AccessDeniedException nếu tài khoản không tồn tại, không hoạt động hoặc không có quyền
     */
    // Giao việc lọc trạng thái/vai trò cho Repository; nếu không có kết quả thì từ chối ngay tại server.
    @Transactional(readOnly = true)
    public Long requireEditorId(Long userId) {
        return actorRepository.findActiveEditorId(userId)
                .orElseThrow(() -> new AccessDeniedException("Apartment editor access is required."));
    }

    /** Tìm ID tài khoản Admin đang hoạt động cho Controller quản trị tài khoản. */
    @Transactional(readOnly = true)
    public Long requireAdminId(Long userId) {
        return actorRepository.findActiveAdminId(userId)
                .orElseThrow(() -> new AccessDeniedException("Administrator access is required."));
    }

    /** Tìm tài khoản đang hoạt động cho thao tác đổi mật khẩu cá nhân. */
    @Transactional(readOnly = true)
    public Long requireActiveAccountId(Long userId) {
        return actorRepository.findActiveAccountId(userId)
                .orElseThrow(() -> new AccessDeniedException("An active account is required."));
    }
}
