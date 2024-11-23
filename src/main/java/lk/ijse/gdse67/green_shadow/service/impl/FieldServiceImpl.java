package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.FieldDao;
import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.service.FieldService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {

    private final Mapping mapping;

    private final FieldDao fieldDao;

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
        return mapping.toFieldDTO(fieldDao.findAll());
    }

    @Override
    public void saveField(FieldDTO fieldDTO) {
        System.out.println("Enter fieldService");
        fieldDao.save(mapping.toFieldEntity(fieldDTO));
    }
}
