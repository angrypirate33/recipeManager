package com.angrypirate.controllers;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    @FXML
    private Button closeRecipeButton;

    private Recipe recipe;

    public void initialize() {
        setupButtonKeyPress(closeRecipeButton);
    }

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

    private void setupButtonKeyPress(Button button) {
        button.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                button.fire();
                event.consume();
            }
        });
    }

    @FXML
    private void handleClose() {
        // Close the window
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
