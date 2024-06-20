package io.nology.postcode_api.suburbpostcode;

import io.nology.postcode_api.exceptions.BadRequestException;
import io.nology.postcode_api.exceptions.NotFoundException;
import io.nology.postcode_api.suburbpostcode.postcode.PostcodeRepository;
import io.nology.postcode_api.suburbpostcode.suburb.Suburb;
import io.nology.postcode_api.suburbpostcode.suburb.SuburbRepository;
import io.nology.postcode_api.suburbpostcode.utils.SuburbPostcodeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SuburbPostcodeServiceTest {

    @InjectMocks
    private SuburbPostcodeService service;

    @Mock
    private SuburbRepository suburbRepository;

    @Mock
    private PostcodeRepository postcodeRepository;

    @Mock
    private SuburbPostcodeUtils utils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSuburbs_ShouldReturnSuburbs() {
        Suburb suburb = new Suburb();
        when(suburbRepository.findAll()).thenReturn(Collections.singletonList(suburb));

        assertEquals(1, service.getAllSuburbs().size());
    }

    @Test
    void getSuburbsByPostcodeDTO_ShouldThrowNotFoundException() {
        when(postcodeRepository.findByCode("2000")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getSuburbsByPostcodeDTO("2000"));
    }

    @Test
    void getPostcodesBySuburbDTO_ShouldThrowNotFoundException() {
        when(suburbRepository.findByName("Sydney")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getPostcodesBySuburbDTO("Sydney"));
    }

    @Test
    void addSuburbPostcode_ShouldThrowBadRequestException_WhenSuburbIsEmpty() {
        SuburbPostcodeCreateDTO dto = new SuburbPostcodeCreateDTO();
        dto.setSuburbName(Collections.emptySet());
        dto.setPostcode(Collections.singleton("2000"));

        assertThrows(BadRequestException.class, () -> service.addSuburbPostcode(dto));
    }

    @Test
    void addSuburbPostcode_ShouldThrowBadRequestException_WhenPostcodeIsEmpty() {
        SuburbPostcodeCreateDTO dto = new SuburbPostcodeCreateDTO();
        dto.setSuburbName(Collections.singleton("Sydney"));
        dto.setPostcode(Collections.emptySet());

        assertThrows(BadRequestException.class, () -> service.addSuburbPostcode(dto));
    }

    @Test
    void deleteSuburbPostcode_ShouldThrowNotFoundException() {
        when(postcodeRepository.findByCode("2000")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteSuburbPostcode("2000", null));
    }

    @Test
    void updateSuburbOrPostcode_ShouldThrowBadRequestException() {
        SuburbPostcodeUpdateDTO dto = new SuburbPostcodeUpdateDTO();
        dto.setSuburbs(Collections.singleton("New Sydney"));
        dto.setPostcodes(Collections.singleton("2001"));

        assertThrows(BadRequestException.class, () -> service.updateSuburbOrPostcode(null, "", dto));
    }
}
