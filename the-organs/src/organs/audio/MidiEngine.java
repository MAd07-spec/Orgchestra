package organs.audio;

import javax.sound.midi.*;
import java.io.InputStream;

public class MidiEngine {

    private Synthesizer synth;
    private MidiChannel[] channels;

    public MidiEngine() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();

            InputStream sf2Stream = getClass().getResourceAsStream("/organs/resources/soundfont.sf2");
            if (sf2Stream == null) {
                System.err.println("SoundFont not found: /organs/resources/soundfont.sf2");
                return;
            }

            Soundbank bank = MidiSystem.getSoundbank(sf2Stream);
            synth.loadAllInstruments(bank);

            channels = synth.getChannels();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void programChange(int channel, int program) {
        if (channels == null) return;
        channels[channel].programChange(program);
    }

    public void noteOn(int channel, int note, int velocity) {
        if (channels == null) return;
        channels[channel].noteOn(note, velocity);
    }

    public void noteOff(int channel, int note) {
        if (channels == null) return;
        channels[channel].noteOff(note);
    }
}