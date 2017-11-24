package guru.springframework.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@NoArgsConstructor
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amout;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure uom;

    @ManyToOne
    private Recipe recipe;

    public Ingredient(String description, BigDecimal amout, UnitOfMeasure uom) {
        this.description = description;
        this.amout = amout;
        this.uom = uom;
    }

    public Ingredient(String description, BigDecimal amout, UnitOfMeasure uom, Recipe recipe) {
        this.description = description;
        this.amout = amout;
        this.uom = uom;
        this.recipe = recipe;
    }

}
