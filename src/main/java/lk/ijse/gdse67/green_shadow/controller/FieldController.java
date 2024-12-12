package lk.ijse.gdse67.green_shadow.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/field")
@CrossOrigin(origins = "*")
public class FieldController {

    private final FieldService fieldService;
    private final AppUtil appUtil;

    @RolesAllowed({"MANAGER", "SCIENTIST"})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveField(
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
            fieldDTO.setImage1(appUtil.generateImage(fieldImage1));
            fieldDTO.setImage2(appUtil.generateImage(fieldImage2));
            fieldService.saveField(fieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RolesAllowed({"MANAGER", "SCIENTIST"})
    @PatchMapping(value = "/{fieldCode}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateField(
            @RequestPart("fieldName") String fieldName,
            @RequestPart("fieldLocation") String fieldLocation,
            @RequestPart("extendSizeOfField") String extendSizeOfField,
            @RequestPart("fieldsCrop") List<String> fieldsCrop,
            @RequestPart("fieldsStaff") List<String> fieldStaff,
            @RequestPart(value = "fieldImage1", required = false) MultipartFile fieldImage1,
            @RequestPart(value = "fieldImage2", required = false) MultipartFile fieldImage2,
            @PathVariable("fieldCode") String fieldCode
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
            fieldDTO.setFieldCode(fieldCode);
            fieldDTO.setFieldName(fieldName);
            fieldDTO.setLocation(location);
            fieldDTO.setExtendSizeOfField(extendSizeOfField);
            fieldDTO.setCrops(fieldsCrop);
            fieldDTO.setStaff(fieldStaff);
            fieldDTO.setImage1(appUtil.generateImage(fieldImage1));
            fieldDTO.setImage2(appUtil.generateImage(fieldImage2));
            fieldService.updateField(fieldDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RolesAllowed({"MANAGER", "ADMINISTRATIVE", "SCIENTIST"})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllFields() {
        try{
            return ResponseEntity.ok(fieldService.getAllField());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error | crop details fetch unsuccessfully.\nMore Reason\n"+e);
        }

    }


    @RolesAllowed({"MANAGER", "SCIENTIST"})
    @DeleteMapping(value = "/{fieldCode}")
    public ResponseEntity<Void> deleteField(@PathVariable("fieldCode") String fieldCode) {
        try{
            String regexForLogCode = "^FIELD-\\d{3,4}$";
            Pattern regexPattern = Pattern.compile(regexForLogCode);
            Matcher regexMatcher =regexPattern.matcher(fieldCode);
            if (!regexMatcher.matches()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.deleteField(fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
