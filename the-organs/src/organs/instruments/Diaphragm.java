package organs.instruments;

import organs.audio.MidiEngine;

public class Diaphragm implements OrganInstrument {

    private final MidiEngine midi = new MidiEngine();

    @Override
    public void play() {
        // GM timpani-like note (low)
        int note = 47;      // Low timpani range
        int velocity = 110;

        midi.noteOn(9, note, velocity);

        // short hit
        new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            midi.noteOff(9, note);
        }).start();
    }
}