package organs.instruments;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;


public class Kidneys implements OrganInstrument {

    private static final float SR = 44100f;

    private int uses = 0;
    private boolean sold = false;
    private int soldClickCounter = 0;

    @Override
    public void play() {

        if (!sold) {
            uses++;
            playBongo();

            if (uses == 5) {
                showSellPopup();
            }
        } else {
            soldClickCounter++;
            if (soldClickCounter % 2 == 0) {
                playBongo();
            }
        }
    }

    private void playBongo() {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();

                int samples = (int)(SR * 0.25);
                byte[] buffer = new byte[samples * 2];

                double freq = 150 + Math.random() * 100;

                for (int i = 0; i < samples; i++) {
                    double t = i / SR;
                    double env = Math.exp(-t * 8);
                    double tone = Math.sin(2 * Math.PI * freq * t);

                    short out = (short)(tone * env * 22000);
                    buffer[i * 2]     = (byte)(out & 0xff);
                    buffer[i * 2 + 1] = (byte)((out >> 8) & 0xff);
                }

                line.write(buffer, 0, buffer.length);
                line.drain();
                line.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showSellPopup() {
        Object[] options = {"Yes", "Yes"};
        JOptionPane.showOptionDialog(
            null,
            "Warning:\nAfter selling, this kidney will only play on every 2nd click.",
            "Opportunity - Do you want to sell it?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        sold = true;
    }
}