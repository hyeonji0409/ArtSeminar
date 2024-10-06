// birth
const birthYearEl = document.querySelector('#birth-year')
isYearOptionExisted = false;
birthYearEl.addEventListener('focus', function () {
    // != year
    if (!isYearOptionExisted) {
        isYearOptionExisted = true
        for (var year = 1990; year <= 2020; year++) {
            const YearOption = document.createElement('option')
            YearOption.setAttribute('value', year)
            YearOption.innerText = year
            this.appendChild(YearOption)
        }
    }
});

const birthMonth = document.querySelector('#birth-month')
isMonthOptionExisted = false;
birthMonth.addEventListener('focus', function () {
    if (!isMonthOptionExisted) {
        isMonthOptionExisted = true
        for (var month = 1; month <= 12; month++) {
            const MonthOption = document.createElement('option')
            MonthOption.setAttribute('value', month)
            MonthOption.innerText = month
            this.appendChild(MonthOption)
        }
    }
});

const birthDay = document.querySelector('#birth-day')
isDayOptionExisted = false;
birthDay.addEventListener('focus', function () {
    if (!isDayOptionExisted) {
        isDayOptionExisted = true
        for (var day = 1; day <= 31; day++) {
            const DayOption = document.createElement('option')
            DayOption.setAttribute('value', day)
            DayOption.innerText = day
            this.appendChild(DayOption)
        }
    }

});

function Inputbirth() {
    const birthYear = document.querySelector("#birth-year").value;
    const birthMonth = document.querySelector("#birth-month").value;
    const birthDay = document.querySelector("#birth-day").value;

    if (birthYear && birthMonth && birthDay) {
        const fullDate = `${birthYear}/${birthMonth.padStart(2, '0')}/${birthDay.padStart(2, '0')}`;
        console.log("Selected Birthdate:", fullDate);
        // 여기서 fullDate를 서버로 전송하거나, 다른 처리를 할 수 있습니다.
        document.getElementById('birth').value = fullDate;

    } else {
        console.log("Please select all fields");
    }
}

// 기수 계산
document.addEventListener('DOMContentLoaded', function () {
    var currentYear = new Date().getFullYear();
    var joinYear = currentYear;

    var level = currentYear - 2017 + 1;

    document.getElementById('year').value = level;
})


// 비밀먼호 확인
$("#password,#chk-password").keyup(function () {
    let password = $('#password').val();
    let chk_password = $('#chk-password').val();

    if (password =='' && chk_password =='') {
        $('#pwConfirm').text("8~16자의 영문 대소문자, 숫자, 특수문자로 입력하세요.");
        return;
    }

    if (password.length >= 8 && chk_password.length >= 8) {
        if (password == chk_password) {
            $('#pwConfirm').text("비밀번호가 일치합니다.").css('color', 'green');
            return;
        }
        if (password != chk_password) {
            $('#pwConfirm').text("비밀번호가 일치하지 않습니다.").css('color', 'red');
            return;
        }
    }

});

$('#joinForm').submit(function () {
    let password = $('#password').val();
    let chk_password = $('#chk-password').val();

    if (password != chk_password) {
        alert("비밀번호가 일치하지 않습니다.")
        $('#password').val('').focus();
        $('#chk-password').val('');
        return false;
    }

    if (password.trim()=='') {
        alert("비밀번호를 입력하세요.");
        $('#password').val('').focus();
    }

    let right_pw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/;

    if(!right_pw.test(password)) {
        alert("8~16자의 영문 대소문자, 숫자, 특수문자로 입력하세요.");
        $('#password').val('').focus();
        $('#chk-password').val('');
        return false;
    }

})