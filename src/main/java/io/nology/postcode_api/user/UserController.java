package io.nology.postcode_api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/allUsers")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return UserMapper.toDTO(userService.getUserById(id));
    }

    @PostMapping("/admin/createUser")
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        return UserMapper.toDTO(userService.createUser(user));
    }

    @PatchMapping("users/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(currentUsername);

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        userService.checkUpdatePermission(currentUser, id, isAdmin);

        User userToUpdate = userService.getUserById(id);

        if (userDTO.getUsername() != null) {
            userToUpdate.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null) {
            userToUpdate.setPassword(userService.encodePassword(userDTO.getPassword()));
        }

        return UserMapper.toDTO(userService.updateUser(userToUpdate));
    }

    @PatchMapping("/admin/updateRole/{id}")
    public UserDTO updateUserRoles(@PathVariable Long id, @RequestBody Set<String> roles) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(currentUsername);

        boolean isUserAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        userService.checkAdminPermission(isUserAdmin, roles, currentUser);

        return UserMapper.toDTO(userService.updateUserRoles(id, roles));
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
