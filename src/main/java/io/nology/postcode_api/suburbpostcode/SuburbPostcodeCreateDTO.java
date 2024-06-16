package io.nology.postcode_api.suburbpostcode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public class SuburbPostcodeCreateDTO {
    private Long id;

    @NotNull(message = "Suburb name is required")
    private Set<String> suburb;

    @NotNull(message = "Postcodes are required")
    private Set<@Pattern(regexp = "\\d{4}", message = "Postcode must be 4 digits") String> postcode;

    private int count;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getSuburb() {
        return suburb;
    }

    public void setSuburbName(Set<String> suburb) {
        this.suburb = suburb;
    }

    public Set<String> getPostcode() {
        return postcode;
    }

    public void setPostcode(Set<String> postcode) {
        this.postcode = postcode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
