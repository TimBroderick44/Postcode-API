package io.nology.postcode_api.suburbpostcode;

import io.nology.postcode_api.exceptions.NotFoundException;
import io.nology.postcode_api.suburbpostcode.postcode.Postcode;
import io.nology.postcode_api.suburbpostcode.postcode.PostcodeRepository;
import io.nology.postcode_api.suburbpostcode.suburb.Suburb;
import io.nology.postcode_api.suburbpostcode.suburb.SuburbRepository;
import io.nology.postcode_api.suburbpostcode.utils.SuburbPostcodeUtils;
import io.nology.postcode_api.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SuburbPostcodeService {

    @Autowired
    private SuburbRepository suburbRepository;

    @Autowired
    private PostcodeRepository postcodeRepository;

    @Autowired
    private SuburbPostcodeUtils utils;

    public List<Suburb> getAllSuburbs() {
        return suburbRepository.findAll();
    }

    public SuburbPostcodeCreateDTO getSuburbsByPostcodeDTO(String code) {
        Postcode postcode = postcodeRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Postcode not found: " + code));
        return SuburbPostcodeMapper.toDTO(postcode);
    }

    public SuburbPostcodeCreateDTO getPostcodesBySuburbDTO(String name) {
        Suburb suburb = suburbRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Suburb not found: " + name));
        return SuburbPostcodeMapper.toDTO(suburb);
    }

    @Transactional
    public void addSuburbPostcode(SuburbPostcodeCreateDTO dto) {
        if (dto.getSuburb().isEmpty() || dto.getPostcode().isEmpty()) {
            throw new BadRequestException("Suburb names and postcodes cannot be empty");
        }

        if (dto.getSuburb().size() > 1 && dto.getPostcode().size() > 1) {
            throw new BadRequestException("You cannot add several suburbs with several postcodes");
        }

        for (String suburb : dto.getSuburb()) {
            if (suburbRepository.findByName(suburb).isPresent()) {
                throw new BadRequestException("Suburb already exists: " + suburb + ". Please update instead!");
            }
        }

        for (String postcode : dto.getPostcode()) {
            if (postcodeRepository.findByCode(postcode).isPresent()) {
                throw new BadRequestException("Postcode already exists: " + postcode + ". Please update instead!");
            }
        }

        Set<Postcode> postcodes = dto.getPostcode().stream()
                .map(code -> postcodeRepository.findByCode(code)
                        .orElseGet(() -> postcodeRepository.save(SuburbPostcodeMapper.toEntity(code))))
                .collect(Collectors.toSet());

        // Admin should update existing suburbs, etc. but just in case, if they somehow add a suburb that already exists,
        // I won't override existing relationships, but add the postcodes to the existing suburb

        for (String suburbName : dto.getSuburb()) {
            Optional<Suburb> existingSuburb = suburbRepository.findByName(suburbName);
            if (existingSuburb.isPresent()) {
                Suburb suburb = existingSuburb.get();
                suburb.getPostcodes().addAll(postcodes);
                suburbRepository.save(suburb);
            } else {
                Suburb suburb = SuburbPostcodeMapper.toEntity(suburbName, postcodes);
                suburbRepository.save(suburb);
            }
        }
    }

    @Transactional
    public void deleteSuburbPostcode(String code, String name) {
        if (code != null) {
            Postcode postcode = postcodeRepository.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Postcode not found: " + code));
            utils.removeOrphanedSuburbs(postcode.getSuburbs(), postcode);
            postcodeRepository.delete(postcode);
        } else if (name != null) {
            Suburb suburb = suburbRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException("Suburb not found: " + name));
            utils.removeOrphanedPostcodes(suburb.getPostcodes(), suburb);
            suburbRepository.delete(suburb);
        } else {
            throw new BadRequestException("Either postcode or suburb must be provided");
        }
    }

    @Transactional
    public SuburbPostcodeCreateDTO updateSuburbOrPostcode(String postcodeCode, String suburbName, SuburbPostcodeUpdateDTO dto) {
        if (suburbName != null && !suburbName.isEmpty()) {
            return updateSuburb(suburbName, dto);
        } else if (postcodeCode != null && !postcodeCode.isEmpty()) {
            return updatePostcode(postcodeCode, dto);
        } else {
            throw new BadRequestException("Either postcode or suburb must be provided");
        }
    }

    private SuburbPostcodeCreateDTO updateSuburb(String suburbName, SuburbPostcodeUpdateDTO dto) {
        Suburb existingSuburb = suburbRepository.findByName(suburbName)
                .orElseThrow(() -> new NotFoundException("Suburb not found: " + suburbName));

        if (dto.getPostcode() != null && !dto.getPostcode().isEmpty()) {
            Set<Postcode> newPostcodes = dto.getPostcode().stream()
                    .map(code -> postcodeRepository.findByCode(code)
                            .orElseGet(() -> postcodeRepository.save(SuburbPostcodeMapper.toEntity(code))))
                    .collect(Collectors.toSet());
            existingSuburb.setPostcodes(newPostcodes);
        }

        if (dto.getSuburb() != null && !dto.getSuburb().isEmpty()) {
            if (dto.getSuburb().size() > 1) {
                throw new BadRequestException("You cannot add additional suburbs to a suburb. Update the postcode instead.");
            }
            // Gets the first element of the set and sets it with the new name
            existingSuburb.setName(dto.getSuburb().iterator().next());
        }

        Suburb updatedSuburb = suburbRepository.save(existingSuburb);
        return SuburbPostcodeMapper.toDTO(updatedSuburb);
    }

    private SuburbPostcodeCreateDTO updatePostcode(String postcodeCode, SuburbPostcodeUpdateDTO dto) {
        Postcode existingPostcode = postcodeRepository.findByCode(postcodeCode)
                .orElseThrow(() -> new NotFoundException("Postcode not found: " + postcodeCode));

        if (dto.getPostcode() != null && !dto.getPostcode().isEmpty()) {
            if (dto.getPostcode().size() > 1) {
                throw new BadRequestException("You cannot add additional postcodes to a postcode. Update the suburb instead.");
            }
            existingPostcode.setCode(dto.getPostcode().iterator().next());
        }

        if (dto.getSuburb() != null && !dto.getSuburb().isEmpty()) {
            Set<Suburb> newSuburbs = dto.getSuburb().stream()
                    .map(name -> suburbRepository.findByName(name)
                            .orElseGet(() -> suburbRepository.save(SuburbPostcodeMapper.toEntity(name, Set.of(existingPostcode)))))
                    .collect(Collectors.toSet());
            existingPostcode.setSuburbs(newSuburbs);
        }

        Postcode updatedPostcode = postcodeRepository.save(existingPostcode);
        return SuburbPostcodeMapper.toDTO(updatedPostcode);
    }
}
