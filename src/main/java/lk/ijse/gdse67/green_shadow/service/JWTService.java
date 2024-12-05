package lk.ijse.gdse67.green_shadow.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


public interface JWTService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    boolean validateToken(String token,UserDetails userDetails);
    String refreshToken(UserDetails userDetails);
}
