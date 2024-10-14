package com.angrypirate.controllers;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class RecipeViewController {

    @FXML
    private Label titleLabel;

    @FXML
    private ListView<String> ingredientsListView;

    @FXML
    private TextArea instructionsTextArea;

    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        displayRecipeDetails();
    }

    private void displayRecipeDetails() {
        if (recipe != null) {
            titleLabel.setText(recipe.getTitle());

            // Display ingredients
            ingredientsListView.getItems().clear();
            for (Ingredient ingredient : recipe.getIngredients()) {
                String ingredientText = String.format("%s %s %s",
                        ingredient.getQuantity(),
                        ingredient.getUnit(),
                        ingredient.getName());
                ingredientsListView.getItems().add(ingredientText);
            }

            // Display instructions
            instructionsTextArea.clear();
            String instructions = recipe.getInstructions().stream()
                    .map(step -> "- " + step)
                    .collect(Collectors.joining("\n"));
            instructionsTextArea.setText(instructions);

        }
    }

    private String formatNutritionalInfo(com.angrypirate.models.NutritionalInfo info) {
        return String.format("Calories: %.2f kcal\nFat: %.2f g\nProtein: %.2f g\nCarbohydrates: %.2f g\nFiber: %.2f g\nSugar: %.2f g\nSodium: %.2f mg",
                info.getCalories(),
                info.getFat(),
                info.getProtein(),
                info.getCarbohydrates(),
                info.getFiber(),
                info.getSugar(),
                info.getSodium());
    }

    @FXML
    private void handleClose() {
        // Close the window
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
