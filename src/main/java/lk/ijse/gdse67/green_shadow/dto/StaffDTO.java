package lk.ijse.gdse67.green_shadow.dto;

import lk.ijse.gdse67.green_shadow.Enum.Designation;
import lk.ijse.gdse67.green_shadow.Enum.Gender;
import lk.ijse.gdse67.green_shadow.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaffDTO {

    private String staffId;
    private String firstName;
    private String lastName;
    private Designation designation;
    private Gender gender;
    private String joinedDate;
    private String dateOfBirth;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String contactNumber;
    private String email;
    private Role role;
    private List<String> field = new ArrayList<>();
    private List<String> vehicles = new ArrayList<>();
    private List<String> equipments = new ArrayList<>();

}
