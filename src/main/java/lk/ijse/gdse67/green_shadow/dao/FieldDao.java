package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldDao extends JpaRepository<FieldEntity,String> {

    @Query("SELECT f FROM FieldEntity f WHERE f.fieldCode LIKE 'FIELD-%' ORDER BY CAST(SUBSTRING(f.fieldCode, 7) AS int) DESC LIMIT 1")
    FieldEntity findLastFieldCode();

}
