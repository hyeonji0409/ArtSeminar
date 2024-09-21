package fileETC;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="file")
@Getter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String savedFilename;
    private String savedPath;

    @Builder
    public File(Long id, String originalFilename, String savedFilename, String savedPath) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.savedFilename = savedFilename;
        this.savedPath = savedPath;
    }


}
