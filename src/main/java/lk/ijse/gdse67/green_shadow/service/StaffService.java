package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.StaffDTO;

import java.io.Serializable;
import java.util.List;

public interface StaffService extends Serializable {
    void saveStaff(StaffDTO staffDTO);

    String generateStaffId();

}
