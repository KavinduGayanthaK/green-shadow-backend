package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.CropDTO;

import java.io.Serializable;
import java.util.List;

public interface CropService extends Serializable {
    String generateCropCode();
    void saveCrop(CropDTO cropDTO);
    List<CropDTO> getAllCrop();
}
