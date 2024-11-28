package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffDao extends JpaRepository<StaffEntity,String > {

}
