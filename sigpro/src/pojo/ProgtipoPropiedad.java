package pojo;
// Generated Oct 20, 2017 12:16:45 PM by Hibernate Tools 5.2.3.Final

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
 * ProgtipoPropiedad generated by hbm2java
 */
@Entity
@Table(name = "progtipo_propiedad", catalog = "sipro")
public class ProgtipoPropiedad implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5369371231783762428L;
	private ProgtipoPropiedadId id;
	private ProgramaPropiedad programaPropiedad;
	private ProgramaTipo programaTipo;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Integer estado;

	public ProgtipoPropiedad() {
	}

	public ProgtipoPropiedad(ProgtipoPropiedadId id, ProgramaPropiedad programaPropiedad, ProgramaTipo programaTipo) {
		this.id = id;
		this.programaPropiedad = programaPropiedad;
		this.programaTipo = programaTipo;
	}

	public ProgtipoPropiedad(ProgtipoPropiedadId id, ProgramaPropiedad programaPropiedad, ProgramaTipo programaTipo,
			String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion, Integer estado) {
		this.id = id;
		this.programaPropiedad = programaPropiedad;
		this.programaTipo = programaTipo;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "programaPropiedadid", column = @Column(name = "programa_propiedadid", nullable = false)),
			@AttributeOverride(name = "programaTipoid", column = @Column(name = "programa_tipoid", nullable = false)) })
	public ProgtipoPropiedadId getId() {
		return this.id;
	}

	public void setId(ProgtipoPropiedadId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "programa_propiedadid", nullable = false, insertable = false, updatable = false)
	public ProgramaPropiedad getProgramaPropiedad() {
		return this.programaPropiedad;
	}

	public void setProgramaPropiedad(ProgramaPropiedad programaPropiedad) {
		this.programaPropiedad = programaPropiedad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "programa_tipoid", nullable = false, insertable = false, updatable = false)
	public ProgramaTipo getProgramaTipo() {
		return this.programaTipo;
	}

	public void setProgramaTipo(ProgramaTipo programaTipo) {
		this.programaTipo = programaTipo;
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
