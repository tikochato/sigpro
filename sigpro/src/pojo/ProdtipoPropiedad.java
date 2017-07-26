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
 * ProdtipoPropiedad generated by hbm2java
 */
@Entity
@Table(name = "prodtipo_propiedad", catalog = "sipro")
public class ProdtipoPropiedad implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1062279495911906317L;
	private ProdtipoPropiedadId id;
	private ProductoPropiedad productoPropiedad;
	private ProductoTipo productoTipo;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;

	public ProdtipoPropiedad() {
	}

	public ProdtipoPropiedad(ProdtipoPropiedadId id, ProductoPropiedad productoPropiedad, ProductoTipo productoTipo,
			String usuarioCreo, Date fechaCreacion) {
		this.id = id;
		this.productoPropiedad = productoPropiedad;
		this.productoTipo = productoTipo;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public ProdtipoPropiedad(ProdtipoPropiedadId id, ProductoPropiedad productoPropiedad, ProductoTipo productoTipo,
			String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion) {
		this.id = id;
		this.productoPropiedad = productoPropiedad;
		this.productoTipo = productoTipo;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "productoTipoid", column = @Column(name = "producto_tipoid", nullable = false)),
			@AttributeOverride(name = "productoPropiedadid", column = @Column(name = "producto_propiedadid", nullable = false)) })
	public ProdtipoPropiedadId getId() {
		return this.id;
	}

	public void setId(ProdtipoPropiedadId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_propiedadid", nullable = false, insertable = false, updatable = false)
	public ProductoPropiedad getProductoPropiedad() {
		return this.productoPropiedad;
	}

	public void setProductoPropiedad(ProductoPropiedad productoPropiedad) {
		this.productoPropiedad = productoPropiedad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_tipoid", nullable = false, insertable = false, updatable = false)
	public ProductoTipo getProductoTipo() {
		return this.productoTipo;
	}

	public void setProductoTipo(ProductoTipo productoTipo) {
		this.productoTipo = productoTipo;
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
