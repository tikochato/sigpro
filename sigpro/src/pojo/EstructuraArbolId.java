package pojo;
// Generated Sep 12, 2017 3:58:47 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EstructuraArbolId generated by hbm2java
 */
@Embeddable
public class EstructuraArbolId implements java.io.Serializable {

	private Integer prestamo;
	private Integer componente;
	private Integer producto;
	private Integer subproducto;
	private Integer actividad;
	private Long treelevel;
	private String treepath;
	private Date fechaInicio;
	private Date fechaFin;

	public EstructuraArbolId() {
	}

	public EstructuraArbolId(Integer prestamo, Integer componente, Integer producto, Integer subproducto,
			Integer actividad, Long treelevel, String treepath, Date fechaInicio, Date fechaFin) {
		this.prestamo = prestamo;
		this.componente = componente;
		this.producto = producto;
		this.subproducto = subproducto;
		this.actividad = actividad;
		this.treelevel = treelevel;
		this.treepath = treepath;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	@Column(name = "prestamo")
	public Integer getPrestamo() {
		return this.prestamo;
	}

	public void setPrestamo(Integer prestamo) {
		this.prestamo = prestamo;
	}

	@Column(name = "componente")
	public Integer getComponente() {
		return this.componente;
	}

	public void setComponente(Integer componente) {
		this.componente = componente;
	}

	@Column(name = "producto")
	public Integer getProducto() {
		return this.producto;
	}

	public void setProducto(Integer producto) {
		this.producto = producto;
	}

	@Column(name = "subproducto")
	public Integer getSubproducto() {
		return this.subproducto;
	}

	public void setSubproducto(Integer subproducto) {
		this.subproducto = subproducto;
	}

	@Column(name = "actividad")
	public Integer getActividad() {
		return this.actividad;
	}

	public void setActividad(Integer actividad) {
		this.actividad = actividad;
	}

	@Column(name = "treelevel")
	public Long getTreelevel() {
		return this.treelevel;
	}

	public void setTreelevel(Long treelevel) {
		this.treelevel = treelevel;
	}

	@Column(name = "treepath_", length = 511)
	public String getTreepath() {
		return this.treepath;
	}

	public void setTreepath(String treepath) {
		this.treepath = treepath;
	}

	@Column(name = "fecha_inicio", length = 19)
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Column(name = "fecha_fin", length = 19)
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EstructuraArbolId))
			return false;
		EstructuraArbolId castOther = (EstructuraArbolId) other;

		return ((this.getPrestamo() == castOther.getPrestamo()) || (this.getPrestamo() != null
				&& castOther.getPrestamo() != null && this.getPrestamo().equals(castOther.getPrestamo())))
				&& ((this.getComponente() == castOther.getComponente()) || (this.getComponente() != null
						&& castOther.getComponente() != null && this.getComponente().equals(castOther.getComponente())))
				&& ((this.getProducto() == castOther.getProducto()) || (this.getProducto() != null
						&& castOther.getProducto() != null && this.getProducto().equals(castOther.getProducto())))
				&& ((this.getSubproducto() == castOther.getSubproducto())
						|| (this.getSubproducto() != null && castOther.getSubproducto() != null
								&& this.getSubproducto().equals(castOther.getSubproducto())))
				&& ((this.getActividad() == castOther.getActividad()) || (this.getActividad() != null
						&& castOther.getActividad() != null && this.getActividad().equals(castOther.getActividad())))
				&& ((this.getTreelevel() == castOther.getTreelevel()) || (this.getTreelevel() != null
						&& castOther.getTreelevel() != null && this.getTreelevel().equals(castOther.getTreelevel())))
				&& ((this.getTreepath() == castOther.getTreepath()) || (this.getTreepath() != null
						&& castOther.getTreepath() != null && this.getTreepath().equals(castOther.getTreepath())))
				&& ((this.getFechaInicio() == castOther.getFechaInicio())
						|| (this.getFechaInicio() != null && castOther.getFechaInicio() != null
								&& this.getFechaInicio().equals(castOther.getFechaInicio())))
				&& ((this.getFechaFin() == castOther.getFechaFin()) || (this.getFechaFin() != null
						&& castOther.getFechaFin() != null && this.getFechaFin().equals(castOther.getFechaFin())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getPrestamo() == null ? 0 : this.getPrestamo().hashCode());
		result = 37 * result + (getComponente() == null ? 0 : this.getComponente().hashCode());
		result = 37 * result + (getProducto() == null ? 0 : this.getProducto().hashCode());
		result = 37 * result + (getSubproducto() == null ? 0 : this.getSubproducto().hashCode());
		result = 37 * result + (getActividad() == null ? 0 : this.getActividad().hashCode());
		result = 37 * result + (getTreelevel() == null ? 0 : this.getTreelevel().hashCode());
		result = 37 * result + (getTreepath() == null ? 0 : this.getTreepath().hashCode());
		result = 37 * result + (getFechaInicio() == null ? 0 : this.getFechaInicio().hashCode());
		result = 37 * result + (getFechaFin() == null ? 0 : this.getFechaFin().hashCode());
		return result;
	}

}
