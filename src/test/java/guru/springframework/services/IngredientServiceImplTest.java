package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    //@Mock
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    IngredientRepository ingredientRepository;

    IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

        ingredientService = new IngredientServiceImpl(ingredientRepository, ingredientToIngredientCommand);
    }

    @Test
    public void findByRecipeIdAndIdTest() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setRecipe(recipe);

        recipe.addIngredient(ingredient);

        Optional<Ingredient> optionalIngredient = Optional.of(ingredient);

        //when
        when(ingredientRepository.findByRecipe_IdAndId(anyLong(), anyLong())).thenReturn(optionalIngredient);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndId(1L, 1L);

        assertEquals(Long.valueOf(1L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        verify(ingredientRepository, times(1)).findByRecipe_IdAndId(anyLong(), anyLong());
    }
}