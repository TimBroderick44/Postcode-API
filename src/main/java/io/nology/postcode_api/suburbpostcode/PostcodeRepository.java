package io.nology.postcode_api.suburbpostcode;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostcodeRepository extends JpaRepository<Postcode, Long> {
    Optional<Postcode> findByCode(String code);
}
