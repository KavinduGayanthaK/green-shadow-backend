package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.UserDao;
import lk.ijse.gdse67.green_shadow.dto.UserDTO;
import lk.ijse.gdse67.green_shadow.entity.impl.UserEntity;
import lk.ijse.gdse67.green_shadow.secure.JWTAuthResponse;
import lk.ijse.gdse67.green_shadow.service.AuthService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final Mapping mapping;

    @Override
    public void signUp(UserDTO userDTO) {
        userDao.save(mapping.toUserEntity(userDTO));
    }
}
