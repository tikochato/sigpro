package pojo;
// Generated May 2, 2017 5:32:45 PM by Hibernate Tools 5.2.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ProductoPropiedadValorId generated by hbm2java
 */
@Embeddable
public class ProductoPropiedadValorId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4882144025786672539L;
	private int productoPropiedadid;
	private int productoid;

	public ProductoPropiedadValorId() {
	}

	public ProductoPropiedadValorId(int productoPropiedadid, int productoid) {
		this.productoPropiedadid = productoPropiedadid;
		this.productoid = productoid;
	}

	@Column(name = "producto_propiedadid", nullable = false)
	public int getProductoPropiedadid() {
		return this.productoPropiedadid;
	}

	public void setProductoPropiedadid(int productoPropiedadid) {
		this.productoPropiedadid = productoPropiedadid;
	}

	@Column(name = "productoid", nullable = false)
	public int getProductoid() {
		return this.productoid;
	}

	public void setProductoid(int productoid) {
		this.productoid = productoid;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ProductoPropiedadValorId))
			return false;
		ProductoPropiedadValorId castOther = (ProductoPropiedadValorId) other;

		return (this.getProductoPropiedadid() == castOther.getProductoPropiedadid())
				&& (this.getProductoid() == castOther.getProductoid());
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getProductoPropiedadid();
		result = 37 * result + this.getProductoid();
		return result;
	}

}
