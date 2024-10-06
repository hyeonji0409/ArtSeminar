package com.artineer.artineer_renewal.dto;

import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class NoticeDto {

    private Long no;
    private String title;
    private String story;
    private int hit;

    @ElementCollection
    private List<String> file;

    private int comment;
    private String regdate;
    private String ip;

    private String username;
    private String name;
    private int year;

    @Builder
    public NoticeDto(Long no, String title, String story, int hit, List<String> file) {
        this.no = no;
        this.title = title;
        this.story = story;
        this.hit = hit;
        this.file = file;
    }

    public List<String> getFile(List<String> file) {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }


}
