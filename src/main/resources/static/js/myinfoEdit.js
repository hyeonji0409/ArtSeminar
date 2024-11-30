
// '수정' 버튼 클릭 시 disabled 해제
document.getElementById('editbtn').addEventListener('click', function() {
    // 모든 input 요소를 선택
    const inputs = document.querySelectorAll('.info__items input');

    // 각 input 요소의 disabled 속성을 false로 설정
    inputs.forEach(function(input) {
        // input.disabled = false;
        input.readOnly = false;
    });

    // 수정 버튼 숨기기
    document.getElementById('editbtn').style.display = 'none';

    // 필요하면 '완료'와 '취소' 버튼을 보이도록 설정
    document.querySelector('.editPageBtn').style.display = 'block';
    // document.querySelector('#password1').style.display = 'block';
    // document.querySelector('#password2').style.display = 'block';
    // document.querySelector('.pw-lb1').style.display = 'block';
    // document.querySelector('.pw-lb2').style.display = 'block';

    document.querySelectorAll('.gender').forEach(v => v.style.pointerEvents = 'all')
});

// '완료', '취소' 버튼 클릭 시 disabled 활성화
document.getElementById('savebtn').addEventListener('click', function() {
    // 모든 input 요소를 선택
    const inputs = document.querySelectorAll('.info__items input');

    // 각 input 요소의 disabled 속성을 false로 설정
    inputs.forEach(function(input) {
        // input.disabled = true;
    });

    // 수정 버튼 보이기
    document.getElementById('editbtn').style.display = 'block';

    // 수정하면 '완료'와 '취소' 버튼 숨기기
    document.querySelector('.editPageBtn').style.display = 'none';
    // document.querySelector('#password1').style.display = 'none';
    // document.querySelector('#password2').style.display = 'none';
    // document.querySelector('.pw-lb1').style.display = 'none';
    // document.querySelector('.pw-lb2').style.display = 'none';

    document.querySelector('.gender').style.pointerEvents = 'none'
});

document.getElementById('canclebtn').addEventListener('click', function() {
    const inputs = document.querySelectorAll('.info__items input');

    inputs.forEach(function(input) {
        input.disabled = true;
    });

    document.getElementById('editbtn').style.display = 'block';


    document.querySelector('.editPageBtn').style.display = 'none';
    // document.querySelector('#password1').style.display = 'none';
    // document.querySelector('#password2').style.display = 'none';
    // document.querySelector('.pw-lb1').style.display = 'none';
    // document.querySelector('.pw-lb2').style.display = 'none';
});

// 페이지 로드 후 실행
document.addEventListener("DOMContentLoaded", function () {
    const userSex = document.getElementById("userSex").value; // 현재 사용자 성별 값
    const maleLabel = document.querySelector('label[for="male"]');
    const femaleLabel = document.querySelector('label[for="female"]');
    const radioButtons = document.querySelectorAll('input[name="sex"]');

    // 초기 선택 상태 설정
    if (userSex === "male") {
        document.getElementById("male").checked = true;
        maleLabel.style.backgroundColor = "var(--color-blue)"; // 선택된 라벨 색
        maleLabel.style.color = "white"
        femaleLabel.style.backgroundColor = "white"; // 기본 색
    } else if (userSex === "female") {
        document.getElementById("female").checked = true;
        femaleLabel.style.backgroundColor = "var(--color-blue)";
        femaleLabel.style.color = "white"
        maleLabel.style.backgroundColor = "white";
    }

    // 라벨 배경색 업데이트 함수
    function updateLabelColors() {
        radioButtons.forEach((radio) => {
            const label = document.querySelector(`label[for="${radio.id}"]`);
            if (radio.checked) {
                label.style.backgroundColor = "var(--color-blue)"; // 선택된 라벨 색
                label.style.color = "white"
            } else {
                label.style.backgroundColor = "white"; // 기본 색
                label.style.color = "black"
            }
        });
    }

    // 각 라디오 버튼에 change 이벤트 추가
    radioButtons.forEach((radio) => {
        radio.addEventListener("change", updateLabelColors);
    });
});



