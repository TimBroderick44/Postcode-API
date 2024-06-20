package io.nology.postcode_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SuburbPostcodeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSuburbPostcode {
    String message() default "Invalid suburb or postcode";
    String fieldType();
    // No idea what these are but reccommended to leave with default values
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
