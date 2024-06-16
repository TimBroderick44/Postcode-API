package io.nology.postcode_api.suburbpostcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SuburbPostcodeController {

    @Autowired
    private SuburbPostcodeService service;

    @GetMapping("/allSuburbsPostcodes")
    public ResponseEntity<List<SuburbPostcodeCreateDTO>> getAllSuburbs() {
        List<SuburbPostcodeCreateDTO> allSuburbs = service.getAllSuburbs().stream()
                .map(SuburbPostcodeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(allSuburbs);
    }

    @GetMapping("/suburb")
    public ResponseEntity<SuburbPostcodeCreateDTO> getSuburbsByPostcode(@RequestParam String postcode) {
        SuburbPostcodeCreateDTO dto = service.getSuburbsByPostcodeDTO(postcode);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/postcode")
    public ResponseEntity<SuburbPostcodeCreateDTO> getPostcodesBySuburb(@RequestParam String suburb) {
        SuburbPostcodeCreateDTO dto = service.getPostcodesBySuburbDTO(suburb);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Void> addSuburbPostcode(@RequestBody @Valid SuburbPostcodeCreateDTO suburbPostcodeDTO) {
        service.addSuburbPostcode(suburbPostcodeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<Void> deleteSuburbPostcode(@RequestParam(required = false) String postcode, @RequestParam(required = false) String suburb) {
        service.deleteSuburbPostcode(postcode, suburb);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/update")
    public ResponseEntity<SuburbPostcodeCreateDTO> updateSuburbOrPostcode(
            @RequestParam(required = false) String postcode,
            @RequestParam(required = false) String suburb,
            @RequestBody @Valid SuburbPostcodeUpdateDTO updateDTO) {

        SuburbPostcodeCreateDTO result = service.updateSuburbOrPostcode(postcode, suburb, updateDTO);
        return ResponseEntity.ok(result);
    }
}
