package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.LogDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


public interface LogService extends Serializable {
    void saveLog(LogDTO logDTO);
    String generateLogCode();
    List<LogDTO> getAllLog();
    void updateLog(LogDTO logDTO);
    void deleteLog(String logCode);
}
