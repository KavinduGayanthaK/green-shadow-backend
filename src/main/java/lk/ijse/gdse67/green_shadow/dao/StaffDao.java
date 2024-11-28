package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffDao extends JpaRepository<StaffEntity,String > {
    @Query("SELECT s FROM StaffEntity s WHERE s.staffId LIKE 'STAFF-%' ORDER BY CAST(SUBSTRING(s.staffId, 7) AS int) DESC LIMIT 1")
    StaffEntity findLastStaffId();
}
