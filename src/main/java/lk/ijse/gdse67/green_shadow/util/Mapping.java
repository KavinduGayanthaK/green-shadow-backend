package lk.ijse.gdse67.green_shadow.util;


import lk.ijse.gdse67.green_shadow.dao.CropDao;
import lk.ijse.gdse67.green_shadow.dao.EquipmentDao;
import lk.ijse.gdse67.green_shadow.dao.StaffDao;
import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {
    private final CropDao cropDao;
    private final StaffDao staffDao;

    private final ModelMapper modelMapper;

    public Mapping(CropDao cropDao, EquipmentDao equipmentDao, StaffDao staffDao, ModelMapper modelMapper) {
        this.cropDao = cropDao;
        this.staffDao = staffDao;
        this.modelMapper = modelMapper;
    }

    public List<FieldDTO> toFieldDTO(List<FieldEntity> fieldEntities) {
        return modelMapper.map(fieldEntities,new TypeToken<List<FieldDTO>>() {}.getType());
    }

    public FieldEntity toFieldEntity(FieldDTO fieldDTO) {
        System.out.println("sadasd");
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setFieldCode(fieldDTO.getFieldCode());
        fieldEntity.setFieldName(fieldDTO.getFieldName());
        fieldEntity.setLocation(fieldDTO.getLocation());
        fieldEntity.setExtendSizeOfField(fieldDTO.getExtendSizeOfField());
        fieldEntity.setImage1(fieldDTO.getImage1());
        fieldEntity.setImage2(fieldDTO.getImage2());
        System.out.println("sadasd");
        fieldEntity.setCrops(fieldDTO.getCrops().stream().map(cropCodes->
                cropDao.findById(cropCodes).orElseThrow(()->new NotFoundException("Crop not found : "+cropCodes))).toList());
        fieldEntity.setStaffs(fieldDTO.getStaff().stream().map(saffId->
                staffDao.findById(saffId).orElseThrow(()->new NotFoundException("Staff not found : "+saffId))).toList());
        System.out.println("Field Entity");
        return fieldEntity;
    }

}
