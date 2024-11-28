package lk.ijse.gdse67.green_shadow.entity.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lk.ijse.gdse67.green_shadow.Enum.Role;
import lk.ijse.gdse67.green_shadow.entity.SuperEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity implements SuperEntity {
    @Id
    private String userName;
    private String password;
    private Role role;
}
