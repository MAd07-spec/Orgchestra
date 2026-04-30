package organs.instruments;

import organs.audio.MidiEngine;

public class Lungs implements OrganInstrument {

    private final MidiEngine midi = new MidiEngine();
    private final int channel = 1;
    private final int note = 55; // low drone

    public Lungs() {
        midi.programChange(channel, 68); // Reed / oboe-like
    }

    @Override
    public void play() {}

    public void start() {
        midi.noteOn(channel, note, 110);
    }

    public void stop() {
        midi.noteOff(channel, note);
    }
}