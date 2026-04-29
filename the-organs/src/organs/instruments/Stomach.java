package organs.instruments;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Stomach implements OrganInstrument {

    private static final float SR = 44100f;
    private final AtomicBoolean playing = new AtomicBoolean(false);
    private Thread audioThread;
    private int uses = 0;

    @Override
    public void play() {
        // not used
    }

    public void start() {
        uses++;

        if (uses > 2) {
            showSickPopup();
            return;
        }

        if (playing.get()) return;
        playing.set(true);

        audioThread = new Thread(this::runDigeridoo);
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void stop() {
        playing.set(false);
    }

    private void runDigeridoo() {
        try {
            AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];
            double phase = 0;
            double freq = 70 + Math.random() * 20;

            while (playing.get()) {
                for (int i = 0; i < buffer.length / 2; i++) {
                    double growl = Math.tanh(Math.sin(phase) * 4);
                    double air = (Math.random() * 2 - 1) * 0.15;
                    double sample = growl * 0.8 + air;

                    short out = (short)(sample * 16000);
                    buffer[i * 2] = (byte)(out & 0xff);
                    buffer[i * 2 + 1] = (byte)((out >> 8) & 0xff);

                    phase += 2 * Math.PI * freq / SR;
                }
                line.write(buffer, 0, buffer.length);
            }

            line.stop();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- POPUP + COUNTDOWN ----------
    private void showSickPopup() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Uh oh");
            alert.setHeaderText("i told you id get sick bwahhhhhh!");

            Label countdownLabel = new Label("Closing in 3...");
            alert.getDialogPane().setContent(countdownLabel);
            alert.show();

            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> countdownLabel.setText("Closing in 2...")),
                new KeyFrame(Duration.seconds(2), e -> countdownLabel.setText("Closing in 1...")),
                new KeyFrame(Duration.seconds(3), e -> {
                    alert.close();
                    Platform.exit();
                })
            );

            timeline.play();
        });
    }
}