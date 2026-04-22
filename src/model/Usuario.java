package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario implements Serializable {

	final public static int TAM_MIN_NOME = 3;
	final public static int TAM_MAX_NOME = 30;
	final public static int TAM_MIN_SENHA = 4;
	final public static int TAM_MAX_SENHA = 20;
	final public static int TAM_MIN_LOGIN = 5;
	final public static int TAM_MAX_LOGIN = 50;

	private int id;
	private String nome;
	private String login;
	private String senhaHash;

	private int totalPartidas;
	private int vitorias;
	private int derrotas;

	private int melhorTempoFacil;
	private int melhorTempoMedio;
	private int melhorTempoDificil;

	public Usuario() {
		this.totalPartidas = 0;
		this.vitorias = 0;
		this.derrotas = 0;
		this.melhorTempoFacil = 0;
		this.melhorTempoMedio = 0;
		this.melhorTempoDificil = 0;
	}

	public Usuario(String nome, String login, String senha) throws ModelException {
		this.setNome(nome);
		this.setLogin(login);
		this.setSenha(senha);
		this.totalPartidas = 0;
		this.vitorias = 0;
		this.derrotas = 0;
		this.melhorTempoFacil = 0;
		this.melhorTempoMedio = 0;
		this.melhorTempoDificil = 0;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) throws ModelException {
		Usuario.validarNome(nome);
		this.nome = nome;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) throws ModelException {
		Usuario.validarLogin(login);
		this.login = login;
	}

	public String getSenhaHash() {
		return this.senhaHash;
	}

	public void setSenha(String senha) throws ModelException {
		Usuario.validarSenha(senha);
		this.senhaHash = Usuario.hashSHA256(senha);
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	public int getTotalPartidas() {
		return this.totalPartidas;
	}

	public void setTotalPartidas(int totalPartidas) {
		this.totalPartidas = totalPartidas;
	}

	public int getVitorias() {
		return this.vitorias;
	}

	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}

	public int getDerrotas() {
		return this.derrotas;
	}

	public void setDerrotas(int derrotas) {
		this.derrotas = derrotas;
	}

	public int getMelhorTempoFacil() {
		return this.melhorTempoFacil;
	}

	public void setMelhorTempoFacil(int melhorTempoFacil) {
		this.melhorTempoFacil = melhorTempoFacil;
	}

	public int getMelhorTempoMedio() {
		return this.melhorTempoMedio;
	}

	public void setMelhorTempoMedio(int melhorTempoMedio) {
		this.melhorTempoMedio = melhorTempoMedio;
	}

	public int getMelhorTempoDificil() {
		return this.melhorTempoDificil;
	}

	public void setMelhorTempoDificil(int melhorTempoDificil) {
		this.melhorTempoDificil = melhorTempoDificil;
	}

	public boolean verificarSenha(String senha) {
		return this.senhaHash.equals(Usuario.hashSHA256(senha));
	}

	public static String hashSHA256(String texto) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(texto.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algoritmo SHA-256 não disponível", e);
		}
	}

	@Override
	public String toString() {
		return this.nome;
	}

	public static void validarNome(String nome) throws ModelException {
		if (nome == null || nome.length() == 0)
			throw new ModelException("O nome do usuário não pode ser nulo!");
		if (nome.length() < TAM_MIN_NOME || nome.length() > TAM_MAX_NOME)
			throw new ModelException("O nome deve ter entre " + TAM_MIN_NOME + " e " + TAM_MAX_NOME + " caracteres!");
	}

	public static void validarSenha(String senha) throws ModelException {
		if (senha == null || senha.length() == 0)
			throw new ModelException("A senha não pode ser nula!");
		if (senha.length() < TAM_MIN_SENHA || senha.length() > TAM_MAX_SENHA)
			throw new ModelException(
					"A senha deve ter entre " + TAM_MIN_SENHA + " e " + TAM_MAX_SENHA + " caracteres!");
	}

	public static void validarLogin(String login) throws ModelException {
		if (login == null || login.length() == 0)
			throw new ModelException("O login não pode ser nulo!");
		if (login.length() < TAM_MIN_LOGIN || login.length() > TAM_MAX_LOGIN)
			throw new ModelException(
					"O login deve ter entre " + TAM_MIN_LOGIN + " e " + TAM_MAX_LOGIN + " caracteres!");
	}

	public void incrementarPartida(boolean venceu) {
		this.totalPartidas++;
		if (venceu) {
			this.vitorias++;
		} else {
			this.derrotas++;
		}
	}

	public void atualizarMelhorTempo(String dificuldade, int tempo) {
		switch (dificuldade.toLowerCase()) {
			case "facil":
				if (this.melhorTempoFacil == 0 || tempo < this.melhorTempoFacil) {
					this.melhorTempoFacil = tempo;
				}
				break;
			case "medio":
				if (this.melhorTempoMedio == 0 || tempo < this.melhorTempoMedio) {
					this.melhorTempoMedio = tempo;
				}
				break;
			case "dificil":
				if (this.melhorTempoDificil == 0 || tempo < this.melhorTempoDificil) {
					this.melhorTempoDificil = tempo;
				}
				break;
		}
	}
}
