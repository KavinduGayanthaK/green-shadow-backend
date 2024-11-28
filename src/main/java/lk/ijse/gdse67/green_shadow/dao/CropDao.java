package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.CropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CropDao extends JpaRepository<CropEntity,String> {

    @Query("SELECT c FROM CropEntity c WHERE c.cropCode LIKE 'CROP-%' ORDER BY CAST(SUBSTRING(c.cropCode,6)AS int) DESC LIMIT 1")
    CropEntity findLastCropCode();
}
