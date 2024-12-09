package com.artineer.artineer_renewal.dto;

import com.artineer.artineer_renewal.entity.Comment;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
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

    private List<Comment> comments;

    private String regdate;
    private String ip;

    private String username;
    private String name;
    private int year;

    @Builder
    public ReferenceDto(Long no, String title, String story, int hit, String username,String name,int year, String regdate, List<String> file, List<Comment> comments) {
        this.no = no;
        this.title = title;
        this.story = story;
        this.hit = hit;
        this.username = username;
        this.name = name;
        this.regdate = regdate;
        this.year = year;
        this.file = file;
        this.comments = comments;
    }

    public List<String> getFile(List<String> file) {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }


}
