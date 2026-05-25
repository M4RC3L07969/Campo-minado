package model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DaoCriacaoTabela {

	public DaoCriacaoTabela() {
		criarBancoDeDados();
		criarTabelaUsuario();
		criarTabelaPartida();
	}

	private void criarBancoDeDados() {
		String sql = "CREATE DATABASE IF NOT EXISTS Campo_minado";
		Connection conexao = null;
		PreparedStatement operacao = null;
		FileInputStream arquivo = null;

		try {
			Properties props = new Properties();
			arquivo = new FileInputStream("db.properties");
			props.load(arquivo);

			String url = props.getProperty("db.url").replace("/Campo_minado", "");
			String usuario = props.getProperty("db.usuario");
			String senha = props.getProperty("db.senha");

			conexao = DriverManager.getConnection(url, usuario, senha);
			operacao = conexao.prepareStatement(sql);
			operacao.execute();

			System.out.println("Banco de dados Campo_minado criado com sucesso!");
		} catch (SQLException e) {
			System.out.println("Erro ao criar banco de dados: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("Erro: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				if (operacao != null)
					operacao.close();
				if (conexao != null)
					conexao.close();
				if (arquivo != null)
					arquivo.close();
			} catch (SQLException | IOException e) {
				System.out.println("Erro ao fechar recursos: " + e.getMessage());
			}
		}
	}

	private void criarTabelaUsuario() {
		String sql = "CREATE TABLE IF NOT EXISTS usuario (" +
				"id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
				"nome VARCHAR(30), " +
				"login VARCHAR(50), " +
				"senha_hash VARCHAR(64), " +
				"total_partidas INT, " +
				"vitorias INT, " +
				"derrotas INT, " +
				"melhor_tempo_facil INT, " +
				"melhor_tempo_medio INT, " +
				"melhor_tempo_dificil INT)";
		Connection conexao = null;
		PreparedStatement operacao = null;
		FileInputStream arquivo = null;

		try {
			Properties props = new Properties();
			arquivo = new FileInputStream("db.properties");
			props.load(arquivo);

			String url = props.getProperty("db.url");
			String usuario = props.getProperty("db.usuario");
			String senha = props.getProperty("db.senha");

			conexao = DriverManager.getConnection(url, usuario, senha);
			operacao = conexao.prepareStatement(sql);
			operacao.execute();

			System.out.println("Tabela usuario criada com sucesso!");
		} catch (SQLException e) {
			System.out.println("Erro ao criar tabela usuario: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("Erro: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				if (operacao != null)
					operacao.close();
				if (conexao != null)
					conexao.close();
				if (arquivo != null)
					arquivo.close();
			} catch (SQLException | IOException e) {
				System.out.println("Erro ao fechar recursos: " + e.getMessage());
			}
		}
	}

	private void criarTabelaPartida() {
		String sql = "CREATE TABLE IF NOT EXISTS partida (" +
				"id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
				"usuario_id INT, " +
				"modo VARCHAR(10), " +
				"tempo INT, " +
				"resultado CHAR(7), " +
				"data_partida DATE, " +
				"FOREIGN KEY (usuario_id) REFERENCES usuario(id))";
		Connection conexao = null;
		PreparedStatement operacao = null;
		FileInputStream arquivo = null;

		try {
			Properties props = new Properties();
			arquivo = new FileInputStream("db.properties");
			props.load(arquivo);

			String url = props.getProperty("db.url");
			String usuario = props.getProperty("db.usuario");
			String senha = props.getProperty("db.senha");

			conexao = DriverManager.getConnection(url, usuario, senha);
			operacao = conexao.prepareStatement(sql);
			operacao.execute();

			System.out.println("Tabela partida criada com sucesso!");
		} catch (SQLException e) {
			System.out.println("Erro ao criar tabela partida: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("Erro: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				if (operacao != null)
					operacao.close();
				if (conexao != null)
					conexao.close();
				if (arquivo != null)
					arquivo.close();
			} catch (SQLException | IOException e) {
				System.out.println("Erro ao fechar recursos: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		new DaoCriacaoTabela();
	}
}
