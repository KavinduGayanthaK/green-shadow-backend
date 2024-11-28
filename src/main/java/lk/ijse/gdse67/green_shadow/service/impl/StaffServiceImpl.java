package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.EquipmentDao;
import lk.ijse.gdse67.green_shadow.dao.StaffDao;
import lk.ijse.gdse67.green_shadow.dao.StaffEquipmentDetailsDao;
import lk.ijse.gdse67.green_shadow.dto.StaffDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lk.ijse.gdse67.green_shadow.service.StaffService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffDao staffDao;

    private final Mapping mapping;


    @Autowired
    public StaffServiceImpl(StaffDao staffDao, EquipmentDao equipmentDao, StaffEquipmentDetailsDao equipmentDetailsDao, Mapping mapping) {
        this.staffDao = staffDao;

        this.mapping = mapping;
    }

    @Override
    public void saveStaff(StaffDTO staffDTO) {
        staffDao.save(mapping.toStaffEntity(staffDTO));
    }

    @Override
    public String generateStaffId() {
        StaffEntity lastStaff = staffDao.findLastStaffId();

        if (lastStaff != null) {
            String lastStaffId = lastStaff.getStaffId();
            try {
                int lastNumber = Integer.parseInt(lastStaffId.replace("STAFF-", ""));
                return String.format("STAFF-%03d", lastNumber + 1);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid field code format: " + lastStaffId, e);
            }
        } else {
            return "STAFF-001";
        }
    }


}
