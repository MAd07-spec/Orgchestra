package organs.instruments;

import organs.audio.MidiEngine;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
    JOptionPane.showMessageDialog(
        null,
        "You've used the stomach 3 times in 10 seconds.\nSlow down or it will get sick.",
        "Uh oh - Something feels wrong...",
        JOptionPane.WARNING_MESSAGE
    );
}

private void showSickPopup() {
    new Thread(() -> {
        JOptionPane optionPane = new JOptionPane(
            "Closing in 3...",
            JOptionPane.ERROR_MESSAGE
        );
        JDialog dialog = optionPane.createDialog("Too late - i told you id get sick blwahhhhhh!");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

        for (int i = 3; i > 0; i--) {
            try {
                final int count = i;
                SwingUtilities.invokeLater(() -> optionPane.setMessage("Closing in " + count + "..."));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        dialog.dispose();
        System.exit(0);
    }).start();
}
}