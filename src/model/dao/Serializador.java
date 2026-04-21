package model.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import model.Usuario;

public class Serializador {

	public static void salvarObjetos() {
		try {
			FileOutputStream fos = new FileOutputStream("objetos.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(DaoUsuario.obterTodos());

			oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problema no salvamento dos objetos: " + e.getMessage());
		}
	}

	public static void recuperarObjetos() {
		try {
			FileInputStream fis = new FileInputStream("objetos.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);

			DaoUsuario.recuperarTodos((Usuario[]) ois.readObject());

			ois.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problema na recuperação dos objetos: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Problema na recuperação dos objetos: " + e.getMessage());
		}
	}

}
