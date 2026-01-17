package lu.cnfpc.edujobapp.dto.response;

public class AuthUserResponseDto {
    private String token;
    private String type = "Bearer";  // Token type
    private Long userId;
    private String username;
    private String role;

    // Constructors
    public AuthUserResponseDto() {  
    }
    public AuthUserResponseDto(String token, Long userId, String username, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
    // Getters and Setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
}
