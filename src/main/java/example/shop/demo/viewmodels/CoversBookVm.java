package example.shop.demo.viewmodels;

import example.shop.demo.entities.Cover;
import lombok.Builder;

@Builder
public record CoversBookVm(String urlImage) {
    public static CoversBookVm from(Cover cover) {
        return CoversBookVm.builder()
                .urlImage(cover.getUrlImage())
                .build();
    }
}