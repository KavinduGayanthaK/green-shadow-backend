package lk.ijse.gdse67.green_shadow.controller;

import lk.ijse.gdse67.green_shadow.dto.ChangePasswordDTO;
import lk.ijse.gdse67.green_shadow.dto.UserDTO;
import lk.ijse.gdse67.green_shadow.dto.UserWithKey;
import lk.ijse.gdse67.green_shadow.exception.DataPersistException;
import lk.ijse.gdse67.green_shadow.secure.JWTAuthResponse;
import lk.ijse.gdse67.green_shadow.secure.SignIn;
import lk.ijse.gdse67.green_shadow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signup",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try{
            authService.signUp(userDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e ) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/signin",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTAuthResponse> signIn(@RequestBody SignIn signIn) {
        System.out.println("Enter user controller");
        System.out.println("Auth details:"+signIn);
        System.out.println(authService.signIn(signIn));
        return ResponseEntity.ok(authService.signIn(signIn));
    }

    @PostMapping("refresh")
    public ResponseEntity<JWTAuthResponse> signIn(@RequestParam("refreshToken") String existingToken) {
        return ResponseEntity.ok(authService.refreshToken(existingToken));
    }

    @PostMapping(value = "/sendCode")
    public ResponseEntity<Void> sendCode(@RequestBody() UserWithKey userWithKey) {
        if (authService.sendCodeToChangePassword(userWithKey)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody() ChangePasswordDTO changePasswordDTO) {
        try {
            authService.changePassword(changePasswordDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
