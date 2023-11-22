package project.demo.domain.bord;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
public class Bord {

    public Bord() {
    }

    public Bord(String userName, String title, String body) {
        this.userName = userName;
        this.title = title;
        this.body = body;
    }

    public Bord(String userName, String title, String body, long memberId) {
        this.userName = userName;
        this.title = title;
        this.body = body;
        this.memberId=memberId;
    }

    private String userName;
    private  LocalDateTime localDateTime;
    private long id;
    private long memberId;
    private MultipartFile attachFile;

    @NotBlank
    @Size(max=20)
    private String title;
    @NotBlank
    @Size(min = 10, max = 1000)
    private String body;

    private Boolean open;  //공개 비공개 여부
    private List<String> regions;  //등록 지역
    private BordType bordType;  //글 종류
    private String location;  //???


}
