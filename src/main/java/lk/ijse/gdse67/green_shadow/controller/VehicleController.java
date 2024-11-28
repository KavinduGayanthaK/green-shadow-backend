package lk.ijse.gdse67.green_shadow.controller;

import lk.ijse.gdse67.green_shadow.dao.VehicleDao;
import lk.ijse.gdse67.green_shadow.dto.VehicleDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vehicle")
@CrossOrigin(origins = "*")
public class VehicleController {



}
