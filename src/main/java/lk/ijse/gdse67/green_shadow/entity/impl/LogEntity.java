package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lk.ijse.gdse67.green_shadow.entity.SuperEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "log")
public class LogEntity implements SuperEntity {
    @Id
    private String logCode;
    private String logDetail;
    private LocalDate logDate;
    private String logImage;



    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "field_log_detais",
            joinColumns = @JoinColumn(name = "log_code"),
            inverseJoinColumns = @JoinColumn(name = "field_code"))
    private List<FieldEntity> fields = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "staff_log_detais",
            joinColumns = @JoinColumn(name = "log_code"),
            inverseJoinColumns = @JoinColumn(name = "staff_code"))
    private List<StaffEntity> staff = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "crop_log_detais",
            joinColumns = @JoinColumn(name = "log_code"),
            inverseJoinColumns = @JoinColumn(name = "crop_code"))
    private List<CropEntity> crop = new ArrayList<>();
}
