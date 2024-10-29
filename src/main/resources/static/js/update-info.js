
/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"]:not([name="detailAddress"]), input[type="number"], input[type="email"], input[type="password"]');


inputBoxes.forEach(v =>  v.addEventListener("change", (e) => {
    console.log("인풋박스에 값:" + e.target.value )
    // e.target.validity.badInput 는  number type 검사를 위함.
    if (e.target.value || e.target.validity.badInput ) {
        // e.target.nextElementSibling.classList.add('focused-label')
        e.target.parentElement.querySelector("label").classList.add('focused-label')
    } else {
        // e.target.nextElementSibling.classList.remove('focused-label')
        e.target.parentElement.querySelector("label").classList.remove('focused-label')
    }
}))
inputBoxes.forEach(v =>  v.addEventListener("blur", (e) => {
    console.log("인풋박스에 값:" + e.target.value )
    // e.target.validity.badInput 는  number type 검사를 위함.
    if (e.target.value || e.target.validity.badInput ) {
        // e.target.nextElementSibling.classList.add('focused-label')
        e.target.parentElement.querySelector("label").classList.add('focused-label')
    } else {
        // e.target.nextElementSibling.classList.remove('focused-label')
        e.target.parentElement.querySelector("label").classList.remove('focused-label')
    }
}))
/*
* 개념: https://yozm.wishket.com/magazine/detail/120/
* 코드: https://code-study.tistory.com/21
* 참고:https://getbootstrap.kr/docs/5.0/forms/validation/
* ---------------------------------------------------------------------------------- */



/* check submit form validation */
const form = document.querySelector('form');
const idInput = document.querySelector('input[name="username"]');
const roadFullAddrInput = document.querySelector('input[name="roadAddress"]');
const detailAddrInput = document.querySelector('input[name="detailAddress"]');

idInput.focus()
idInput.blur()



idInput.addEventListener('blur', (e) => {
    checkValidation("username", e.target);
})
roadFullAddrInput.addEventListener('blur', (e) => {
    checkValidation("roadAddress", e.target);
})
detailAddrInput.addEventListener('blur', (e) => {
    checkValidation("roadAddress", e.target);
})

// 성별 포커스 시 즉시 체크
// genderInputs.forEach((v)=> v.addEventListener('focus', (e) => {e.target.checked= true }))




form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // 제출 시에 유효성 검사와 무효한 인풋에 포커스
    if (
        ( await checkValidation("username", idInput) === false ? idInput.focus() : true)  &&
        // ( await checkValidation("password", passwordInput) === false ? passwordInput.focus() : true) &&
        // ( await checkValidation("email", emailInput) === false ? emailInput.focus() : true) &&
        // ( await checkValidation("name", nameInput) === false ? nameInput.focus() : true) &&
        // ( await checkValidation("birth", birthdayInput) === false ? birthdayInput.focus() : true) &&
        // ( await checkValidation("tel", contactNumberInput) === false ? contactNumberInput.focus() : true) &&
        // ( await checkValidation("year", cohortInput) === false ? cohortInput.focus() : true) &&
        ( await checkValidation("roadAddress", roadFullAddrInput) === false ? roadFullAddrInput.focus() : true) &&
        ( await checkValidation("roadAddress", detailAddrInput) === false ? detailAddrInput.focus() : true)
    ) {
        form.submit();
        alert("수정되었습니다.");
    }

})

// 서버에 유효한 값인지 판별 요청
async function checkValidation(valueName, eventTarget) {
    let isValid = false;
    console.log("sending value to server: " +  eventTarget.value)

    await fetch(`/sign-up/check-${valueName}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Ensure the server understands the content type
        },
        body: JSON.stringify({value: eventTarget.value})
    })
        .then(res => res.text())
        .then(data => {
            const errorMsgBox = document.querySelector(`#${valueName}Box .error-msg`);
            console.log("received:" +  eventTarget.id, eventTarget.value)

            if (eventTarget.value==="" || !errMsg[valueName].pattern.test(eventTarget.value)) {
                errorMsgBox.textContent = errMsg[valueName].invalid
                eventTarget.classList.add('invalid-input');
                eventTarget.parentElement.querySelector("label").classList.add('invalid-input');
                eventTarget.parentElement.classList.add('invalid-input');
            }
            else {
                errorMsgBox.textContent = null
                eventTarget.classList.remove('invalid-input');
                eventTarget.parentElement.querySelector("label").classList.remove('invalid-input');
                eventTarget.parentElement.classList.remove('invalid-input');
                isValid = true;
            }
        })
        .catch( error => {
            console.error(error);
        })

    return isValid;
}


// 에러 메세지 객체들
// 출처 https://choiiis.github.io/web/toy-project-sign-up-and-in-page-3/
const errMsg = {
    username: {
        pattern: /^[a-z\d]{5,20}$/,
        invalid: "5~20자의 영문 소문자, 숫자만 사용 가능합니다.",
        // success: "사용 가능한 아이디입니다.",
        fail: "이미 사용 중인 아이디입니다."
    },
    roadAddress: {
        pattern: /\S+/,
        invalid: "'찾기' 버튼으로 주소를 입력해 주세요.",
        fail: "'찾기' 버튼으로 주소를 입력해 주세요.fail"
    },
    detailAddress: {
        pattern: /\S+/,
        invalid: "상세주소를 입력해 주세요.",
        fail: "상세주소를 입력해 주세요.fail"
    }
}