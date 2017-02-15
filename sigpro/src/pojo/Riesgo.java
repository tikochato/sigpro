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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Riesgo generated by hbm2java
 */
@Entity
@Table(name = "riesgo", catalog = "sipro")
public class Riesgo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1660426989288288218L;
	private Integer id;
	private RiesgoTipo riesgoTipo;
	private String nombre;
	private String descripcion;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private Set<ObjetoRiesgo> objetoRiesgos = new HashSet<ObjetoRiesgo>(0);
	private Set<RiesgoPropiedadValor> riesgoPropiedadValors = new HashSet<RiesgoPropiedadValor>(0);

	public Riesgo() {
	}

	public Riesgo(RiesgoTipo riesgoTipo, String nombre, String usuarioCreo, Date fechaCreacion, int estado) {
		this.riesgoTipo = riesgoTipo;
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public Riesgo(RiesgoTipo riesgoTipo, String nombre, String descripcion, String usuarioCreo, String usuarioActualizo,
			Date fechaCreacion, Date fechaActualizacion, int estado, Set<ObjetoRiesgo> objetoRiesgos,
			Set<RiesgoPropiedadValor> riesgoPropiedadValors) {
		this.riesgoTipo = riesgoTipo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.objetoRiesgos = objetoRiesgos;
		this.riesgoPropiedadValors = riesgoPropiedadValors;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "riesgo_tipoid", nullable = false)
	public RiesgoTipo getRiesgoTipo() {
		return this.riesgoTipo;
	}

	public void setRiesgoTipo(RiesgoTipo riesgoTipo) {
		this.riesgoTipo = riesgoTipo;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "riesgo")
	public Set<ObjetoRiesgo> getObjetoRiesgos() {
		return this.objetoRiesgos;
	}

	public void setObjetoRiesgos(Set<ObjetoRiesgo> objetoRiesgos) {
		this.objetoRiesgos = objetoRiesgos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "riesgo")
	public Set<RiesgoPropiedadValor> getRiesgoPropiedadValors() {
		return this.riesgoPropiedadValors;
	}

	public void setRiesgoPropiedadValors(Set<RiesgoPropiedadValor> riesgoPropiedadValors) {
		this.riesgoPropiedadValors = riesgoPropiedadValors;
	}

}
