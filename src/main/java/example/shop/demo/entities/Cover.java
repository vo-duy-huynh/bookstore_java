package example.shop.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cover")
public class Cover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "urlImage", length = 500, nullable = false)
    @Size(min = 1, max = 500, message = "UrlImage must be between 1 and 500 characters")
    @NotBlank(message = "Name must not be blank")
    private String urlImage;

    @Column(name = "isThumbnail", nullable = false)
    private Boolean isThumbnail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ToString.Exclude
    private Product product;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Cover cover = (Cover) o;
//        return getId() != null && Objects.equals(getId(), cover.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
}