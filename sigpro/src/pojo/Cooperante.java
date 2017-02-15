package pojo;
// Generated Feb 8, 2017 5:37:26 PM by Hibernate Tools 5.2.0.CR1

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
 * Cooperante generated by hbm2java
 */
@Entity
@Table(name = "cooperante", catalog = "sipro")
public class Cooperante implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1922903718479351746L;
	private Integer id;
	private String codigo;
	private String nombre;
	private String descripcion;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private Set<Proyecto> proyectos = new HashSet<Proyecto>(0);

	public Cooperante() {
	}

	public Cooperante(String codigo, String nombre, String usuarioCreo, Date fechaCreacion, int estado) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public Cooperante(String codigo, String nombre, String descripcion, String usuarioCreo, String usuarioActualizo,
			Date fechaCreacion, Date fechaActualizacion, int estado, Set<Proyecto> proyectos) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.proyectos = proyectos;
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

	@Column(name = "codigo", nullable = false, length = 30)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "nombre", nullable = false, length = 1000)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", length = 4000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "usuario_creo", nullable = false, length = 30)
	public String getUsuarioCreo() {
		return this.usuarioCreo;
	}

	public void setUsuarioCreo(String usuarioCreo) {
		this.usuarioCreo = usuarioCreo;
	}

	@Column(name = "usuario_actualizo", length = 30)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cooperante")
	public Set<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(Set<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

}
