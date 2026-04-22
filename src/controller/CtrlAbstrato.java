package controller;

abstract public class CtrlAbstrato {

    final private CtrlAbstrato ctrlPai;

    public CtrlAbstrato(CtrlAbstrato pai) {
        this.ctrlPai = pai;
    }

    public CtrlAbstrato getCtrlPai() {
        return this.ctrlPai;
    }

    abstract public void encerrar();

    abstract public Object getBemTangivel();
}
