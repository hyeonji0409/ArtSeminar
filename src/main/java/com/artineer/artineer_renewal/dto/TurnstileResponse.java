package com.artineer.artineer_renewal.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Data
@ToString
public class TurnstileResponse {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("error-codes")
    private List<String> errorCodes;
    @JsonProperty("challenge_ts")
    private Instant challengeTs;
    @JsonProperty("hostname")
    private String hostname;


    // todo 이거 왜 자동생성 안됨
    public boolean getSuccess(){
        return success;
    }
}
