document.addEventListener("DOMContentLoaded", function () {


    // 오버레이 요소 추가 (모달 배경 흐림 효과)
    let overlay = document.createElement("div");
    overlay.classList.add("modal-overlay");
    document.body.appendChild(overlay);

    document.querySelectorAll(".card.custom-card").forEach(function (card) {
        card.addEventListener("click", function () {
            var modalId = this.getAttribute("data-modal-id");
            var modal = document.getElementById(modalId);

            console.log("클릭한 모달 ID:", modalId);

            // 모든 모달 숨기기 (중복 실행 방지)
            document.querySelectorAll(".custom-modal").forEach(function (m) {
                if (m !== modal) {
                    m.classList.add("hidden");
                }
            });

            if (modal) {
                // 이미 열린 모달이면 다시 열지 않음
                if (!modal.classList.contains("hidden")) {
                    console.log("이미 열린 모달:", modalId);
                    return;
                }

                modal.classList.remove("hidden"); // 모달 보이기
                document.body.classList.add("modal-open"); //흐리게 만들어줌
                console.log(" 모달 표시됨:", modalId);
            } else {
                console.error("모달을 찾을 수 없음:", modalId);
            }
        });
    });

    document.querySelectorAll("[data-modal-close]").forEach(function (btn) {
        btn.addEventListener("click", function () {
            var modal = this.closest(".custom-modal");
            if (modal) {
                modal.classList.add("hidden"); // 모달 숨기기
                document.body.classList.remove("modal-open");//배경흐리게한거 제거
                console.log("모달 닫힘:", modal.id);
            }
        });
    });

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("custom-modal")) {
            event.target.classList.add("hidden");
            document.body.classList.remove("modal-open");//흐리게 제거
            console.log("모달 바깥 클릭으로 닫힘");
        }
    });
});
