package guru.springframework.repositories;

import guru.springframework.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByRecipe_IdAndIdOrderByIdAsc(Long recipeId, Long id);
}
