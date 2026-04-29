package organs.instruments;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Appendix implements OrganInstrument {

    @Override
    public void play() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appendix");
            alert.setHeaderText("No function.");
            alert.setContentText("Cmon. You should know this.");
            alert.showAndWait();
        });
    }
}