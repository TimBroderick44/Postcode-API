package io.nology.postcode_api.suburbpostcode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SuburbPostcodeCreateDTO {
    private Long id;

    @NotNull(message = "Suburb is required")
    private String suburb;

    @NotNull(message = "Postcode is required")
    @Pattern(regexp = "\\d{4}", message = "Postcode must be 4 digits")    
    private String postcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
