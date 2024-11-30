package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.FieldDao;
import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.*;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.FieldService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class FieldServiceImpl implements FieldService {

    private final Mapping mapping;

    private final FieldDao fieldDao;

    @Autowired
    public FieldServiceImpl(Mapping mapping, FieldDao fieldDao) {
        this.mapping = mapping;
        this.fieldDao = fieldDao;
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
        return mapping.toGetAllFieldDTO(fieldDao.findAll());
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
}
