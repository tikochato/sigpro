package pojo;
// Generated Sep 19, 2017 6:41:06 AM by Hibernate Tools 5.2.3.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * MvEpPrestamo generated by hbm2java
 */
@Entity
@Table(name = "mv_ep_prestamo", catalog = "sipro")
public class MvEpPrestamo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -508159434711013885L;
	private MvEpPrestamoId id;

	public MvEpPrestamo() {
	}

	public MvEpPrestamo(MvEpPrestamoId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "ejercicio", column = @Column(name = "ejercicio")),
			@AttributeOverride(name = "fuente", column = @Column(name = "fuente")),
			@AttributeOverride(name = "organismo", column = @Column(name = "organismo")),
			@AttributeOverride(name = "correlativo", column = @Column(name = "correlativo")),
			@AttributeOverride(name = "enero", column = @Column(name = "enero", precision = 59)),
			@AttributeOverride(name = "febrero", column = @Column(name = "febrero", precision = 59)),
			@AttributeOverride(name = "marzo", column = @Column(name = "marzo", precision = 59)),
			@AttributeOverride(name = "abril", column = @Column(name = "abril", precision = 59)),
			@AttributeOverride(name = "mayo", column = @Column(name = "mayo", precision = 59)),
			@AttributeOverride(name = "junio", column = @Column(name = "junio", precision = 59)),
			@AttributeOverride(name = "julio", column = @Column(name = "julio", precision = 59)),
			@AttributeOverride(name = "agosto", column = @Column(name = "agosto", precision = 59)),
			@AttributeOverride(name = "septiembre", column = @Column(name = "septiembre", precision = 59)),
			@AttributeOverride(name = "octubre", column = @Column(name = "octubre", precision = 59)),
			@AttributeOverride(name = "noviembre", column = @Column(name = "noviembre", precision = 59)),
			@AttributeOverride(name = "diciembre", column = @Column(name = "diciembre", precision = 59)) })
	public MvEpPrestamoId getId() {
		return this.id;
	}

	public void setId(MvEpPrestamoId id) {
		this.id = id;
	}

}
