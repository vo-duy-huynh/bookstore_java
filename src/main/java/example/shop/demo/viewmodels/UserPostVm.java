package example.shop.demo.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
public record UserPostVm(String userName, String email, String phone) {
//    getter
    public String getUserName() {
          return userName;
     }
     public String getEmail() {
          return email;
     }
        public String getPhone() {
            return phone;
        }

}

