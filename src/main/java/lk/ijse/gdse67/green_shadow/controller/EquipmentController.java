package lk.ijse.gdse67.green_shadow.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.gdse67.green_shadow.dto.EquipmentDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.service.EquipmentService;
import lk.ijse.gdse67.green_shadow.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {


    private final EquipmentService equipmentService;

    @RolesAllowed({"MANAGER", "ADMINISTRATIVE"})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        try {
            equipmentDTO.setEquipmentId(equipmentService.generateEquipmentCode());
            System.out.println("Assigned count"+equipmentDTO.getAssignedCount());
            System.out.println("Total count"+equipmentDTO.getTotalCount());
            equipmentService.save(equipmentDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RolesAllowed({"MANAGER", "ADMINISTRATIVE", "SCIENTIST"})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }







    @RolesAllowed({"MANAGER", "ADMINISTRATIVE"})
    @PutMapping("/{equipmentId}")
    public ResponseEntity<?> updateEquipment(@PathVariable String equipmentId, @RequestBody EquipmentDTO equipment) {
        try {
            equipmentService.updateEquipment(equipmentId, equipment);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @RolesAllowed({"MANAGER", "ADMINISTRATIVE"})
    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable String equipmentId) {
        try{
            boolean isDeleted = equipmentService.deleteEquipment(equipmentId);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (DataPersistException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e ){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
