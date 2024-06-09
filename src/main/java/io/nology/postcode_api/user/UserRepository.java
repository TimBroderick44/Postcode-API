package io.nology.postcode_api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import io.nology.postcode_api.role.Role;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  long countByRoles(Role role); 
}
