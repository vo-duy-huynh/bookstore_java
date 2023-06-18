package example.shop.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class TransactionDTO implements Serializable {
    public long amount;
    public String bankCode;
    public String orderDescription;
    public String orderType;
    public String status;
    public String message;
    public String createDate;
}
