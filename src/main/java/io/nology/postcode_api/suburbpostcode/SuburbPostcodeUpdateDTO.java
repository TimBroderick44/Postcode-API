package io.nology.postcode_api.suburbpostcode;

import jakarta.validation.constraints.Pattern;
import java.util.Set;

public class SuburbPostcodeUpdateDTO {
    private Set<String> suburb;

    private Set<@Pattern(regexp = "\\d{4}", message = "Postcode must be 4 digits") String> postcode;

    public Set<String> getSuburb() {
        return suburb;
    }

    public void setSuburbs(Set<String> suburb) {
        this.suburb = suburb;
    }

    public Set<String> getPostcode() {
        return postcode;
    }

    public void setPostcodes(Set<String> postcode) {
        this.postcode = postcode;
    }
}
