package example.shop.demo.daos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long bookId;
    private String cover;
    private String bookName;
    private Double price;
    private int quantity;
}
