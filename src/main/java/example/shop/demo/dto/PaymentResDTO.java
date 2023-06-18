package example.shop.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentResDTO implements Serializable {
    public String status;
    public String message;
    public String url;
}