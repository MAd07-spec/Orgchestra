package organs.instruments;

import organs.audio.MidiEngine;

public class VocalCords implements OrganInstrument {

    private final MidiEngine midi = new MidiEngine();
    private final int channel = 4;

    // Two guitar-string notes (low + high)
    private final int[] notes = {52, 59}; // E3 / B3-ish
    private int index = 0;

    public VocalCords() {
        // Electric Guitar (Clean) – GM program 27
        midi.programChange(channel, 27);
    }

    @Override
    public void play() {
        int note = notes[index];
        index = (index + 1) % notes.length;

        midi.noteOn(channel, note, 110);

        // Short pluck decay
        new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            midi.noteOff(channel, note);
        }).start();
    }
}