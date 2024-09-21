package com.artineer.artineer_renewal.dto;

import jakarta.persistence.ElementCollection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ReferenceDto {

    private Long no;
    private String title;
    private String story;
    private int hit;

    @ElementCollection
    private List<String> file;

    private int comment;
    private String regdate;
    private String ip;


    // file 처리
    public List<String> getFile(List<String> file) {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }


}
