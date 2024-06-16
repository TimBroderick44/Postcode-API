package io.nology.postcode_api.suburbpostcode;

import io.nology.postcode_api.exceptions.NotFoundException;
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

    // public List<Postcode> getAllPostcodes() {
    //     return postcodeRepository.findAll();
    // }

    // public Set<Suburb> getSuburbsByPostcode(String code) {
    //     Postcode postcode = postcodeRepository.findByCode(code)
    //             .orElseThrow(() -> new NotFoundException("Suburb not found for postcode: " + code));
    //     return postcode.getSuburbs();
    // }

    // public Suburb getPostcodesBySuburb(String name) {
    //     return suburbRepository.findByName(name)
    //             .orElseThrow(() -> new NotFoundException("Postcode not found for suburb: " + name));
    // }

    // public Long getPostcodeIdByCode(String code) {
    //     return postcodeRepository.findByCode(code)
    //             .orElseThrow(() -> new NotFoundException("Postcode not found: " + code))
    //             .getId();
    // }

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

    // @Transactional
    // public void addSuburbPostcode(SuburbPostcodeCreateDTO dto) {
    // Set<Postcode> postcodes = dto.getPostcode().stream()
    // .map(code -> postcodeRepository.findByCode(code)
    // .orElseGet(() ->
    // postcodeRepository.save(SuburbPostcodeMapper.toEntity(code))))
    // .collect(Collectors.toSet());

    // for (String suburbName : dto.getSuburb()) {
    // Optional<Suburb> existingSuburb = suburbRepository.findByName(suburbName);
    // if (existingSuburb.isPresent()) {
    // Suburb suburb = existingSuburb.get();
    // suburb.getPostcodes().addAll(postcodes);
    // suburbRepository.save(suburb);
    // } else {
    // Suburb suburb = SuburbPostcodeMapper.toEntity(suburbName, postcodes);
    // suburbRepository.save(suburb);
    // }
    // }
    // }

    @Transactional
    public void addSuburbPostcode(SuburbPostcodeCreateDTO dto) {
        // Validate input
        if (dto.getSuburb().isEmpty()) {
            throw new BadRequestException("Suburb names cannot be empty");
        }

        if (dto.getPostcode().isEmpty()) {
            throw new BadRequestException("Postcodes cannot be empty");
        }

        if (dto.getSuburb().size() > 1 && dto.getPostcode().size() > 1) {
            throw new BadRequestException("You cannot add several suburbs with several postcodes");
        }

        Set<String> existingSuburbs = suburbRepository.findAll().stream()
                .map(Suburb::getName)
                .collect(Collectors.toSet());

        Set<String> existingPostcodes = postcodeRepository.findAll().stream()
                .map(Postcode::getCode)
                .collect(Collectors.toSet());

        StringBuilder errorMessage = new StringBuilder();

        Set<Postcode> postcodes = dto.getPostcode().stream()
                .map(code -> {
                    if (existingPostcodes.contains(code)) {
                        errorMessage.append("The postcode: ").append(code).append(" already exists in the database. ");
                    }
                    return postcodeRepository.findByCode(code)
                            .orElseGet(() -> postcodeRepository.save(SuburbPostcodeMapper.toEntity(code)));
                })
                .collect(Collectors.toSet());

        for (String suburbName : dto.getSuburb()) {
            if (existingSuburbs.contains(suburbName)) {
                errorMessage.append("The suburb: ").append(suburbName).append(" already exists in the database. ");
            }
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

        if (errorMessage.length() > 0) {
            throw new BadRequestException(errorMessage.toString());
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
    public SuburbPostcodeCreateDTO updateSuburbOrPostcode(String postcodeCode, String suburbName,
            SuburbPostcodeUpdateDTO dto) {
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

        boolean isUpdated = false;

        if (updatePostcodesForSuburb(existingSuburb, dto.getPostcode())) {
            isUpdated = true;
        }
        if (updateSuburbName(existingSuburb, dto.getSuburb())) {
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new BadRequestException("No changes were made.");
        }

        Suburb updatedSuburb = suburbRepository.save(existingSuburb);
        return SuburbPostcodeMapper.toDTO(updatedSuburb);
    }

    private boolean updatePostcodesForSuburb(Suburb suburb, Set<String> postcodeCodes) {
        if (postcodeCodes != null && !postcodeCodes.isEmpty()) {
            Set<Postcode> oldPostcodes = suburb.getPostcodes();
            Set<Postcode> newPostcodes = postcodeCodes.stream()
                    .map(code -> postcodeRepository.findByCode(code)
                            .orElseGet(() -> postcodeRepository.save(SuburbPostcodeMapper.toEntity(code))))
                    .collect(Collectors.toSet());
            if (!oldPostcodes.equals(newPostcodes)) {
                suburb.setPostcodes(newPostcodes);
                utils.removeOrphanedPostcodes(oldPostcodes, suburb);
                return true;
            }
        }
        return false;
    }

    private boolean updateSuburbName(Suburb suburb, Set<String> suburbNames) {
        if (suburbNames != null && !suburbNames.isEmpty()) {
            if (suburbNames.size() > 1) {
                throw new BadRequestException("You cannot add suburbs - only edit the original value.");
            }
            String newSuburbName = suburbNames.iterator().next();
            if (!newSuburbName.equals(suburb.getName())) {
                suburb.setName(newSuburbName);
                return true;
            }
        }
        return false;
    }

    private SuburbPostcodeCreateDTO updatePostcode(String postcodeCode, SuburbPostcodeUpdateDTO dto) {
        Postcode existingPostcode = postcodeRepository.findByCode(postcodeCode)
                .orElseThrow(() -> new NotFoundException("Postcode not found: " + postcodeCode));

        boolean isUpdated = false;

        if (updatePostcodeCode(existingPostcode, dto.getPostcode())) {
            isUpdated = true;
        }
        if (updateSuburbsForPostcode(existingPostcode, dto.getSuburb())) {
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new BadRequestException("No changes were made.");
        }

        Postcode updatedPostcode = postcodeRepository.save(existingPostcode);
        return SuburbPostcodeMapper.toDTO(updatedPostcode);
    }

    private boolean updatePostcodeCode(Postcode postcode, Set<String> postcodeCodes) {
        if (postcodeCodes != null && !postcodeCodes.isEmpty()) {
            if (postcodeCodes.size() > 1) {
                throw new BadRequestException("You cannot add postcodes - only edit the original value.");
            }
            String newPostcode = postcodeCodes.iterator().next();
            if (!newPostcode.equals(postcode.getCode())) {
                postcode.setCode(newPostcode);
                return true;
            }
        }
        return false;
    }

    private boolean updateSuburbsForPostcode(Postcode postcode, Set<String> suburbNames) {
        if (suburbNames != null && !suburbNames.isEmpty()) {
            Set<Suburb> oldSuburbs = postcode.getSuburbs();
            Set<Suburb> newSuburbs = suburbNames.stream()
                    .map(name -> suburbRepository.findByName(name)
                            .orElseGet(
                                    () -> suburbRepository.save(SuburbPostcodeMapper.toEntity(name, Set.of(postcode)))))
                    .collect(Collectors.toSet());
            if (!oldSuburbs.equals(newSuburbs)) {
                postcode.setSuburbs(newSuburbs);
                utils.removeOrphanedSuburbs(oldSuburbs, postcode);
                return true;
            }
        }
        return false;
    }
}
