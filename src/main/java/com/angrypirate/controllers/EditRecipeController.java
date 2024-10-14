package com.angrypirate.controllers;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.Recipe;
import com.angrypirate.services.RecipeService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditRecipeController {

    @FXML
    private TextField recipeTitleField;

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
    private TableColumn<Ingredient, Double> quantityColumn;

    @FXML
    private TableColumn<Ingredient, String> unitColumn;

    @FXML
    private ListView<String> instructionsListView;

    @FXML
    private TextField instructionField;

    private ObservableList<Ingredient> recipeIngredients;

    private ObservableList<String> instructions;

    private RecipeService recipeService = new RecipeService();

    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        initializeData();
    }

    private void initializeData() {
        // Initialize ingredients and instructions
        recipeIngredients = FXCollections.observableArrayList(recipe.getIngredients());
        instructions = FXCollections.observableArrayList(recipe.getInstructions());

        // Set data to UI components
        recipeTitleField.setText(recipe.getTitle());

        recipeIngredientsTable.setItems(recipeIngredients);
        instructionsListView.setItems(instructions);

        // Initialize table columns
        recipeNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getQuantity()).asObject());
        unitColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit()));
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
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please add at least one ingredient.");
            return;
        }

        // Update the recipe object
        recipe.setTitle(title);
        recipe.setIngredients(new ArrayList<>(recipeIngredients));
        recipe.setInstructions(new ArrayList<>(instructions));

        // Update the recipe in the database
        recipeService.updateRecipe(recipe);

        // Close the window
        Stage stage = (Stage) recipeTitleField.getScene().getWindow();
        stage.close();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Recipe updated successfully!");
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        Stage stage = (Stage) recipeTitleField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.initOwner(recipeTitleField.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
