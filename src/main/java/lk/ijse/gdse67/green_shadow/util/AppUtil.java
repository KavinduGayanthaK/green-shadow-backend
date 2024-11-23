package lk.ijse.gdse67.green_shadow.util;

import lk.ijse.gdse67.green_shadow.service.FieldService;
import lk.ijse.gdse67.green_shadow.service.impl.FieldServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class AppUtil {

    private final FieldService fieldService;

    public AppUtil(FieldServiceImpl fieldService) {
        this.fieldService = fieldService;
    }

    public String generateFieldCode() {
        return fieldService.generateFieldCode(); // Delegate to FieldService
    }

    public String generateFieldImage(MultipartFile fieldImage) throws IOException {
        if (fieldImage == null || fieldImage.isEmpty()) {
            return null;
        }
        byte[] byteImage = fieldImage.getBytes();
        return Base64.getEncoder().encodeToString(byteImage);
    }
}



