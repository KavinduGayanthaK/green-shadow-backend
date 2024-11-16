package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity {
    @Id
    private String equipmentId;
    private String equipmentName;
    private String Type;
    private String status;
    @ManyToMany(mappedBy = "equipments")
    private List<FieldEntity> fields = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "staff_equipment_details",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_code"))
    private List<StaffEntity> staff = new ArrayList<>();
}
