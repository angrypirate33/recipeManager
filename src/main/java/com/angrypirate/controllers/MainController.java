package com.angrypirate.controllers;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.Recipe;
import com.angrypirate.viewmodels.IngredientViewModel;
import com.angrypirate.services.IngredientService;
import com.angrypirate.services.RecipeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField unitField;
    @FXML
    private TableView<Ingredient> recipeIngredientsTable;
    @FXML
    private TableColumn<Ingredient, String> recipeNameColumn;
    @FXML
    private TableColumn<Ingredient, String> quantityColumn;
    @FXML
    private TableColumn<Ingredient, String> unitColumn;
    @FXML
    private ListView<String> instructionsListView;
    @FXML
    private TextField instructionField;
    @FXML
    private TextField recipeTitleField;

    private ObservableList<Ingredient> recipeIngredients;
    private ObservableList<String> instructions;
    private RecipeService recipeService = new RecipeService();

    @FXML
    public void initialize() {
        recipeIngredients = FXCollections.observableArrayList();
        instructions = FXCollections.observableArrayList();

        recipeIngredientsTable.setItems(recipeIngredients);
        instructionsListView.setItems(instructions);

        recipeNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject().asString());
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
    }

    @FXML
    private void handleAddIngredient() {
        String name = ingredientNameField.getText();
        String quantityText = quantityField.getText();
        String unit = unitField.getText();

        if (name == null || name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter an ingredient name.");
            return;
        }

        if (quantityText == null || quantityText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a quantity.");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number for quantity.");
            return;
        }

        if (unit == null || unit.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a unit.");
            return;
        }

        Ingredient ingredient = new Ingredient(name, quantity, unit);
        recipeIngredients.add(ingredient);

        // Clear input fields
        ingredientNameField.clear();
        quantityField.clear();
        unitField.clear();
    }

    @FXML
    private void handleAddInstruction() {
        String instruction = instructionField.getText();
        if (instruction != null && !instruction.isEmpty()) {
            instructions.add(instruction);
            instructionField.clear();
        } else {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter an instruction.");
        }
    }

    @FXML
    private void handleSaveRecipe() {
        String title = recipeTitleField.getText();
        if (title == null || title.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a recipe title.");
            return;
        }

        if (recipeIngredients.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Recipe Error", "Recipe must have at least one ingredient.");
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title);

        List<Ingredient> ingredients = new ArrayList<>(recipeIngredients);
        recipe.setIngredients(ingredients);

        // Save recipe
        recipeService.addRecipe(recipe);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Recipe saved successfully!");

        // Clear recipe data
        recipeTitleField.clear();
        recipeIngredients.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
