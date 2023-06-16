package example.shop.demo.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private Integer quantity;
    private String description;
    private Boolean isSale;
    private Integer salePercent;
    private Long categoryId;

}
