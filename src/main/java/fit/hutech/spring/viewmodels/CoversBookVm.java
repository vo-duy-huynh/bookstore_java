package fit.hutech.spring.viewmodels;

import fit.hutech.spring.entities.Book;
import fit.hutech.spring.entities.Cover;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
public record CoversBookVm(String urlImage) {
    public static CoversBookVm from(Cover cover) {
        return CoversBookVm.builder()
                .urlImage(cover.getUrlImage())
                .build();
    }
}