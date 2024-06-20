package io.nology.postcode_api.suburbpostcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.nology.postcode_api.suburbpostcode.postcode.Postcode;
import io.nology.postcode_api.suburbpostcode.postcode.PostcodeRepository;
import io.nology.postcode_api.suburbpostcode.suburb.Suburb;
import io.nology.postcode_api.suburbpostcode.suburb.SuburbRepository;

import java.util.Set;

@Component
public class SuburbPostcodeUtils {

    @Autowired
    private SuburbRepository suburbRepository;

    @Autowired
    private PostcodeRepository postcodeRepository;

    // public void deleteSuburbAndOrphanedPostcodes(Suburb suburb) {
    //     removeOrphanedPostcodes(suburb.getPostcodes(), suburb);
    //     suburbRepository.delete(suburb);
    // }

    public void removeOrphanedSuburbs(Set<Suburb> oldSuburbs, Postcode postcode) {
        oldSuburbs.forEach(suburb -> {
            suburb.getPostcodes().remove(postcode);
            if (suburb.getPostcodes().isEmpty()) {
                suburbRepository.delete(suburb);
            } else {
                suburbRepository.save(suburb);
            }
        });
    }

    public void removeOrphanedPostcodes(Set<Postcode> oldPostcodes, Suburb suburb) {
        oldPostcodes.forEach(postcode -> {
            postcode.getSuburbs().remove(suburb);
            if (postcode.getSuburbs().isEmpty()) {
                postcodeRepository.delete(postcode);
            } else {
                postcodeRepository.save(postcode);
            }
        });
    }
}