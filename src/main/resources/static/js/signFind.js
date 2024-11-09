
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
            <form name="form" id="form" action="/email/verification" method="post">
          <div style="
          margin-top: 0;
          display: flex;
          justify-content: center;
          width: auto">
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
          
<!--          <div id="emailBox" class="inputBox">-->
<!--            <input id="injeungBox" type="text" name="injeung">-->
<!--            <label for="injeung">인증번호</label>-->
<!--            <div class="error-msg"></div>-->
<!--          </div>-->

          <div style="text-align: center;" class="inputBox">
          <input type="hidden" name="what" value="username">
            <button id="formBtn" type="button">아이디 찾기</button>
          </div>
        </form>
        `;
    } else {
        inner = `
            <form name="form" id="form" action="/email/verification" method="post">
          <div style="
          margin-top: 0;
          display: flex;
          justify-content: center;
          width: auto;">
            <a href=${idAddr.href}>아이디 찾기</a>&nbsp&nbsp/&nbsp&nbsp<a href=${pwAddr.href}>비밀번호 찾기</a>
          </div>

          <div id="nameBox" class="inputBox">
            <input id="name" type="text" name="name">
            <label for="name">이름</label>
            <div class="error-msg"></div>
          </div>
          <div id="emailBox" class="inputBox">
            <input id="email" type="text" name="email" style="width: 75%">
            <label for="email">이메일</label>
            <button id="sendBtn" type="button" style="display: inline; width: 23%;">발송</button>
            <div class="error-msg"></div>
          </div>
          
          <div id="emailBox" class="inputBox">
            <input id="injeungBox" type="text" name="injeung">
            <label for="injeung">인증번호</label>
            <div class="error-msg"></div>
          </div>

          <div style="text-align: center;" class="inputBox">
          <input type="hidden" name="what" value="password">
            <button id="formBtn" type="button">비밀번호 찾기</button>
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


formBtn = document.querySelector("#formBtn");

sendBtn = document.querySelector("#sendBtn");
try {
    sendBtn.addEventListener('click', (e) => {
        sendBtn.style.fontSize = "16px"
        sendBtn.innerHTML = "발송중"
        sendBtn.style.backgroundColor = "var(--hover)"

        const formdata = new FormData(form);

        fetch("/email/send", {
            method: "POST",
            body: formdata
        }).then(res=> {
            console.log(res);
            if (res.status === 200) {
                alert(`${formdata.get("email")}(으)로 인증번호가 발송되었습니다.`)
                sendBtn.innerHTML = "발송됨"

            }
            else {
                alert("회원 정보를 찾을 수 없습니다.")
                sendBtn.innerHTML = "발송"
            }
            sendBtn.style.backgroundColor = "var(--subtle)"
        }
        )
    })
} catch (e) {}


formBtn.addEventListener('click', (e) => {

    const formdata = new FormData(form);

    fetch("/email/verification", {
        method: "POST",
        body: formdata
    }).then(res=> res.json()
    ).then(data => {
        console.log(data);
        if (data.username) {
            alert(`아이디는 "${data.username}" 입니다.`);
            return;
        }
        if (data.code === 200) {
            alert(`"${data.password}"으로 임시 비밀번호가 설정되었습니다.`)
        }
        else {
            alert("인증번호가 올바르지 않습니다.")
        }

    })
})



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
