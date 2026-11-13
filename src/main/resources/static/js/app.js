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
});
