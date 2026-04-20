package model;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class Card implements Serializable {

	final public static int TAM_MIN_NOME = 3;
	final public static int TAM_MAX_NOME = 30;
	final public static int MIN_ATRIBUTO = 1;
	final public static int MAX_ATRIBUTO = 100;

	private String nome;
	private int velocidade;
	private int forca;
	private int inteligencia;
	private int habilidade;
	private byte[] imagem;

	public Card(String nome, int velocidade, int forca, int inteligencia, int habilidade, byte[] imagem)
			throws ModelException {
		super();
		this.setNome(nome);
		this.setVelocidade(velocidade);
		this.setForca(forca);
		this.setInteligencia(inteligencia);
		this.setHabilidade(habilidade);
		this.setImagem(imagem);
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) throws ModelException {
		Card.validarNome(nome);
		this.nome = nome;
	}

	public int getVelocidade() {
		return this.velocidade;
	}

	public void setVelocidade(int velocidade) throws ModelException {
		Card.validarAtributo(velocidade, "Velocidade");
		this.velocidade = velocidade;
	}

	public int getForca() {
		return this.forca;
	}

	public void setForca(int forca) throws ModelException {
		Card.validarAtributo(forca, "Força");
		this.forca = forca;
	}

	public int getInteligencia() {
		return this.inteligencia;
	}

	public void setInteligencia(int inteligencia) throws ModelException {
		Card.validarAtributo(inteligencia, "Inteligência");
		this.inteligencia = inteligencia;
	}

	public int getHabilidade() {
		return this.habilidade;
	}

	public void setHabilidade(int habilidade) throws ModelException {
		Card.validarAtributo(habilidade, "Habilidade");
		this.habilidade = habilidade;
	}

	public byte[] getImagem() {
		return this.imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public ImageIcon getImagemIcon() {
		if (this.imagem != null && this.imagem.length > 0)
			return new ImageIcon(this.imagem);
		return null;
	}

	public String getTemImagem() {
		return (this.imagem != null && this.imagem.length > 0) ? "Sim" : "Não";
	}

	@Override
	public String toString() {
		return this.nome + " (V:" + this.velocidade + " F:" + this.forca
				+ " I:" + this.inteligencia + " H:" + this.habilidade + ")";
	}

	public static void validarNome(String nome) throws ModelException {
		if (nome == null || nome.length() == 0)
			throw new ModelException("O nome da carta não pode ser nulo!");
		if (nome.length() < TAM_MIN_NOME || nome.length() > TAM_MAX_NOME)
			throw new ModelException(
					"O nome da carta deve ter entre " + TAM_MIN_NOME + " e " + TAM_MAX_NOME + " caracteres!");
	}

	public static void validarAtributo(int valor, String nomeAtributo) throws ModelException {
		if (valor < MIN_ATRIBUTO || valor > MAX_ATRIBUTO)
			throw new ModelException(nomeAtributo + " deve estar entre " + MIN_ATRIBUTO + " e " + MAX_ATRIBUTO + "!");
	}
}
