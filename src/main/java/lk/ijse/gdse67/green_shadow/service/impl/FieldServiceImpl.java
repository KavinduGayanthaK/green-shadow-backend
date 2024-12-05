package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.CropDao;
import lk.ijse.gdse67.green_shadow.dao.FieldDao;
import lk.ijse.gdse67.green_shadow.dao.StaffDao;
import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.*;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.FieldService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class FieldServiceImpl implements FieldService {

    private final Mapping mapping;

    private final FieldDao fieldDao;
    private final CropDao cropDao;
    private final StaffDao staffDao;

    @Autowired
    public FieldServiceImpl(Mapping mapping, FieldDao fieldDao, CropDao cropDao, StaffDao staffDao) {
        this.mapping = mapping;
        this.fieldDao = fieldDao;
        this.cropDao = cropDao;
        this.staffDao = staffDao;
    }

    @Override
    public String generateFieldCode() {
        FieldEntity lastField = fieldDao.findLastFieldCode();

        if (lastField != null) {
            String lastFieldCode = lastField.getFieldCode();
            try {
                int lastNumber = Integer.parseInt(lastFieldCode.replace("FIELD-", ""));
                return String.format("FIELD-%03d", lastNumber + 1);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid field code format: " + lastFieldCode, e);
            }
        } else {
            return "FIELD-001";
        }
    }

    @Override
    public List<FieldDTO> getAllField() {
        List<FieldDTO> fieldDTOS = new ArrayList<>();
        for(FieldEntity field : fieldDao.findAll()) {
            List<String> crops = new ArrayList<>();
            List<String> staffs = new ArrayList<>();

            for(CropEntity crop:field.getCrops()){
                crops.add(crop.getCropCode());
            }
            for(StaffEntity staff : field.getStaffs()){
                staffs.add(staff.getStaffId());
            }

            fieldDTOS.add(new FieldDTO(
                    field.getFieldCode(),
                    field.getFieldName(),
                    field.getLocation(),
                    field.getExtendSizeOfField(),
                    field.getImage1(),
                    field.getImage2(),
                    crops,
                    staffs
            ));
        }
        return fieldDTOS;
    }

    @Override
    public void saveField(FieldDTO fieldDTO) {
      fieldDao.save(mapping.toFieldEntity(fieldDTO));
    }

    @Override
    public void deleteField(String fieldCode) {
        FieldEntity field = fieldDao.findById(fieldCode)
                .orElseThrow(() -> new NotFoundException("Field not found: " + fieldCode));


        for (CropEntity crop : field.getCrops()) {
            crop.getFields().remove(field);
        }
        field.getCrops().clear();


        field.getLogs().forEach(log -> log.getFields().remove(field));
        field.getEquipments().forEach(equipment -> equipment.getFields().remove(field));
        field.getStaffs().forEach(staff -> staff.getFields().remove(field));


        fieldDao.delete(field);
    }

    @Override
    public void updateField(FieldDTO fieldDTO) {
        FieldEntity existField = fieldDao.findById(fieldDTO.getFieldCode()).
                orElseThrow(()->new NotFoundException("Crop not found : " + fieldDTO.getFieldCode()));

        existField.setFieldName(fieldDTO.getFieldName());
        existField.setLocation(fieldDTO.getLocation());
        existField.setExtendSizeOfField(fieldDTO.getExtendSizeOfField());
        existField.setImage1(fieldDTO.getImage1());
        existField.setImage2(fieldDTO.getImage2());


        for (CropEntity crop : existField.getCrops()) {
            crop.getFields().remove(existField); // Remove old references
        }
        existField.getCrops().clear();

        for (String cropId : fieldDTO.getCrops()) {
            CropEntity crop = cropDao.findById(cropId)
                    .orElseThrow(() -> new NotFoundException("Field not found: " + cropId));
            existField.getCrops().add(crop);
            crop.getFields().add(existField); // Synchronize both sides
        }

        for (StaffEntity staff : existField.getStaffs()) {
            staff.getFields().remove(existField); // Remove old references
        }
        existField.getStaffs().clear();

        for (String staffId : fieldDTO.getStaff()) {
            StaffEntity staff = staffDao.findById(staffId)
                    .orElseThrow(() -> new NotFoundException("Field not found: " + staffId));
            existField.getStaffs().add(staff);
            staff.getFields().add(existField); // Synchronize both sides
        }

        fieldDao.save(existField);
    }
}
