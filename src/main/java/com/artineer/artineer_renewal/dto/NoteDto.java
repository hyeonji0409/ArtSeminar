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
public class NoteDto {
    private Long no;
    private String title;
    private String story;
    private int hit;
    private String name;
    private String pw;

    @ElementCollection
    private List<String> file;

    private int comment;

    private List<Comment> comments;

    private String regdate;
    private String ip;

    @Builder
    public NoteDto(Long no, String title, String story,String name,String pw, int hit, String regdate, List<String> file, List<Comment> comments) {
        this.no = no;
        this.title = title;
        this.story = story;
        this.name = name;
        this.pw = pw;
        this.hit = hit;
        this.regdate = regdate;
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
