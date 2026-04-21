package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario implements Serializable {

	final public static int TAM_MIN_NOME = 3;
	final public static int TAM_MAX_NOME = 30;
	final public static int TAM_MIN_SENHA = 4;
	final public static int TAM_MAX_SENHA = 20;

	private String nome;
	private String senhaHash;

	public Usuario(String nome, String senha) throws ModelException {
		this.setNome(nome);
		this.setSenha(senha);
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) throws ModelException {
		Usuario.validarNome(nome);
		this.nome = nome;
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
			throw new ModelException("A senha deve ter entre " + TAM_MIN_SENHA + " e " + TAM_MAX_SENHA + " caracteres!");
	}
}
