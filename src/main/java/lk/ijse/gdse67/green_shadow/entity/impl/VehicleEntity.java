package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lk.ijse.gdse67.green_shadow.Enum.VehicleFuelType;
import lk.ijse.gdse67.green_shadow.Enum.VehicleStatus;
import lk.ijse.gdse67.green_shadow.entity.SuperEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vehicle")
public class VehicleEntity implements SuperEntity {
    @Id
    private String licensePlateNumber;
    private String category;
    private VehicleFuelType fuelType;
    private VehicleStatus status;
    private String remarks;
    @ManyToOne()
    @JoinColumn(name = "vehicles",nullable = false)
    private StaffEntity staff;
}
