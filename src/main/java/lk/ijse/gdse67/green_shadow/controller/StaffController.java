package lk.ijse.gdse67.green_shadow.controller;
import lk.ijse.gdse67.green_shadow.dto.StaffDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.service.StaffService;
import lk.ijse.gdse67.green_shadow.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;
    private final AppUtil appUtil;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@RequestBody StaffDTO staffDTO) {
        try{

            staffDTO.setStaffId(appUtil.generateStaffId());
            staffService.saveStaff(staffDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDTO> getAllStaff(){

            System.out.println(staffService.getAllStaff());
            return staffService.getAllStaff();

    }
}