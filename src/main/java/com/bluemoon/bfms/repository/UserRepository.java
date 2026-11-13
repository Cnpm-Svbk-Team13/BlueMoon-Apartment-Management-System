package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
