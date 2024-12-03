package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.CropDao;
import lk.ijse.gdse67.green_shadow.dao.FieldDao;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CropServiceImpl implements CropService {

    private final CropDao cropDao;
    private final FieldDao fieldDao;
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
       List<CropDTO> cropDTOS = new ArrayList<>();
       for(CropEntity crop: cropDao.findAll()) {
           List<String> fields = new ArrayList<>();

           for(FieldEntity field: crop.getFields()) {
               fields.add(field.getFieldCode());
           }

           cropDTOS.add(new CropDTO(
                   crop.getCropCode(),
                   crop.getCommonName(),
                   crop.getScientificName(),
                   crop.getCropCategory(),
                   crop.getCropSeason(),
                   fields,
                   crop.getCropImage()
           ));
       }
       return cropDTOS;
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

    @Override
    public void updateCrop(CropDTO cropDTO) {
        CropEntity existCrop = cropDao.findById(cropDTO.getCropCode()).
                orElseThrow(()->new NotFoundException("Crop not found : " + cropDTO.getCropCode()));

        existCrop.setCommonName(cropDTO.getCommonName());
        existCrop.setScientificName(cropDTO.getScientificName());
        existCrop.setCropCategory(cropDTO.getCropCategory());
        existCrop.setCropSeason(cropDTO.getCropSeason());


        for (FieldEntity field : existCrop.getFields()) {
            field.getCrops().remove(existCrop); // Remove old references
        }
        existCrop.getFields().clear();

        for (String fieldId : cropDTO.getFields()) {
            FieldEntity field = fieldDao.findById(fieldId)
                    .orElseThrow(() -> new NotFoundException("Field not found: " + fieldId));
            existCrop.getFields().add(field);
            field.getCrops().add(existCrop); // Synchronize both sides
        }

        existCrop.setCropImage(cropDTO.getCropImage());

        cropDao.save(existCrop);
    }


}
