package organs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import organs.instruments.*;

public class OrganApp extends Application {

    @Override
    public void start(Stage stage) {

        Pane root = new Pane();

        // -------- INSTRUMENTS --------
        Heart heart = new Heart();
        Lungs lungs = new Lungs();
        Trachea trachea = new Trachea();
        Diaphragm diaphragm = new Diaphragm();
        Stomach stomach = new Stomach();
        Kidneys kidneys = new Kidneys();
        Intestines intestines = new Intestines();
        Appendix appendix = new Appendix();
        VocalCords vocalCords = new VocalCords();

        // -------- BODY IMAGE --------
        ImageView bodyView = new ImageView(new Image("file:resources/body.png"));
        bodyView.setFitWidth(800);
        bodyView.setPreserveRatio(true);
        root.getChildren().add(bodyView);

        // -------- LEFT SIDE --------
        Button vocalCordsBtn = debugBox("VOCAL CORDS (V)", 30, 60);
        vocalCordsBtn.setOnAction(e -> vocalCords.play());

        Button tracheaBtn = debugBox("TRACHEA (T)", 30, 180);
        tracheaBtn.setOnMousePressed(e -> trachea.start());
        tracheaBtn.setOnMouseReleased(e -> trachea.stop());

        Button heartBtn = debugBox("HEART (H)", 30, 330);
        heartBtn.setOnAction(e -> heart.play());

        Button diaphragmBtn = debugBox("DIAPHRAGM (D)", 30, 450);
        diaphragmBtn.setOnAction(e -> diaphragm.play());

        Button appendixBtn = debugBox("APPENDIX (A)", 30, 650);
        appendixBtn.setOnAction(e -> appendix.play());

        // -------- RIGHT SIDE --------
        Button lungsBtn = debugBox("LUNGS (L)", 590, 90);
        lungsBtn.setOnMousePressed(e -> lungs.start());
        lungsBtn.setOnMouseReleased(e -> lungs.stop());

        Button stomachBtn = debugBox("STOMACH (S)", 590, 250);
        stomachBtn.setOnMousePressed(e -> stomach.start());
        stomachBtn.setOnMouseReleased(e -> stomach.stop());

        Button kidneysBtn = debugBox("KIDNEYS (K)", 590, 440);
        kidneysBtn.setOnAction(e -> kidneys.play());

        Button intestinesBtn = debugBox("INTESTINES (drag mouse in box)", 590, 650);
        intestinesBtn.setOnMousePressed(e -> {
            double freq = 100 + (650 - e.getY()) * 2;
            intestines.start(freq);
        });
        intestinesBtn.setOnMouseDragged(e -> {
            double freq = 100 + (650 - e.getY()) * 2;
            intestines.updateFrequency(freq + Math.random() * 30);
        });
        intestinesBtn.setOnMouseReleased(e -> intestines.stop());

        root.getChildren().addAll(
                vocalCordsBtn,
                tracheaBtn,
                heartBtn,
                diaphragmBtn,
                appendixBtn,
                lungsBtn,
                stomachBtn,
                kidneysBtn,
                intestinesBtn);

        Scene scene = new Scene(root, 800, 1000);

        // -------- KEYBOARD CONTROLS --------
        scene.setOnKeyPressed(e -> {
            KeyCode k = e.getCode();
            if (k == KeyCode.H)
                heart.play();
            if (k == KeyCode.L)
                lungs.start();
            if (k == KeyCode.T)
                trachea.start();
            if (k == KeyCode.D)
                diaphragm.play();
            if (k == KeyCode.S)
                stomach.start();
            if (k == KeyCode.K)
                kidneys.play();
            if (k == KeyCode.I)
                intestines.start(220);
            if (k == KeyCode.A)
                appendix.play();
            if (k == KeyCode.V)
                vocalCords.play();
        });

        scene.setOnKeyReleased(e -> {
            KeyCode k = e.getCode();
            if (k == KeyCode.L)
                lungs.stop();
            if (k == KeyCode.T)
                trachea.stop();
            if (k == KeyCode.S)
                stomach.stop();
            if (k == KeyCode.I)
                intestines.stop();
        });

        stage.setTitle("The Organs");
        stage.setScene(scene);
        stage.show();
    }

    // -------- DEBUG BUTTON --------
    private Button debugBox(String label, double x, double y) {
        Button b = new Button(label);
        b.setLayoutX(x);
        b.setLayoutY(y);
        b.setPrefWidth(180);
        b.setPrefHeight(50);
        b.setStyle(
                "-fx-background-color: yellow;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2;");
        return b;
    }
}