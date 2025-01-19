/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"]:not([name="detailAddress"]), input[type="number"], input[type="email"], input[type="password"], #role');

inputBoxes.forEach(v => v.addEventListener("blur", (e) => {
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

document.querySelector('select').blur()

/* check submit form validation */
const form = document.querySelector('#form');
const querySubmitButton = document.querySelector('#querySubmit');
const saveButton = document.querySelector('#save-changes');
const cleanupButton = document.querySelector('#clean');
const deleteButton = document.querySelector('#delete');
const pkInput = document.querySelector('input[name="pk"]');
const idInput = document.querySelector('input[name="username"]');
const passwordInput = document.querySelector('input[name="password"]');
const emailInput = document.querySelector('input[name="email"]');
const nameInput = document.querySelector('input[name="name"]');
const birthdayInput = document.querySelector('input[name="birth"]');
const contactNumberInput = document.querySelector('input[name="tel"]');
const cohortInput = document.querySelector('input[name="year"]');
const zipcodeInput = document.querySelector('input[name="zipcode"]');
const roadFullAddrInput = document.querySelector('input[name="roadAddress"]');
const detailAddrInput = document.querySelector('input[name="detailAddress"]');
// const submitBtn = document.querySelector('button[type="submit"]');
const genderInputs = document.querySelectorAll('input[name="sex"]');
const roleInput = document.querySelector('#role');

idInput.parentElement.querySelector("label").classList.add('focused-label')
emailInput.parentElement.querySelector("label").classList.add('focused-label')
nameInput.parentElement.querySelector("label").classList.add('focused-label')
birthdayInput.parentElement.querySelector("label").classList.add('focused-label')
contactNumberInput.parentElement.querySelector("label").classList.add('focused-label')
cohortInput.parentElement.querySelector("label").classList.add('focused-label')
roadFullAddrInput.parentElement.querySelector("label").classList.add('focused-label')
roleInput.parentElement.querySelector("label").classList.add('focused-label')


// idInput.addEventListener('blur', (e) => {
//     checkValidation("username", e.target);
// })
passwordInput.addEventListener('blur', (e) => {
    checkValidation("password", e.target);
})
emailInput.addEventListener('blur', (e) => {
    checkValidation("email", e.target);
})
birthdayInput.addEventListener('blur', (e) => {
    checkValidation("birth", e.target);
})
contactNumberInput.addEventListener('blur', (e) => {
    checkValidation("tel", e.target);
})
cohortInput.addEventListener('blur', (e) => {
    checkValidation("year", e.target);
})
nameInput.addEventListener('blur', (e) => {
    checkValidation("name", e.target);
})
// roadFullAddrInput.addEventListener('blur', (e) => {
//     checkValidation("roadAddress", e.target);
// })
detailAddrInput.addEventListener('blur', (e) => {
    checkValidation("roadAddress", e.target);
})
roleInput.addEventListener('blur', (e) => {
    checkValidation("role", e.target);
})

// 성별 포커스 시 즉시 체크
genderInputs.forEach((v) => v.addEventListener('focus', (e) => {
    e.target.checked = true
}))


saveButton.addEventListener('click', async (e) => {
    e.preventDefault();

    // 젠더는 트리 구조가 좀 달라서 일단 이렇게...
    function checkValidationGender() {
        const GenderChecked = document.querySelector('input[name="sex"]:checked');

        return (  document.querySelector(`#${"sex"}Box .error-msg`).textContent = GenderChecked ? null : errMsg["sex"].invalid ) === null;
    }


    // 제출 시에 유효성 검사와 무효한 인풋에 포커스
    if (
        // ( await checkValidation("username", idInput) === false ? idInput.focus() : true)  &&
        ( await checkValidation("password", passwordInput) === false ? passwordInput.focus() : true) &&
        ( await checkValidation("email", emailInput) === false ? emailInput.focus() : true) &&
        ( await checkValidation("name", nameInput) === false ? nameInput.focus() : true) &&
        ( await checkValidation("birth", birthdayInput) === false ? birthdayInput.focus() : true) &&
        ( await checkValidation("tel", contactNumberInput) === false ? contactNumberInput.focus() : true) &&
        checkValidationGender() &&
        ( await checkValidation("year", cohortInput) === false ? cohortInput.focus() : true) &&
        ( await checkValidation("roadAddress", roadFullAddrInput) === false ? roadFullAddrInput.focus() : true) &&
        ( await checkValidation("roadAddress", detailAddrInput) === false ? detailAddrInput.focus() : true) &&
        ( await checkValidation("role", roleInput) === false ? roleInput.focus() : true)
    ) {
        if ( !confirm("기존 정보는 복구할 수 없습니다.\n정말 수정하시겠습니까?") ) return;
        form.submit();
        alert("수정이 완료되었습니다.");
    }
    else {
        if (!confirm("일부 항목의 포멧이 올바르지 않습니다.\n이러한 문제는 추후에 심각한 오류를 일으킬 수 있습니다.\n정말 제출하시겠습니까?")) return;
        form.submit();
        alert("수정이 완료되었습니다.");
    }
})

cleanupButton.addEventListener('click', async (e) => {
    e.preventDefault();
    alert("보존 기한이 지난 회원정보는 클린업하세요.\n직접 공백으로 제출해주세요.")

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
    //     if ( !confirm("해당 회원의 민감한 정보를 지웁니다.\n정말 진행하시겠습니까?") ) return;
// todo 아래 구현 필요

    //     let req = new FormData(form);
    //     req.set("mode", "clean");
    //     form.submit();
    //     alert("클린업이 완료되었습니다.");
    // }
    // else {
    //     if (!confirm("일부 항목의 포멧이 올바르지 않습니다.\n이러한 문제는 추후에 심각한 오류를 일으킬 수 있습니다.\n정말 제출하시겠습니까?")) return;
    //     form.submit();
    //     alert("클린업이 완료되었습니다.");
    // }
})

deleteButton.addEventListener("click", async () => {

    let flag = confirm("삭제된 계정은 DB에서 완전히 제거됩니다.\n정말 삭제하시겠습니까?")
    if (!flag) return
    let confirmText = prompt("삭제하려는 사용자의 아이디를 정확히 입력해주세요.", '아이디를 제출하는 즉시, 계정이 "삭제"됩니다.')
    if( confirmText !== idInput.value) { alert("올바르지 않습니다."); return; }


    const formData = new FormData()
    formData.append("username",idInput.value)

    let response = await fetch("/user/withdrawal", {
            method: "POST",
            body: formData
        }
    )

    if (response.status === 200) {
        alert("계정이 삭제되었다.")
    }
    else {
        alert("알 수 없는 문제가 발생하였다.")
    }

    location.href = "/admin"
})

// 서버에 유효한 값인지 판별 요청
async function checkValidation(valueName, eventTarget) {
    let isValid = false;
    console.log("sending value to server: " +  eventTarget.value)

    await fetch(`/user/update/check-${valueName}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Ensure the server understands the content type
        },
        body: JSON.stringify({
            username: idInput.value,
            value: eventTarget.value
        })
    })
        .then(res => res.text())
        .then(data => {
            const errorMsgBox = document.querySelector(`#${valueName}Box .error-msg`);
            console.log("received:" +  eventTarget.id, eventTarget.value)

            if (eventTarget.value!=="" && data === "invalid") {
                errorMsgBox.textContent = errMsg[valueName].fail
                eventTarget.classList.add('invalid-input');
                eventTarget.nextElementSibling.classList.add('invalid-input');
            }
            else if (!errMsg[valueName].pattern.test(eventTarget.value)) {
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
    password: {
        pattern: /^\S*$/,
        invalid: "관리자가 설정 시, 공백이 아닌 임의의 문자열을 입력하세요.",
        fail: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요.fail"
    },
    email: {
        pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
        invalid: "이메일 형식이 올바르지 않습니다.",
        fail: "이미 사용 중인 이메일 입니다."
    },
    name: {
        pattern: /\S+/,
        invalid: "이름을 작성해 주세요.",
        fail: "이름을 작성해 주세요.fail"
    },
    birth: {
        datePattern : /^(\d{4})(\d{2})(\d{2})$/,
        pattern: /^(?=\d)(?:(?:19\d\d|20\d\d)([-.\/])(?:1[012]|0?[1-9])\1(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29(?=.0?2.(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)(?:\s|$))|(?:2[0-8]|1\d|0?[1-9]))(?:(?=\s\d)\s|$))?(((0?[1-9]|1[012])(:[0-5]\d){0,2}(\s[AP]M))|([01]\d|2[0-3])(:[0-5]\d){1,2})?$/,
        invalid: "올바른 생년월일 8자리를 입력해 주세요.",
        fail: "올바른 생년월일 8자리를 입력해 주세요.fail"
    },
    tel: {
        pattern: /^\d{3}-\d{4}-\d{4}$/,
        hyphenPattern: /^(\d{2,3})(\d{3,4})(\d{4})$/,
        invalid: '전화번호를 확인해 주세요.',
        fail: "이미 사용 중인 전화번호 입니다."
    },
    sex: {
        pattern: /^(male|female)$/,
        invalid: "성별을 선택해주세요.",
        fail: "성별을 선택해주세요.fail"
    },
    year: {
        pattern: /^\d+$/,
        invalid: "숫자만 입력해 주세요.",
        fail: "숫자만 입력해 주세요.fail",
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
    },
    role: {
        pattern: /\S+/,
        invalid: "똑바로 입력해주세요.",
        fail: "똑바로 입력..fail"
    }
}



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
    document.querySelector(`#search-form > div > div > select[name='sex'] > option[value=${sex}]`).selected = true
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > select[name='sort'] > option[value=${sort}]`).selected = true
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > select[name='order'] > option[value=${order}]`).selected = true
} catch (e) {}
try {
    document.querySelector(`#search-form > div > div > select[name='role'] > option[value=${role}]`).selected = true
} catch (e) {}





/* 회원정보 수정하기 */
rows = document.querySelectorAll("#starter-section > div.container > div:nth-child(3) > table > tbody > tr")
editButtons = document.querySelectorAll(".edit-btn")


editButtons.forEach(v => v.addEventListener("click", (e) => {
    console.log(userForEdit.content[e.target.dataset.memberId]);
    let eachUser = userForEdit.content[e.target.dataset.memberId];


    pkInput.value = eachUser.no
    idInput.value = eachUser.username
    emailInput.value = eachUser.email
    nameInput.value = eachUser.name
    birthdayInput.value = eachUser.birth
    contactNumberInput.value = eachUser.tel
    cohortInput.value = eachUser.year
    zipcodeInput.value = eachUser.zipcode
    roadFullAddrInput.value = eachUser.roadAddress
    detailAddrInput.value = eachUser.detailAddress
// const submitBtn = document.querySelector('button[type="submit"]');
    genderInputs.forEach(v=>{
        console.log(eachUser.sex)
        if (v.value === eachUser.sex) v.checked = true;
    });

    roleInput.querySelector(`#role > option[value=${eachUser.role}]`).selected = true
}))





birthdayInput.onchange = (e) => {
    e.target.value = e.target.value
        .replace(/[^0-9]/g, '')
        .replace(errMsg.birth.datePattern, '$1/$2/$3')
}

contactNumberInput.onchange = (e) => {
    e.target.value = e.target.value
        .replace(/[^0-9]/g, '')
        .replace(errMsg.tel.hyphenPattern, `$1-$2-$3`);
}

