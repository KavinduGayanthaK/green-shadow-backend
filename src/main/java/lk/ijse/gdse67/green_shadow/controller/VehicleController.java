package lk.ijse.gdse67.green_shadow.controller;

import lk.ijse.gdse67.green_shadow.dto.VehicleDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vehicle")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        try{
            vehicleService.saveVehicle(vehicleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getAllVehicle() {
        return vehicleService.getAllVehicle();
    }

    @DeleteMapping(value = "/{licensePlateNumber}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("licensePlateNumber") String licensePlateNumber) {
        try{
            String regexForLicensePlateNumber = "^[A-Z]{2,3}-\\d{4}$";
            Pattern regexPattern = Pattern.compile(regexForLicensePlateNumber);
            Matcher regexMatcher =regexPattern.matcher(licensePlateNumber);
            if (!regexMatcher.matches()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            vehicleService.deleteVehicle(licensePlateNumber);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{licensePlateNumber}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVehicle(@PathVariable("licensePlateNumber")String licensePlateNumber,
                                           @RequestBody VehicleDTO vehicleDTO) {
        try{
            vehicleService.updateVehicle(licensePlateNumber,vehicleDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
