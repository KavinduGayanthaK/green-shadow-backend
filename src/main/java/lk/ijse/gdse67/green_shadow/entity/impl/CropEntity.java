package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "crop")
public class CropEntity {
    @Id
    private String cropCode;
    private String commonName;
    private String scientificName;
    private String cropCategory;
    private String cropSeason;

    @ManyToMany(mappedBy = "crops")
    private List<FieldEntity> fields;
    private String cropImage;

    @ManyToMany(mappedBy = "crop")
    private List<LogEntity> logs;
}
