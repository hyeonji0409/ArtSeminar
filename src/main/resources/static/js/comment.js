document.getElementById('new_comment').addEventListener('submit', function (e) {
    e.preventDefault();
    var form = e.target;
    var formData = new FormData(form);

    fetch('/comments/add', {
        method: 'POST',
        body: formData
    }).then(function (response) {
        if (response.ok) {
            location.reload();
        }
        else {
            alert('댓글 등록에 실패했습니다.');
        }
    })
});

const reportButtons =  document.getElementsByClassName('report_comment_btn');
for (const button of reportButtons) {
    button.addEventListener('click', function (e) {
        e.preventDefault();

        alert('신고 기능은 준비중입니다.');
    });
}

const updateButtons =  document.getElementsByClassName('update_comment_btn')
for (const button of updateButtons) {
    button.addEventListener('click', function (e) {
        e.preventDefault();

        if (document.querySelector('textarea.content_box')) return

        const commentElement = e.target.closest('[data-no]');
        const commentNo = commentElement.dataset.no;
        const commentContentBox = commentElement.querySelector('.content_box');
        const commentContent = commentContentBox.innerText;

        // 새로운 input 요소 생성
        const input = document.createElement('textarea');
        input.value = commentContent;
        input.style.width = '55%';
        input.style.height = '5rem';
        input.style.resize = 'none';
        input.className = commentContentBox.className; // 기존 클래스 복사
        commentContentBox.replaceWith(input);


        const edit_btn = document.createElement('button');
        edit_btn.innerHTML = '수정하기';
        edit_btn.classList.add('btn');
        edit_btn.classList.add('btn-outline-primary')
        input.parentElement.insertBefore(edit_btn, input.nextSibling);


        edit_btn.addEventListener('click', function (e) {
            const formData = new FormData();
            formData.append('no', commentNo);
            formData.append('memo', input.value);

            fetch('/comments/update', {
                method: 'POST',
                body: formData
            }).then(function (response) {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('댓글 수정에 실패했습니다.');
                }
            })
        });
    });
}

const deleteButtons =  document.getElementsByClassName('delete_comment_btn')
for (const button of deleteButtons) {
    button.addEventListener('click', function (e) {
        e.preventDefault();

        const commentElement = e.target.closest('[data-no]');
        const commentNo = commentElement.dataset.no;

        const formData = new FormData();
        formData.append('no', commentNo);

        fetch('/comments/delete', {
            method: 'POST',
            body: formData
        }).then(function (response) {
            if (response.ok) {
                location.reload();
            }
            else {
                alert('댓글 삭제에 실패했습니다.');
            }
        })
    });
}