package pojo;
// Generated Sep 28, 2017 10:43:48 AM by Hibernate Tools 5.2.3.Final

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
 * MetaAvance generated by hbm2java
 */
@Entity
@Table(name = "meta_avance", catalog = "sipro")
public class MetaAvance implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4311127270017829639L;
	private MetaAvanceId id;
	private Meta meta;
	private String usuario;
	private Integer valorEntero;
	private String valorString;
	private BigDecimal valorDecimal;
	private Date valorTiempo;
	private int estado;
	private Date fechaIngreso;

	public MetaAvance() {
	}

	public MetaAvance(MetaAvanceId id, Meta meta, String usuario, int estado, Date fechaIngreso) {
		this.id = id;
		this.meta = meta;
		this.usuario = usuario;
		this.estado = estado;
		this.fechaIngreso = fechaIngreso;
	}

	public MetaAvance(MetaAvanceId id, Meta meta, String usuario, Integer valorEntero, String valorString,
			BigDecimal valorDecimal, Date valorTiempo, int estado, Date fechaIngreso) {
		this.id = id;
		this.meta = meta;
		this.usuario = usuario;
		this.valorEntero = valorEntero;
		this.valorString = valorString;
		this.valorDecimal = valorDecimal;
		this.valorTiempo = valorTiempo;
		this.estado = estado;
		this.fechaIngreso = fechaIngreso;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "metaid", column = @Column(name = "metaid", nullable = false)),
			@AttributeOverride(name = "fecha", column = @Column(name = "fecha", nullable = false, length = 19)) })
	public MetaAvanceId getId() {
		return this.id;
	}

	public void setId(MetaAvanceId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metaid", nullable = false, insertable = false, updatable = false)
	public Meta getMeta() {
		return this.meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@Column(name = "usuario", nullable = false, length = 30)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_ingreso", nullable = false, length = 19)
	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

}
