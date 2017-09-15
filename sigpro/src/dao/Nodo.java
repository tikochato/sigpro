package dao;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Nodo {
	@Expose
	int id;
	@Expose
	int objeto_tipo;
	@Expose
	String nombre;
	@Expose
	int nivel;
	@Expose
	String treepath;
	@Expose
	ArrayList<Nodo> children;
	@Expose(serialize = false)
	Nodo parent;
	
	public Nodo(int id, int objeto_tipo, String nombre, int nivel, String treepath, ArrayList<Nodo> children, Nodo parent) {
		super();
		this.id = id;
		this.objeto_tipo = objeto_tipo;
		this.nombre = nombre;
		this.nivel = nivel;
		this.treepath = treepath;
		this.children = children;
		this.parent = parent;
	}
}
