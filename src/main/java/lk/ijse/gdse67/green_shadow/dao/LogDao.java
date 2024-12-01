package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.LogEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDao extends JpaRepository<LogEntity,String > {

    @Query("SELECT l FROM LogEntity l WHERE l.logCode LIKE 'LOG-%' ORDER BY CAST(SUBSTRING(l.logCode, 7) AS int) DESC LIMIT 1")
    LogEntity findLastLogCode();
}
