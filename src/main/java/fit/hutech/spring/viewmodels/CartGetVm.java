package fit.hutech.spring.viewmodels;

import lombok.Builder;

@Builder
public record CartGetVm(String title, Double price,Integer quantity,  String cover) {
    public static CartGetVm from(String title, Double price, Integer quantity, String cover) {
        return CartGetVm.builder()
                .title(title)
                .price(price)
                .quantity(quantity)
                .cover(cover)
                .build();
    }
}
