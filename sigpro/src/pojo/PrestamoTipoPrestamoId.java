package pojo;
// Generated Nov 12, 2017 1:30:38 AM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PrestamoTipoPrestamoId generated by hbm2java
 */
@Embeddable
public class PrestamoTipoPrestamoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7536640975880993796L;
	private int prestamoId;
	private int tipoPrestamoId;

	public PrestamoTipoPrestamoId() {
	}

	public PrestamoTipoPrestamoId(int prestamoId, int tipoPrestamoId) {
		this.prestamoId = prestamoId;
		this.tipoPrestamoId = tipoPrestamoId;
	}

	@Column(name = "prestamoId", nullable = false)
	public int getPrestamoId() {
		return this.prestamoId;
	}

	public void setPrestamoId(int prestamoId) {
		this.prestamoId = prestamoId;
	}

	@Column(name = "tipoPrestamoId", nullable = false)
	public int getTipoPrestamoId() {
		return this.tipoPrestamoId;
	}

	public void setTipoPrestamoId(int tipoPrestamoId) {
		this.tipoPrestamoId = tipoPrestamoId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PrestamoTipoPrestamoId))
			return false;
		PrestamoTipoPrestamoId castOther = (PrestamoTipoPrestamoId) other;

		return (this.getPrestamoId() == castOther.getPrestamoId())
				&& (this.getTipoPrestamoId() == castOther.getTipoPrestamoId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPrestamoId();
		result = 37 * result + this.getTipoPrestamoId();
		return result;
	}

}
