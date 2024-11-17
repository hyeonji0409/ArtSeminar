
/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"]:not([name="detailAddress"]), input[type="number"], input[type="email"], input[type="password"]');

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
const passwordInput = document.querySelector('input[name="password"]');
const emailInput = document.querySelector('input[name="email"]');
const nameInput = document.querySelector('input[name="name"]');
const birthdayInput = document.querySelector('input[name="birth"]');
const contactNumberInput = document.querySelector('input[name="tel"]');
const cohortInput = document.querySelector('input[name="year"]');
const roadFullAddrInput = document.querySelector('input[name="roadAddress"]');
const detailAddrInput = document.querySelector('input[name="detailAddress"]');
// const submitBtn = document.querySelector('button[type="submit"]');
const genderInputs = document.querySelectorAll('input[name="sex"]');



idInput.addEventListener('blur', (e) => {
    checkValidation("username", e.target);
})
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

// 성별 포커스 시 즉시 체크
genderInputs.forEach((v)=> v.addEventListener('focus', (e) => {e.target.checked= true }))




form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // 젠더는 트리 구조가 좀 달라서 일단 이렇게...
    function checkValidationGender() {
        const GenderChecked = document.querySelector('input[name="sex"]:checked');

        return (  document.querySelector(`#${"sex"}Box .error-msg`).textContent = GenderChecked ? null : errMsg["sex"].invalid ) === null;
    }


    // 제출 시에 유효성 검사와 무효한 인풋에 포커스
    if (
        ( await checkValidation("username", idInput) === false ? idInput.focus() : true)  &&
        ( await checkValidation("password", passwordInput) === false ? passwordInput.focus() : true) &&
        ( await checkValidation("email", emailInput) === false ? emailInput.focus() : true) &&
        ( await checkValidation("name", nameInput) === false ? nameInput.focus() : true) &&
        ( await checkValidation("birth", birthdayInput) === false ? birthdayInput.focus() : true) &&
        ( await checkValidation("tel", contactNumberInput) === false ? contactNumberInput.focus() : true) &&
        checkValidationGender() &&
        ( await checkValidation("year", cohortInput) === false ? cohortInput.focus() : true) &&
        ( await checkValidation("roadAddress", roadFullAddrInput) === false ? roadFullAddrInput.focus() : true) &&
        ( await checkValidation("roadAddress", detailAddrInput) === false ? detailAddrInput.focus() : true)
    ) {
        form.submit();
        alert("회원가입이 완료되었습니다.");
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

            if (eventTarget.value!=="" && data === "invalid") {
                errorMsgBox.textContent = errMsg[valueName].fail
                eventTarget.classList.add('invalid-input');
                eventTarget.nextElementSibling.classList.add('invalid-input');
            }
            else if (eventTarget.value==="" || !errMsg[valueName].pattern.test(eventTarget.value)) {
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
        pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/,
        invalid: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요.",
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
        pattern: /^(?=\d)(?:(?:1[6-9]|[2-9]\d)?\d\d([-.\/])(?:1[012]|0?[1-9])\1(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29(?=.0?2.(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)(?:\x20|$))|(?:2[0-8]|1\d|0?[1-9]))(?:(?=\x20\d)\x20|$))?(((0?[1-9]|1[012])(:[0-5]\d){0,2}(\x20[AP]M))|([01]\d|2[0-3])(:[0-5]\d){1,2})?$/,
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
    }
}



/* 주소 API */
/* key 24.08.25일 부터 9개월? 사용가능: devU01TX0FVVEgyMDI0MDgyNTEzMjQ1ODExNTAzNDc= */
function goPopup(){
    // 주소검색을 수행할 팝업 페이지를 호출합니다.
    // 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://business.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
    var pop = window.open("/juso/jusoPopup","pop",`width=570,height=420, scrollbars=yes, resizable=yes, left=${(window.screen.width-570)/2}, top=${(window.screen.height-420)/2}`);

    // 모바일 웹인 경우, 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://business.juso.go.kr/addrlink/addrMobileLinkUrl.do)를 호출하게 됩니다.
    //var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes");
}

function jusoCallBack(roadFullAddr, roadAddrPart1, addrDetail){
    // 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
    document.form.roadAddress.value = roadAddrPart1;
    // document.form.roadAddrPart1.value = roadAddrPart1;
    // document.form.roadAddrPart2.value = roadAddrPart2;
    document.form.detailAddress.value = addrDetail;
    // document.form.engAddr.value = engAddr;
    // document.form.jibunAddr.value = jibunAddr;
    // document.form.zipNo.value = zipNo;
    // document.form.admCd.value = admCd;
    // document.form.rnMgtSn.value = rnMgtSn;
    // document.form.bdMgtSn.value = bdMgtSn;
    // document.form.detBdNmList.value = detBdNmList;
    // /** 2017년 2월 추가제공 **/
    // document.form.bdNm.value = bdNm;
    // document.form.bdKdcd.value = bdKdcd;
    // document.form.siNm.value = siNm;
    // document.form.sggNm.value = sggNm;
    // document.form.emdNm.value = emdNm;
    // document.form.liNm.value = liNm;
    // document.form.rn.value = rn;
    // document.form.udrtYn.value = udrtYn;
    // document.form.buldMnnm.value = buldMnnm;
    // document.form.buldSlno.value = buldSlno;
    // document.form.mtYn.value = mtYn;
    // document.form.lnbrMnnm.value = lnbrMnnm;
    // document.form.lnbrSlno.value = lnbrSlno;
    // /** 2017년 3월 추가제공 **/
    // document.form.emdNo.value = emdNo;

    /* 주소 입력시 유효성 검사*/
    checkValidation("roadAddress", roadFullAddrInput);
    roadFullAddrInput.dispatchEvent(new Event('blur'));
}




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