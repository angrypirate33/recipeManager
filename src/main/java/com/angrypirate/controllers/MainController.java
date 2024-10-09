package com.angrypirate.controllers;

import java.io.IOException;

import com.angrypirate.MainApp;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    private void switchToSecondary() throws IOException {
        MainApp.setRoot("secondary");
    }
}
