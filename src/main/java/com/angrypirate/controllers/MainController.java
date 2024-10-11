package com.angrypirate.controllers;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.Recipe;
import com.angrypirate.viewmodels.IngredientViewModel;
import com.angrypirate.services.FoodDataApiService;
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
    private TextField searchField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField unitField;

    @FXML
    private TextField recipeTitleField;
    @FXML
    private TableView<IngredientViewModel> searchResultsTable;

    @FXML
    private TableColumn<IngredientViewModel, String> nameColumn;

    @FXML
    private TableColumn<IngredientViewModel, String> fdcIdColumn;

    @FXML
    private TableView<IngredientViewModel> recipeIngredientsTable;

    @FXML
    private TableColumn<IngredientViewModel, String> recipeNameColumn;

    @FXML
    private TableColumn<IngredientViewModel, String> quantityColumn;

    @FXML
    private TableColumn<IngredientViewModel, String> unitColumn;

    private ObservableList<IngredientViewModel> searchResults = FXCollections.observableArrayList();
    private ObservableList<IngredientViewModel> recipeIngredients = FXCollections.observableArrayList();

    @FXML
    private ListView<String> instructionsListView;

    @FXML
    private TextField instructionField;

    private ObservableList<String> instructionsList = FXCollections.observableArrayList();

    private FoodDataApiService apiService = new FoodDataApiService();
    private IngredientService ingredientService = new IngredientService();
    private RecipeService recipeService = new RecipeService();

    @FXML
    public void initialize() {
        // Set the items for the tables
        searchResultsTable.setItems(searchResults);
        recipeIngredientsTable.setItems(recipeIngredients);
        instructionsListView.setItems(instructionsList);
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search Error", "Please enter a search query.");
            return;
        }

        try {
            List<Ingredient> results = apiService.searchIngredients(query);
            List<IngredientViewModel> viewModels = new ArrayList<>();
            for (Ingredient ingredient : results) {
                viewModels.add(new IngredientViewModel(ingredient));
            }
            searchResults.setAll(viewModels);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "API Error", "Failed to retrieve search results.");
        }
    }

    @FXML
    private void handleAddIngredient() {
        IngredientViewModel selectedViewModel = searchResultsTable.getSelectionModel().getSelectedItem();
        if (selectedViewModel == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an ingredient from the search results.");
            return;
        }

        String quantityText = quantityField.getText();
        String unit = unitField.getText();

        if (quantityText == null || quantityText.isEmpty() || unit == null || unit.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter quantity and unit.");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Quantity must be a valid number.");
            return;
        }

        // Clone the selected ingredient and set quantity and unit
        Ingredient ingredient = new Ingredient();
        ingredient.setFdcId(selectedViewModel.getFdcId());
        ingredient.setName(selectedViewModel.getName());
        ingredient.setQuantity(quantity);
        ingredient.setUnit(unit);

        // Fetch and set nutritional info
        try {
            Ingredient fullIngredient = apiService.getIngredient(ingredient.getFdcId(), quantity, unit);
            ingredient.setNutritionalInfo(fullIngredient.getNutritionalInfo());
            ingredient.setLastUpdated(fullIngredient.getLastUpdated());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "API Error", "Failed to retrieve ingredient details.");
            return;
        }

        IngredientViewModel ingredientViewModel = new IngredientViewModel(ingredient);
        recipeIngredients.add(ingredientViewModel);

        // Clear input fields
        quantityField.clear();
        unitField.clear();
    }

    @FXML
    private void handleAddInstruction() {
        String instruction = instructionField.getText();
        if (instruction == null || instruction.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter an instruction.");
            return;
        }
        instructionsList.add(instruction);
        instructionField.clear();
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

        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientViewModel viewModel : recipeIngredients) {
            ingredients.add(viewModel.getIngredient());
        }
        recipe.setIngredients(ingredients);

        // Calculate nutritional info

        recipe.setNutritionalInfo(recipeService.calculateRecipeNutrition(recipe));

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
