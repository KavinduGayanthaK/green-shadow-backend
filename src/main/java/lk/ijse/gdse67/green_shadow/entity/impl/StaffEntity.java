package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lk.ijse.gdse67.green_shadow.Enum.Designation;
import lk.ijse.gdse67.green_shadow.Enum.Gender;
import lk.ijse.gdse67.green_shadow.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "staff")
public class StaffEntity {
    @Id
    private String staffId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Designation designation;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date joinedDate;
    private Date dateOfBirth;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private int contactNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "staffs")
    private List<FieldEntity> fields = new ArrayList<>();

    @OneToMany(mappedBy = "staff",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<VehicleEntity> vehicles = new ArrayList<>();

    @ManyToMany(mappedBy = "staff")
    private List<LogEntity> logs;

    @ManyToMany(mappedBy = "staff")
    private List<EquipmentEntity> equipments;
}
