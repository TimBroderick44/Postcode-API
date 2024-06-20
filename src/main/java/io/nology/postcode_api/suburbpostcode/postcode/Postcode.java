package io.nology.postcode_api.suburbpostcode.postcode;

import jakarta.persistence.*;
import java.util.Set;

import io.nology.postcode_api.suburbpostcode.suburb.Suburb;

import java.util.HashSet;

@Entity
@Table(name = "postcodes")
public class Postcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToMany(mappedBy = "postcodes", fetch = FetchType.EAGER)
    private Set<Suburb> suburb = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Suburb> getSuburbs() {
        return suburb;
    }

    public void setSuburbs(Set<Suburb> suburb) {
        this.suburb = suburb;
    }
}
