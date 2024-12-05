package lk.ijse.gdse67.green_shadow.service.impl;

import lk.ijse.gdse67.green_shadow.dao.UserDao;
import lk.ijse.gdse67.green_shadow.dto.ChangePasswordDTO;
import lk.ijse.gdse67.green_shadow.dto.UserDTO;
import lk.ijse.gdse67.green_shadow.dto.UserWithKey;
import lk.ijse.gdse67.green_shadow.entity.impl.UserEntity;
import lk.ijse.gdse67.green_shadow.exception.NotFoundException;
import lk.ijse.gdse67.green_shadow.secure.JWTAuthResponse;
import lk.ijse.gdse67.green_shadow.secure.SignIn;
import lk.ijse.gdse67.green_shadow.service.AuthService;
import lk.ijse.gdse67.green_shadow.service.EmailService;
import lk.ijse.gdse67.green_shadow.service.JWTService;
import lk.ijse.gdse67.green_shadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final Mapping mapping;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public void signUp(UserDTO userDTO) {
        emailService.sendEmail(userDTO.getEmail(), "Email From GreenShadow", "Your user email:  " + userDTO.getEmail() + "\n Your temporary password to login: " + userDTO.getPassword());
        userDao.save(mapping.toUserEntity(userDTO));
    }

    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword()));
        UserEntity user = userDao.findByEmail(signIn.getEmail())
                .orElseThrow(()-> new NotFoundException("User not found"));
        System.out.println("User dao"+user);
        var generatedToken = jwtService.generateToken(user);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    @Override
    public JWTAuthResponse refreshToken(String accessToken) {
        //extract user name
        String username = jwtService.extractUsername(accessToken);
        //is existed the user
        var findUser = userDao.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("user not found"));
        var refreshToken = jwtService.refreshToken(findUser);
        return JWTAuthResponse.builder().token(refreshToken).build();
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        Optional<UserEntity> byEmail = userDao.findByEmail(changePasswordDTO.getEmail());
        if (byEmail.isPresent()) {
            byEmail.get().setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        }
    }

    @Override
    public boolean sendCodeToChangePassword(UserWithKey userWithKey) {
        Optional<UserEntity> byEmail = userDao.findByEmail(userWithKey.getEmail());
        if (byEmail.isPresent()) {
            emailService.sendEmail(userWithKey.getEmail(), "Your password change Code From Green Shadow(PVT) Ltd.", "Dont share with anyone:  " + userWithKey.getCode());
            return true;
        }
        return false;
    }
}
