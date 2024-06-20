package io.nology.postcode_api.suburbpostcode;

import java.util.Set;
import java.util.stream.Collectors;

import io.nology.postcode_api.suburbpostcode.postcode.Postcode;
import io.nology.postcode_api.suburbpostcode.suburb.Suburb;

public class SuburbPostcodeMapper {

    public static SuburbPostcodeCreateDTO toDTO(Object entity) {
        SuburbPostcodeCreateDTO dto = new SuburbPostcodeCreateDTO();

        if (entity instanceof Suburb) {
            Suburb suburb = (Suburb) entity;
            dto.setId(suburb.getId());
            dto.setSuburbName(Set.of(suburb.getName()));
            Set<String> postcodes = suburb.getPostcodes().stream()
                    .map(Postcode::getCode)
                    .collect(Collectors.toSet());
            dto.setPostcode(postcodes);
            dto.setCount(postcodes.size());
        } else if (entity instanceof Postcode) {
            Postcode postcode = (Postcode) entity;
            dto.setId(postcode.getId());
            Set<String> suburbNames = postcode.getSuburbs().stream()
                    .map(Suburb::getName)
                    .collect(Collectors.toSet());
            dto.setSuburbName(suburbNames);
            dto.setPostcode(Set.of(postcode.getCode()));
            dto.setCount(suburbNames.size());
        } else {
            throw new IllegalArgumentException("Invalid entity type");
        }

        return dto;
    }

    public static Suburb toEntity(String suburbName, Set<Postcode> postcodes) {
        Suburb suburb = new Suburb();
        suburb.setName(suburbName);
        suburb.setPostcodes(postcodes);
        return suburb;
    }

    public static Postcode toEntity(String code) {
        Postcode postcode = new Postcode();
        postcode.setCode(code);
        return postcode;
    }
}