package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.VehicleDTO;

import java.io.Serializable;
import java.util.List;

public interface VehicleService extends Serializable {
    void saveVehicle(VehicleDTO vehicleDTO);
    List<VehicleDTO> getAllVehicle();
}
