package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.CropDao;
import lk.ijse.gdse67.green_shadow.dto.CropDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.CropEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.CropService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.CachedRowSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CropServiceImpl implements CropService {

    private final CropDao cropDao;
    private final Mapping mapping;
    @Override
    public String generateCropCode() {
        CropEntity lastCrop =cropDao.findLastCropCode();

        if (lastCrop != null) {
            String lastCropCode = lastCrop.getCropCode();
            try {
                int lastNumber = Integer.parseInt(lastCropCode.replace("CROP-",""));
                return String.format("CROP-%03d",lastNumber+1);
            }catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid field code format: "+lastCropCode,e);
            }
        }else {
            return "CROP-001";
        }
    }

    @Override
    public void saveCrop(CropDTO cropDTO) {
        cropDao.save(mapping.toCropEntity(cropDTO));
    }

    @Override
    public List<CropDTO> getAllCrop() {
        return mapping.toGetAllCropDTO(cropDao.findAll());
    }

    @Override
    public void deleteCrop(String cropCode) {
        CropEntity crop = cropDao.findById(cropCode).
                orElseThrow(()->new NotFoundException("Crop not found: " + cropCode));

        for (FieldEntity field : crop.getFields()) {
            crop.getFields().remove(field);

        }
        crop.getFields().clear();
        crop.getLogs().forEach(log -> log.getCrop().remove(crop));

        cropDao.delete(crop);


    }


}
