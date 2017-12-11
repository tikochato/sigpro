package pojo;
// Generated Dec 11, 2017 5:35:04 PM by Hibernate Tools 5.2.3.Final

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
 * SctipoPropiedad generated by hbm2java
 */
@Entity
@Table(name = "sctipo_propiedad", catalog = "sipro")
public class SctipoPropiedad implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3322638967523362167L;
	private SctipoPropiedadId id;
	private SubcomponentePropiedad subcomponentePropiedad;
	private SubcomponenteTipo subcomponenteTipo;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;

	public SctipoPropiedad() {
	}

	public SctipoPropiedad(SctipoPropiedadId id, SubcomponentePropiedad subcomponentePropiedad,
			SubcomponenteTipo subcomponenteTipo, String usuarioCreo, Date fechaCreacion) {
		this.id = id;
		this.subcomponentePropiedad = subcomponentePropiedad;
		this.subcomponenteTipo = subcomponenteTipo;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public SctipoPropiedad(SctipoPropiedadId id, SubcomponentePropiedad subcomponentePropiedad,
			SubcomponenteTipo subcomponenteTipo, String usuarioCreo, String usuarioActualizo, Date fechaCreacion,
			Date fechaActualizacion) {
		this.id = id;
		this.subcomponentePropiedad = subcomponentePropiedad;
		this.subcomponenteTipo = subcomponenteTipo;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "subcomponenteTipoid", column = @Column(name = "subcomponente_tipoid", nullable = false)),
			@AttributeOverride(name = "subcomponentePropiedadid", column = @Column(name = "subcomponente_propiedadid", nullable = false)) })
	public SctipoPropiedadId getId() {
		return this.id;
	}

	public void setId(SctipoPropiedadId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcomponente_propiedadid", nullable = false, insertable = false, updatable = false)
	public SubcomponentePropiedad getSubcomponentePropiedad() {
		return this.subcomponentePropiedad;
	}

	public void setSubcomponentePropiedad(SubcomponentePropiedad subcomponentePropiedad) {
		this.subcomponentePropiedad = subcomponentePropiedad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcomponente_tipoid", nullable = false, insertable = false, updatable = false)
	public SubcomponenteTipo getSubcomponenteTipo() {
		return this.subcomponenteTipo;
	}

	public void setSubcomponenteTipo(SubcomponenteTipo subcomponenteTipo) {
		this.subcomponenteTipo = subcomponenteTipo;
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
