package organs.instruments;

import organs.audio.SoundEngine;

public class Heart implements OrganInstrument {

    @Override
    public void play() {
        // lub (deep)
        SoundEngine.play808(55, 200);

        try { Thread.sleep(120); } catch (InterruptedException ignored) {}

        // dub (slightly higher + shorter)
        SoundEngine.play808(67, 130);
    }
}
