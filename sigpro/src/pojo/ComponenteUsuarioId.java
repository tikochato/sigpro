package pojo;
// Generated Nov 2, 2017 8:47:48 AM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ComponenteUsuarioId generated by hbm2java
 */
@Embeddable
public class ComponenteUsuarioId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -297937538350198936L;
	private int componenteid;
	private String usuario;

	public ComponenteUsuarioId() {
	}

	public ComponenteUsuarioId(int componenteid, String usuario) {
		this.componenteid = componenteid;
		this.usuario = usuario;
	}

	@Column(name = "componenteid", nullable = false)
	public int getComponenteid() {
		return this.componenteid;
	}

	public void setComponenteid(int componenteid) {
		this.componenteid = componenteid;
	}

	@Column(name = "usuario", nullable = false, length = 30)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ComponenteUsuarioId))
			return false;
		ComponenteUsuarioId castOther = (ComponenteUsuarioId) other;

		return (this.getComponenteid() == castOther.getComponenteid())
				&& ((this.getUsuario() == castOther.getUsuario()) || (this.getUsuario() != null
						&& castOther.getUsuario() != null && this.getUsuario().equals(castOther.getUsuario())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getComponenteid();
		result = 37 * result + (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		return result;
	}

}
