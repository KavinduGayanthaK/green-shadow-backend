package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lk.ijse.gdse67.green_shadow.Enum.VehicleFuelType;
import lk.ijse.gdse67.green_shadow.Enum.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "vehicle")
public class VehicleEntity {
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
