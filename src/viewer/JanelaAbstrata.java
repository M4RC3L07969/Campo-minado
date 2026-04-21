package viewer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.CtrlAbstrato;

abstract public class JanelaAbstrata extends JFrame {

    final private CtrlAbstrato ctrl;

    public JanelaAbstrata(CtrlAbstrato c) {
        this.ctrl = c;
    }

    public CtrlAbstrato getCtrl() {
        return this.ctrl;
    }

    public void notificar(String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }

}
