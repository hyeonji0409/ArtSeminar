
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
});

// '수정' 버튼 클릭 시 disabled 해제
document.getElementById('savebtn').addEventListener('click', function() {
    // 모든 input 요소를 선택
    const inputs = document.querySelectorAll('.info__items input');

    // 각 input 요소의 disabled 속성을 false로 설정
    inputs.forEach(function(input) {
        // input.disabled = true;
    });

    // 수정 버튼 숨기기
    document.getElementById('editbtn').style.display = 'block';

    // 필요하면 '완료'와 '취소' 버튼을 보이도록 설정
    document.querySelector('.editPageBtn').style.display = 'none';
});

document.getElementById('canclebtn').addEventListener('click', function() {
    const inputs = document.querySelectorAll('.info__items input');

    inputs.forEach(function(input) {
        input.disabled = true;
    });

    document.getElementById('editbtn').style.display = 'block';
    document.querySelector('.editPageBtn').style.display = 'none';
});

