package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.interceptor.CacheAspectSupport;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "field")
public class FieldEntity {
    @Id
    private String fieldCode;
    private String fieldName;
    private Point location;
    private String extendSizeOfField;
    @Column(columnDefinition = "LONGTEXT")
    private String image1;
    @Column(columnDefinition = "LONGTEXT")
    private String image2;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "field_crop_details",
            joinColumns = @JoinColumn(name = "field_code"),
            inverseJoinColumns = @JoinColumn(name = "crop_code"))
    private List<CropEntity> crops = new ArrayList<>();

    @ManyToMany(mappedBy = "fields")
    private List<LogEntity> logs = new ArrayList<>();

    @ManyToMany(mappedBy = "fields")
    private List<EquipmentEntity> equipments = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "field_staff_details",
    joinColumns = @JoinColumn(name = "field_code"),
    inverseJoinColumns = @JoinColumn(name = "staff_code"))
    private List<StaffEntity> staffs = new ArrayList<>();

}
