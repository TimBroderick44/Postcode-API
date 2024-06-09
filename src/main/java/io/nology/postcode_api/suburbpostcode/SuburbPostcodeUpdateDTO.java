package io.nology.postcode_api.suburbpostcode;

import jakarta.validation.constraints.Pattern;

public class SuburbPostcodeUpdateDTO {
  private long id;

  private String suburb;

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
