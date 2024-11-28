package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
}
