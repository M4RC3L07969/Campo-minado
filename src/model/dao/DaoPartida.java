package model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Properties;

import java.time.LocalDate;
import java.util.List;

import model.Partida;
import model.PeriodoRanking;
import model.RankingEntry;
import model.Usuario;

public class DaoPartida {

    public DaoPartida() {
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

    public boolean incluir(Partida novo) {
        if (novo == null)
            return false;

        String sql = "INSERT INTO partida (usuario_id, modo, tempo, resultado, data_partida) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            int usuarioId = novo.getUsuario() != null ? novo.getUsuario().getId() : 0;
            operacao.setInt(1, usuarioId);
            operacao.setString(2, novo.getModo());
            operacao.setInt(3, novo.getTempo());
            operacao.setString(4, novo.getResultado());

            Date dataSql = null;
            if (novo.getDataPartida() != null && !novo.getDataPartida().isEmpty()) {
                dataSql = Date.valueOf(novo.getDataPartida());
            }
            operacao.setDate(5, dataSql);

            operacao.execute();

            ResultSet rs = operacao.getGeneratedKeys();
            if (rs.next()) {
                novo.setId(rs.getInt(1));
            }

            conexao.close();
            operacao.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao incluir partida: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao converter data: " + e.getMessage());
            return false;
        }
    }

    public boolean remover(Partida ex) {
        if (ex == null || ex.getId() == 0)
            return false;

        String sql = "DELETE FROM partida WHERE id = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setInt(1, ex.getId());
            int linhasAfetadas = operacao.executeUpdate();

            conexao.close();
            operacao.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover partida: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(Partida alterado) {
        if (alterado == null || alterado.getId() == 0)
            return false;

        String sql = "UPDATE partida SET usuario_id = ?, modo = ?, tempo = ?, resultado = ?, data_partida = ? WHERE id = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);

            int usuarioId = alterado.getUsuario() != null ? alterado.getUsuario().getId() : 0;
            operacao.setInt(1, usuarioId);
            operacao.setString(2, alterado.getModo());
            operacao.setInt(3, alterado.getTempo());
            operacao.setString(4, alterado.getResultado());

            Date dataSql = null;
            if (alterado.getDataPartida() != null && !alterado.getDataPartida().isEmpty()) {
                dataSql = Date.valueOf(alterado.getDataPartida());
            }
            operacao.setDate(5, dataSql);
            operacao.setInt(6, alterado.getId());

            int linhasAfetadas = operacao.executeUpdate();

            conexao.close();
            operacao.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar partida: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao converter data: " + e.getMessage());
            return false;
        }
    }

    public void removerPartidasPorUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == 0)
            return;

        String sql = "DELETE FROM partida WHERE usuario_id = ?";

        try {
            Connection conexao = getConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);
            operacao.setInt(1, usuario.getId());
            operacao.executeUpdate();

            conexao.close();
            operacao.close();
        } catch (SQLException e) {
            System.out.println("Erro ao remover partidas do usuario: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static Partida[] obterTodos() {
        String sql = "SELECT * FROM partida";

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

            ArrayList<Partida> lista = new ArrayList<>();
            while (resultado.next()) {
                lista.add(criarPartidaDoResultSet(resultado));
            }

            conexao.close();
            operacao.close();
            return lista.toArray(new Partida[0]);
        } catch (SQLException e) {
            System.out.println("Erro ao consultar partidas: " + e.getMessage());
            return new Partida[0];
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return new Partida[0];
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return new Partida[0];
        }
    }

    private static Partida criarPartidaDoResultSet(ResultSet resultado) throws SQLException {
        Partida partida = new Partida();
        partida.setId(resultado.getInt("id"));

        int usuarioId = resultado.getInt("usuario_id");
        if (usuarioId > 0) {
            Usuario usuario = new DaoUsuario().obterUsuarioPeloId(usuarioId);
            partida.setUsuario(usuario);
        }

        partida.setModo(resultado.getString("modo"));
        partida.setTempo(resultado.getInt("tempo"));
        partida.setResultado(resultado.getString("resultado"));

        Date dataSql = resultado.getDate("data_partida");
        if (dataSql != null) {
            partida.setDataPartida(dataSql.toString());
        }

        return partida;
    }

    public static List<RankingEntry> obterMelhoresTemposPorPeriodo(String modo, PeriodoRanking periodo) {
        LocalDate dataInicio = periodo.getDataInicio();
        String sql = "SELECT p.usuario_id, u.nome, u.login, MIN(p.tempo) as melhor_tempo, COUNT(p.id) as total_partidas "
                +
                "FROM partida p JOIN usuario u ON p.usuario_id = u.id " +
                "WHERE p.modo = ? AND p.resultado = 'Vitoria' ";

        if (dataInicio != null) {
            sql += "AND p.data_partida >= ? ";
        }

        sql += "GROUP BY p.usuario_id, u.nome, u.login ORDER BY melhor_tempo ASC LIMIT 10";

        List<RankingEntry> lista = new ArrayList<>();

        try {
            Connection conexao = getStaticConnection();
            PreparedStatement operacao = conexao.prepareStatement(sql);

            operacao.setString(1, modo);
            if (dataInicio != null) {
                operacao.setDate(2, Date.valueOf(dataInicio));
            }

            ResultSet resultado = operacao.executeQuery();

            while (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("usuario_id"));
                try {
                    usuario.setNome(resultado.getString("nome"));
                    usuario.setLogin(resultado.getString("login"));
                } catch (Exception e) {
                }
                usuario.setSenhaHash("");

                int melhorTempo = resultado.getInt("melhor_tempo");
                int totalPartidas = resultado.getInt("total_partidas");

                lista.add(new RankingEntry(usuario, melhorTempo, totalPartidas));
            }

            conexao.close();
            operacao.close();
        } catch (SQLException e) {
            System.out.println("Erro ao consultar ranking: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return lista;
    }

    private static Connection getStaticConnection() throws SQLException, FileNotFoundException, IOException {
        Properties props = new Properties();
        FileInputStream arquivo = new FileInputStream("db.properties");
        props.load(arquivo);

        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.usuario");
        String senha = props.getProperty("db.senha");

        return DriverManager.getConnection(url, usuario, senha);
    }
}
