package lk.ijse.gdse67.green_shadow.dto;

import lk.ijse.gdse67.green_shadow.entity.impl.CropEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogDTO {
    private String logCode;
    private String logDetail;
    private String logDate;
    private String logImage;
    private List<String> fields = new ArrayList<>();
    private List<String> staff = new ArrayList<>();
    private List<String> crop = new ArrayList<>();
}
