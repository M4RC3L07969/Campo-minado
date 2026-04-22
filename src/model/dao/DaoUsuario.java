package model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import model.Usuario;
import model.ModelException;

public class DaoUsuario {

    public DaoUsuario() {
        super();
    }

    private Connection getConnection() throws SQLException, FileNotFoundException, IOException {
        Properties props = new Properties();
        FileInputStream arquivo = new FileInputStream("db.properties");
        props.load(arquivo);

        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.usuario");
        String senha = props.getProperty("db.senha");

        return DriverManager.getConnection(url, usuario, senha);
    }

    public boolean incluir(Usuario novo) {
        if (novo == null)
            return false;
        if (obterUsuarioPeloNome(novo.getNome()) != null)
            return false;
        if (obterUsuarioPeloLogin(novo.getLogin()) != null)
            return false;

        String sql = "INSERT INTO usuario (nome, login, senha_hash, total_partidas, vitorias, derrotas, melhor_tempo_facil, melhor_tempo_medio, melhor_tempo_dificil) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            operacao.setString(1, novo.getNome());
            operacao.setString(2, novo.getLogin());
            operacao.setString(3, novo.getSenhaHash());
            operacao.setInt(4, novo.getTotalPartidas());
            operacao.setInt(5, novo.getVitorias());
            operacao.setInt(6, novo.getDerrotas());
            operacao.setInt(7, novo.getMelhorTempoFacil());
            operacao.setInt(8, novo.getMelhorTempoMedio());
            operacao.setInt(9, novo.getMelhorTempoDificil());
            operacao.execute();

            ResultSet rs = operacao.getGeneratedKeys();
            if (rs.next()) {
                novo.setId(rs.getInt(1));
            }

            conexao.close();
            operacao.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao incluir usuario: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public boolean remover(Usuario ex) {
        if (ex == null || ex.getId() == 0)
            return false;

        String sql = "DELETE FROM usuario WHERE id = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setInt(1, ex.getId());
            int linhasAfetadas = operacao.executeUpdate();

            conexao.close();
            operacao.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover usuario: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(Usuario alterado) {
        if (alterado == null || alterado.getId() == 0)
            return false;

        String sql = "UPDATE usuario SET nome = ?, login = ?, senha_hash = ?, total_partidas = ?, vitorias = ?, derrotas = ?, melhor_tempo_facil = ?, melhor_tempo_medio = ?, melhor_tempo_dificil = ? WHERE id = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setString(1, alterado.getNome());
            operacao.setString(2, alterado.getLogin());
            operacao.setString(3, alterado.getSenhaHash());
            operacao.setInt(4, alterado.getTotalPartidas());
            operacao.setInt(5, alterado.getVitorias());
            operacao.setInt(6, alterado.getDerrotas());
            operacao.setInt(7, alterado.getMelhorTempoFacil());
            operacao.setInt(8, alterado.getMelhorTempoMedio());
            operacao.setInt(9, alterado.getMelhorTempoDificil());
            operacao.setInt(10, alterado.getId());
            int linhasAfetadas = operacao.executeUpdate();

            conexao.close();
            operacao.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar usuario: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public Usuario obterUsuarioPeloNome(String nome) {
        String sql = "SELECT * FROM usuario WHERE nome = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setString(1, nome);
            ResultSet resultado = operacao.executeQuery();

            Usuario usuario = null;
            if (resultado.next()) {
                usuario = criarUsuarioDoResultSet(resultado);
            }

            conexao.close();
            operacao.close();
            return usuario;
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuario: " + e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public Usuario obterUsuarioPeloLogin(String login) {
        String sql = "SELECT * FROM usuario WHERE login = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setString(1, login);
            ResultSet resultado = operacao.executeQuery();

            Usuario usuario = null;
            if (resultado.next()) {
                usuario = criarUsuarioDoResultSet(resultado);
            }

            conexao.close();
            operacao.close();
            return usuario;
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuario: " + e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public Usuario obterUsuarioPeloId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setInt(1, id);
            ResultSet resultado = operacao.executeQuery();

            Usuario usuario = null;
            if (resultado.next()) {
                usuario = criarUsuarioDoResultSet(resultado);
            }

            conexao.close();
            operacao.close();
            return usuario;
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuario: " + e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static Usuario[] obterTodos() {
        String sql = "SELECT * FROM usuario";

        try {
            Properties props = new Properties();
            FileInputStream arquivo = new FileInputStream("db.properties");
            props.load(arquivo);

            String url = props.getProperty("db.url");
            String usuario = props.getProperty("db.usuario");
            String senha = props.getProperty("db.senha");

            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            PreparedStatement operacao = conexao.prepareStatement(sql);
            ResultSet resultado = operacao.executeQuery();

            ArrayList<Usuario> lista = new ArrayList<>();
            while (resultado.next()) {
                lista.add(criarUsuarioDoResultSet(resultado));
            }

            conexao.close();
            operacao.close();
            return lista.toArray(new Usuario[0]);
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuarios: " + e.getMessage());
            return new Usuario[0];
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return new Usuario[0];
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return new Usuario[0];
        }
    }

    private static Usuario criarUsuarioDoResultSet(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultado.getInt("id"));
        try {
            usuario.setNome(resultado.getString("nome"));
            usuario.setLogin(resultado.getString("login"));
        } catch (ModelException e) {
        }
        usuario.setSenhaHash(resultado.getString("senha_hash"));
        usuario.setTotalPartidas(resultado.getInt("total_partidas"));
        usuario.setVitorias(resultado.getInt("vitorias"));
        usuario.setDerrotas(resultado.getInt("derrotas"));
        usuario.setMelhorTempoFacil(resultado.getInt("melhor_tempo_facil"));
        usuario.setMelhorTempoMedio(resultado.getInt("melhor_tempo_medio"));
        usuario.setMelhorTempoDificil(resultado.getInt("melhor_tempo_dificil"));
        return usuario;
    }
}
