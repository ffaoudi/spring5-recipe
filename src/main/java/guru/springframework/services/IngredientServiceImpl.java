package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, UnitOfMeasureRepository unitOfMeasureRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Transactional
    @Override
    public IngredientCommand findByRecipeIdAndId(long recipeId, long ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByRecipe_IdAndIdOrderByIdAsc(recipeId, ingredientId);

        if (optionalIngredient.isPresent()) {
            return ingredientToIngredientCommand.convert(optionalIngredient.get());
        }

        return ingredientToIngredientCommand.convert(new Ingredient());
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()){
            log.error("Recipe not found for id: {}", command.getRecipeId());
            return new IngredientCommand();
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        //Optional<Ingredient> ingredientOptional = ingredientRepository.findByRecipe_IdAndId(command.getRecipeId(), command.getId());
        if(ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository
                    .findById(command.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
        } else {
            recipe.addIngredient(ingredientCommandToIngredient.convert(command));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream()
        .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
        .findFirst()
        .get());
    }
}
