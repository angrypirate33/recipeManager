package com.angrypirate;

import java.io.IOException;
import javafx.fxml.FXML;

public class RecipeEditorController {

    @FXML
    private void switchToPrimary() throws IOException {
        MainApp.setRoot("primary");
    }
}