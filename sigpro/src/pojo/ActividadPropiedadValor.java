package pojo;
// Generated May 2, 2017 5:32:45 PM by Hibernate Tools 5.2.1.Final

import java.math.BigDecimal;
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
 * ActividadPropiedadValor generated by hbm2java
 */
@Entity
@Table(name = "actividad_propiedad_valor", catalog = "sipro")
public class ActividadPropiedadValor implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2516081280708538662L;
	private ActividadPropiedadValorId id;
	private Actividad actividad;
	private ActividadPropiedad actividadPropiedad;
	private Integer valorEntero;
	private String valorString;
	private BigDecimal valorDecimal;
	private Date valorTiempo;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Integer estado;

	public ActividadPropiedadValor() {
	}

	public ActividadPropiedadValor(ActividadPropiedadValorId id, Actividad actividad,
			ActividadPropiedad actividadPropiedad) {
		this.id = id;
		this.actividad = actividad;
		this.actividadPropiedad = actividadPropiedad;
	}

	public ActividadPropiedadValor(ActividadPropiedadValorId id, Actividad actividad,
			ActividadPropiedad actividadPropiedad, Integer valorEntero, String valorString, BigDecimal valorDecimal,
			Date valorTiempo, String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion,
			Integer estado) {
		this.id = id;
		this.actividad = actividad;
		this.actividadPropiedad = actividadPropiedad;
		this.valorEntero = valorEntero;
		this.valorString = valorString;
		this.valorDecimal = valorDecimal;
		this.valorTiempo = valorTiempo;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "actividadid", column = @Column(name = "actividadid", nullable = false)),
			@AttributeOverride(name = "actividadPropiedadid", column = @Column(name = "actividad_propiedadid", nullable = false)) })
	public ActividadPropiedadValorId getId() {
		return this.id;
	}

	public void setId(ActividadPropiedadValorId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actividadid", nullable = false, insertable = false, updatable = false)
	public Actividad getActividad() {
		return this.actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actividad_propiedadid", nullable = false, insertable = false, updatable = false)
	public ActividadPropiedad getActividadPropiedad() {
		return this.actividadPropiedad;
	}

	public void setActividadPropiedad(ActividadPropiedad actividadPropiedad) {
		this.actividadPropiedad = actividadPropiedad;
	}

	@Column(name = "valor_entero")
	public Integer getValorEntero() {
		return this.valorEntero;
	}

	public void setValorEntero(Integer valorEntero) {
		this.valorEntero = valorEntero;
	}

	@Column(name = "valor_string", length = 4000)
	public String getValorString() {
		return this.valorString;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	@Column(name = "valor_decimal", precision = 15)
	public BigDecimal getValorDecimal() {
		return this.valorDecimal;
	}

	public void setValorDecimal(BigDecimal valorDecimal) {
		this.valorDecimal = valorDecimal;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valor_tiempo", length = 19)
	public Date getValorTiempo() {
		return this.valorTiempo;
	}

	public void setValorTiempo(Date valorTiempo) {
		this.valorTiempo = valorTiempo;
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
