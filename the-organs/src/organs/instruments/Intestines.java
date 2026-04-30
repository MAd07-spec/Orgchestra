package organs.instruments;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Intestines implements OrganInstrument {

    private static final float SR = 44100f;

    private final AtomicBoolean playing = new AtomicBoolean(false);
    private Thread audioThread;

    private volatile double frequency = 200;

    @Override
    public void play() {
        // not used
    }

    public void start(double startFreq) {
        if (playing.get()) return;
        playing.set(true);
        frequency = startFreq;

        audioThread = new Thread(this::runSynth);
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void updateFrequency(double freq) {
        this.frequency = freq;
    }

    public void stop() {
        playing.set(false);
    }

    private void runSynth() {
        try {
            AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];
            double phase = 0;

            while (playing.get()) {
                for (int i = 0; i < buffer.length / 2; i++) {

                    // chaotic waveform
                    double wave =
                        Math.sin(phase) *
                        Math.signum(Math.sin(phase * 0.3));

                    double noise =
                        (Math.random() * 2 - 1) * 0.4;

                    double sample = wave * 0.7 + noise * 0.3;
                    short out = (short)(sample * 14000);

                    buffer[i * 2]     = (byte)(out & 0xff);
                    buffer[i * 2 + 1] = (byte)((out >> 8) & 0xff);

                    phase += 2 * Math.PI * frequency / SR;
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