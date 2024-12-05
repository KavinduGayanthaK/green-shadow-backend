package lk.ijse.gdse67.green_shadow.service;

import lk.ijse.gdse67.green_shadow.dto.ChangePasswordDTO;
import lk.ijse.gdse67.green_shadow.dto.UserDTO;
import lk.ijse.gdse67.green_shadow.dto.UserWithKey;
import lk.ijse.gdse67.green_shadow.secure.JWTAuthResponse;
import lk.ijse.gdse67.green_shadow.secure.SignIn;

public interface AuthService {
    void signUp(UserDTO userDTO);
    JWTAuthResponse signIn(SignIn signIn);
    JWTAuthResponse refreshToken(String accessToken);
    void changePassword(ChangePasswordDTO changePasswordDTO);
    boolean sendCodeToChangePassword(UserWithKey userWithKey);
}
