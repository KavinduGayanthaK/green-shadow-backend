package lk.ijse.gdse67.green_shadow.controller;

import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.service.FieldService;
import lk.ijse.gdse67.green_shadow.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;
import java.util.List;
import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/field")
@CrossOrigin(origins = "http://localhost:63343")
public class FieldController {

    private final FieldService fieldService;
    private final AppUtil appUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUser(
            @RequestPart("fieldName") String fieldName,
            @RequestPart("fieldLocation") String fieldLocation,
            @RequestPart("extendSizeOfField") String extendSizeOfField,
            @RequestPart("fieldsCrop") List<String> fieldsCrop,
            @RequestPart("fieldsStaff") List<String> fieldStaff,
            @RequestPart(value = "fieldImage1", required = false) MultipartFile fieldImage1,
            @RequestPart(value = "fieldImage2", required = false) MultipartFile fieldImage2
    ) {
        try {
            String[] locationParts = fieldLocation.split(",");
            if (locationParts.length < 2) {
                throw new IllegalAccessException("Invalid field location format");
            }
            int x = parseInt(locationParts[0].trim());
            int y = parseInt(locationParts[1].trim());
            Point location = new Point(x, y);
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setFieldCode(appUtil.generateFieldCode());
            fieldDTO.setFieldName(fieldName);
            fieldDTO.setLocation(location);
            fieldDTO.setExtendSizeOfField(extendSizeOfField);
            fieldDTO.setCrops(fieldsCrop);
            fieldDTO.setStaff(fieldStaff);
            fieldDTO.setImage1(appUtil.generateFieldImage(fieldImage1));
            fieldDTO.setImage2(appUtil.generateFieldImage(fieldImage2));
            fieldService.saveField(fieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FieldDTO> getAllFields() {
        System.out.println(fieldService.getAllField());
        return fieldService.getAllField();
    }
}
