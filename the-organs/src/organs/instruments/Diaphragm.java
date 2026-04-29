package organs.instruments;

import javax.sound.sampled.*;

public class Diaphragm implements OrganInstrument {

    private static final float SR = 44100f;

    @Override
    public void play() {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(SR, 16, 1, true, false);
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();

                int samples = (int)(SR * 0.35);
                byte[] buffer = new byte[samples * 2];

                double freq = 90 + Math.random() * 30;

                for (int i = 0; i < samples; i++) {
                    double t = i / SR;
                    double env = Math.exp(-t * 10);
                    double tone = Math.sin(2 * Math.PI * freq * t);

                    short out = (short)(tone * env * 16000);
                    buffer[i * 2] = (byte)(out & 0xff);
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
}