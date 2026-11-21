document.addEventListener("DOMContentLoaded", () => {
    // flash message auto-hide
    const flashMessages = document.querySelectorAll(".flash-message");
    flashMessages.forEach((msg) => {
        setTimeout(() => {
            msg.classList.add("is-hiding");
            
            setTimeout(() => {
                msg.remove();
            }, 400);
        }, 3500);
    });

    const toggleButton = document.querySelector("[data-sidebar-toggle]");
    const sidebar = document.querySelector(".sidebar");

    if (!toggleButton || !sidebar) {
        return;
    }

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

    // FE-03: tìm kiếm nhanh trên bảng tài khoản.
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

    // FE-03: xác nhận trước khi gửi thao tác đổi trạng thái.
    document.querySelectorAll("form[data-confirm]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            const message = form.dataset.confirm;
            if (message && !window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    // FE-03: kiểm tra hai mật khẩu nhập trùng nhau phía trình duyệt.
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
});
