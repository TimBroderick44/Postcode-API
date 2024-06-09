package io.nology.postcode_api.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles().stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/get")
    public ResponseEntity<RoleDTO> getRoleByName(@RequestParam String role) {
        Role roleEntity = roleService.getRoleByName(role);
        return ResponseEntity.ok(RoleMapper.toDTO(roleEntity));
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        Role role = RoleMapper.toEntity(roleDTO);
        return RoleMapper.toDTO(roleService.createRole(role));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRole(@RequestParam String role) {
        roleService.deleteRoleByName(role);
        return ResponseEntity.ok().build();
    }
}
