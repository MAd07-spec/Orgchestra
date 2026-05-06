package organs.instruments;

import javax.swing.JOptionPane;

public class Appendix implements OrganInstrument {

    @Override
        public void play() {
            JOptionPane.showMessageDialog(null, "Cmon. You should know this....", "Appendix",  JOptionPane.INFORMATION_MESSAGE);
        }
    
}