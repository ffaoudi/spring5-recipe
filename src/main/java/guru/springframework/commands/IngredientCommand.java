package guru.springframework.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class IngredientCommand {
    private Long id;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureCommand uom;
}
