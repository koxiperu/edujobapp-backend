package lu.cnfpc.edujobapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginUserRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public LoginUserRequestDto() {
    }   
    public LoginUserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
