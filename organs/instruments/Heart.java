package organs.instruments;

import javax.sound.sampled.*;

public class Heart implements OrganInstrument {

    private static final float SR = 44100f;

    @Override
    public void play() {
        new Thread(this::play808).start();
    }

    private void play808() {
        try {
            AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            int samples = (int)(SR * 0.6);
            byte[] buffer = new byte[samples * 2];

            double phase = 0;

            for (int i = 0; i < samples; i++) {
                double t = i / SR;

                // Pitch drop: 120 Hz → 45 Hz
                double freq = 120 * Math.exp(-t * 8) + 45;

                // Amplitude envelope
                double env = Math.exp(-t * 6);

                double sample = Math.sin(phase) * env;
                short out = (short)(sample * 28000);

                buffer[i * 2]     = (byte)(out & 0xff);
                buffer[i * 2 + 1] = (byte)((out >> 8) & 0xff);

                phase += 2 * Math.PI * freq / SR;
            }

            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}