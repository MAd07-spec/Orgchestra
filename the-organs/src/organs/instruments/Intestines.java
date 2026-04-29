package organs.instruments;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Intestines implements OrganInstrument {

    private static final float SR = 44100f;
    private final AtomicBoolean playing = new AtomicBoolean(false);
    private Thread audioThread;

    @Override
    public void play() {
        // not used (hold instrument)
    }

    public void start() {
        if (playing.get()) return;
        playing.set(true);

        audioThread = new Thread(this::runChaos);
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void stop() {
        playing.set(false);
    }

    private void runChaos() {
        try {
            AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];
            double phase = 0;
            double freq = 50 + Math.random() * 800;

            while (playing.get()) {
                for (int i = 0; i < buffer.length / 2; i++) {

                    // constantly mutate frequency + waveform
                    freq += (Math.random() * 2 - 1) * 20;
                    freq = Math.max(20, Math.min(2000, freq));

                    double wave =
                            Math.sin(phase) *
                            Math.signum(Math.sin(phase * 0.7));

                    double noise = (Math.random() * 2 - 1) * 0.6;

                    double sample = wave * 0.6 + noise;
                    short out = (short)(sample * 14000);

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