package pojo;
// Generated Dec 12, 2017 12:13:39 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * PepDetalle generated by hbm2java
 */
@Entity
@Table(name = "pep_detalle", catalog = "sipro")
public class PepDetalle implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1505702568807349976L;
	private int proyectoid;
	private Proyecto proyecto;
	private String observaciones;
	private String alertivos;
	private String elaborado;
	private String aprobado;
	private String autoridad;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;

	public PepDetalle() {
	}

	public PepDetalle(Proyecto proyecto, String usuarioCreo, Date fechaCreacion, int estado) {
		this.proyecto = proyecto;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public PepDetalle(Proyecto proyecto, String observaciones, String alertivos, String elaborado, String aprobado,
			String autoridad, String usuarioCreo, String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion,
			int estado) {
		this.proyecto = proyecto;
		this.observaciones = observaciones;
		this.alertivos = alertivos;
		this.elaborado = elaborado;
		this.aprobado = aprobado;
		this.autoridad = autoridad;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "proyecto"))
	@Id
	@GeneratedValue(generator = "generator")

	@Column(name = "proyectoid", unique = true, nullable = false)
	public int getProyectoid() {
		return this.proyectoid;
	}

	public void setProyectoid(int proyectoid) {
		this.proyectoid = proyectoid;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	@Column(name = "observaciones", length = 4000)
	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Column(name = "alertivos", length = 4000)
	public String getAlertivos() {
		return this.alertivos;
	}

	public void setAlertivos(String alertivos) {
		this.alertivos = alertivos;
	}

	@Column(name = "elaborado", length = 100)
	public String getElaborado() {
		return this.elaborado;
	}

	public void setElaborado(String elaborado) {
		this.elaborado = elaborado;
	}

	@Column(name = "aprobado", length = 100)
	public String getAprobado() {
		return this.aprobado;
	}

	public void setAprobado(String aprobado) {
		this.aprobado = aprobado;
	}

	@Column(name = "autoridad", length = 100)
	public String getAutoridad() {
		return this.autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
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

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

}
