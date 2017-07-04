package pojo;
// Generated 4/07/2017 09:16:40 AM by Hibernate Tools 5.2.0.CR1

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
 * Colaborador generated by hbm2java
 */
@Entity
@Table(name = "colaborador", catalog = "sipro")
public class Colaborador implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3846156985157468691L;
	private Integer id;
	private UnidadEjecutora unidadEjecutora;
	private Usuario usuario;
	private String pnombre;
	private String snombre;
	private String papellido;
	private String sapellido;
	private Long cui;
	private int estado;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Set<Proyecto> proyectos = new HashSet<Proyecto>(0);
	private Set<ProyectoMiembro> proyectoMiembros = new HashSet<ProyectoMiembro>(0);
	private Set<Riesgo> riesgos = new HashSet<Riesgo>(0);

	public Colaborador() {
	}

	public Colaborador(UnidadEjecutora unidadEjecutora, Usuario usuario, String pnombre, String papellido, Long cui,
			int estado, String usuarioCreo, Date fechaCreacion) {
		this.unidadEjecutora = unidadEjecutora;
		this.usuario = usuario;
		this.pnombre = pnombre;
		this.papellido = papellido;
		this.cui = cui;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
	}

	public Colaborador(UnidadEjecutora unidadEjecutora, Usuario usuario, String pnombre, String snombre,
			String papellido, String sapellido, Long cui, int estado, String usuarioCreo, String usuarioActualizo,
			Date fechaCreacion, Date fechaActualizacion, Set<Proyecto> proyectos, Set<ProyectoMiembro> proyectoMiembros,
			Set<Riesgo> riesgos) {
		this.unidadEjecutora = unidadEjecutora;
		this.usuario = usuario;
		this.pnombre = pnombre;
		this.snombre = snombre;
		this.papellido = papellido;
		this.sapellido = sapellido;
		this.cui = cui;
		this.estado = estado;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.proyectos = proyectos;
		this.proyectoMiembros = proyectoMiembros;
		this.riesgos = riesgos;
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
	@JoinColumn(name = "unidad_ejecutoraunidad_ejecutora", nullable = false)
	public UnidadEjecutora getUnidadEjecutora() {
		return this.unidadEjecutora;
	}

	public void setUnidadEjecutora(UnidadEjecutora unidadEjecutora) {
		this.unidadEjecutora = unidadEjecutora;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuariousuario", nullable = false)
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "pnombre", nullable = false)
	public String getPnombre() {
		return this.pnombre;
	}

	public void setPnombre(String pnombre) {
		this.pnombre = pnombre;
	}

	@Column(name = "snombre")
	public String getSnombre() {
		return this.snombre;
	}

	public void setSnombre(String snombre) {
		this.snombre = snombre;
	}

	@Column(name = "papellido", nullable = false)
	public String getPapellido() {
		return this.papellido;
	}

	public void setPapellido(String papellido) {
		this.papellido = papellido;
	}

	@Column(name = "sapellido")
	public String getSapellido() {
		return this.sapellido;
	}

	public void setSapellido(String sapellido) {
		this.sapellido = sapellido;
	}

	@Column(name = "cui", nullable = false)
	public long getCui() {
		return this.cui;
	}

	public void setCui(long cui) {
		this.cui = cui;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "colaborador")
	public Set<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(Set<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "colaborador")
	public Set<ProyectoMiembro> getProyectoMiembros() {
		return this.proyectoMiembros;
	}

	public void setProyectoMiembros(Set<ProyectoMiembro> proyectoMiembros) {
		this.proyectoMiembros = proyectoMiembros;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "colaborador")
	public Set<Riesgo> getRiesgos() {
		return this.riesgos;
	}

	public void setRiesgos(Set<Riesgo> riesgos) {
		this.riesgos = riesgos;
	}

}
