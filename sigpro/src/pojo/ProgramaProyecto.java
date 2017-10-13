package pojo;
// Generated Oct 13, 2017 9:03:33 AM by Hibernate Tools 5.2.3.Final

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
 * ProgramaProyecto generated by hbm2java
 */
@Entity
@Table(name = "programa_proyecto", catalog = "sipro")
public class ProgramaProyecto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -939795092107463732L;
	private ProgramaProyectoId id;
	private Programa programa;
	private Proyecto proyecto;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Integer estado;

	public ProgramaProyecto() {
	}

	public ProgramaProyecto(ProgramaProyectoId id, Programa programa, Proyecto proyecto) {
		this.id = id;
		this.programa = programa;
		this.proyecto = proyecto;
	}

	public ProgramaProyecto(ProgramaProyectoId id, Programa programa, Proyecto proyecto, String usuarioCreo,
			String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion, Integer estado) {
		this.id = id;
		this.programa = programa;
		this.proyecto = proyecto;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "programaid", column = @Column(name = "programaid", nullable = false)),
			@AttributeOverride(name = "proyectoid", column = @Column(name = "proyectoid", nullable = false)) })
	public ProgramaProyectoId getId() {
		return this.id;
	}

	public void setId(ProgramaProyectoId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "programaid", nullable = false, insertable = false, updatable = false)
	public Programa getPrograma() {
		return this.programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proyectoid", nullable = false, insertable = false, updatable = false)
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	@Column(name = "usuario_creo", length = 30)
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
	@Column(name = "fecha_creacion", length = 19)
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

	@Column(name = "estado")
	public Integer getEstado() {
		return this.estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

}
