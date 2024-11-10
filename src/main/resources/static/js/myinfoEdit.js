
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
    document.querySelector('#password1').style.display = 'block';
    document.querySelector('#password2').style.display = 'block';
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
    document.querySelector('#password1').style.display = 'none';
    document.querySelector('#password2').style.display = 'none';
});

document.getElementById('canclebtn').addEventListener('click', function() {
    const inputs = document.querySelectorAll('.info__items input');

    inputs.forEach(function(input) {
        input.disabled = true;
    });

    document.getElementById('editbtn').style.display = 'block';
    document.querySelector('.editPageBtn').style.display = 'none';
    document.querySelector('#password1').style.display = 'none';
    document.querySelector('#password2').style.display = 'none';
});

var userSex = document.getElementById('userSex').value;

document.addEventListener("DOMContentLoaded", function() {
    if (userSex === 'male') {
        document.getElementById('male').checked = true;
        document.querySelector('label[for="male"]').style.backgroundColor = "var(--color-blue)"  // 남자 라벨 배경색 변경
    } else if (userSex === 'female') {
        document.getElementById('female').checked = true;
        document.querySelector('label[for="female"]').style.backgroundColor = "var(--color-blue)";  // 여자 라벨 배경색 변경
    }
});