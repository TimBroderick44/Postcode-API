package io.nology.postcode_api.role;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Set<Role> findByNameIn(Set<String> names);
}
