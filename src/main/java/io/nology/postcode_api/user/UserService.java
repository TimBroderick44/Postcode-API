package io.nology.postcode_api.user;

import io.nology.postcode_api.exceptions.NotFoundException;
import io.nology.postcode_api.exceptions.BadRequestException;
import io.nology.postcode_api.role.RoleRepository;
import io.nology.postcode_api.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role not found: ROLE_USER"));

        Role managedUserRole = roleRepository.findById(userRole.getId())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userRole.getId()));

        user.setRoles(Set.of(managedUserRole));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Set<Role> managedRoles = user.getRoles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new NotFoundException("Role not found: " + role.getName())))
                .collect(Collectors.toSet());

        user.setRoles(managedRoles);
        return userRepository.save(user);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User updateUserRoles(Long userId, Set<String> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Set<Role> roleEntities = roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new NotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roleEntities);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (isAdmin) {
            throw new BadRequestException("Cannot delete an admin user");
        }

        userRepository.delete(user);
    }

    public void checkUpdatePermission(User currentUser, Long id, boolean isAdmin) {
        if (!currentUser.getId().equals(id) && !isAdmin) {
            throw new BadRequestException("You do not have permission to update this user");
        }
    }
    
    public void checkAdminPermission(boolean isUserAdmin, Set<String> roles, User currentUser) {
        if (!isUserAdmin) {
            throw new BadRequestException("You do not have permission to update user roles");
        }
    
        User userUpdate = getUserById(currentUser.getId());
    
        if (!roles.contains("ROLE_ADMIN") && userUpdate.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new BadRequestException("Cannot remove admin role from user");
        }
    
        if (roles.contains("ROLE_ADMIN") && !isUserAdmin) {
            throw new BadRequestException("Only admins can add admin role");
        }
    }
}
