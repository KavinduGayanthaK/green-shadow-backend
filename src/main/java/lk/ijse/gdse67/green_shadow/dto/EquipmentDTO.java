package lk.ijse.gdse67.green_shadow.dto;

import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipmentDTO {
    private String equipmentId;
    private String equipmentName;
    private String type;
    private String status;
    private int totalCount;
    private int assignedCount;
    private List<String> fields = new ArrayList<>();
    private List<String> staff = new ArrayList<>();
}
