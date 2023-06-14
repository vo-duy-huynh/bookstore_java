package example.shop.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String password;

    private List<Long> roleIds;

    private String username;
    private String phone;
    private String provider;

}
