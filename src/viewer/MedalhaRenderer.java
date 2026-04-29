package viewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import model.Usuario;

public class MedalhaRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private final ImageIcon ouro;
	private final ImageIcon prata;
	private final ImageIcon bronze;

	private final List<Usuario> ranking;
	private final Usuario usuarioLogado;

	public MedalhaRenderer(List<Usuario> ranking, Usuario usuarioLogado) {
		this.ranking = ranking;
		this.usuarioLogado = usuarioLogado;

		ouro = carregarIcone("gold.png");
		prata = carregarIcone("silver.png");
		bronze = carregarIcone("bronze.png");

		setHorizontalAlignment(CENTER);
	}

	private ImageIcon carregarIcone(String nomeArquivo) {
		URL resourceUrl = getClass().getResource("/icons/" + nomeArquivo);
		if (resourceUrl != null) {
			System.out.println("Ícone encontrado no classpath: " + resourceUrl);
			return new ImageIcon(resourceUrl);
		}

		String classPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		File projectDir = new File(classPath).getParentFile();

		if (projectDir.getName().equals("bin")) {
			projectDir = projectDir.getParentFile();
		}

		String[] possiblePaths = {
				projectDir.getAbsolutePath() + "/src/resources/icons/" + nomeArquivo,
				projectDir.getAbsolutePath() + "/resources/icons/" + nomeArquivo,
				"src/resources/icons/" + nomeArquivo,
				"resources/icons/" + nomeArquivo,
				"C:/Users/mmaia/Documents/trabalho POO/Jogo-super-trunfo/src/resources/icons/" + nomeArquivo
		};

		for (String path : possiblePaths) {
			File file = new File(path);
			System.out.println("Tentando: " + file.getAbsolutePath() + " -> existe: " + file.exists());
			if (file.exists()) {
				System.out.println("Ícone encontrado em: " + file.getAbsolutePath());
				return new ImageIcon(file.getAbsolutePath());
			}
		}

		System.err.println("Ícone não encontrado: " + nomeArquivo);
		return null;
	}

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		setFont(table.getFont());
		setForeground(table.getForeground());

		int pos = 0;
		if (value instanceof Integer) {
			pos = (Integer) value;
		} else if (value instanceof String) {
			try {
				pos = Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				setText("");
				setIcon(null);
				return this;
			}
		}

		setText("");
		setIcon(null);

		if (pos == 1) {
			setIcon(ouro);
		} else if (pos == 2) {
			setIcon(prata);
		} else if (pos == 3) {
			setIcon(bronze);
		} else if (pos > 0) {
			setText(String.valueOf(pos));
			setHorizontalAlignment(CENTER);
		}

		if (usuarioLogado != null && row < ranking.size() && ranking.get(row).getId() == usuarioLogado.getId()) {
			setFont(getFont().deriveFont(Font.BOLD));
			setForeground(new Color(0, 100, 200));
		} else if (!isSelected) {
			setForeground(table.getForeground());
			setFont(table.getFont());
		}

		return this;
	}
}
