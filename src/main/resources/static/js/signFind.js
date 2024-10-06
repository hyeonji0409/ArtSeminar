
    console.log(whatWillFind)

const kk = document.querySelector("#starter-section > div.container")
const idAddr = document.querySelector("#idAddr")
const pwAddr = document.querySelector("#pwAddr")

kk.innerHTML = element(whatWillFind);



function element(whatWillFind) {
    let inner;


    // id 찾기는 email과 이름로 회원정보 찾고 email에 인증번호 발송
    // pw는 아이디만 입력하면 기록된 db에서 email에 인증번호 발송
    if (whatWillFind === 'id') {
        inner = `
            <form name="form" id="form">
          <div style="
          margin-top: 0;
          display: flex;
          justify-content: center;">
            <a href=${idAddr.href}>아이디 찾기</a>&nbsp&nbsp/&nbsp&nbsp<a href=${pwAddr.href}>비밀번호 찾기</a>
          </div>

          <div id="nameBox" class="inputBox">
            <input id="name" type="text" name="name">
            <label for="name">이름</label>
            <div class="error-msg"></div>
          </div>
          <div id="emailBox" class="inputBox">
            <input id="email" type="text" name="email">
            <label for="email">이메일</label>
            <div class="error-msg"></div>
          </div>
          
          <div id="emailBox" class="inputBox">
            <input id="injeungBox" type="text" name="injeung">
            <label for="injeung">인증번호</label>
            <div class="error-msg"></div>
          </div>

          <div style="text-align: center;" class="inputBox">
            <button type="submit">아이디 찾기</button>
          </div>
        </form>
        `;
    } else {
        inner = `
            <form name="form" id="form">
          <div style="
          margin-top: 0;
          display: flex;
          justify-content: center;">
            <a href=${idAddr.href}>아이디 찾기</a>&nbsp&nbsp/&nbsp&nbsp<a href=${pwAddr.href}>비밀번호 찾기</a>
          </div>\

          <div id="nameBox" class="inputBox">
            <input id="userIdBox" type="text" name="userId">
            <label for="userId">아이디</label>
            <div class="error-msg"></div>
          </div>
          <div id="emailBox" class="inputBox">
            <input id="injeungBox" type="text" name="injeung">
            <label for="injeung">인증번호</label>
            <div class="error-msg"></div>
          </div>

          <div style="text-align: center;" class="inputBox">
            <button type="submit">비밀번호 찾기</button>
          </div>
        </form>
        `;
    }

    return inner;
}



/* input placeholder animation*/
const inputBoxes = document.querySelectorAll('input[type="text"], input[type="password"]');

inputBoxes.forEach(v => v.addEventListener("change", (e) => {
    console.log(e.target.value)

    if (e.target.value) {
        e.target.nextElementSibling.classList.remove('blurred-label')
        e.target.nextElementSibling.classList.add('focused-label')
    }
    else {
        e.target.nextElementSibling.classList.remove('focused-label')
        e.target.nextElementSibling.classList.add('blurred-label')
    }
}))

/* 출처
* 개념: https://yozm.wishket.com/magazine/detail/120/
* 코드: https://code-study.tistory.com/21
* */





/* check submit form validation */
const form = document.querySelector('form');
const idInput = document.querySelector('input[name="userId"]');
const passwordInput = document.querySelector('input[name="password"]');
// const submitBtn = document.querySelector('button[type="submit"]');



// form.addEventListener('submit', async (e) => {
//     e.preventDefault();
//
//     fetch(
//         "sign-in",
//         {
//             method: "POST",
//             headers: {
//                 'Content-Type': 'application/json'  // Ensure the server understands the content type
//             },
//             body: JSON.stringify({userId: idInput.value, password: passwordInput.value})
//         })
//         .then(res => {
//         if (res.status === 200) {
//             // 로그인 실패
//             return res.text();
//         }
//         else if (res.status === 409) {
//             // 불가능
//             return res.text();
//         }
//         else throw new Error(`Status Code is ${res.status} : ${res.statusText}`);
//     })
//
//     form.submit();
//
// })


// 에러 메세지 객체들
// 출처 https://choiiis.github.io/web/toy-project-sign-up-and-in-page-3/
const errMsg = {
    userId: {
        pattern: /^(?=.*[a-z])[a-z\d]{5,20}$/,
        invalid: "5~20자의 영문 소문자, 숫자만 사용 가능합니다.",
        // success: "사용 가능한 아이디입니다.",
        fail: "이미 사용 중인 아이디입니다."
    },
    password: {
        pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/,
        invalid: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요.",
        fail: "8~20자의 영문 대소문자, 숫자와 특수기호를 이용해주세요."
    },
    email: {
        pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
        invalid: "이메일 형식이 올바르지 않습니다.",
        fail: "이미 사용 중인 이메일 입니다."
    },
    name: {
        pattern: /\S+/,
        invalid: "이름을 작성해 주세요.",
        fail: "이름을 작성해 주세요."
    },
    birthday: {
        pattern: /^(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$/,
        invalid: "올바른 생년월일 8자리를 입력해 주세요",
        fail: "올바른 생년월일 8자리를 입력해 주세요"
    },
    contactNumber: {
        pattern: /^010-?\d{4}-?\d{4}$/,
        invalid: "전화번호를 확인해 주세요.",
        fail: "이미 사용 중인 전화번호 입니다."
    },
    cohort: {
        pattern: /^\d+$/,
        invalid: "숫자만 입력해 주세요.",
        fail: "숫자만 입력해 주세요.f",
    },
    roadFullAddr: {
        pattern: /\S+/,
        invalid: "'찾기' 버튼으로 주소를 입력해 주세요.",
        fail: "'찾기' 버튼으로 주소를 입력해 주세요."
    }
}
