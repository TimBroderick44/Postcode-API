package io.nology.postcode_api.role;

import jakarta.validation.constraints.NotBlank;

public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
