package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.repositories.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    //private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Transactional
    @Override
    public IngredientCommand findByRecipeIdAndId(long recipeId, long ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByRecipe_IdAndId(recipeId, ingredientId);

        if (optionalIngredient.isPresent()) {
            return ingredientToIngredientCommand.convert(optionalIngredient.get());
        }

        return ingredientToIngredientCommand.convert(new Ingredient());
    }
}
