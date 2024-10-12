
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

 function openModal(imageSrc) {
     const modal = document.getElementById('imageModal');
     const modalImage = document.getElementById('modalImage');
     modal.style.display = 'block'; // 모달 보이기
     modalImage.src = imageSrc; // 클릭한 이미지 설정
 }

function closeModal() {
    var modal = document.getElementById("imageModal");
    modal.style.display = "none"; // 모달을 숨김
}




