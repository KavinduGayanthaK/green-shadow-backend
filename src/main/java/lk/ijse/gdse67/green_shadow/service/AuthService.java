package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.UserDTO;
import lk.ijse.gdse67.green_shadow.secure.JWTAuthResponse;

public interface AuthService {
    void signUp(UserDTO userDTO);
}
