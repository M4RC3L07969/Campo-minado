package controller;

abstract public class CtrlAbstrato {
    //
    // ATRIBUTO
    //

    final private CtrlAbstrato ctrlPai;

    //
    // MÉTODOS
    //
    public CtrlAbstrato(CtrlAbstrato pai) {
        this.ctrlPai = pai;
    }

    public CtrlAbstrato getCtrlPai() {
        return this.ctrlPai;
    }

    abstract public void encerrar();

    abstract public Object getBemTangivel();
}
