package lk.ijse.gdse67.green_shadow.dto;

import lk.ijse.gdse67.green_shadow.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String email;
    private String password;
    private Role role;
}
