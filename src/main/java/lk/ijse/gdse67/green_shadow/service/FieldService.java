package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.FieldDTO;

import java.io.Serializable;
import java.util.List;

public interface FieldService extends Serializable {
    String generateFieldCode();
    List<FieldDTO> getAllField();

    void saveField(FieldDTO fieldDTO);

    void deleteField(String fieldCode);
}
