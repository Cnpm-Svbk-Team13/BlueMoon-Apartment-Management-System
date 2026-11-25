document.addEventListener("DOMContentLoaded", () => {
    // 1. Flash message auto-hide
    const flashMessages = document.querySelectorAll(".flash-message");
    flashMessages.forEach((msg) => {
        setTimeout(() => {
            msg.classList.add("is-hiding");

            setTimeout(() => {
                msg.remove();
            }, 400);
        }, 3500);
    });

    // 2. Sidebar toggle
    const toggleButton = document.querySelector("[data-sidebar-toggle]");
    const sidebar = document.querySelector(".sidebar");

    if (toggleButton && sidebar) {
        toggleButton.addEventListener("click", () => {
            const isOpen = sidebar.classList.toggle("is-open");
            toggleButton.setAttribute("aria-expanded", String(isOpen));
        });

        document.addEventListener("click", (event) => {
            const isMobile = window.matchMedia("(max-width: 700px)").matches;
            const clickedOutside = !sidebar.contains(event.target) && !toggleButton.contains(event.target);

            if (isMobile && clickedOutside && sidebar.classList.contains("is-open")) {
                sidebar.classList.remove("is-open");
                toggleButton.setAttribute("aria-expanded", "false");
            }
        });
    }

    // 3. FE-03: Tìm kiếm nhanh trên bảng tài khoản
    const userSearch = document.querySelector("[data-user-search]");
    const userRows = Array.from(document.querySelectorAll("[data-user-row]"));
    const noUserResults = document.querySelector("[data-user-no-results]");

    if (userSearch && userRows.length > 0) {
        userSearch.addEventListener("input", () => {
            const query = userSearch.value.trim().toLocaleLowerCase("vi");
            let visibleCount = 0;

            userRows.forEach((row) => {
                const matches = row.textContent.toLocaleLowerCase("vi").includes(query);
                row.hidden = !matches;
                if (matches) visibleCount += 1;
            });

            if (noUserResults) {
                noUserResults.hidden = visibleCount !== 0;
            }
        });
    }

    // 4. FE-03: Xác nhận trước khi gửi thao tác đổi trạng thái
    document.querySelectorAll("form[data-confirm]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            const message = form.dataset.confirm;
            if (message && !window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    // 5. FE-03: Kiểm tra hai mật khẩu nhập trùng nhau phía trình duyệt
    document.querySelectorAll("[data-account-form], [data-password-form]").forEach((form) => {
        const password = form.querySelector("[data-password-input]");
        const confirmation = form.querySelector("[data-password-confirm]");
        const error = form.querySelector("[data-password-error]");

        if (!password || !confirmation) return;

        const validatePasswords = () => {
            const matches = confirmation.value === "" || password.value === confirmation.value;
            confirmation.setCustomValidity(matches ? "" : "Hai mật khẩu chưa khớp.");

            if (error) {
                error.hidden = matches;
            }

            return matches;
        };

        password.addEventListener("input", validatePasswords);
        confirmation.addEventListener("input", validatePasswords);
        form.addEventListener("submit", (event) => {
            if (!validatePasswords()) {
                event.preventDefault();
                confirmation.reportValidity();
            }
        });
    });

    // 6. FE-04: Apartment form validation
    const apartmentForm = document.querySelector("[data-apartment-form]");
    if (apartmentForm) {
        const roomInput = apartmentForm.querySelector("#roomNumber");
        const floorInput = apartmentForm.querySelector("#floorNumber");
        const areaInput = apartmentForm.querySelector("#areaSqm");
        const typeSelect = apartmentForm.querySelector("#apartmentType");
        const statusSelect = apartmentForm.querySelector("#status");
        const descInput = apartmentForm.querySelector("#description");

        const isAllowedControlKey = (event) => {
            if (event.ctrlKey || event.metaKey) return true;
            const allowedKeys = [
                "Backspace", "Delete", "Tab", "Escape", "Enter",
                "ArrowLeft", "ArrowRight", "ArrowUp", "ArrowDown", "Home", "End"
            ];
            return allowedKeys.includes(event.key);
        };

        // Chặn nhập chữ/ký tự lạ cho ô Tầng (chỉ cho phép số nguyên 0-9)
        if (floorInput) {
            floorInput.addEventListener("keydown", (event) => {
                if (isAllowedControlKey(event)) return;
                if (!/^[0-9]$/.test(event.key)) {
                    event.preventDefault();
                }
            });

            floorInput.addEventListener("input", () => {
                floorInput.value = floorInput.value.replace(/\D/g, "");
            });
        }

        // Chặn nhập chữ/ký tự lạ cho ô Diện tích (chỉ cho phép số và tối đa 1 dấu chấm, 2 số thập phân)
        if (areaInput) {
            areaInput.addEventListener("keydown", (event) => {
                if (isAllowedControlKey(event)) return;

                if (event.key === ".") {
                    if (areaInput.value.includes(".") || areaInput.value.length === 0) {
                        event.preventDefault();
                    }
                    return;
                }

                if (!/^[0-9]$/.test(event.key)) {
                    event.preventDefault();
                }
            });

            areaInput.addEventListener("input", () => {
                let val = areaInput.value.replace(/[^0-9.]/g, "");
                const parts = val.split(".");
                if (parts.length > 2) {
                    val = parts[0] + "." + parts.slice(1).join("");
                }
                if (parts[1] && parts[1].length > 2) {
                    val = parts[0] + "." + parts[1].slice(0, 2);
                }
                areaInput.value = val;
            });
        }

        // Xóa thông báo lỗi ngay khi người dùng nhập hoặc thay đổi giá trị
        [roomInput, floorInput, areaInput, typeSelect, statusSelect, descInput].forEach((field) => {
            if (!field) return;
            const eventType = field.tagName === "SELECT" ? "change" : "input";
            field.addEventListener(eventType, () => {
                field.setCustomValidity("");
                field.classList.remove("field-invalid");
            });
        });

        apartmentForm.addEventListener("submit", (event) => {
            let valid = true;

            const setFieldError = (field, message) => {
                if (!field) return;
                field.setCustomValidity(message);
                if (message) {
                    field.classList.add("field-invalid");
                    valid = false;
                } else {
                    field.classList.remove("field-invalid");
                }
            };

            // 1. Kiểm tra Mã căn hộ (bắt buộc, tối đa 20 ký tự)
            if (roomInput) {
                const roomVal = roomInput.value.trim();
                if (!roomVal) {
                    setFieldError(roomInput, "Vui lòng nhập mã căn hộ.");
                } else if (roomVal.length > 20) {
                    setFieldError(roomInput, "Mã căn hộ không được vượt quá 20 ký tự.");
                } else {
                    setFieldError(roomInput, "");
                }
            }

            // 2. Kiểm tra Tầng (bắt buộc, số nguyên từ 1 đến 30)
            if (floorInput) {
                const floorRaw = floorInput.value.trim();
                const floor = Number(floorRaw);
                if (!floorRaw) {
                    setFieldError(floorInput, "Vui lòng nhập số tầng.");
                } else if (!Number.isInteger(floor) || floor < 1 || floor > 30) {
                    setFieldError(floorInput, "Số tầng phải là số nguyên từ 1 đến 30.");
                } else {
                    setFieldError(floorInput, "");
                }
            }

            // 3. Kiểm tra Diện tích (bắt buộc, từ 0.01 đến 9999999999.99)
            if (areaInput) {
                const areaRaw = areaInput.value.trim();
                const area = Number(areaRaw);
                const validDecimalFormat = /^\d+(\.\d{1,2})?$/.test(areaRaw);

                if (!areaRaw) {
                    setFieldError(areaInput, "Vui lòng nhập diện tích thông thủy.");
                } else if (!Number.isFinite(area) || area < 0.01 || area > 9999999999.99) {
                    setFieldError(areaInput, "Diện tích phải từ 0.01 đến 9,999,999,999.99 m².");
                } else if (!validDecimalFormat) {
                    setFieldError(areaInput, "Diện tích chỉ được phép tối đa 2 chữ số thập phân.");
                } else {
                    setFieldError(areaInput, "");
                }
            }

            // 4. Kiểm tra Loại mặt bằng (bắt buộc chọn)
            if (typeSelect) {
                if (!typeSelect.value) {
                    setFieldError(typeSelect, "Vui lòng chọn loại mặt bằng.");
                } else {
                    setFieldError(typeSelect, "");
                }
            }

            // 5. Kiểm tra Trạng thái (nếu có trên form sửa)
            if (statusSelect) {
                if (!statusSelect.value) {
                    setFieldError(statusSelect, "Vui lòng chọn trạng thái.");
                } else {
                    setFieldError(statusSelect, "");
                }
            }

            // 6. Kiểm tra Mô tả (không bắt buộc, tối đa 1000 ký tự)
            if (descInput) {
                if (descInput.value.length > 1000) {
                    setFieldError(descInput, "Mô tả không được vượt quá 1000 ký tự.");
                } else {
                    setFieldError(descInput, "");
                }
            }

            if (!valid) {
                event.preventDefault();
                apartmentForm.reportValidity();
            }
        });
    }

    // FE-05: validation email chủ hộ.
    const householdForm = document.querySelector("[data-household-form]");
    if (householdForm) {
        const nationalId = householdForm.querySelector("#nationalId");
        const phone = householdForm.querySelector("#phone");
        const email = householdForm.querySelector("#email");
        const movedInDate = householdForm.querySelector("#movedInDate");

        householdForm.addEventListener("submit", (event) => {
            let valid = true;

            if (nationalId && !/^[0-9]{12}$/.test(nationalId.value.trim())) {
                nationalId.setCustomValidity("Số CCCD phải gồm đúng 12 chữ số.");
                valid = false;
            } else if (nationalId) {
                nationalId.setCustomValidity("");
            }

            if (phone && !/^[0-9]{10}$/.test(phone.value.trim())) {
                phone.setCustomValidity("Số điện thoại phải gồm đúng 10 chữ số.");
                valid = false;
            } else if (phone) {
                phone.setCustomValidity("");
            }

            if (email && email.value.trim() !== "" &&
                !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
                email.setCustomValidity("Vui lòng nhập email hợp lệ.");
                valid = false;
            } else if (email) {
                email.setCustomValidity("");
            }

            if (movedInDate && movedInDate.required && movedInDate.value === "") {
                movedInDate.setCustomValidity("Vui lòng chọn ngày chuyển vào.");
                valid = false;
            } else if (movedInDate) {
                movedInDate.setCustomValidity("");
            }

            if (!valid) {
                event.preventDefault();
                householdForm.reportValidity();
            }
        });
    }
});
