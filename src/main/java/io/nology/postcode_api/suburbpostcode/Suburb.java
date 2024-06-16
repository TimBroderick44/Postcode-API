package io.nology.postcode_api.suburbpostcode;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "suburbs")
public class Suburb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "suburbs_postcodes",
        joinColumns = @JoinColumn(name = "suburb_id"),
        inverseJoinColumns = @JoinColumn(name = "postcode_id")
    )
    private Set<Postcode> postcodes = new HashSet<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Postcode> getPostcodes() {
        return postcodes;
    }

    public void setPostcodes(Set<Postcode> postcodes) {
        this.postcodes = postcodes;
    }
}
