package com.artineer.artineer_renewal.dto;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JusoDto {
    private String inputYn; // 주소를 찾았는지, 그래서 부모 창 콜백함수을 호출할 것인지, 결국 팝업 창을 끄는지
    private String  roadFullAddr; // db에 저장할 예정인 풀네임 주소
    private String roadAddrPart1; // 폼에 보이게 할 수도 있는 도로명주소(예시. 전북특별자치도 익산시 익산대로460)
    private String addrDetail; // 폼에 보이게 할 수도 있는 직접 입력한 상세주소(예시. @@아파트 101동 2021)


    // 이하 불필요 정보들
    private String roadAddrPart2;
    private String engAddr;
    private String jibunAddr;
    private String zipNo;
    private String admCd;
    private String rnMgtSn;
    private String bdMgtSn;
    private String detBdNmList;
    private String bdNm;
    private String bdKdcd;
    private String siNm;
    private String sggNm;
    private String emdNm;
    private String liNm;
    private String rn;
    private String udrtYn;
    private String buldMnnm;
    private String buldSlno;
    private String mtYn;
    private String lnbrMnnm;
    private String lnbrSlno;
    private String emdNo;
}
