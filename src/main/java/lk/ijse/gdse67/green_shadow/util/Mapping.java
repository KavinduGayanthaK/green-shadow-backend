package lk.ijse.gdse67.green_shadow.util;


import lk.ijse.gdse67.green_shadow.dao.CropDao;
import lk.ijse.gdse67.green_shadow.dao.EquipmentDao;
import lk.ijse.gdse67.green_shadow.dao.FieldDao;
import lk.ijse.gdse67.green_shadow.dao.StaffDao;
import lk.ijse.gdse67.green_shadow.dto.CropDTO;
import lk.ijse.gdse67.green_shadow.dto.FieldDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.CropEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.FieldEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.CropService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Mapping {
    private final CropDao cropDao;
    private final StaffDao staffDao;
    private final FieldDao fieldDao;

    private final ModelMapper modelMapper;

    public Mapping(CropDao cropDao, StaffDao staffDao, FieldDao fieldDao, ModelMapper modelMapper) {
        this.cropDao = cropDao;
        this.staffDao = staffDao;
        this.fieldDao = fieldDao;
        this.modelMapper = modelMapper;
    }


    public FieldEntity toFieldEntity(FieldDTO fieldDTO) {
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setFieldCode(fieldDTO.getFieldCode());
        fieldEntity.setFieldName(fieldDTO.getFieldName());
        fieldEntity.setLocation(fieldDTO.getLocation());
        fieldEntity.setExtendSizeOfField(fieldDTO.getExtendSizeOfField());
        fieldEntity.setImage1(fieldDTO.getImage1());
        fieldEntity.setImage2(fieldDTO.getImage2());
        fieldEntity.setCrops(fieldDTO.getCrops().stream().map(cropCodes->
                cropDao.findById(cropCodes).orElseThrow(()->new NotFoundException("Crop not found : "+cropCodes))).toList());
        fieldEntity.setStaffs(fieldDTO.getStaff().stream().map(saffId->
                staffDao.findById(saffId).orElseThrow(()->new NotFoundException("Staff not found : "+saffId))).toList());

        return fieldEntity;
    }

    public List<FieldDTO> toGetAllFieldDTO(List<FieldEntity> fieldEntities) {
        List<FieldDTO> dtoList = new ArrayList<>();

        fieldEntities.forEach(fieldEntity -> {
            FieldDTO fieldDTO = new FieldDTO(
                    fieldEntity.getFieldCode(),
                    fieldEntity.getFieldName(),
                    fieldEntity.getLocation(),
                    fieldEntity.getExtendSizeOfField(),
                    fieldEntity.getImage1(),
                    fieldEntity.getImage2(),
                    fieldEntity.getCrops().stream()
                            .map(CropEntity::getCropCode)
                            .toList(),
                    fieldEntity.getStaffs().stream()
                            .map(StaffEntity::getStaffId)
                            .toList()
            );
            dtoList.add(fieldDTO);
        });

        return dtoList;
    }

    public List<CropDTO> toGetAllCropDTO(List<CropEntity> cropEntities) {
        List<CropDTO> cropDTOS = new ArrayList<>();

        cropEntities.forEach(cropEntity->{
            CropDTO cropDTO = new CropDTO(
                    cropEntity.getCropCode(),
                    cropEntity.getCommonName(),
                    cropEntity.getScientificName(),
                    cropEntity.getCropCategory(),
                    cropEntity.getCropSeason(),
                    cropEntity.getFields().stream().map(FieldEntity::getFieldCode)
                            .toList(),
                    cropEntity.getCropImage()
            );
            cropDTOS.add(cropDTO);
        });
      return cropDTOS;
    }

    public CropEntity  toCropEntity(CropDTO cropDTO) {
        CropEntity cropEntity = new CropEntity();
        cropEntity.setCropCode(cropDTO.getCropCode());
        cropEntity.setCommonName(cropDTO.getCommonName());
        cropEntity.setScientificName(cropDTO.getScientificName());
        cropEntity.setCropCategory(cropDTO.getCropCategory());
        cropEntity.setCropSeason(cropDTO.getCropSeason());
        cropEntity.setFields(cropDTO.getFields().stream().map(fieldCode->
                fieldDao.findById(fieldCode).orElseThrow(()->new NotFoundException("Field not found : "+fieldCode))).toList());
        cropEntity.setCropImage(cropDTO.getCropImage());
        return cropEntity;
    }

}
