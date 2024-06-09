package io.nology.postcode_api.suburbpostcode;

import io.nology.postcode_api.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SuburbPostcodeService {

    @Autowired
    private SuburbPostcodeRepository repository;

    public List<SuburbPostcode> getAllSuburbs() {
        return repository.findAll();
    }

    public SuburbPostcode getSuburbByPostcode(String postcode) {
        return repository.findByPostcode(postcode)
                .orElseThrow(() -> new NotFoundException("Suburb not found for postcode: " + postcode));
    }

    public SuburbPostcode getPostcodeBySuburb(String suburb) {
        return repository.findBySuburb(suburb)
                .orElseThrow(() -> new NotFoundException("Postcode not found for suburb: " + suburb));
    }

    public SuburbPostcode addSuburbPostcode(SuburbPostcode suburbPostcode) {
        return repository.save(suburbPostcode);
    }

    @Transactional
    public boolean deleteSuburbPostcode(String postcode, String suburb) {
        Optional<SuburbPostcode> suburbPostcode = Optional.empty();
        if (postcode != null) {
            suburbPostcode = repository.findByPostcode(postcode);
        } else if (suburb != null) {
            suburbPostcode = repository.findBySuburb(suburb);
        }

        if (suburbPostcode.isPresent()) {
            repository.delete(suburbPostcode.get());
            return true;
        }
        throw new NotFoundException(postcode + suburb + " not found");
    }

    @Transactional
    public SuburbPostcode updateSuburbPostcode(String postcode, String suburb, SuburbPostcode updatedSuburbPostcode) {
        Optional<SuburbPostcode> suburbPostcode = Optional.empty();
        if (postcode != null) {
            suburbPostcode = repository.findByPostcode(postcode);
        } else if (suburb != null) {
            suburbPostcode = repository.findBySuburb(suburb);
        }
    
        if (suburbPostcode.isPresent()) {
            SuburbPostcode existingSuburbPostcode = suburbPostcode.get();
            if (updatedSuburbPostcode.getSuburb() != null) {
                existingSuburbPostcode.setSuburb(updatedSuburbPostcode.getSuburb());
            }
            if (updatedSuburbPostcode.getPostcode() != null) {
                existingSuburbPostcode.setPostcode(updatedSuburbPostcode.getPostcode());
            }
            repository.save(existingSuburbPostcode);
            return existingSuburbPostcode;
        }
        throw new NotFoundException("Suburb/Postcode not found");
    }
}
