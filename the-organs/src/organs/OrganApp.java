package organs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import organs.instruments.Heart;
import organs.instruments.Lungs;
import organs.instruments.Trachea;


public class OrganApp extends Application {

    @Override
    public void start(Stage stage) {

        System.out.println(">>> SIDE ORGAN DEBUG MODE <<<");

        Pane root = new Pane();

        Heart heartInstrument = new Heart();

        // ---------------- BODY IMAGE ----------------
        Image bodyImage = new Image("file:resources/body.png");
        ImageView bodyView = new ImageView(bodyImage);
        bodyView.setFitWidth(800);
        bodyView.setPreserveRatio(true);
        root.getChildren().add(bodyView);

        // ---------------- LEFT SIDE ORGANS ----------------
        Button vocalCords = debugBox("VOCAL CORDS", 30, 60);
        
        Trachea trachea = new Trachea();
        Button tracheaBtn = debugBox("TRACHEA", 30, 180);

        tracheaBtn.setOnMousePressed(e -> trachea.start());
        tracheaBtn.setOnMouseReleased(e -> trachea.stop());
        
        Button heart      = debugBox("HEART",       30, 330);
        heart.setOnAction(e -> heartInstrument.play());
        Button diaphragm  = debugBox("DIAPHRAGM",   30, 450);
        Button appendix   = debugBox("APPENDIX",    30, 650);

        // ---------------- RIGHT SIDE ORGANS ----------------
        Lungs lungsInstrument = new Lungs();
        Button lungs = debugBox("LUNGS", 590, 90);
        lungs.setOnMousePressed(e -> lungsInstrument.start());
        lungs.setOnMouseReleased(e -> lungsInstrument.stop());
        
        Button stomach    = debugBox("STOMACH",     590, 250);
        Button kidneys    = debugBox("KIDNEYS",     590, 440);
        Button intestines = debugBox("INTESTINES",  590, 650);

        root.getChildren().addAll(
            vocalCords,
            tracheaBtn,
            heart,
            diaphragm,
            appendix,
            lungs,
            stomach,
            kidneys,
            intestines
        );


        Scene scene = new Scene(root, 800, 1000);
        stage.setTitle("The Organs – Side Alignment Debug");
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- DEBUG BOX HELPER ----------------
    private Button debugBox(String label, double x, double y) {
        Button b = new Button(label);
        b.setLayoutX(x);
        b.setLayoutY(y);
        b.setPrefWidth(160);
        b.setPrefHeight(50);
        b.setStyle(
            "-fx-background-color: yellow;" +
            "-fx-text-fill: black;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;"
        );
        b.setOnAction(e ->
            System.out.println(label + " CLICKED")
        );
        return b;
    }
}