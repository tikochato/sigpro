package pojo;
// Generated Jul 13, 2017 10:05:07 AM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ProyectoMiembro generated by hbm2java
 */
@Entity
@Table(name = "proyecto_miembro", catalog = "sipro")
public class ProyectoMiembro implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2252167333551892961L;
	private ProyectoMiembroId id;
	private Colaborador colaborador;
	private Proyecto proyecto;
	private int estado;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private String usuarioCreo;
	private String usuarioActualizo;

	public ProyectoMiembro() {
	}

	public ProyectoMiembro(ProyectoMiembroId id, Colaborador colaborador, Proyecto proyecto, int estado,
			Date fechaCreacion, String usuarioCreo) {
		this.id = id;
		this.colaborador = colaborador;
		this.proyecto = proyecto;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.usuarioCreo = usuarioCreo;
	}

	public ProyectoMiembro(ProyectoMiembroId id, Colaborador colaborador, Proyecto proyecto, int estado,
			Date fechaCreacion, Date fechaActualizacion, String usuarioCreo, String usuarioActualizo) {
		this.id = id;
		this.colaborador = colaborador;
		this.proyecto = proyecto;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "proyectoid", column = @Column(name = "proyectoid", nullable = false)),
			@AttributeOverride(name = "colaboradorid", column = @Column(name = "colaboradorid", nullable = false)) })
	public ProyectoMiembroId getId() {
		return this.id;
	}

	public void setId(ProyectoMiembroId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "colaboradorid", nullable = false, insertable = false, updatable = false)
	public Colaborador getColaborador() {
		return this.colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proyectoid", nullable = false, insertable = false, updatable = false)
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
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

}
