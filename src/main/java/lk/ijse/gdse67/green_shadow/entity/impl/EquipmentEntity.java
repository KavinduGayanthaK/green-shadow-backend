package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lk.ijse.gdse67.green_shadow.entity.SuperEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "equipment")
public class EquipmentEntity implements SuperEntity {
    @Id
    private String equipmentId;
    private String equipmentName;
    private String type;
    private String status; // "Available" or "Out of Service"
    private int totalCount; // Total available count of equipment
    @Column(columnDefinition = "int default 0")
    private int assignedCount; // Track how many are assigned

    // Relationships remain the same
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "field_equipment_details",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "field_code"))
    private List<FieldEntity> fields = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "staff_equipment_details",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_code"))
    private List<StaffEntity> staff = new ArrayList<>();


}