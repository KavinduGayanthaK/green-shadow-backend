package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.CropDao;
import lk.ijse.gdse67.green_shadow.entity.impl.CropEntity;
import lk.ijse.gdse67.green_shadow.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.rowset.CachedRowSet;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropDao cropDao;
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
}
