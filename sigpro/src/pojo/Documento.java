package pojo;
// Generated May 15, 2017 4:04:46 PM by Hibernate Tools 5.2.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Documento generated by hbm2java
 */
@Entity
@Table(name = "documento", catalog = "sipro")
public class Documento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3045261203196833993L;
	private Integer id;
	private String nombre;
	private String extension;
	private int idTipoObjeto;
	private int idObjeto;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Integer estado;

	public Documento() {
	}

	public Documento(String nombre, String extension, int idTipoObjeto, int idObjeto) {
		this.nombre = nombre;
		this.extension = extension;
		this.idTipoObjeto = idTipoObjeto;
		this.idObjeto = idObjeto;
	}

	public Documento(String nombre, String extension, int idTipoObjeto, int idObjeto, String usuarioCreo,
			String usuarioActualizo, Date fechaCreacion, Date fechaActualizacion, Integer estado) {
		this.nombre = nombre;
		this.extension = extension;
		this.idTipoObjeto = idTipoObjeto;
		this.idObjeto = idObjeto;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
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

	@Column(name = "nombre", nullable = false, length = 1000)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "extension", nullable = false, length = 45)
	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Column(name = "id_tipo_objeto", nullable = false)
	public int getIdTipoObjeto() {
		return this.idTipoObjeto;
	}

	public void setIdTipoObjeto(int idTipoObjeto) {
		this.idTipoObjeto = idTipoObjeto;
	}

	@Column(name = "id_objeto", nullable = false)
	public int getIdObjeto() {
		return this.idObjeto;
	}

	public void setIdObjeto(int idObjeto) {
		this.idObjeto = idObjeto;
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
