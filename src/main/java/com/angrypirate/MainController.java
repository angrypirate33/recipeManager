package com.angrypirate;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    private void switchToSecondary() throws IOException {
        MainApp.setRoot("secondary");
    }
}
