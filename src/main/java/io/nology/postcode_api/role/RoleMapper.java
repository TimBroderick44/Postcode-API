package io.nology.postcode_api.role;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName().trim());
        return dto;
    }

    public static Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName().trim());
        return role;
    }
}
