package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.*;
import lk.ijse.gdse67.green_shadow.entity.SuperEntity;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "crop")
public class CropEntity implements SuperEntity {
    @Id
    private String cropCode;
    private String commonName;
    private String scientificName;
    private String cropCategory;
    private String cropSeason;

    @ManyToMany(mappedBy = "crops",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<FieldEntity> fields;
    @Column(columnDefinition = "LONGTEXT")
    private String cropImage;

    @ManyToMany(mappedBy = "crop",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<LogEntity> logs;
}
