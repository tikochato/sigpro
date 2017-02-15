package pojo;
// Generated Feb 8, 2017 5:37:26 PM by Hibernate Tools 5.2.0.CR1

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
 * CtipoPropiedad generated by hbm2java
 */
@Entity
@Table(name = "ctipo_propiedad", catalog = "sipro")
public class CtipoPropiedad implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1654431130457292415L;
	private CtipoPropiedadId id;
	private ComponentePropiedad componentePropiedad;
	private ComponenteTipo componenteTipo;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;

	public CtipoPropiedad() {
	}

	public CtipoPropiedad(CtipoPropiedadId id, ComponentePropiedad componentePropiedad, ComponenteTipo componenteTipo,
			String usuarioCreo, Date fechaCreacion) {
		this.id = id;
		this.componentePropiedad = componentePropiedad;
		this.componenteTipo = componenteTipo;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public CtipoPropiedad(CtipoPropiedadId id, ComponentePropiedad componentePropiedad, ComponenteTipo componenteTipo,
			String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion) {
		this.id = id;
		this.componentePropiedad = componentePropiedad;
		this.componenteTipo = componenteTipo;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "componenteTipoid", column = @Column(name = "componente_tipoid", nullable = false)),
			@AttributeOverride(name = "componentePropiedadid", column = @Column(name = "componente_propiedadid", nullable = false)) })
	public CtipoPropiedadId getId() {
		return this.id;
	}

	public void setId(CtipoPropiedadId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "componente_propiedadid", nullable = false, insertable = false, updatable = false)
	public ComponentePropiedad getComponentePropiedad() {
		return this.componentePropiedad;
	}

	public void setComponentePropiedad(ComponentePropiedad componentePropiedad) {
		this.componentePropiedad = componentePropiedad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "componente_tipoid", nullable = false, insertable = false, updatable = false)
	public ComponenteTipo getComponenteTipo() {
		return this.componenteTipo;
	}

	public void setComponenteTipo(ComponenteTipo componenteTipo) {
		this.componenteTipo = componenteTipo;
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
