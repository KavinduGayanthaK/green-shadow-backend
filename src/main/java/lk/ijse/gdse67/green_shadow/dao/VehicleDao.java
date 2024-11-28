package lk.ijse.gdse67.green_shadow.dao;

import lk.ijse.gdse67.green_shadow.entity.impl.VehicleEntity;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDao extends JpaRepository<VehicleEntity,String > {
}
