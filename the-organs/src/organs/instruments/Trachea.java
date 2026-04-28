package organs.instruments;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Trachea implements OrganInstrument {

    private static final float SR = 44100f;
    private final AtomicBoolean playing = new AtomicBoolean(false);
    private Thread audioThread;

    @Override
    public void play() {
        // not used (press/hold instrument)
    }

    public void start() {
        if (playing.get()) return;
        playing.set(true);

        double freq = 440; // fixed flute tone (A4-ish)

        audioThread = new Thread(() -> runFlute(freq));
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void stop() {
        playing.set(false);
    }

    private void runFlute(double freq) {
        try {
            AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];
            double phase = 0;

            while (playing.get()) {
                for (int i = 0; i < buffer.length / 2; i++) {

                    // soft, breathy sine with slight air noise
                    double sine = Math.sin(phase);
                    double air  = (Math.random() * 2 - 1) * 0.02;

                    double sample = sine * 0.8 + air;
                    short out = (short)(sample * 15000);

                    buffer[i * 2]     = (byte)(out & 0xff);
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
}