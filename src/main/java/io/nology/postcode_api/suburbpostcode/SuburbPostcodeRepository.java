package io.nology.postcode_api.suburbpostcode;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SuburbPostcodeRepository extends JpaRepository<SuburbPostcode, Long> {
    Optional<SuburbPostcode> findBySuburb(String suburb);
    Optional<SuburbPostcode> findByPostcode(String postcode);
}
