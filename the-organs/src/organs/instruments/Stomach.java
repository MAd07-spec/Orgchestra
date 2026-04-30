package organs.instruments;

import organs.audio.MidiEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Deque;

public class Stomach implements OrganInstrument {

    private final MidiEngine midi = new MidiEngine();

    private final int channel = 3;
    private final int note = 38; // low drone (didgeridoo feel)

    private boolean playing = false;

    // ---- timing logic ----
    private final Deque<Long> pressTimes = new ArrayDeque<>();
    private static final long WINDOW_MS = 10_000;

    public Stomach() {
        midi.programChange(channel, 68); // reed / drone-like
    }

    @Override
    public void play() {
        // not used (hold instrument)
    }

    public void start() {
        long now = System.currentTimeMillis();

        // remove old presses outside 10s window
        while (!pressTimes.isEmpty() && now - pressTimes.peekFirst() > WINDOW_MS) {
            pressTimes.pollFirst();
        }

        pressTimes.addLast(now);

        int count = pressTimes.size();

        if (count == 3) {
            showWarningPopup();
        }

        if (count >= 5) {
            showSickPopup();
            return;
        }

        if (!playing) {
            playing = true;
            midi.noteOn(channel, note, 100);
        }
    }

    public void stop() {
        if (!playing) return;
        playing = false;
        midi.noteOff(channel, note);
    }

    // ---- POPUPS ----

    private void showWarningPopup() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uh oh");
            alert.setHeaderText("Something feels wrong...");
            alert.setContentText(
                "You've used the stomach 3 times in 10 seconds.\n" +
                "Slow down or it will get sick."
            );
            alert.show();
        });
    }

    private void showSickPopup() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Too late");
            alert.setHeaderText("i told you id get sick bwahhhhhh!");

            Label countdown = new Label("Closing in 3...");
            alert.getDialogPane().setContent(countdown);
            alert.show();

            Timeline t = new Timeline(
                new KeyFrame(Duration.seconds(1),
                    e -> countdown.setText("Closing in 2...")),
                new KeyFrame(Duration.seconds(2),
                    e -> countdown.setText("Closing in 1...")),
                new KeyFrame(Duration.seconds(3),
                    e -> {
                        alert.close();
                        Platform.exit();
                    })
            );
            t.play();
        });
    }
}