package lk.ijse.gdse67.green_shadow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.gdse67.green_shadow.dao.EquipmentDao;
import lk.ijse.gdse67.green_shadow.dao.FieldDao;
import lk.ijse.gdse67.green_shadow.dao.StaffDao;
import lk.ijse.gdse67.green_shadow.dto.EquipmentDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.EquipmentEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.EquipmentService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentDao equipmentDao;
    private final Mapping mapping;
    private final FieldDao fieldDao;
    private final StaffDao staffDao;

    @Autowired
    public EquipmentServiceImpl(EquipmentDao equipmentDao, Mapping mapping, FieldDao fieldDao, StaffDao staffDao) {
        this.equipmentDao = equipmentDao;
        this.mapping = mapping;
        this.fieldDao = fieldDao;
        this.staffDao = staffDao;

    }

    @Override
    public String generateEquipmentCode() {
        EquipmentEntity lastEquipment = equipmentDao.findLastEquipmentId();

        if (lastEquipment != null) {
            String lastEquipmentId = lastEquipment.getEquipmentId();
            try {
                int lastNumber = Integer.parseInt(lastEquipmentId.replace("EQUIP-", ""));
                return String.format("EQUIP-%03d", lastNumber + 1);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid field code format: " + lastEquipmentId, e);
            }
        } else {
            return "EQUIP-001";
        }
    }

    @Override
    public void save(EquipmentDTO equipmentDTO) {
        EquipmentEntity equipmentEntity = new EquipmentEntity();

        // Set fields from DTO to Entity
        equipmentEntity.setEquipmentId(equipmentDTO.getEquipmentId());
        equipmentEntity.setEquipmentName(equipmentDTO.getEquipmentName());
        equipmentEntity.setType(equipmentDTO.getType());
        equipmentEntity.setStatus(equipmentDTO.getStatus());
        equipmentEntity.setTotalCount(equipmentDTO.getTotalCount());
        equipmentEntity.setAssignedCount(equipmentDTO.getAssignedCount());

        // Handle relationships with fields
        if (equipmentDTO.getFields() != null && !equipmentDTO.getFields().isEmpty()) {
            for (String fieldCode : equipmentDTO.getFields()) {
                FieldEntity fieldEntity = fieldDao.findById(fieldCode)
                        .orElseThrow(() -> new NotFoundException("Field not found: " + fieldCode));
                equipmentEntity.getFields().add(fieldEntity);
                fieldEntity.getEquipments().add(equipmentEntity); // Bidirectional synchronization
            }
        }

        // Handle relationships with staff
        if (equipmentDTO.getStaff() != null && !equipmentDTO.getStaff().isEmpty()) {
            for (String staffCode : equipmentDTO.getStaff()) {
                StaffEntity staffEntity = staffDao.findById(staffCode)
                        .orElseThrow(() -> new NotFoundException("Staff not found: " + staffCode));
                equipmentEntity.getStaff().add(staffEntity);
                staffEntity.getEquipments().add(equipmentEntity); // Bidirectional synchronization
            }
        }


        equipmentDao.save(equipmentEntity);
    }

    @Override
    public List<EquipmentDTO> getAllEquipment() {
        List<EquipmentEntity> equipmentEntities = equipmentDao.findAll(); // Fetch all equipment entities from the database
        List<EquipmentDTO> equipmentDTOList = new ArrayList<>();

        for (EquipmentEntity equipment : equipmentEntities) {
            List<String> fieldCodes = new ArrayList<>();
            List<String> staffCodes = new ArrayList<>();

            // Extract field codes
            for (FieldEntity field : equipment.getFields()) {
                fieldCodes.add(field.getFieldCode());
            }

            // Extract staff codes
            for (StaffEntity staff : equipment.getStaff()) {
                staffCodes.add(staff.getStaffId());
            }

            // Create EquipmentDTO and add it to the list
            equipmentDTOList.add(new EquipmentDTO(
                    equipment.getEquipmentId(),
                    equipment.getEquipmentName(),
                    equipment.getType(),
                    equipment.getStatus(),
                    equipment.getTotalCount(),
                    equipment.getAssignedCount(),
                    fieldCodes,
                    staffCodes
            ));
        }

        return equipmentDTOList;
    }



    @Override
    public void updateEquipment(String equipmentId, EquipmentDTO equipmentDTO) {

    }

    @Override
    public boolean deleteEquipment(String equipmentId) {
        return false;
    }





}