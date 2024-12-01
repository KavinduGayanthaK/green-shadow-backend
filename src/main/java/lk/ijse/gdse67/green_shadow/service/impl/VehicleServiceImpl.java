package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.VehicleDao;
import lk.ijse.gdse67.green_shadow.dto.VehicleDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.VehicleEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.VehicleService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class VehicleServiceImpl implements VehicleService {

    private final VehicleDao vehicleDao;
    private final Mapping mapping;

    @Autowired
    public VehicleServiceImpl(VehicleDao vehicleDao, Mapping mapping) {
        this.vehicleDao = vehicleDao;
        this.mapping = mapping;
    }

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        vehicleDao.save(mapping.toVehicleEntity(vehicleDTO));
    }

    @Override
    public List<VehicleDTO> getAllVehicle() {
        return mapping.vehicleDTO(vehicleDao.findAll());
    }

    @Override
    public void deleteVehicle(String licensePlateNumber) {
        VehicleEntity vehicle = vehicleDao.findById(licensePlateNumber)
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + licensePlateNumber));

        if (vehicle.getStaff() != null) {
            StaffEntity staff = vehicle.getStaff();
            staff.getVehicles().remove(vehicle);
            vehicle.setStaff(null);
        }

        vehicleDao.delete(vehicle);
    }
}
