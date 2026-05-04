package viewer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.CtrlAbstrato;
import util.ThemeManager;

abstract public class JanelaAbstrata extends JFrame {

    final private CtrlAbstrato ctrl;

    public JanelaAbstrata(CtrlAbstrato c) {
        this.ctrl = c;
        ThemeManager.getInstance().registerWindow(this);
    }

    public CtrlAbstrato getCtrl() {
        return this.ctrl;
    }

    public void notificar(String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }

    @Override
    public void dispose() {
        ThemeManager.getInstance().unregisterWindow(this);
        super.dispose();
    }

}
