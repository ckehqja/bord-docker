package project.demo.web.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JoinDTO {
    @NotEmpty
    String userId;
    @NotEmpty
    String password;
    @NotEmpty
    String userName;
}
