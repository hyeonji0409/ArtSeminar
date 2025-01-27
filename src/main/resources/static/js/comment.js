createComment = (formData) => {
    fetch('/comments/add', {
        method: 'POST',
        body: formData
    }).then(function (response) {
        if (response.ok) {
            location.reload();
        } else {
            alert('댓글 등록에 실패했습니다.');
        }
    })
};

document.querySelectorAll('.createCmt_btn').forEach((btn) =>
    btn.addEventListener('click', (e) => {
        e.preventDefault()

        const form = e.target.closest('.new_comment');
        let formData = new FormData(form);
        // console.log(formData);
        // alert(...formData.entries())
        createComment(formData);
    })
)

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




const uniqueArticleInfo = document.querySelector('.new_comment');
const bbsName = uniqueArticleInfo.querySelector('input[name="bbsName"]').value
const bbsNo = uniqueArticleInfo.querySelector('input[name="bbsNo"]').value
const username = uniqueArticleInfo.querySelector('input[name="username"]').value

let replyForm

const replyButtons =  document.querySelectorAll('.reply_comment_btn')
replyButtons.forEach( (button) => {
    button.addEventListener('click', function (e) {
        e.preventDefault();

        const commentElement = e.target.closest('.comment');
        const commentElementId = commentElement.dataset.no;

        if (replyForm) replyForm.remove()
        replyForm = document.createElement('form');
        replyForm.className = 'reply-form';
        replyForm.innerHTML = `
            <input type="hidden" name="bbsName" value="${bbsName}" />
            <input type="hidden" name="bbsNo" value="${bbsNo}" />
            <input type="hidden" name="username" value="${username}" />
            <input type="hidden" name="replys" value="${commentElementId}" />
            <div class="d-flex align-items-center">
            <input class="form-control" type="text" name="memo" placeholder="댓글을 입력하세요." style="width: 350px" autocomplete="off" required />
            <button type="submit" class="btn _btn createCmt_btn" style="width: 80px;">등록</button>
            </div>
        `;

        replyForm.querySelector('.createCmt_btn').addEventListener('click', (e) => {
            e.preventDefault()

            const form = e.target.closest('.reply-form');
            let formData = new FormData(form);
            createComment(formData);
        })

        commentElement.firstElementChild.append(replyForm);
        replyForm.querySelector('input[name="memo"]').focus();
    })
})