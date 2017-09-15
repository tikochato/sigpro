package dao;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Nodo {
	@Expose
	public int id;
	@Expose
	public int objeto_tipo;
	@Expose
	public String nombre;
	@Expose
	public int nivel;
	@Expose
	public ArrayList<Nodo> children;
	@Expose(serialize = false)
	public Nodo parent;
	
	public Nodo(int id, int objeto_tipo, String nombre, int nivel, ArrayList<Nodo> children, Nodo parent) {
		super();
		this.id = id;
		this.objeto_tipo = objeto_tipo;
		this.nombre = nombre;
		this.nivel = nivel;
		this.children = children;
		this.parent = parent;
	}
}
