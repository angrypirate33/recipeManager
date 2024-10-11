package com.angrypirate.controllers;

import com.angrypirate.models.Recipe;
import com.angrypirate.services.RecipeService;
import com.angrypirate.viewmodels.RecipeViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class RecipesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<RecipeViewModel> recipesTable;

    @FXML
    private TableColumn<RecipeViewModel, String> titleColumn;

    // Other columns as needed

    private ObservableList<RecipeViewModel> recipesList = FXCollections.observableArrayList();

    private RecipeService recipeService = new RecipeService();

    @FXML
    public void initialize() {
        // Initialize the recipes table
        recipesTable.setItems(recipesList);
        loadRecipes();
    }

    private void loadRecipes() {
        // Fetch recipes from the database
        List<Recipe> recipes = recipeService.getAllRecipes();
        recipesList.clear();
        for (Recipe recipe : recipes) {
            recipesList.add(new RecipeViewModel(recipe));
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            loadRecipes(); // Load all recipes if search query is empty
            return;
        }
        // Implement search logic
        List<Recipe> recipes = recipeService.searchRecipes(query);
        recipesList.clear();
        for (Recipe recipe : recipes) {
            recipesList.add(new RecipeViewModel(recipe));
        }
    }

    @FXML
    private void handleViewRecipe() {
        RecipeViewModel selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recipe to view.");
            return;
        }
        // Implement logic to display recipe details
    }

    @FXML
    private void handleEditRecipe() {
        // Implement logic to edit the selected recipe
    }

    @FXML
    private void handleDeleteRecipe() {
        RecipeViewModel selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recipe to delete.");
            return;
        }
        recipeService.deleteRecipe(selectedRecipe.getRecipe().getId());
        recipesList.remove(selectedRecipe);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
