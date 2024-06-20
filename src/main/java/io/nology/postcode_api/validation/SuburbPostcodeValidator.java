package io.nology.postcode_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.regex.Pattern;

public class SuburbPostcodeValidator implements ConstraintValidator<ValidSuburbPostcode, Set<String>> {

    private static final Pattern SUBURB_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    private static final Pattern POSTCODE_PATTERN = Pattern.compile("\\d{4}");
    private String fieldType;

    @Override
    public void initialize(ValidSuburbPostcode constraintAnnotation) {
        this.fieldType = constraintAnnotation.fieldType();
    }

    @Override
    public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldType + " is required").addConstraintViolation();
            return false;
        }

        for (String value : values) {
            if (value == null || value.isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(fieldType + " cannot be blank").addConstraintViolation();
                return false;
            }
            if (fieldType.equals("Suburb")) {
                if (!SUBURB_PATTERN.matcher(value).matches()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Suburb must contain only letters and spaces").addConstraintViolation();
                    return false;
                }
            } else if (fieldType.equals("Postcode")) {
                if (!POSTCODE_PATTERN.matcher(value).matches()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Postcode must be 4 digits").addConstraintViolation();
                    return false;
                }
            }
        }
        return true;
    }
}
