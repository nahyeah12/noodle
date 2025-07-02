package com.ppi.utility.importer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main application class that integrates Spring Boot with JavaFX.
 * This class serves as the entry point for the desktop application.
 */
@SpringBootApplication
public class MainApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    /**
     * Initializes the Spring Boot application context.
     * This method is called by JavaFX before the start() method.
     * It allows Spring to manage dependencies for JavaFX components.
     */
    @Override
    public void init() throws Exception {
        // Initialize Spring Boot application context
        springContext = SpringApplication.run(MainApplication.class);

        // Load the FXML file using Spring's FXMLLoader to allow dependency injection in the controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean); // Make Spring manage the controller's dependencies
        rootNode = fxmlLoader.load();
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Create a new scene with the loaded FXML root node
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("PPI Utility Importer");
        stage.setScene(scene);
        stage.setResizable(false); // Prevent resizing for simplicity
        stage.show();
    }

    /**
     * This method is called when the application should stop.
     * It's used to gracefully shut down the Spring context.
     */
    @Override
    public void stop() throws Exception {
        springContext.close(); // Close the Spring application context
        Platform.exit(); // Exit the JavaFX platform
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application
    }
}
