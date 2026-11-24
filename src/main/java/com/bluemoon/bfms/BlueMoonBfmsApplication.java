package com.bluemoon.bfms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Điểm khởi chạy ứng dụng Spring Boot BlueMoon BFMS.
 *
 * <p>Đặt lớp này ở package gốc để component scan mặc định tìm thấy Controller,
 * Service, Repository và cấu hình trong các package con.</p>
 */
@SpringBootApplication
public class BlueMoonBfmsApplication {

    /**
     * Khởi tạo Spring Application Context và chạy máy chủ web.
     *
     * @param args tham số dòng lệnh truyền cho Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(BlueMoonBfmsApplication.class, args);
    }
}
