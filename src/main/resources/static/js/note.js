function promptPasswordAndProceed(button, action) {
    const actionText = action === 'edit' ? '수정' : '삭제';
    const password = prompt(`노트를 ${actionText}하려면 비밀번호를 입력하세요:`);

    if (password === null || password.trim() === "") {
        alert("비밀번호를 입력해야 합니다.");
        return; // 비밀번호 미입력 시 취소
    }

    // HTML의 data-* 속성에서 URL 가져오기
    const targetUrl =
        action === 'edit'
            ? button.getAttribute('data-edit-url')
            : button.getAttribute('data-delete-url');

    fetch(`/note/${action}/verify`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ password }) // 비밀번호 서버로 전달
    })
        .then(response => {
            if (response.ok) {
                alert(`노트가 성공적으로 ${actionText}되었습니다.`);
                window.location.href = targetUrl; // 렌더링된 URL로 이동
            } else {
                response.json().then(data => {
                    alert(data.message || "비밀번호가 틀렸습니다.");
                });
            }
        })
        .catch(error => {
            console.error("에러 발생:", error);
            alert("서버와의 통신 중 문제가 발생했습니다.");
        });
}
