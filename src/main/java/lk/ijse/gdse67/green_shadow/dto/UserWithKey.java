package lk.ijse.gdse67.green_shadow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithKey {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String code;
}
