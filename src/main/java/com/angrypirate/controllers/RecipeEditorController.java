package com.angrypirate.controllers;

import java.io.IOException;

import com.angrypirate.MainApp;
import javafx.fxml.FXML;

public class RecipeEditorController {

    @FXML
    private void switchToPrimary() throws IOException {
        MainApp.setRoot("primary");
    }
}