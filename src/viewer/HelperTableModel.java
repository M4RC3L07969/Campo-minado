package viewer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class HelperTableModel {
	private DefaultTableModel tableModel;
	private ArrayList<String> listaAtributos = new ArrayList<String>();
	private Object[][] tabela;

	public HelperTableModel(Object[] objetos) {
		if (objetos == null || objetos.length == 0 || objetos[0] == null) {
			this.listaAtributos = new ArrayList<String>();
			this.tabela = new Object[0][0];
			this.tableModel = new DefaultTableModel(this.getTabela(), this.getAtributos());
			return;
		}
		Class classe = objetos[0].getClass();
		for (Method m : classe.getMethods()) {
			String nomeMetodo = m.getName();
			if (nomeMetodo.startsWith("get") && !nomeMetodo.equals("getClass") && !nomeMetodo.contains("Formatado"))
				listaAtributos.add(nomeMetodo.substring(3));
		}
		tabela = new Object[objetos.length][listaAtributos.size()];
		for (int i = 0; i < objetos.length; i++) {
			if (objetos[i] == null)
				continue;
			for (int j = 0; j < listaAtributos.size(); j++) {
				try {
					String atributo = listaAtributos.get(j);
					Method get = classe.getMethod("get" + atributo);

					// Tenta usar método formatado se disponível
					Method getFormatado = null;
					try {
						getFormatado = classe.getMethod("get" + atributo + "Formatado");
					} catch (NoSuchMethodException e) {
						// Método formatado não existe, usa o padrão
					}

					Object valor;
					if (getFormatado != null) {
						valor = getFormatado.invoke(objetos[i]);
					} else {
						valor = get.invoke(objetos[i]);
					}
					tabela[i][j] = valor;
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		this.tableModel = new DefaultTableModel(this.getTabela(), this.getAtributos());
	}

	public Object[] getAtributos() {
		return listaAtributos.toArray();
	}

	public Object[][] getTabela() {
		return this.tabela;
	}

	public DefaultTableModel getTableModel() {
		return this.tableModel;
	}
}
