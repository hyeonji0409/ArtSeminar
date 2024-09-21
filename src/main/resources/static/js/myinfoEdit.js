// 수정 버튼 클릭 시 실행되는 함수
document.getElementById("editbtn").addEventListener("click", function() {
    // '수정' 버튼 숨기기
    this.style.display = "none";

    // '완료' 및 '취소' 버튼 보이기
    document.querySelector(".eidtPageBtn").style.display = "block";

    // 모든 input 요소의 'disabled' 속성 해제
    let inputs = document.querySelectorAll(".info__items input");
    inputs.forEach(function(input) {
        input.disabled = false; // input을 활성화
    });
});