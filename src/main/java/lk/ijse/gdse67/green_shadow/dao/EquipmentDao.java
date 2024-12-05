package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.EquipmentEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentDao extends JpaRepository<EquipmentEntity,String > {

    @Query("SELECT e FROM EquipmentEntity e WHERE e.equipmentId LIKE 'EQUIP-%' ORDER BY CAST(SUBSTRING(e.equipmentId, 7) AS int) DESC LIMIT 1")
    EquipmentEntity findLastEquipmentId();

}
