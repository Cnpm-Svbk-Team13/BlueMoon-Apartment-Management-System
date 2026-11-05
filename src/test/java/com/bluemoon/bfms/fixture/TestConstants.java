package com.bluemoon.bfms.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Hằng số khớp 1-1 với database/seed_data.sql (Sprint 1).
 * KhÔNG import entity/enum
 * Enum được biểu diễn bằng String đúng như giá trị lưu trong DB.
 */
public final class TestConstants {
    private TestConstants() {
    }

    // ---------- Tài khoản demo ----------
    public static final String DEMO_PASSWORD = "Password@123";

    public static final String ADMIN = "admin";
    public static final String ACCOUNTANT = "ketoan";
    public static final String CASHIER = "thuquy";
    public static final String INACTIVE = "bqt_inactive"; // is_active = FALSE

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_ACCOUNTANT = "ROLE_ACCOUNTANT";
    public static final String ROLE_CASHIER = "ROLE_CASHIER";

    public static final String ADMIN_EMAIL = "admin@example.test";
    public static final String INACTIVE_EMAIL = "inactive@example.test";

    /** username → role, dùng cho test @ParameterizedTest phân quyền. */
    public static final List<String[]> ACTIVE_ACCOUNTS = List.of(
            new String[] { ADMIN, ROLE_ADMIN },
            new String[] { ACCOUNTANT, ROLE_ACCOUNTANT },
            new String[] { CASHIER, ROLE_CASHIER });

    // ---------- Người (national_id) ----------
    public static final String NID_AN = "000000000001"; // đại diện cũ P.601
    public static final String NID_LAN = "000000000002"; // đại diện hiện tại P.601
    public static final String NID_BINH = "000000000003"; // chủ hộ P.602
    public static final String NID_DAT = "000000000004"; // tạm trú P.602
    public static final String NID_CUONG = "000000000005"; // chủ hộ P.1204
    public static final String NID_DUC = "000000000006"; // tạm vắng P.1204

    public static final String NAME_AN = "Nguyễn An Mẫu";
    public static final String NAME_LAN = "Trần Lan Mẫu";
    public static final String NAME_BINH = "Lê Bình Mẫu";
    public static final String NAME_CHILD_NO_ID = "Bé Mẫu Không CCCD"; // national_id, phone NULL
    public static final String NAME_DAT = "Phạm Đạt Mẫu";
    public static final String NAME_CUONG = "Hoàng Cường Mẫu";
    public static final String NAME_DUC = "Hoàng Đức Mẫu";

    // ---------- Căn hộ ----------
    public static final String ROOM_KIOT = "K.101";
    public static final String ROOM_COMMERCIAL_VACANT = "TM.201";
    public static final String ROOM_HEAD_CHANGED = "P.601"; // có lịch sử thay đại diện
    public static final String ROOM_TEMP_RESIDENT = "P.602"; // có người tạm trú
    public static final String ROOM_NO_HOUSEHOLD = "P.701"; // VACANT, chưa khai báo hộ
    public static final String ROOM_TEMP_ABSENT = "P.1204"; // có người tạm vắng
    public static final String ROOM_PENTHOUSE = "PH.3001";

    public static final String TYPE_KIOT = "KIOT";
    public static final String TYPE_COMMERCIAL = "COMMERCIAL";
    public static final String TYPE_RESIDENTIAL = "RESIDENTIAL";
    public static final String TYPE_PENTHOUSE = "PENTHOUSE";

    public static final String APT_VACANT = "VACANT";
    public static final String APT_OCCUPIED = "OCCUPIED";

    // ---------- Hộ khẩu ----------
    public static final String REG_P601 = "HK-BM-00601";
    public static final String REG_P602 = "HK-BM-00602";
    public static final String REG_P1204 = "HK-BM-01204";

    // ---------- Cư trú ----------
    public static final String REL_HEAD = "HEAD";
    public static final String REL_CHILD = "CHILD";
    public static final String REL_TENANT = "TENANT";

    public static final String RES_THUONG_TRU = "THUONG_TRU";
    public static final String RES_TAM_TRU = "TAM_TRU";
    public static final String RES_TAM_VANG = "TAM_VANG";

    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_FEMALE = "FEMALE";

    // ---------- Số dòng sau khi seed (kiểm tra seed chạy đúng) ----------
    public static final int COUNT_PERSONS = 11; // 4 staff + 7 shared
    public static final int COUNT_USERS = 4;
    public static final int COUNT_APARTMENTS = 7;
    public static final int COUNT_HOUSEHOLDS = 3;
    public static final int COUNT_RESIDENTS = 6;
    public static final int COUNT_HEAD_HISTORY = 4;
    public static final int COUNT_AUDIT_LOGS = 4;
    public static final int COUNT_OCCUPIED_APARTMENTS = 3;
    public static final int COUNT_VACANT_APARTMENTS = 4;

    // ---------- Giá trị biên (dùng lại cho Bảng quyết định Ngày 4) ----------
    public static final int FLOOR_MIN = 1;
    public static final int FLOOR_MAX = 30;
    public static final int FLOOR_BELOW_MIN = FLOOR_MIN - 1; // 0 → invalid
    public static final int FLOOR_ABOVE_MAX = FLOOR_MAX + 1; // 31 → invalid

    public static final BigDecimal AREA_ZERO = BigDecimal.ZERO; // invalid (> 0)
    public static final BigDecimal AREA_NEGATIVE = new BigDecimal("-1.00"); // invalid
    public static final BigDecimal AREA_SMALLEST_VALID = new BigDecimal("0.01");
    public static final BigDecimal AREA_TYPICAL = new BigDecimal("75.50"); // = P.601 / P.701

    /** Mảng biên tầng: {floor, hợp lệ?} cho @MethodSource / @CsvSource. */
    public static final int[][] FLOOR_BOUNDARY_CASES = {
            { FLOOR_BELOW_MIN, 0 }, { FLOOR_MIN, 1 }, { 2, 1 },
            { 29, 1 }, { FLOOR_MAX, 1 }, { FLOOR_ABOVE_MAX, 0 }
    };

    // ---------- Sinh giá trị duy nhất, không đụng seed ----------
    private static final AtomicLong SEQ = new AtomicLong(1);

    /** CCCD 12 số, tiền tố 9 để không trùng seed (000000000xxx). */
    public static String uniqueNationalId() {
        return String.format("9%011d", SEQ.getAndIncrement());
    }

    /** SĐT 10 số, tiền tố 09 + 8 số, không trùng seed (0900000xxx). */
    public static String uniquePhone() {
        return String.format("098%07d", SEQ.getAndIncrement());
    }

    public static String uniqueEmail() {
        return "t" + SEQ.getAndIncrement() + "@example.test";
    }

    public static String uniqueRoom() {
        return "T." + SEQ.getAndIncrement();
    }

    public static String uniqueRegistrationCode() {
        return String.format("HK-T-%05d", SEQ.getAndIncrement());
    }

    // ---------- Theo schema.sql ----------
    public static final String ROLE_RESIDENT = "ROLE_RESIDENT";
    public static final String GENDER_OTHER = "OTHER";

    /** BCrypt cost 10 của "Password@123", cùng hash với seed_data.sql. */
    public static final String DEMO_PASSWORD_HASH = "$2a$10$ljvESH1dmbST9gRG0Gr1tOKvvvsfcVe3FbO4nV8jFqPsgrxNYXVXu";

    public static final String HH_ACTIVE = "ACTIVE";
    public static final String HH_MOVED_OUT = "MOVED_OUT";
    public static final String HH_TRANSFERRED = "TRANSFERRED";

    public static final String REL_SPOUSE = "SPOUSE";
    public static final String REL_PARENT = "PARENT";
    public static final String REL_OTHER = "OTHER";

    // Biên diện tích theo DECIMAL(12,2)
    public static final BigDecimal AREA_MAX_VALID = new BigDecimal("9999999999.99");
    public static final BigDecimal AREA_OVERFLOW = new BigDecimal("10000000000.00"); // 22003
    public static final BigDecimal AREA_ROUNDS_TO_ZERO = new BigDecimal("0.004"); // → 0.00, vi phạm CHECK

    /** SQLSTATE dùng để assert đúng loại lỗi, không chỉ "có lỗi". */
    public static final class SqlState {
        private SqlState() {
        }

        public static final String CHECK_VIOLATION = "23514";
        public static final String UNIQUE_VIOLATION = "23505";
        public static final String FK_VIOLATION = "23503";
        public static final String NUMERIC_OUT_OF_RANGE = "22003";
        public static final String WRONG_TX_ISOLATION = "25000";
        public static final String INSUFFICIENT_PRIVILEGE = "42501";
    }

    /** username phải lowercase, không khoảng trắng (CHECK trong schema). */
    public static String uniqueUsername() {
        return "u" + SEQ.getAndIncrement();
    }

    /**
     * JSON snapshot đúng cấu trúc is_valid_head_snapshot(): email null → JSON null.
     */
    public static String snapshotJson(String fullName, String nationalId, String phone, String email) {
        return "{\"full_name\":" + jsonString(fullName)
                + ",\"national_id\":" + jsonString(nationalId)
                + ",\"phone\":" + jsonString(phone)
                + ",\"email\":" + jsonString(email) + "}";
    }

    private static String jsonString(String s) {
        if (s == null)
            return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}