package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Truy vấn hồ sơ cá nhân.
 *
 * <p>Spring Data JPA sinh SQL tương đương từ tên phương thức, ví dụ
 * findByNationalId tạo truy vấn theo cột national_id.</p>
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Tìm hồ sơ bằng CCCD, không trả nhiều kết quả vì CCCD là duy nhất trong schema.
     *
     * Tham số nationalId: CCCD cần tìm
     * Kết quả: hồ sơ nếu tồn tại
     */
    Optional<Person> findByNationalId(String nationalId);
}
