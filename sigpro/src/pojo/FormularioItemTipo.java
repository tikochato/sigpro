package pojo;
// Generated Aug 28, 2017 4:46:57 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * FormularioItemTipo generated by hbm2java
 */
@Entity
@Table(name = "formulario_item_tipo", catalog = "sipro")
public class FormularioItemTipo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4259636783217482318L;
	private Integer id;
	private DatoTipo datoTipo;
	private String nombre;
	private String descripcion;
	private Integer estado;
	private String usuarioCreo;
	private Date fechaCreacion;
	private String usuarioActualizacion;
	private Date fechaActualizacion;
	private Set<FormularioItem> formularioItems = new HashSet<FormularioItem>(0);

	public FormularioItemTipo() {
	}

	public FormularioItemTipo(DatoTipo datoTipo, String nombre, String usuarioCreo, Date fechaCreacion) {
		this.datoTipo = datoTipo;
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public FormularioItemTipo(DatoTipo datoTipo, String nombre, String descripcion, Integer estado, String usuarioCreo,
			Date fechaCreacion, String usuarioActualizacion, Date fechaActualizacion,
			Set<FormularioItem> formularioItems) {
		this.datoTipo = datoTipo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.usuarioActualizacion = usuarioActualizacion;
		this.fechaActualizacion = fechaActualizacion;
		this.formularioItems = formularioItems;
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
	@JoinColumn(name = "dato_tipoid", nullable = false)
	public DatoTipo getDatoTipo() {
		return this.datoTipo;
	}

	public void setDatoTipo(DatoTipo datoTipo) {
		this.datoTipo = datoTipo;
	}

	@Column(name = "nombre", nullable = false, length = 1000)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", length = 4000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "estado")
	public Integer getEstado() {
		return this.estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	@Column(name = "usuario_creo", nullable = false, length = 30)
	public String getUsuarioCreo() {
		return this.usuarioCreo;
	}

	public void setUsuarioCreo(String usuarioCreo) {
		this.usuarioCreo = usuarioCreo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", nullable = false, length = 19)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "usuario_actualizacion", length = 30)
	public String getUsuarioActualizacion() {
		return this.usuarioActualizacion;
	}

	public void setUsuarioActualizacion(String usuarioActualizacion) {
		this.usuarioActualizacion = usuarioActualizacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_actualizacion", length = 19)
	public Date getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "formularioItemTipo")
	public Set<FormularioItem> getFormularioItems() {
		return this.formularioItems;
	}

	public void setFormularioItems(Set<FormularioItem> formularioItems) {
		this.formularioItems = formularioItems;
	}

}
