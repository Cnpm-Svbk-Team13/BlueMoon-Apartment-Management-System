/**
 * Các repository truy cập dữ liệu PostgreSQL thông qua Spring Data JPA
 * và một số adapter JDBC phục vụ thao tác khóa đặc thù của schema.
 *
 * <p>Truy vấn và ghi dữ liệu được đóng gói tại đây; quy tắc nghiệp vụ vẫn thuộc
 * Service để giữ đúng luồng Controller → Service → Repository → Entity.</p>
 */
package com.bluemoon.bfms.repository;
