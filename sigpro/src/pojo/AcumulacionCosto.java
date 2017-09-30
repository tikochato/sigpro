package pojo;
// Generated Sep 29, 2017 9:09:55 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AcumulacionCosto generated by hbm2java
 */
@Entity
@Table(name = "acumulacion_costo", catalog = "sipro")
public class AcumulacionCosto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8015213596989810385L;
	private Integer id;
	private String nombre;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private Set<Componente> componentes = new HashSet<Componente>(0);
	private Set<Producto> productos = new HashSet<Producto>(0);
	private Set<Actividad> actividads = new HashSet<Actividad>(0);
	private Set<Proyecto> proyectos = new HashSet<Proyecto>(0);
	private Set<Subproducto> subproductos = new HashSet<Subproducto>(0);

	public AcumulacionCosto() {
	}

	public AcumulacionCosto(String nombre, String usuarioCreo, Date fechaCreacion, int estado) {
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public AcumulacionCosto(String nombre, String usuarioCreo, String usuarioActualizo, Date fechaCreacion,
			Date fechaActualizacion, int estado, Set<Componente> componentes, Set<Producto> productos,
			Set<Actividad> actividads, Set<Proyecto> proyectos, Set<Subproducto> subproductos) {
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.componentes = componentes;
		this.productos = productos;
		this.actividads = actividads;
		this.proyectos = proyectos;
		this.subproductos = subproductos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "nombre", nullable = false, length = 45)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "usuario_creo", nullable = false, length = 45)
	public String getUsuarioCreo() {
		return this.usuarioCreo;
	}

	public void setUsuarioCreo(String usuarioCreo) {
		this.usuarioCreo = usuarioCreo;
	}

	@Column(name = "usuario_actualizo", length = 45)
	public String getUsuarioActualizo() {
		return this.usuarioActualizo;
	}

	public void setUsuarioActualizo(String usuarioActualizo) {
		this.usuarioActualizo = usuarioActualizo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", nullable = false, length = 19)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_actualizacion", length = 19)
	public Date getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acumulacionCosto")
	public Set<Componente> getComponentes() {
		return this.componentes;
	}

	public void setComponentes(Set<Componente> componentes) {
		this.componentes = componentes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acumulacionCosto")
	public Set<Producto> getProductos() {
		return this.productos;
	}

	public void setProductos(Set<Producto> productos) {
		this.productos = productos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acumulacionCosto")
	public Set<Actividad> getActividads() {
		return this.actividads;
	}

	public void setActividads(Set<Actividad> actividads) {
		this.actividads = actividads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acumulacionCosto")
	public Set<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(Set<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acumulacionCosto")
	public Set<Subproducto> getSubproductos() {
		return this.subproductos;
	}

	public void setSubproductos(Set<Subproducto> subproductos) {
		this.subproductos = subproductos;
	}

}
