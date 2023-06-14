package example.shop.demo.viewmodels;

import example.shop.demo.entities.Category;
import lombok.Builder;

@Builder
public record CategoryGetVm(Long id,String name) {
    public static CategoryGetVm from(Category category) {
        return CategoryGetVm.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
