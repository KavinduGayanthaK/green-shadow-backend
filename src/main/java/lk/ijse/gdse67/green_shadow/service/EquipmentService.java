package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.EquipmentDTO;

import java.io.Serializable;
import java.util.List;

public interface EquipmentService extends Serializable {

    String generateEquipmentCode();
    void save(EquipmentDTO equipmentDTO);
    List<EquipmentDTO> getAllEquipment();


    void updateEquipment(String equipmentId, EquipmentDTO equipmentDTO);
    boolean deleteEquipment(String equipmentId);


}
