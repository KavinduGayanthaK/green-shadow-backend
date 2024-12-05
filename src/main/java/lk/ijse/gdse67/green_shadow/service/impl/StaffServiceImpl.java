package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.*;
import lk.ijse.gdse67.green_shadow.dto.EquipmentDTO;
import lk.ijse.gdse67.green_shadow.dto.StaffDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.*;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.StaffService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    private final StaffDao staffDao;
    private final EquipmentDao equipmentDao;
    private final FieldDao fieldDao;
    private final VehicleDao vehicleDao;


    private final Mapping mapping;


    @Autowired
    public StaffServiceImpl(StaffDao staffDao, EquipmentDao equipmentDao, FieldDao fieldDao, VehicleDao vehicleDao,  Mapping mapping) {
        this.staffDao = staffDao;
        this.equipmentDao = equipmentDao;
        this.fieldDao = fieldDao;
        this.vehicleDao = vehicleDao;


        this.mapping = mapping;
    }

    @Override
    public void saveStaff(StaffDTO staffDTO) {
        List<FieldEntity> fieldsList = new ArrayList<>();
        List<LogEntity> logEntities = new ArrayList<>();
        List<EquipmentEntity> equipmentEntities = new ArrayList<>();
        List<VehicleEntity> vehicleEntities = new ArrayList<>();



        for (String id : staffDTO.getEquipments()) {
            if (equipmentDao.existsById(id)) {
                equipmentEntities.add(equipmentDao.getReferenceById(id));
            }
        }
        for (String id : staffDTO.getVehicles()) {
            if (vehicleDao.existsById(id)) {
                vehicleEntities.add(vehicleDao.getReferenceById(id));
            }
        }

        for (String id : staffDTO.getField()) {
            if (fieldDao.existsById(id)) {
                fieldsList.add(fieldDao.getReferenceById(id));
            }
        }
        staffDTO.setStaffId(staffDTO.getStaffId());
        StaffEntity staffEntity = mapping.toStaffEntity(staffDTO);
        staffEntity.setFields(fieldsList);
        staffEntity.setEquipments(equipmentEntities);
        staffEntity.setVehicles(vehicleEntities);


        for (EquipmentEntity equipmentEntity : equipmentEntities) {
            equipmentEntity.getStaff().add(staffEntity);
        }
        for (LogEntity logEntity : logEntities) {
            logEntity.getStaff().add(staffEntity);
        }

        for (FieldEntity field : fieldsList) {
            field.getStaffs().add(staffEntity);
        }


        StaffEntity save = staffDao.save(staffEntity);
        for (VehicleEntity vehicleEntity : vehicleEntities) {
            vehicleEntity.setStaff(save);
        }
    }


    @Override
    public String generateStaffId() {
        StaffEntity lastStaff = staffDao.findLastStaffId();

        if (lastStaff != null) {
            String lastStaffId = lastStaff.getStaffId();
            try {
                int lastNumber = Integer.parseInt(lastStaffId.replace("STAFF-", ""));
                return String.format("STAFF-%03d", lastNumber + 1);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid field code format: " + lastStaffId, e);
            }
        } else {
            return "STAFF-001";
        }
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        List<StaffDTO> staffList = new ArrayList<>();
        for(StaffEntity staff : staffDao.findAll()) {
            List<String> vehicleList = new ArrayList<>();
            List<String> fieldList = new ArrayList<>();
            List<String> equipmentList = new ArrayList<>();

            for (VehicleEntity vehicle: staff.getVehicles()) {
                vehicleList.add(vehicle.getLicensePlateNumber());
            }
            for (FieldEntity field: staff.getFields()) {
                fieldList.add(field.getFieldCode());
            }
            for (EquipmentEntity equipment : staff.getEquipments()) {
                EquipmentDTO equipmentDTO = new EquipmentDTO();
                equipmentDTO.setEquipmentId(equipment.getEquipmentId());
                // Assuming you have a method to get the count of assigned equipment
                equipmentDTO.setAssignedCount(equipment.getAssignedCount());
                equipmentList.add(String.valueOf(equipmentDTO));
            }


            staffList.add(new StaffDTO(
                    staff.getStaffId(),
                    staff.getFirstName(),
                    staff.getLastName(),
                    staff.getDesignation(),
                    staff.getGender(),
                    String.valueOf(staff.getJoinedDate()),
                    String.valueOf(staff.getDateOfBirth()),
                    staff.getAddress1(),
                    staff.getAddress2(),
                    staff.getAddress3(),
                    staff.getAddress4(),
                    staff.getAddress5(),
                    staff.getContactNumber(),
                    staff.getEmail(),
                    staff.getRole(),
                    fieldList,
                    vehicleList,
                    equipmentList
            ));
        }
        return staffList;
    }


    @Override
    public void deleteStaff(String staffId) {
        StaffEntity staff = staffDao.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Field not found: " + staffId));

        for (FieldEntity field : staff.getFields()) {
            field.getStaffs().remove(staff);
        }
        staff.getFields().clear();

        for (VehicleEntity vehicle : staff.getVehicles()) {
            vehicle.setStaff(null);
        }
        staff.getVehicles().clear();

        for (LogEntity log : staff.getLogs()) {
            log.getStaff().remove(staff);
        }
        staff.getLogs().clear();

        for (EquipmentEntity equipment : staff.getEquipments()) {
            equipment.getStaff().remove(staff);
        }
        staff.getEquipments().clear();



        staffDao.delete(staff);
    }


    @Override
    public void updateStaff(StaffDTO staffDTO) {
        StaffEntity existingStaff = staffDao.findById(staffDTO.getStaffId())
                .orElseThrow(() -> new NotFoundException("Staff not found: " + staffDTO.getStaffId()));

        // Update primitive fields
        existingStaff.setFirstName(staffDTO.getFirstName());
        existingStaff.setLastName(staffDTO.getLastName());
        existingStaff.setDesignation(staffDTO.getDesignation());
        existingStaff.setGender(staffDTO.getGender());
        existingStaff.setJoinedDate(LocalDate.parse(staffDTO.getJoinedDate()));
        existingStaff.setDateOfBirth(LocalDate.parse(staffDTO.getDateOfBirth()));
        existingStaff.setAddress1(staffDTO.getAddress1());
        existingStaff.setAddress2(staffDTO.getAddress2());
        existingStaff.setAddress3(staffDTO.getAddress3());
        existingStaff.setAddress4(staffDTO.getAddress4());
        existingStaff.setAddress5(staffDTO.getAddress5());
        existingStaff.setContactNumber(staffDTO.getContactNumber());
        existingStaff.setEmail(staffDTO.getEmail());
        existingStaff.setRole(staffDTO.getRole());

        // Synchronize and update fields (bidirectional relationship)
        updateFields(existingStaff, staffDTO);

        // Synchronize and update vehicles
        updateVehicles(existingStaff, staffDTO);

        // Synchronize and update equipments
        updateEquipments(existingStaff, staffDTO);

        // Save updated entity
        staffDao.save(existingStaff);
    }

    private void updateFields(StaffEntity existingStaff, StaffDTO staffDTO) {
        // Remove current references and clear the list
        for (FieldEntity field : existingStaff.getFields()) {
            field.getStaffs().remove(existingStaff); // Remove old references
        }
        existingStaff.getFields().clear();

        // Add new fields from DTO
        for (String fieldId : staffDTO.getField()) {
            FieldEntity field = fieldDao.findById(fieldId)
                    .orElseThrow(() -> new NotFoundException("Field not found: " + fieldId));
            existingStaff.getFields().add(field);
            field.getStaffs().add(existingStaff); // Synchronize both sides
        }
    }

    private void updateVehicles(StaffEntity existingStaff, StaffDTO staffDTO) {
        // Remove current references and clear the list
        for (VehicleEntity vehicle : existingStaff.getVehicles()) {
            vehicle.setStaff(null); // Remove old staff assignment
        }
        existingStaff.getVehicles().clear();

        // Add new vehicles from DTO
        for (String vehicleId : staffDTO.getVehicles()) {
            VehicleEntity vehicle = vehicleDao.findById(vehicleId)
                    .orElseThrow(() -> new NotFoundException("Vehicle not found: " + vehicleId));
            vehicle.setStaff(existingStaff);
            existingStaff.getVehicles().add(vehicle);
        }
    }

    private void updateEquipments(StaffEntity existingStaff, StaffDTO staffDTO) {
        // Remove current references and clear the list
        for (EquipmentEntity equipment : existingStaff.getEquipments()) {
            equipment.getStaff().remove(existingStaff); // Remove old references
        }
        existingStaff.getEquipments().clear();

        // Add new equipment from DTO
        List<EquipmentEntity> equipments = new ArrayList<>();
        for (String equipmentId : staffDTO.getEquipments()) {
            EquipmentEntity equipment = equipmentDao.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("Equipment not found: " + equipmentId));

            // Update the assigned count
            if (equipment.getAssignedCount() < equipment.getTotalCount()) {
                equipment.setAssignedCount(equipment.getAssignedCount() + 1);
            } else {
                throw new RuntimeException("Equipment " + equipment.getEquipmentName() + " is fully assigned.");
            }

            equipments.add(equipment);
        }

        // Set the equipment to the staff entity
        existingStaff.setEquipments(equipments);
    }



}
