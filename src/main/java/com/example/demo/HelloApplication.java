package com.example.demo;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        RegistrationAndLoginPage page = new RegistrationAndLoginPage();
        page.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}