package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Truy vấn tài khoản. Các truy vấn trả User tải Person trong cùng truy vấn để
 * tránh phát sinh thêm một truy vấn cho mỗi tài khoản khi đọc thông tin cá nhân.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = "person")
    Optional<User> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "person")
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = "person")
    Page<User> findAll(Pageable pageable);

    /**
     * Tìm tài khoản đăng nhập và nạp hồ sơ cá nhân liên kết trong cùng truy vấn.
     *
     * Tham số username: username đã được chuẩn hóa
     * Kết quả: tài khoản nếu tồn tại
     */
    @EntityGraph(attributePaths = "person")
    Optional<User> findByUsername(String username);

    /**
     * Liệt kê tài khoản đang hoạt động theo trang, kèm hồ sơ cá nhân.
     *
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang tài khoản đang hoạt động
     */
    @EntityGraph(attributePaths = "person")
    Page<User> findByActiveTrue(Pageable pageable);
}
