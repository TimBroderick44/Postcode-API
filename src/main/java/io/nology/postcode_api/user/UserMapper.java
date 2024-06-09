package io.nology.postcode_api.user;

import io.nology.postcode_api.role.Role;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername().trim());        
        dto.setRoles(user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet()));
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());
        return user;
    }
}
