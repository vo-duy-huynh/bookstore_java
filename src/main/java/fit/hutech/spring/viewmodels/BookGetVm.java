package fit.hutech.spring.viewmodels;

import fit.hutech.spring.entities.Book;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookGetVm(Long id, String title, String author, Double price, Integer quantity, String description,
                        Boolean isSale, Integer salePercent, String Category) {
    public static BookGetVm from(@NotNull Book book) {
        return BookGetVm.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .isSale(book.getIsSale())
                .salePercent(book.getSalePercent())
                .Category(book.getCategory().getName())
                .build();
    }
}
