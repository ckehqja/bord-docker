package project.demo.domain.Member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Member {

    long id;
    @NotBlank
    String userId;
    @NotEmpty
    String password;
    @NotEmpty
    String userName;


    public Member() {
    }

    public Member(String userId, String password, String userName) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
    }
}
