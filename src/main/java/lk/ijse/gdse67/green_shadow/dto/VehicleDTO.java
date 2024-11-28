package lk.ijse.gdse67.green_shadow.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lk.ijse.gdse67.green_shadow.Enum.VehicleFuelType;
import lk.ijse.gdse67.green_shadow.Enum.VehicleStatus;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleDTO {
    private String licensePlateNumber;
    private String category;
    private VehicleFuelType fuelType;
    private VehicleStatus status;
    private String remarks;
    private String staff;
}
