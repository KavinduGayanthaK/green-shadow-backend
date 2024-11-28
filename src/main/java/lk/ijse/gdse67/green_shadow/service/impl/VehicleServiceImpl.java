package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.VehicleDao;
import lk.ijse.gdse67.green_shadow.dto.VehicleDTO;
import lk.ijse.gdse67.green_shadow.service.VehicleService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleDao vehicleDao;
    private final Mapping mapping;

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        vehicleDao.save(mapping.toVehicleEntity(vehicleDTO));
    }

    @Override
    public List<VehicleDTO> getAllVehicle() {
        return mapping.vehicleDTO(vehicleDao.findAll());
    }
}
