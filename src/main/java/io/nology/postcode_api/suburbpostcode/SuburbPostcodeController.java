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
    public List<SuburbPostcodeCreateDTO> getAllSuburbs() {
        return service.getAllSuburbs().stream()
                .map(SuburbPostcodeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/suburb")
    public ResponseEntity<SuburbPostcodeCreateDTO> getSuburbByPostcode(@RequestParam String postcode) {
        return ResponseEntity.ok(SuburbPostcodeMapper.toDTO(service.getSuburbByPostcode(postcode)));
    }

    @GetMapping("/postcode")
    public ResponseEntity<SuburbPostcodeCreateDTO> getPostcodeBySuburb(@RequestParam String suburb) {
        return ResponseEntity.ok(SuburbPostcodeMapper.toDTO(service.getPostcodeBySuburb(suburb)));
    }

    @PostMapping("/admin/add")
    public SuburbPostcodeCreateDTO addSuburbPostcode(@RequestBody @Valid SuburbPostcodeCreateDTO suburbPostcodeDTO) {
        SuburbPostcode suburbPostcode = SuburbPostcodeMapper.toEntity(suburbPostcodeDTO);
        return SuburbPostcodeMapper.toDTO(service.addSuburbPostcode(suburbPostcode));
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<Void> deleteSuburbPostcode(@RequestParam(required = false) String postcode, @RequestParam(required = false) String suburb) {
        service.deleteSuburbPostcode(postcode, suburb);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/update")
    public ResponseEntity<SuburbPostcodeUpdateDTO> updateSuburbPostcode(@RequestParam(required = false) String postcode, @RequestParam(required = false) String suburb, @RequestBody @Valid SuburbPostcodeUpdateDTO suburbPostcodeUpdateDTO) {
        SuburbPostcode updatedSuburbPostcode = SuburbPostcodeMapper.toEntity(suburbPostcodeUpdateDTO);
        SuburbPostcode updated = service.updateSuburbPostcode(postcode, suburb, updatedSuburbPostcode);
        return ResponseEntity.ok(SuburbPostcodeMapper.toUpdateDTO(updated));
    }
}
