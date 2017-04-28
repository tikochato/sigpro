package pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;

import javax.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;


@Entity
@Table(name = "documento", catalog = "sipro")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Documento implements java.io.Serializable {

	private static final long serialVersionUID = -4792005424725185513L;
	private Integer id;
	private String nombre;
	private String descripcion;
	private String extension;
	private Integer idObjeto;
	private Integer idTipoObjeto;

	public Documento() {
	}

	public Documento(String nombre, String descripcion, String extension, Integer idTipoObjeto, Integer idObjeto) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.extension = extension;
		this.idTipoObjeto = idTipoObjeto;
		this.idObjeto = idObjeto;
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

	@Column(name = "nombre", length = 45)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", length = 45)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "extension", length = 45)
	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	@Column(name = "id_tipo_objeto")
	public Integer getIdTipoObjeto() {
		return this.idTipoObjeto;
	}

	public void setIdTipoObjeto(Integer idTipoObjeto) {
		this.idTipoObjeto = idTipoObjeto;
	}

	@Column(name = "id_objeto")
	public Integer getIdObjeto() {
		return this.idObjeto;
	}

	public void setIdObjeto(Integer idObjeto) {
		this.idObjeto = idObjeto;
	}

}