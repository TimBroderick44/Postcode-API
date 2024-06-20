package io.nology.postcode_api.suburbpostcode;

import java.util.Set;

import io.nology.postcode_api.validation.ValidSuburbPostcode;

public class SuburbPostcodeCreateDTO {
    private Long id;

    @ValidSuburbPostcode(fieldType = "Suburb")    
    private Set<String> suburb;

    @ValidSuburbPostcode(fieldType = "Postcode")
    private Set<String> postcode;

//     private Set<@Pattern(regexp = "^[a-zA-Z ]+$", message = "Suburb must contain only letters") 
//     @NotBlank(message = "Suburb name cannot be blank") 
//     String> suburb;

// private Set<@Pattern(regexp = "\\d{4}", message = "Postcode must be 4 digits") 
//     @NotBlank(message = "Postcode cannot be blank") 
//     String> postcode;

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
