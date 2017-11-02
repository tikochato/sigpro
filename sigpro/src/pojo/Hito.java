package pojo;
// Generated Nov 2, 2017 9:36:21 AM by Hibernate Tools 5.2.3.Final

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
 * Hito generated by hbm2java
 */
@Entity
@Table(name = "hito", catalog = "sipro")
public class Hito implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2377231229909665638L;
	private Integer id;
	private HitoTipo hitoTipo;
	private Proyecto proyecto;
	private String nombre;
	private String descripcion;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private Date fecha;
	private Set<HitoResultado> hitoResultados = new HashSet<HitoResultado>(0);

	public Hito() {
	}

	public Hito(HitoTipo hitoTipo, Proyecto proyecto, String nombre, String usuarioCreo, Date fechaCreacion, int estado,
			Date fecha) {
		this.hitoTipo = hitoTipo;
		this.proyecto = proyecto;
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
		this.fecha = fecha;
	}

	public Hito(HitoTipo hitoTipo, Proyecto proyecto, String nombre, String descripcion, String usuarioCreo,
			String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion, int estado, Date fecha,
			Set<HitoResultado> hitoResultados) {
		this.hitoTipo = hitoTipo;
		this.proyecto = proyecto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.fecha = fecha;
		this.hitoResultados = hitoResultados;
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
	@JoinColumn(name = "hito_tipoid", nullable = false)
	public HitoTipo getHitoTipo() {
		return this.hitoTipo;
	}

	public void setHitoTipo(HitoTipo hitoTipo) {
		this.hitoTipo = hitoTipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proyectoid", nullable = false)
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha", nullable = false, length = 19)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hito")
	public Set<HitoResultado> getHitoResultados() {
		return this.hitoResultados;
	}

	public void setHitoResultados(Set<HitoResultado> hitoResultados) {
		this.hitoResultados = hitoResultados;
	}

}
