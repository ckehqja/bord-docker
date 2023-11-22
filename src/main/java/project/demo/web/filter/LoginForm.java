package project.demo.web.filter;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {
    @NotEmpty
    String userId;
    @NotEmpty
    String password;
}
