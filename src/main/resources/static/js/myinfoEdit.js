
// '수정' 버튼 클릭 시 disabled 해제
document.getElementById('editbtn').addEventListener('click', function() {
    // 모든 input 요소를 선택
    const inputs = document.querySelectorAll('.info__items input');

    // 각 input 요소의 disabled 속성을 false로 설정
    inputs.forEach(function(input) {
        // input.disabled = false;
        input.readOnly = false;
    });

    // 수정 버튼 숨기기
    document.getElementById('editbtn').style.display = 'none';

    // 필요하면 '완료'와 '취소' 버튼을 보이도록 설정
    document.querySelector('.editPageBtn').style.display = 'block';
});

// '확인' 버튼 클릭 시 '수정'버튼 및 readOnly 재설정
document.getElementById('savebtn').addEventListener('click', function() {
    // 모든 input 요소를 선택
    const inputs = document.querySelectorAll('.info__items input');

    // 각 input 요소의 readOnly 속성을 true 설정
    inputs.forEach(function(input) {
        input.readOnly = true;
    });

    document.getElementById('form').submit();

    // 수정 버튼 숨기기
    document.getElementById('editbtn').style.display = 'block';

    // 필요하면 '완료'와 '취소' 버튼을 보이도록 설정
    document.querySelector('.editPageBtn').style.display = 'none';
});

document.getElementById('canclebtn').addEventListener('click', function() {
    const inputs = document.querySelectorAll('.info__items input');

    inputs.forEach(function(input) {
        input.readOnly = true;
    });

    document.getElementById('editbtn').style.display = 'block';
    document.querySelector('.editPageBtn').style.display = 'none';
});

// 데이터베이스에서 sex 값을 가져왔다고 가정
const sexFromDB = "남자";  // 남자 또는 여자 값이 들어옴

// sex 값에 따라 라디오 버튼 체크
if (sexFromDB === "남자") {
    document.getElementById("male").checked = true;
} else if (sexFromDB === "여자") {
    document.getElementById("female").checked = true;
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
    roadFullAddrInput.dispatchEvent(new Event('change'));
}

