
/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"], input[type="password"]');

inputBoxes.forEach(v =>  v.addEventListener("blur", (e) => {
    console.log("인풋박스에 값:" + e.target.value )
    // e.target.validity.badInput 는  number type 검사를 위함.
    if (e.target.value || e.target.validity.badInput ) {
        e.target.nextElementSibling.classList.add('focused-label')
    } else {
        e.target.nextElementSibling.classList.remove('focused-label')
    }
}))



/* check submit form validation */
const form = document.querySelector('form');
const idInput = document.querySelector('input[name="username"]');
const passwordInput = document.querySelector('input[name="password"]');
// const submitBtn = document.querySelector('button[type="submit"]');

idInput.focus()

// form.addEventListener('submit', async (e) => {
//     e.preventDefault();
//     // Todo 로그인 검사는 시큐리티에서
//     alert("시큐리티에서 처리토록")
    //
    // if (
    //     await checkValidation("userId", idInput) &&
    //     await checkValidation("password", passwordInput)
    // ) {
    //     console.log("어ㅐ???")
    //     form.submit();
    // }

// })
//
//
//
//
//
// // 서버에 유효한 값인지 판별 요청
// async function checkValidation(valueName, eventTarget) {
//     let isValid = false;
//     console.log("서버로 보냄" + eventTarget.name)
//
//     await fetch(`/sign-up/check-${valueName}`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'  // Ensure the server understands the content type
//         },
//         body: JSON.stringify({valueName: eventTarget.value})
//     })
//         .then(res => {
//             if (res.status === 200) {
//                 // 유효한
//                 return res.text();
//             }
//             else if (res.status === 409) {
//                 // 불가능
//                 return res.text();
//             }
//             else throw new Error(`Status Code is ${res.status} : ${res.statusText}`);
//         })
//         .then(data => {
//             const errorMsgBox = document.querySelector(`#${"password"}Box .error-msg`);
//             console.log(eventTarget.id, eventTarget.value)
//
//             if (eventTarget.value !== "" && data === "invalid" && eventTarget.id !== "cohort") {
//                 errorMsgBox.textContent = errMsg["password"].fail
//                 eventTarget.classList.add('invalid-input');
//                 eventTarget.nextElementSibling.classList.add('invalid-input');
//             } else if (eventTarget.value === "" || !errMsg["password"].pattern.test(eventTarget.value)) {
//                 errorMsgBox.textContent = errMsg["password"].invalid
//                 eventTarget.classList.add('invalid-input');
//                 eventTarget.nextElementSibling.classList.add('invalid-input');
//             } else {
//                 errorMsgBox.textContent = null
//                 eventTarget.classList.remove('invalid-input');
//                 eventTarget.nextElementSibling.classList.remove('invalid-input');
//                 isValid = true;
//             }
//         })
//         .catch( error => {
//             console.error(error);
//         })
//
//     return isValid;
// }
//
//
// // 에러 메세지 객체들
// // 출처 https://choiiis.github.io/web/toy-project-sign-up-and-in-page-3/
// const errMsg = {
//     password: {
//         pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/,
//         invalid: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요.",
//         fail: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요."
//     }
// }
