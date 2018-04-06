package pojo;
// Generated 5/04/2018 04:15:52 PM by Hibernate Tools 5.2.3.Final

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
 * Recurso generated by hbm2java
 */
@Entity
@Table(name = "recurso", catalog = "sipro")
public class Recurso implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -542857027270659448L;
	private Integer id;
	private RecursoTipo recursoTipo;
	private RecursoUnidadMedida recursoUnidadMedida;
	private String nombre;
	private String descripcion;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private Set<ObjetoRecurso> objetoRecursos = new HashSet<ObjetoRecurso>(0);

	public Recurso() {
	}

	public Recurso(RecursoTipo recursoTipo, RecursoUnidadMedida recursoUnidadMedida, String usuarioCreo,
			Date fechaCreacion, int estado) {
		this.recursoTipo = recursoTipo;
		this.recursoUnidadMedida = recursoUnidadMedida;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public Recurso(RecursoTipo recursoTipo, RecursoUnidadMedida recursoUnidadMedida, String nombre, String descripcion,
			String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion, int estado,
			Set<ObjetoRecurso> objetoRecursos) {
		this.recursoTipo = recursoTipo;
		this.recursoUnidadMedida = recursoUnidadMedida;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.objetoRecursos = objetoRecursos;
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
	@JoinColumn(name = "recurso_tipoid", nullable = false)
	public RecursoTipo getRecursoTipo() {
		return this.recursoTipo;
	}

	public void setRecursoTipo(RecursoTipo recursoTipo) {
		this.recursoTipo = recursoTipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recurso_unidad_medidaid", nullable = false)
	public RecursoUnidadMedida getRecursoUnidadMedida() {
		return this.recursoUnidadMedida;
	}

	public void setRecursoUnidadMedida(RecursoUnidadMedida recursoUnidadMedida) {
		this.recursoUnidadMedida = recursoUnidadMedida;
	}

	@Column(name = "nombre", length = 1000)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recurso")
	public Set<ObjetoRecurso> getObjetoRecursos() {
		return this.objetoRecursos;
	}

	public void setObjetoRecursos(Set<ObjetoRecurso> objetoRecursos) {
		this.objetoRecursos = objetoRecursos;
	}

}
