package io.nology.postcode_api.role;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.nology.postcode_api.exceptions.NotFoundException;
import io.nology.postcode_api.exceptions.BadRequestException;

import java.util.List;
import io.nology.postcode_api.user.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository UserRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Role not found with name: " + name));
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Role not found with name: " + name));

        long userCount = UserRepository.countByRoles(role);
        if (userCount > 0) {
            throw new BadRequestException("Cannot delete role with users assigned to it");
        }

        roleRepository.delete(role);
    }
}
