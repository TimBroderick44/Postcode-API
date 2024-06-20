package io.nology.postcode_api.suburbpostcode;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.nology.postcode_api.suburbpostcode.suburb.Suburb;
import io.nology.postcode_api.suburbpostcode.postcode.Postcode;
import io.nology.postcode_api.suburbpostcode.postcode.PostcodeRepository;
import io.nology.postcode_api.suburbpostcode.suburb.SuburbRepository;

@ExtendWith(MockitoExtension.class)
class SuburbPostcodeControllerTest {

    @Mock
    private SuburbRepository suburbRepository;

    @Mock
    private PostcodeRepository postcodeRepository;

    @Mock
    private SuburbPostcodeService service;

    @InjectMocks
    private SuburbPostcodeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSuburbs_ShouldReturnOk() {
        Suburb suburb = new Suburb();
        suburb.setId(1L);
        suburb.setName("Sydney");
        Postcode postcode = new Postcode();
        postcode.setId(1L);
        postcode.setCode("2000");
        suburb.setPostcodes(Set.of(postcode));

        when(service.getAllSuburbs()).thenReturn(Collections.singletonList(suburb));

        ResponseEntity<List<SuburbPostcodeCreateDTO>> response = controller.getAllSuburbs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Sydney", response.getBody().get(0).getSuburb().iterator().next());
    }

    @Test
    void getSuburbsByPostcode_ShouldReturnOk() {
        Postcode postcode = new Postcode();
        postcode.setId(1L);
        postcode.setCode("2000");
        Suburb suburb = new Suburb();
        suburb.setId(1L);
        suburb.setName("Sydney");
        postcode.setSuburbs(Set.of(suburb));

        SuburbPostcodeCreateDTO dto = new SuburbPostcodeCreateDTO();
        dto.setSuburbName(Set.of("Sydney"));
        dto.setPostcode(Set.of("2000"));

        when(service.getSuburbsByPostcodeDTO("2000")).thenReturn(dto);

        ResponseEntity<SuburbPostcodeCreateDTO> response = controller.getSuburbsByPostcode("2000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sydney", response.getBody().getSuburb().iterator().next());
    }

    @Test
    void getPostcodesBySuburb_ShouldReturnOk() {
        Suburb suburb = new Suburb();
        suburb.setId(1L);
        suburb.setName("Sydney");
        Postcode postcode = new Postcode();
        postcode.setId(1L);
        postcode.setCode("2000");
        suburb.setPostcodes(Set.of(postcode));

        SuburbPostcodeCreateDTO dto = new SuburbPostcodeCreateDTO();
        dto.setSuburbName(Set.of("Sydney"));
        dto.setPostcode(Set.of("2000"));

        when(service.getPostcodesBySuburbDTO("Sydney")).thenReturn(dto);

        ResponseEntity<SuburbPostcodeCreateDTO> response = controller.getPostcodesBySuburb("Sydney");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2000", response.getBody().getPostcode().iterator().next());
    }
}
