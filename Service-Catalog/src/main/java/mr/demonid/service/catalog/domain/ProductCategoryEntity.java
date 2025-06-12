package mr.demonid.service.catalog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "categories")
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false, length = 128)
    private String name;

    @Column(unique = false, nullable = true, length = 512)
    private String description;

    public ProductCategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProductCategoryEntity(String name) {
        this(name, null);
    }

}

