package organs.audio;

import javax.sound.sampled.*;

public class SoundEngine {

    private static final float SAMPLE_RATE = 44100f;

    private static final AudioFormat FORMAT =
            new AudioFormat(SAMPLE_RATE, 16, 1, true, false);

    // 808-style bass thump
    public static void play808(double frequency, int durationMs) {
        try {
            SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT);
            line.open(FORMAT);
            line.start();

            int samples = (int) (SAMPLE_RATE * durationMs / 1000);
            byte[] buffer = new byte[samples * 2];

            for (int i = 0; i < samples; i++) {
                double time = i / SAMPLE_RATE;
                double envelope = Math.exp(-time * 8);
                double angle = 2.0 * Math.PI * frequency * time;

                short value = (short) (Math.sin(angle) * envelope * 32767);
                buffer[i * 2] = (byte) (value & 0xff);
                buffer[i * 2 + 1] = (byte) ((value >> 8) & 0xff);
            }

            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sustained tone (lungs, trachea)
    public static void playSustain(double frequency, int durationMs) {
        try {
            SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT);
            line.open(FORMAT);
            line.start();

            int samples = (int) (SAMPLE_RATE * durationMs / 1000);
            byte[] buffer = new byte[samples * 2];

            for (int i = 0; i < samples; i++) {
                double time = i / SAMPLE_RATE;
                double angle = 2.0 * Math.PI * frequency * time;

                short value = (short) (Math.sin(angle) * 16000);
                buffer[i * 2] = (byte) (value & 0xff);
                buffer[i * 2 + 1] = (byte) ((value >> 8) & 0xff);
            }

            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}