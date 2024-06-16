package io.nology.postcode_api.user;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDTO {
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter and no whitespace")
    private String password;

    @NotBlank(message = "Roles are required")
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}