package lk.ijse.gdse67.green_shadow.util;

import lk.ijse.gdse67.green_shadow.service.*;
import lk.ijse.gdse67.green_shadow.service.impl.CropServiceImpl;
import lk.ijse.gdse67.green_shadow.service.impl.FieldServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class AppUtil {

    private final FieldService fieldService;
    private final CropService cropService;
    private final StaffService staffService;
    private final EquipmentService equipmentService;
    private final LogService logService;

    public AppUtil(FieldServiceImpl fieldService, CropServiceImpl cropService, StaffService staffService, EquipmentService equipmentService, LogService logService) {
        this.fieldService = fieldService;
        this.cropService = cropService;
        this.staffService = staffService;
        this.equipmentService = equipmentService;
        this.logService = logService;
    }

    //Generate field code
    public String generateFieldCode() {
        return fieldService.generateFieldCode();
    }

    //Generate crop code
    public  String generateCropCode() {
        return cropService.generateCropCode();
    }

    //Generate staff id
    public String generateStaffId(){return staffService.generateStaffId();}

    //generate equipment id
    public String generateEquipmentCode(){return equipmentService.generateEquipmentCode();}
    public  String generateImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }
        byte[] byteImage = image.getBytes();
        return Base64.getEncoder().encodeToString(byteImage);
    }

    public String generateLogCode(){
        return logService.generateLogCode();
    }




}



