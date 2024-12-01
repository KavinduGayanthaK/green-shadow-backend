package lk.ijse.gdse67.green_shadow.util;


import lk.ijse.gdse67.green_shadow.Enum.Designation;
import lk.ijse.gdse67.green_shadow.Enum.Gender;
import lk.ijse.gdse67.green_shadow.Enum.Role;
import lk.ijse.gdse67.green_shadow.dao.*;
import lk.ijse.gdse67.green_shadow.dto.*;
import lk.ijse.gdse67.green_shadow.entity.impl.*;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.CropService;
import lk.ijse.gdse67.green_shadow.service.LogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Mapping {
    private final CropDao cropDao;
    private final StaffDao staffDao;
    private final FieldDao fieldDao;
    private final VehicleDao vehicleDao;
    private final EquipmentDao equipmentDao;


    private final ModelMapper modelMapper;

//    public Mapping(CropDao cropDao, StaffDao staffDao, FieldDao fieldDao, VehicleDao vehicleDao, EquipmentDao equipmentDao, LogService logService, ModelMapper modelMapper) {
//        this.cropDao = cropDao;
//        this.staffDao = staffDao;
//        this.fieldDao = fieldDao;
//        this.vehicleDao = vehicleDao;
//        this.equipmentDao = equipmentDao;
//        this.logService = logService;
//        this.modelMapper = modelMapper;
//    }


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

    public CropEntity toCropEntity(CropDTO cropDTO) {
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

    public StaffEntity toStaffEntity(StaffDTO staffDTO) {
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setStaffId(staffDTO.getStaffId());
        staffEntity.setFirstName(staffDTO.getFirstName());
        staffEntity.setLastName(staffDTO.getLastName());
        staffEntity.setDesignation(staffDTO.getDesignation());
        staffEntity.setGender(staffDTO.getGender());
        staffEntity.setJoinedDate(LocalDate.parse(staffDTO.getJoinedDate()));
        staffEntity.setDateOfBirth(LocalDate.parse(staffDTO.getDateOfBirth()));
        staffEntity.setAddress1(staffDTO.getAddress1());
        staffEntity.setAddress2(staffDTO.getAddress2());
        staffEntity.setAddress3(staffDTO.getAddress3());
        staffEntity.setAddress4(staffDTO.getAddress4());
        staffEntity.setAddress5(staffDTO.getAddress5());
        staffEntity.setContactNumber(staffDTO.getContactNumber());
        staffEntity.setEmail(staffDTO.getEmail());
        staffEntity.setRole(staffDTO.getRole());

        // Map fields
        List<FieldEntity> fieldEntities = new ArrayList<>();
        for (String fieldCode : staffDTO.getField()) {
            FieldEntity fieldEntity = fieldDao.findById(fieldCode)
                    .orElseThrow(() -> new NotFoundException("Field not found: " + fieldCode));
            fieldEntities.add(fieldEntity);

            // Update FieldEntity.staffs (owning side)
            fieldEntity.getStaffs().add(staffEntity);
        }
        staffEntity.setFields(fieldEntities);

        // Other mappings remain the same
        staffEntity.setVehicles(staffDTO.getVehicles().stream()
                .map(vehicle -> vehicleDao.findById(vehicle)
                        .orElseThrow(() -> new NotFoundException("Vehicle not found: " + vehicle)))
                .toList());

        staffEntity.setEquipments(staffDTO.getEquipments().stream()
                .map(eq -> equipmentDao.findById(eq)
                        .orElseThrow(() -> new NotFoundException("Equipment not found: " + eq)))
                .toList());

        return staffEntity;
    }




    public EquipmentEntity toEquipmentEntity(EquipmentDTO equipmentDTO) {
        EquipmentEntity equipmentEntity = new EquipmentEntity();
        equipmentEntity.setEquipmentId(equipmentDTO.getEquipmentId());
        equipmentEntity.setEquipmentName(equipmentDTO.getEquipmentName());
        equipmentEntity.setType(equipmentDTO.getType());
        equipmentEntity.setStatus(equipmentDTO.getStatus());
        equipmentEntity.setFields(equipmentDTO.getFields().stream().map(fieldCode->fieldDao.findById(fieldCode).
                orElseThrow(()->new NotFoundException("Field Not Found"+fieldCode))).toList());
        equipmentEntity.setStaff(equipmentDTO.getStaff().stream().map(staffId->staffDao.findById(staffId).
                orElseThrow(()->new NotFoundException("Staff Not Found"+staffId))).toList());

        return equipmentEntity;
    }

    public List<EquipmentDTO> toGetAllEquipmentDTO(List<EquipmentEntity> equipmentEntities) {
        List<EquipmentDTO> equipmentDTOS = new ArrayList<>();

        equipmentEntities.forEach(equipmentEntity->{
            EquipmentDTO equipmentDTO = new EquipmentDTO(
                    equipmentEntity.getEquipmentId(),
                    equipmentEntity.getEquipmentName(),
                    equipmentEntity.getType(),
                    equipmentEntity.getStatus(),
                    equipmentEntity.getFields().stream()
                            .map(FieldEntity::getFieldCode)
                            .toList(),
                    equipmentEntity.getStaff().stream().map(StaffEntity::getStaffId)
                            .toList()

            );
            equipmentDTOS.add(equipmentDTO);
        });
        return equipmentDTOS;
    }

    public List<StaffDTO> toGetAllStaffDTO(List<StaffEntity> staffEntities) {
        List<StaffDTO> staffDTOS = new ArrayList<>();

        staffEntities.forEach(staffEntity->{
            StaffDTO staffDTO = new StaffDTO(
                    staffEntity.getStaffId(),
                    staffEntity.getFirstName(),
                    staffEntity.getLastName(),
                    staffEntity.getDesignation(),
                    staffEntity.getGender(),
                    String.valueOf(staffEntity.getJoinedDate()),
                    String.valueOf(staffEntity.getDateOfBirth()),
                    staffEntity.getAddress1(),
                    staffEntity.getAddress2(),
                    staffEntity.getAddress3(),
                    staffEntity.getAddress4(),
                    staffEntity.getAddress5(),
                    staffEntity.getContactNumber(),
                    staffEntity.getEmail(),
                    staffEntity.getRole(),
                    staffEntity.getFields().stream()
                            .map(FieldEntity::getFieldCode)
                            .toList(),
                    staffEntity.getVehicles().stream().map(VehicleEntity::getLicensePlateNumber)
                            .toList(),
                    staffEntity.getEquipments().stream().map(EquipmentEntity::getEquipmentId)
                            .toList()

            );
            staffDTOS.add(staffDTO);
        });
        return staffDTOS;
    }

    public VehicleEntity toVehicleEntity(VehicleDTO vehicleDTO) {
        VehicleEntity vehicleEntity =  new VehicleEntity();
        vehicleEntity.setLicensePlateNumber(vehicleDTO.getLicensePlateNumber());
        vehicleEntity.setFuelType(vehicleDTO.getFuelType());
        vehicleEntity.setCategory(vehicleDTO.getCategory());
        vehicleEntity.setStatus(vehicleDTO.getStatus());
        vehicleEntity.setRemarks(vehicleDTO.getRemarks());
        StaffEntity staffEntity = staffDao.findById(vehicleDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("Staff not found: " + vehicleDTO.getStaff()));
        vehicleEntity.setStaff(staffEntity);
        staffEntity.getVehicles().add(vehicleEntity);

        return vehicleEntity;
    }

    public List<VehicleDTO> vehicleDTO(List<VehicleEntity> vehicleEntities) {
        return modelMapper.map(vehicleEntities,new TypeToken<List<VehicleDTO>>() {}.getType());
    }



    public LogEntity toLogEntity(LogDTO logDTO) {
        LogEntity logEntity =  new LogEntity();
        logEntity.setLogCode(logDTO.getLogCode());
        logEntity.setLogDetail(logDTO.getLogDetail());
        logEntity.setLogDate(LocalDate.parse(logDTO.getLogDate()));
        logEntity.setLogImage(logDTO.getLogImage());
        logEntity.setFields(logDTO.getFields().stream().map(fields->
                fieldDao.findById(fields).orElseThrow(()->new NotFoundException("Field not found : "+fields))).toList());
        logEntity.setStaff(logDTO.getStaff().stream().map(staff->
                staffDao.findById(staff).orElseThrow(()->new NotFoundException("Staff not found : "+staff))).toList());
        logEntity.setCrop(logDTO.getCrop().stream().map(crop->
                cropDao.findById(crop).orElseThrow(()->new NotFoundException("Crop not found : "+crop))).toList());

        return logEntity;

    }

    public List<LogDTO> toGetAllLogDto(List<LogEntity> logEntities) {
        List<LogDTO> logDTOS = new ArrayList<>();
        logEntities.forEach(logEntity -> {
            LogDTO logDTO = new LogDTO(
                 logEntity.getLogCode(),
                    logEntity.getLogDetail(),
                    String.valueOf(logEntity.getLogDate()),
                    logEntity.getLogImage(),
                    logEntity.getFields().stream().map(FieldEntity::getFieldCode).toList(),
                    logEntity.getStaff().stream().map(StaffEntity::getStaffId).toList(),
                    logEntity.getCrop().stream().map(CropEntity::getCropCode).toList()
            );
            logDTOS.add(logDTO);
        });
        return logDTOS;
    }

}
