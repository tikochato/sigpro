package pojo;
// Generated Sep 19, 2017 6:41:06 AM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
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
 * FormularioItemOpcion generated by hbm2java
 */
@Entity
@Table(name = "formulario_item_opcion", catalog = "sipro")
public class FormularioItemOpcion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 142202333285042255L;
	private Integer id;
	private FormularioItem formularioItem;
	private String etiqueta;
	private Integer valorEntero;
	private String valorString;
	private Date valorTiempo;
	private BigDecimal valorDecimal;
	private int estado;
	private String usuarioCreo;
	private Integer usuarioActualizacion;
	private Date fechaCreacion;
	private Date fechaActualizacion;

	public FormularioItemOpcion() {
	}

	public FormularioItemOpcion(FormularioItem formularioItem, String etiqueta, int estado, String usuarioCreo,
			Date fechaCreacion) {
		this.formularioItem = formularioItem;
		this.etiqueta = etiqueta;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public FormularioItemOpcion(FormularioItem formularioItem, String etiqueta, Integer valorEntero, String valorString,
			Date valorTiempo, BigDecimal valorDecimal, int estado, String usuarioCreo, Integer usuarioActualizacion,
			Date fechaCreacion, Date fechaActualizacion) {
		this.formularioItem = formularioItem;
		this.etiqueta = etiqueta;
		this.valorEntero = valorEntero;
		this.valorString = valorString;
		this.valorTiempo = valorTiempo;
		this.valorDecimal = valorDecimal;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizacion = usuarioActualizacion;
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
	@JoinColumn(name = "formulario_itemid", nullable = false)
	public FormularioItem getFormularioItem() {
		return this.formularioItem;
	}

	public void setFormularioItem(FormularioItem formularioItem) {
		this.formularioItem = formularioItem;
	}

	@Column(name = "etiqueta", nullable = false, length = 4000)
	public String getEtiqueta() {
		return this.etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valor_tiempo", length = 19)
	public Date getValorTiempo() {
		return this.valorTiempo;
	}

	public void setValorTiempo(Date valorTiempo) {
		this.valorTiempo = valorTiempo;
	}

	@Column(name = "valor_decimal", precision = 15)
	public BigDecimal getValorDecimal() {
		return this.valorDecimal;
	}

	public void setValorDecimal(BigDecimal valorDecimal) {
		this.valorDecimal = valorDecimal;
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

	@Column(name = "usuario_actualizacion")
	public Integer getUsuarioActualizacion() {
		return this.usuarioActualizacion;
	}

	public void setUsuarioActualizacion(Integer usuarioActualizacion) {
		this.usuarioActualizacion = usuarioActualizacion;
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
