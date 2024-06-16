package io.nology.postcode_api.suburbpostcode;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SuburbRepository extends JpaRepository<Suburb, Long> {
    Optional<Suburb> findByName(String name);
}
