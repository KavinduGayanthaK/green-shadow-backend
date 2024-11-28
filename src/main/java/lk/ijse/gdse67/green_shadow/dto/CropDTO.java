package lk.ijse.gdse67.green_shadow.dto;

import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.LogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CropDTO {
    private String cropCode;
    private String commonName;
    private String scientificName;
    private String cropCategory;
    private String cropSeason;
    private List<String> fields;
    private String cropImage;

}
