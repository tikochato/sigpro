package pojo;
// Generated Nov 9, 2017 5:32:35 PM by Hibernate Tools 5.2.3.Final

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
 * ObjetoRiesgo generated by hbm2java
 */
@Entity
@Table(name = "objeto_riesgo", catalog = "sipro")
public class ObjetoRiesgo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866307525417712003L;
	private ObjetoRiesgoId id;
	private Riesgo riesgo;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;

	public ObjetoRiesgo() {
	}

	public ObjetoRiesgo(ObjetoRiesgoId id, Riesgo riesgo) {
		this.id = id;
		this.riesgo = riesgo;
	}

	public ObjetoRiesgo(ObjetoRiesgoId id, Riesgo riesgo, String usuarioCreo, String usuarioActualizo,
			Date fechaCreacion, Date fechaActualizacion) {
		this.id = id;
		this.riesgo = riesgo;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "riesgoid", column = @Column(name = "riesgoid", nullable = false)),
			@AttributeOverride(name = "objetoId", column = @Column(name = "objeto_id", nullable = false)),
			@AttributeOverride(name = "objetoTipo", column = @Column(name = "objeto_tipo", nullable = false)) })
	public ObjetoRiesgoId getId() {
		return this.id;
	}

	public void setId(ObjetoRiesgoId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "riesgoid", nullable = false, insertable = false, updatable = false)
	public Riesgo getRiesgo() {
		return this.riesgo;
	}

	public void setRiesgo(Riesgo riesgo) {
		this.riesgo = riesgo;
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

}
