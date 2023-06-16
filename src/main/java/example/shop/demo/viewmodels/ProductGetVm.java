package example.shop.demo.viewmodels;

import example.shop.demo.entities.Category;
import example.shop.demo.entities.Product;
import example.shop.demo.entities.Cover;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductGetVm(Long id, String title, String author, Double price, Integer quantity, String description,
                           Boolean isSale, Integer salePercent, String category, String cover) {
    public static ProductGetVm from(Product product) {
        return ProductGetVm.builder()
                .id(product.getId())
                .title(product.getTitle())
                .author(product.getAuthor())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .isSale(product.getIsSale())
                .salePercent(product.getSalePercent())
                .category(product.getCategory().getName())
                .cover(product.getCovers().get(0).getUrlImage())
                .build();
    }
}
