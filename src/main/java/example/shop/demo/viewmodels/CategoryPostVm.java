package example.shop.demo.viewmodels;

import example.shop.demo.entities.Category;
import lombok.Builder;

@Builder
public record CategoryPostVm(String name) {
    public static CategoryPostVm from(Category category) {
        return new CategoryPostVm(category.getName());
    }
}