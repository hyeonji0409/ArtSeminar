
/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"], input[type="password"]');

inputBoxes.forEach(v =>  v.addEventListener("change", (e) => {
    console.log("인풋박스에 값:" + e.target.value )
    // e.target.validity.badInput 는  number type 검사를 위함.
    if (e.target.value || e.target.validity.badInput ) {
        e.target.nextElementSibling.classList.add('focused-label')
    } else {
        e.target.nextElementSibling.classList.remove('focused-label')
    }
}))

/* 출처
* 개념: https://yozm.wishket.com/magazine/detail/120/
* 코드: https://code-study.tistory.com/21
* */





/* check submit form validation */
const submitBtn = document.querySelector('button[type="submit"]');
const idInput = document.querySelector('input[name="username"]');
const passwordInput = document.querySelector('input[name="password"]');
// const submitBtn = document.querySelector('button[type="submit"]');
const confirmBox = document.querySelector("#confirm")


submitBtn.addEventListener('click', async (e) => {
    e.preventDefault();

    // submitBtn.
    if (confirmBox) {
        console.log("isChecked: ", confirmBox.value);

        if (confirmBox.checked) {
            location.href = "/user/sign-withdrawalConfirm";
        }
        else {
            alert("약관에 동의해주세요.")
        }

        return
    }

    const formData = new FormData(form)

    fetch("/user/withdrawal", {
        method: 'POST',
        body: formData
    }).then(res=> {
        if (res.status===200) alert("탈퇴가 완료되었습니다.")
        else alert("비밀번호가 올바르지 않습니다.")
    })

})