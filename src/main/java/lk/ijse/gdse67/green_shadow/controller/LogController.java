package lk.ijse.gdse67.green_shadow.controller;

import lk.ijse.gdse67.green_shadow.dto.LogDTO;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.service.LogService;
import lk.ijse.gdse67.green_shadow.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/log")
@CrossOrigin(origins = "*")
public class LogController {

    private final LogService logService;
    private final AppUtil appUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveLog(
            @RequestPart("logDetail") String logDetail,
            @RequestPart("logDate") String logDate,
            @RequestPart(value = "logImage",required = false) MultipartFile logImage,
            @RequestPart("fields") List<String> fields,
            @RequestPart("staff") List<String> staff,
            @RequestPart("crop") List<String> crop

    ) {

        try{
            LogDTO logDTO = new LogDTO();
            logDTO.setLogCode(appUtil.generateLogCode());
            logDTO.setLogDetail(logDetail);
            logDTO.setLogDate(logDate);
            logDTO.setLogImage(appUtil.generateImage(logImage));
            logDTO.setFields(fields);
            logDTO.setStaff(staff);
            logDTO.setCrop(crop);
            System.out.println("LOG DTO : "+logDTO);
            logService.saveLog(logDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LogDTO> getAllLog() {
        return logService.getAllLog();
    }

    @PatchMapping(value = "/{logCode}",consumes ={ MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Void> updateLog(
            @RequestPart("logDetail") String logDetail,
            @RequestPart("logDate") String logDate,
            @RequestPart("fields") List<String> fields,
            @RequestPart("staff") List<String> staff,
            @RequestPart("crop") List<String> crop,
            @RequestPart(value = "logImage",required = false) MultipartFile logImage,
            @PathVariable("logCode") String logCode
    ){

        try{
            LogDTO buildLogDTO = new LogDTO();
            buildLogDTO.setLogCode(logCode);
            buildLogDTO.setLogDetail(logDetail);
            buildLogDTO.setLogDate(logDate);
            buildLogDTO.setLogImage(appUtil.generateImage(logImage));
            buildLogDTO.setFields(fields);
            buildLogDTO.setStaff(staff);
            buildLogDTO.setCrop(crop);

            logService.updateLog(buildLogDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{logCode}")
    public ResponseEntity<Void> deleteLog(@PathVariable("logCode") String logCode) {
        try{
            String regexForLogCode = "^LOG-\\d{3,4}$";
            Pattern regexPattern = Pattern.compile(regexForLogCode);
            Matcher regexMatcher =regexPattern.matcher(logCode);
            if (!regexMatcher.matches()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            logService.deleteLog(logCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
