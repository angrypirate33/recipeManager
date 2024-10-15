package com.angrypirate.controllers;

import com.angrypirate.models.Recipe;
import com.angrypirate.services.RecipeService;
import com.angrypirate.viewmodels.RecipeViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<RecipeViewModel> recipesTable;

    @FXML
    private TableColumn<RecipeViewModel, String> titleColumn;

    @FXML
    private Button viewRecipeButton;
    @FXML
    private Button editRecipeButton;
    @FXML
    private Button deleteRecipeButton;
    @FXML
    private Button searchRecipesButton;

    // Other columns as needed

    private ObservableList<RecipeViewModel> recipesList = FXCollections.observableArrayList();

    private RecipeService recipeService = new RecipeService();

    @FXML
    public void initialize() {
        // Initialize the recipes table
        recipesTable.setItems(recipesList);
        loadRecipes();
        setupButtonKeyPress(searchRecipesButton);
        setupButtonKeyPress(viewRecipeButton);
        setupButtonKeyPress(editRecipeButton);
        setupButtonKeyPress(deleteRecipeButton);
    }

    private void setupButtonKeyPress(Button button) {
        button.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                button.fire();
                event.consume();
            }
        });
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

        // Load the RecipeView.fxml file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/angrypirate/views/RecipeView.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected recipe
            RecipeViewController controller = loader.getController();
            controller.setRecipe(selectedRecipe.getRecipe());

            // Show the recipe in a new window
            Stage stage = new Stage();
            stage.setTitle("View Recipe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load recipe view.");
        }
    }

    @FXML
    private void handleEditRecipe() {
        RecipeViewModel selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recipe to edit.");
            return;
        }
        // Load the EditRecipeView.fxml file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/angrypirate/views/EditRecipeView.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected recipe
            EditRecipeController controller = loader.getController();
            controller.setRecipe(selectedRecipe.getRecipe());

            // Show the edit view in a new window
            Stage stage = new Stage();
            stage.setTitle("Edit Recipe");
            stage.setScene(new Scene(root));
            stage.show();

            // Refresh the recipes list after editing
            stage.setOnHiding(event -> loadRecipes());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load edit recipe view.");
        }
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
