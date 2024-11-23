package lk.ijse.gdse67.green_shadow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FieldDTO {
    private String fieldCode;
    private String fieldName;
    private Point location;
    private String extendSizeOfField;
    private String image1;
    private String image2;
    private List<String> crops = new ArrayList<>();
    private List<String> staff = new ArrayList<>();
}
