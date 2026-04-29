package organs.instruments;

import organs.audio.SoundEngine;

public class Heart implements OrganInstrument {

    @Override
    public void play() {
        new Thread(() -> {
            SoundEngine.play808(55, 200);

            try { Thread.sleep(120); } catch (InterruptedException ignored) {}

            SoundEngine.play808(85, 120);
        }).start();
    }
}