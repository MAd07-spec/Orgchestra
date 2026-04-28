package organs.instruments;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Lungs {

    private static final float SR = 44100f;
    private final AtomicBoolean playing = new AtomicBoolean(false);
    private Thread audioThread;

    public void start() {
        if (playing.get()) return;
        playing.set(true);

        // Chanter pitch (Scottish-ish range)
        double chanterFreq = 196 + Math.random() * 60; // G3-ish

        audioThread = new Thread(() -> runBagpipes(chanterFreq));
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void stop() {
        playing.set(false);
    }

    private void runBagpipes(double chanterFreq) {
        try {
            AudioFormat format =
                new AudioFormat(SR, 16, 1, true, false);
            SourceDataLine line =
                AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];

            double chanterPhase = 0;
            double dronePhase = 0;

            // One drone an octave below (classic bagpipe feel)
            double droneFreq = chanterFreq / 2.0;

            double drift = 0;

            while (playing.get()) {
                for (int i = 0; i < buffer.length / 2; i++) {

                    // Very slow pitch drift (NOT vibrato)
                    drift += (Math.random() * 2 - 1) * 0.00002;
                    drift *= 0.999;

                    // Reed‑like wave: clipped sine
                    double chanter =
                        Math.tanh(Math.sin(chanterPhase) * 3.5);

                    double drone =
                        Math.tanh(Math.sin(dronePhase) * 2.5);

                    // Subtle breath hiss INSIDE the tone
                    double air = (Math.random() * 2 - 1) * 0.04;

                    double sample =
                        chanter * 0.6 +
                        drone * 0.3 +
                        air;

                    short out = (short)(sample * 15000);
                    buffer[i*2]     = (byte)(out & 0xff);
                    buffer[i*2 + 1] = (byte)((out >> 8) & 0xff);

                    chanterPhase +=
                        2 * Math.PI * chanterFreq * (1 + drift) / SR;
                    dronePhase +=
                        2 * Math.PI * droneFreq / SR;
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