
/**
 * Click "introduce", "학습/행사" button
 */

function showContent(sectionId) {

    var sections = document.getElementsByClassName('content-section');
    for (var i = 0; i < sections.length; i++) {
        sections[i].style.display = 'none';
    }


    document.getElementById(sectionId).style.display = 'block';
}

/**
 *  둘러보기 modal open/close
 */

// 모달 닫기 함수
function closeModal(event) {
    const modal = document.getElementById('imageModal');
    // 클릭한 대상이 모달 배경일 경우에만 닫기
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}

// 모달 열기 함수 (예시로 유지)
function openModal(imageSrc) {
    const modal = document.getElementById('imageModal');
    const modalImage = document.getElementById('modalImage');
    modalImage.src = imageSrc;
    modal.style.display = 'block';
}





