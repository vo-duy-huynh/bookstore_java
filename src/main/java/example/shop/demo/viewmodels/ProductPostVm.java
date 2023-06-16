package example.shop.demo.viewmodels;

import example.shop.demo.entities.Product;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductPostVm(String title, String author, Double price, Long categoryId) {
    public static ProductPostVm from(@NotNull Product product) {
        return new ProductPostVm(product.getTitle(), product.getAuthor(), product.getPrice(), product.getCategory().getId());
    }
}
