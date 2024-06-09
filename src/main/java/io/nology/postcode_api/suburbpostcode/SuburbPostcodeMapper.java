package io.nology.postcode_api.suburbpostcode;

public class SuburbPostcodeMapper {

    public static SuburbPostcodeCreateDTO toDTO(SuburbPostcode suburbPostcode) {
        SuburbPostcodeCreateDTO dto = new SuburbPostcodeCreateDTO();
        dto.setId(suburbPostcode.getId());
        dto.setSuburb(suburbPostcode.getSuburb());
        dto.setPostcode(suburbPostcode.getPostcode());
        return dto;
    }

    public static SuburbPostcodeUpdateDTO toUpdateDTO(SuburbPostcode suburbPostcode) {
        SuburbPostcodeUpdateDTO dto = new SuburbPostcodeUpdateDTO();
        dto.setId(suburbPostcode.getId());
        dto.setSuburb(suburbPostcode.getSuburb());
        dto.setPostcode(suburbPostcode.getPostcode());
        return dto;
    }

    public static SuburbPostcode toEntity(SuburbPostcodeCreateDTO dto) {
        SuburbPostcode suburbPostcode = new SuburbPostcode();
        suburbPostcode.setId(dto.getId());
        if (dto.getSuburb() != null) {
            suburbPostcode.setSuburb(capitalizeFirstLetter(dto.getSuburb().trim()));
        } else {
            suburbPostcode.setSuburb(null);
        }
        suburbPostcode.setPostcode(dto.getPostcode());
        return suburbPostcode;
    }

    public static SuburbPostcode toEntity(SuburbPostcodeUpdateDTO dto) {
        SuburbPostcode suburbPostcode = new SuburbPostcode();
        suburbPostcode.setId(dto.getId());
        if (dto.getSuburb() != null) {
            suburbPostcode.setSuburb(capitalizeFirstLetter(dto.getSuburb().trim()));
        } else {
            suburbPostcode.setSuburb(null);
        }
        suburbPostcode.setPostcode(dto.getPostcode());
        return suburbPostcode;
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
