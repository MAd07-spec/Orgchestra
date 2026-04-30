package organs.instruments;

import organs.audio.MidiEngine;

public class Trachea implements OrganInstrument {

    private final MidiEngine midi = new MidiEngine();
    private final int channel = 0;
    private final int note = 69; // A4

    public Trachea() {
        midi.programChange(channel, 73); // Flute
    }

    @Override
    public void play() {}

    public void start() {
        midi.noteOn(channel, note, 100);
    }

    public void stop() {
        midi.noteOff(channel, note);
    }
}