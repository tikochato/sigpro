package pojo;
// Generated Dec 11, 2017 5:35:04 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ProyectoRolColaborador generated by hbm2java
 */
@Entity
@Table(name = "proyecto_rol_colaborador", catalog = "sipro")
public class ProyectoRolColaborador implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7245146071097741068L;
	private Integer id;
	private Colaborador colaborador;
	private Proyecto proyecto;
	private RolUnidadEjecutora rolUnidadEjecutora;
	private int estado;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;

	public ProyectoRolColaborador() {
	}

	public ProyectoRolColaborador(Colaborador colaborador, Proyecto proyecto, RolUnidadEjecutora rolUnidadEjecutora,
			int estado, String usuarioCreo, Date fechaCreacion) {
		this.colaborador = colaborador;
		this.proyecto = proyecto;
		this.rolUnidadEjecutora = rolUnidadEjecutora;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public ProyectoRolColaborador(Colaborador colaborador, Proyecto proyecto, RolUnidadEjecutora rolUnidadEjecutora,
			int estado, String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion) {
		this.colaborador = colaborador;
		this.proyecto = proyecto;
		this.rolUnidadEjecutora = rolUnidadEjecutora;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
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
	@JoinColumn(name = "colaboradorid", nullable = false)
	public Colaborador getColaborador() {
		return this.colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proyectoid", nullable = false)
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rol_unidad_ejecutoraid", nullable = false)
	public RolUnidadEjecutora getRolUnidadEjecutora() {
		return this.rolUnidadEjecutora;
	}

	public void setRolUnidadEjecutora(RolUnidadEjecutora rolUnidadEjecutora) {
		this.rolUnidadEjecutora = rolUnidadEjecutora;
	}

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
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

}
