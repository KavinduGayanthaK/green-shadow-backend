package lk.ijse.gdse67.green_shadow.service.impl;

import jdk.jfr.RecordingState;
import lk.ijse.gdse67.green_shadow.dao.LogDao;
import lk.ijse.gdse67.green_shadow.dto.LogDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.LogEntity;
import lk.ijse.gdse67.green_shadow.entity.impl.StaffEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.LogService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    private final LogDao logDao;
    private final Mapping mapping;

    @Autowired
    public LogServiceImpl(LogDao logDao, Mapping mapping) {
        this.logDao = logDao;
        this.mapping = mapping;
    }

    @Override
    public void saveLog(LogDTO logDTO) {
        logDao.save(mapping.toLogEntity(logDTO));
    }

    @Override
    public String generateLogCode() {
        LogEntity lastLog = logDao.findLastLogCode();

        if (lastLog != null) {
            String lastLogCode = lastLog.getLogCode();
            try {
                int lastNumber = Integer.parseInt(lastLogCode.replace("LOG-", ""));
                if (lastNumber+1<1000) {
                    return String.format("LOG-%03d", lastNumber + 1);
                }
                return String.format("LOG-%03d",lastNumber+1);

            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid field code format: " + lastLogCode, e);
            }
        } else {
            return "LOG-001";
        }
    }

    @Override
    public List<LogDTO> getAllLog() {
        return mapping.toGetAllLogDto(logDao.findAll());
    }

    @Override
    public void updateLog(LogDTO logDTO) {
        Optional<LogEntity> tempLog = logDao.findById(logDTO.getLogCode());
        LogEntity logEntity = mapping.toLogEntity(logDTO);
        if (tempLog.isPresent()) {
            tempLog.get().setLogDetail(logEntity.getLogDetail());
            tempLog.get().setLogDate(logEntity.getLogDate());
            tempLog.get().setLogImage(logEntity.getLogImage());
            tempLog.get().setFields(logEntity.getFields());
            tempLog.get().setStaff(logEntity.getStaff());
            tempLog.get().setCrop(logEntity.getCrop());
        }
    }

    @Override
    public void deleteLog(String logCode) {
        Optional<LogEntity> existLog = logDao.findById(logCode);
        if(existLog.isPresent()) {
            logDao.deleteById(logCode);
        }else {
            throw new NotFoundException("Log with code "+logCode+" not found");
        }
    }


}
