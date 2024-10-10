/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"]:not([name="queryValue"]), input[type="number"], input[type="date"], input[type="time"]');

inputBoxes.forEach(v => {
    v.parentElement.querySelector("label").classList.add('focused-label')
    // if (v.value!=='') {
    //     // e.target.nextElementSibling.classList.add('focused-label')
    //     v.parentElement.querySelector("label").classList.add('focused-label')
    // } else {
    //     // e.target.nextElementSibling.classList.remove('focused-label')
    //     v.parentElement.querySelector("label").classList.remove('focused-label')
    // }
})


inputBoxes.forEach(v => v.addEventListener("change", (e) => {
    console.log("인풋박스에 값:" + e.target.value)
    // e.target.validity.badInput 는  number type 검사를 위함.
    if (e.target.value || e.target.validity.badInput) {
        // e.target.nextElementSibling.classList.add('focused-label')
        e.target.parentElement.querySelector("label").classList.add('focused-label')
    } else {
        // e.target.nextElementSibling.classList.remove('focused-label')
        e.target.parentElement.querySelector("label").classList.remove('focused-label')
    }
}))

// /* check submit form validation */
const form = document.querySelector('#form');
const querySubmitButton = document.querySelector('#querySubmit');
const saveButton = document.querySelector('#save-changes');
const deleteButton = document.querySelector('#delete');
const pkInput = document.querySelector('#no');
const titleInput = document.querySelector('input[name="title"]');
const descriptionInput = document.querySelector('input[name="description"]');
// const startInput = document.querySelector('input[name="name"]');
const startDateInput = document.querySelector('input[name="startDate"]');
const endDateInput = document.querySelector('input[name="endDate"]');
const cohortInput = document.querySelector('input[name="year"]');
const roadFullAddrInput = document.querySelector('input[name="roadAddress"]');
const detailAddrInput = document.querySelector('input[name="detailAddress"]');
// const submitBtn = document.querySelector('button[type="submit"]');
const genderInputs = document.querySelectorAll('input[name="sex"]');
const roleInput = document.querySelector('input[name="role"]');
//
// idInput.parentElement.querySelector("label").classList.add('focused-label')
// emailInput.parentElement.querySelector("label").classList.add('focused-label')
// nameInput.parentElement.querySelector("label").classList.add('focused-label')
// birthdayInput.parentElement.querySelector("label").classList.add('focused-label')
// contactNumberInput.parentElement.querySelector("label").classList.add('focused-label')
// cohortInput.parentElement.querySelector("label").classList.add('focused-label')
// roadFullAddrInput.parentElement.querySelector("label").classList.add('focused-label')
// roleInput.parentElement.querySelector("label").classList.add('focused-label')
//
//
// // idInput.addEventListener('blur', (e) => {
// //     checkValidation("username", e.target);
// // })
// passwordInput.addEventListener('blur', (e) => {
//     checkValidation("password", e.target);
// })
// emailInput.addEventListener('blur', (e) => {
//     checkValidation("email", e.target);
// })
// birthdayInput.addEventListener('blur', (e) => {
//     checkValidation("birth", e.target);
// })
// contactNumberInput.addEventListener('blur', (e) => {
//     checkValidation("tel", e.target);
// })
// cohortInput.addEventListener('blur', (e) => {
//     checkValidation("year", e.target);
// })
// nameInput.addEventListener('blur', (e) => {
//     checkValidation("name", e.target);
// })
// // roadFullAddrInput.addEventListener('blur', (e) => {
// //     checkValidation("roadAddress", e.target);
// // })
// detailAddrInput.addEventListener('blur', (e) => {
//     checkValidation("roadAddress", e.target);
// })
// roleInput.addEventListener('blur', (e) => {
//     checkValidation("role", e.target);
// })
//
// // 성별 포커스 시 즉시 체크
// genderInputs.forEach((v) => v.addEventListener('focus', (e) => {
//     e.target.checked = true
// }))


saveButton.addEventListener('click', async (e) => {
    e.preventDefault();
    //
    // // 젠더는 트리 구조가 좀 달라서 일단 이렇게...
    // function checkValidationGender() {
    //     const GenderChecked = document.querySelector('input[name="sex"]:checked');
    //
    //     return (  document.querySelector(`#${"sex"}Box .error-msg`).textContent = GenderChecked ? null : errMsg["sex"].invalid ) === null;
    // }
    //
    //
    // // 제출 시에 유효성 검사와 무효한 인풋에 포커스
    // if (
    //     // ( await checkValidation("username", idInput) === false ? idInput.focus() : true)  &&
    //     ( await checkValidation("password", passwordInput) === false ? passwordInput.focus() : true) &&
    //     ( await checkValidation("email", emailInput) === false ? emailInput.focus() : true) &&
    //     ( await checkValidation("name", nameInput) === false ? nameInput.focus() : true) &&
    //     ( await checkValidation("birth", birthdayInput) === false ? birthdayInput.focus() : true) &&
    //     ( await checkValidation("tel", contactNumberInput) === false ? contactNumberInput.focus() : true) &&
    //     checkValidationGender() &&
    //     ( await checkValidation("year", cohortInput) === false ? cohortInput.focus() : true) &&
    //     ( await checkValidation("roadAddress", roadFullAddrInput) === false ? roadFullAddrInput.focus() : true) &&
    //     ( await checkValidation("roadAddress", detailAddrInput) === false ? detailAddrInput.focus() : true) &&
    //     ( await checkValidation("role", roleInput) === false ? roleInput.focus() : true)
    // ) {
        if ( !confirm("기존 정보는 복구할 수 없습니다.\n정말 수정하시겠습니까?") ) return;
        form.submit();
        alert("수정이 완료되었습니다.");
    // }
})

deleteButton.addEventListener("click", async () => {

    let flag = confirm("삭제된 계정은 DB에서 완전히 제거됩니다.\n정말 삭제하시겠습니까?")
    if (!flag) return

    let no = document.querySelector("#no").value
    alert(no)

    let response = await fetch("calendar/delete", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "no": no
            })
        }
    )

    if (response.status === 200) {
        alert("계정이 삭제되었다.")
    }
    else {
        alert("알 수 없는 문제가 발생하였다.")
    }

    location.href = "/admin/calendar"
})
//
// // 서버에 유효한 값인지 판별 요청
// async function checkValidation(valueName, eventTarget) {
//     let isValid = false;
//     console.log("sending value to server: " +  eventTarget.value)
//
//     await fetch(`/user/update/check-${valueName}`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'  // Ensure the server understands the content type
//         },
//         body: JSON.stringify({
//             username: idInput.value,
//             value: eventTarget.value
//         })
//     })
//         .then(res => res.text())
//         .then(data => {
//             const errorMsgBox = document.querySelector(`#${valueName}Box .error-msg`);
//             console.log("received:" +  eventTarget.id, eventTarget.value)
//
//             if (eventTarget.value!=="" && data === "invalid") {
//                 errorMsgBox.textContent = errMsg[valueName].fail
//                 eventTarget.classList.add('invalid-input');
//                 eventTarget.nextElementSibling.classList.add('invalid-input');
//             }
//             else if (!errMsg[valueName].pattern.test(eventTarget.value)) {
//                 errorMsgBox.textContent = errMsg[valueName].invalid
//                 eventTarget.classList.add('invalid-input');
//                 eventTarget.parentElement.querySelector("label").classList.add('invalid-input');
//                 eventTarget.parentElement.classList.add('invalid-input');
//             }
//             else {
//                 errorMsgBox.textContent = null
//                 eventTarget.classList.remove('invalid-input');
//                 eventTarget.parentElement.querySelector("label").classList.remove('invalid-input');
//                 eventTarget.parentElement.classList.remove('invalid-input');
//                 isValid = true;
//             }
//         })
//         .catch( error => {
//             console.error(error);
//         })
//
//     return isValid;
// }


// 에러 메세지 객체들
// 출처 https://choiiis.github.io/web/toy-project-sign-up-and-in-page-3/
// const errMsg = {
//     username: {
//         pattern: /^[a-z\d]{5,20}$/,
//         invalid: "5~20자의 영문 소문자, 숫자만 사용 가능합니다.",
//         // success: "사용 가능한 아이디입니다.",
//         fail: "이미 사용 중인 아이디입니다."
//     },
//     password: {
//         pattern: /^\S*$/,
//         invalid: "관리자가 설정 시, 공백이 아닌 임의의 문자열을 입력하세요.",
//         fail: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요.fail"
//     },
//     email: {
//         pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
//         invalid: "이메일 형식이 올바르지 않습니다.",
//         fail: "이미 사용 중인 이메일 입니다."
//     },
//     name: {
//         pattern: /\S+/,
//         invalid: "이름을 작성해 주세요.",
//         fail: "이름을 작성해 주세요.fail"
//     },
//     birth: {
//         pattern: /^(19|20)\d{2}\/(0[1-9]|1[0-2])\/(0[1-9]|[12]\d|3[01])$/,
//         invalid: "올바른 생년월일 8자리를 입력해 주세요.",
//         fail: "올바른 생년월일 8자리를 입력해 주세요.fail"
//     },
//     tel: {
//         pattern: /^010-\d{4}-\d{4}$/,
//         invalid: '전화번호("-" 포함)를 확인해 주세요.',
//         fail: "이미 사용 중인 전화번호 입니다."
//     },
//     sex: {
//         pattern: /^(male|female)$/,
//         invalid: "성별을 선택해주세요.",
//         fail: "성별을 선택해주세요.fail"
//     },
//     year: {
//         pattern: /^\d+$/,
//         invalid: "숫자만 입력해 주세요.",
//         fail: "숫자만 입력해 주세요.fail",
//     },
//     roadAddress: {
//         pattern: /\S+/,
//         invalid: "'찾기' 버튼으로 주소를 입력해 주세요.",
//         fail: "'찾기' 버튼으로 주소를 입력해 주세요.fail"
//     },
//     detailAddress: {
//         pattern: /\S+/,
//         invalid: "상세주소를 입력해 주세요.",
//         fail: "상세주소를 입력해 주세요.fail"
//     },
//     role: {
//         pattern: /^(ROLE_GUEST|ROLE_ADMIN)$/,
//         invalid: "똑바로 입력해주세요.",
//         fail: "똑바로 입력..fail"
//     }
// }



// 검색 결과 시 모델속성 값 유지 처리에 관한 코드




// 필터 검색 시 페이지는 1로 하고 조건들을 보냄

querySubmitButton.addEventListener("click", (e) => {
    e.preventDefault()
    document.querySelector("#search-form > input[name='page']").value = 1
    document.querySelector("#search-form").submit()
})

document.querySelector("#reset-button").addEventListener("click", (e) => {
    e.preventDefault()
    // location.pathname 은 /admin 이라는 가정
    location.href = location.pathname
})

document.querySelectorAll("#previous-page, #next-page").forEach(v => v.addEventListener("click", (e) => {
    e.preventDefault()

    document.querySelector("#search-form > input[name='page']").value =
        e.target.id === "previous-page" ?
            Number(document.querySelector("#search-form > input[name='page']").value) - 1 :
            Number(document.querySelector("#search-form > input[name='page']").value) + 1

    document.querySelector("#search-form").submit()
}))

document.querySelectorAll("#starter-section > div.container > nav > ul > li > a").forEach(v =>
    v.addEventListener('click', (e) => {
        e.preventDefault()
        document.querySelector("#search-form > input[name='page']").value = v.innerHTML
        document.querySelector("#search-form").submit()
    }))


// 화면에 검색조건들 유지(업데이트)
try {
    document.querySelector(`#search-form > div > select[name='query'] > option[value=${query}]`).selected = true
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > input[name='start']`).value = startDateTime
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > input[name='end']`).value = endDateTime
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > select[name='order'] > option[value=${order}]`).selected = true
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > select[name='role'] > option[value=${role}]`).selected = true
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > select[name='sort'] > option[value=${sort}]`).selected = true
} catch (e) {}





/* 회원정보 수정하기 */
rows = document.querySelectorAll("#starter-section > div.container > div:nth-child(3) > table > tbody > tr")
editButtons = document.querySelectorAll(".edit-btn")


editButtons.forEach(v => v.addEventListener("click", (e) => {
    let memberId = e.target.dataset.memberId ? e.target.dataset.memberId : "";
    console.log(eventForEdit.content[memberId]);
    if (memberId==="") return
    let eachUser = eventForEdit.content[memberId];

    pkInput.value = eachUser.no
    titleInput.value = eachUser.title
    descriptionInput.value = eachUser.description
    // startInput.value = eachUser.startDate
    startDateInput.value = eachUser.startDate
    endDateInput.value = eachUser.endDate
}))
