package pojo;
// Generated Jun 2, 2017 12:28:44 PM by Hibernate Tools 5.2.1.Final

import java.math.BigDecimal;
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
 * Prestamo generated by hbm2java
 */
@Entity
@Table(name = "prestamo", catalog = "sipro")
public class Prestamo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1280978758787657599L;
	private Integer id;
	private AutorizacionTipo autorizacionTipo;
	private Cooperante cooperante;
	private EjecucionEstado ejecucionEstado;
	private InteresTipo interesTipo;
	private TipoMoneda tipoMoneda;
	private UnidadEjecutora unidadEjecutora;
	private Date fechaCorte;
	private Long codigoPresupuestario;
	private String numeroPrestamo;
	private String destino;
	private String sectorEconomico;
	private Date fechaFirma;
	private String numeroAutorizacion;
	private Date fechaAutorizacion;
	private Integer aniosPlazo;
	private Integer aniosGracia;
	private Date fechaFinEjecucion;
	private Integer peridoEjecucion;
	private BigDecimal porcentajeInteres;
	private BigDecimal porcentajeComisionCompra;
	private BigDecimal montoContratado;
	private BigDecimal amortizado;
	private BigDecimal porAmortizar;
	private BigDecimal principalAnio;
	private BigDecimal interesesAnio;
	private BigDecimal comisionCompromisoAnio;
	private BigDecimal otrosGastos;
	private BigDecimal principalAcumulado;
	private BigDecimal interesesAcumulados;
	private BigDecimal comisionCompromisoAcumulado;
	private BigDecimal otrosCargosAcumulados;
	private BigDecimal presupuestoAsignadoFuncionamiento;
	private BigDecimal prespupuestoAsignadoInversion;
	private BigDecimal presupuestoModificadoFuncionamiento;
	private BigDecimal presupuestoModificadoInversion;
	private BigDecimal presupuestoVigenteFuncionamiento;
	private BigDecimal presupuestoVigenteInversion;
	private BigDecimal prespupuestoDevengadoFuncionamiento;
	private BigDecimal presupuestoDevengadoInversion;
	private BigDecimal presupuestoPagadoFuncionamiento;
	private BigDecimal presupuestoPagadoInversion;
	private BigDecimal saldoCuentas;
	private BigDecimal desembolsadoReal;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private String proyectoPrograma;
	private Date fechaDecreto;
	private Date fechaSuscripcion;
	private Date fechaElegibilidadUe;
	private Date fechaCierreOrigianlUe;
	private Date fechaCierreActualUe;
	private int mesesProrrogaUe;
	private Integer plazoEjecucionUe;
	private BigDecimal montoAsignadoUe;
	private BigDecimal desembolsoAFechaUe;
	private BigDecimal montoPorDesembolsarUe;
	private Date fechaVigencia;
	private BigDecimal montoContratadoUsd;
	private BigDecimal montoContratadoQtz;
	private BigDecimal desembolsoAFechaUsd;
	private BigDecimal montoPorDesembolsarUsd;
	private BigDecimal montoAsignadoUeUsd;
	private BigDecimal montoAsignadoUeQtz;
	private BigDecimal desembolsoAFechaUeUsd;
	private BigDecimal montoPorDesembolsarUeUsd;
	private Set<ObjetoPrestamo> objetoPrestamos = new HashSet<ObjetoPrestamo>(0);

	public Prestamo() {
	}

	public Prestamo(Cooperante cooperante, TipoMoneda tipoMoneda, UnidadEjecutora unidadEjecutora,
			Long codigoPresupuestario, String numeroPrestamo, BigDecimal montoContratado, String usuarioCreo, int estado,
			String proyectoPrograma, Date fechaDecreto, Date fechaSuscripcion, Date fechaElegibilidadUe,
			Date fechaCierreOrigianlUe, Date fechaCierreActualUe, int mesesProrrogaUe, BigDecimal montoAsignadoUe,
			Date fechaVigencia, BigDecimal montoContratadoUsd, BigDecimal montoContratadoQtz,
			BigDecimal desembolsoAFechaUsd, BigDecimal montoPorDesembolsarUsd, BigDecimal montoAsignadoUeUsd,
			BigDecimal montoAsignadoUeQtz, BigDecimal desembolsoAFechaUeUsd, BigDecimal montoPorDesembolsarUeUsd) {
		this.cooperante = cooperante;
		this.tipoMoneda = tipoMoneda;
		this.unidadEjecutora = unidadEjecutora;
		this.codigoPresupuestario = codigoPresupuestario;
		this.numeroPrestamo = numeroPrestamo;
		this.montoContratado = montoContratado;
		this.usuarioCreo = usuarioCreo;
		this.estado = estado;
		this.proyectoPrograma = proyectoPrograma;
		this.fechaDecreto = fechaDecreto;
		this.fechaSuscripcion = fechaSuscripcion;
		this.fechaElegibilidadUe = fechaElegibilidadUe;
		this.fechaCierreOrigianlUe = fechaCierreOrigianlUe;
		this.fechaCierreActualUe = fechaCierreActualUe;
		this.mesesProrrogaUe = mesesProrrogaUe;
		this.montoAsignadoUe = montoAsignadoUe;
		this.fechaVigencia = fechaVigencia;
		this.montoContratadoUsd = montoContratadoUsd;
		this.montoContratadoQtz = montoContratadoQtz;
		this.desembolsoAFechaUsd = desembolsoAFechaUsd;
		this.montoPorDesembolsarUsd = montoPorDesembolsarUsd;
		this.montoAsignadoUeUsd = montoAsignadoUeUsd;
		this.montoAsignadoUeQtz = montoAsignadoUeQtz;
		this.desembolsoAFechaUeUsd = desembolsoAFechaUeUsd;
		this.montoPorDesembolsarUeUsd = montoPorDesembolsarUeUsd;
	}

	public Prestamo(AutorizacionTipo autorizacionTipo, Cooperante cooperante, EjecucionEstado ejecucionEstado,
			InteresTipo interesTipo, TipoMoneda tipoMoneda, UnidadEjecutora unidadEjecutora, Date fechaCorte,
			Long codigoPresupuestario, String numeroPrestamo, String destino, String sectorEconomico, Date fechaFirma,
			String numeroAutorizacion, Date fechaAutorizacion, Integer aniosPlazo, Integer aniosGracia,
			Date fechaFinEjecucion, Integer peridoEjecucion, BigDecimal porcentajeInteres,
			BigDecimal porcentajeComisionCompra, BigDecimal montoContratado, BigDecimal amortizado,
			BigDecimal porAmortizar, BigDecimal principalAnio, BigDecimal interesesAnio,
			BigDecimal comisionCompromisoAnio, BigDecimal otrosGastos, BigDecimal principalAcumulado,
			BigDecimal interesesAcumulados, BigDecimal comisionCompromisoAcumulado, BigDecimal otrosCargosAcumulados,
			BigDecimal presupuestoAsignadoFuncionamiento, BigDecimal prespupuestoAsignadoInversion,
			BigDecimal presupuestoModificadoFuncionamiento, BigDecimal presupuestoModificadoInversion,
			BigDecimal presupuestoVigenteFuncionamiento, BigDecimal presupuestoVigenteInversion,
			BigDecimal prespupuestoDevengadoFuncionamiento, BigDecimal presupuestoDevengadoInversion,
			BigDecimal presupuestoPagadoFuncionamiento, BigDecimal presupuestoPagadoInversion, BigDecimal saldoCuentas,
			BigDecimal desembolsadoReal, String usuarioCreo, String usuarioActualizo, Date fechaCreacion,
			Date fechaActualizacion, int estado, String proyectoPrograma, Date fechaDecreto, Date fechaSuscripcion,
			Date fechaElegibilidadUe, Date fechaCierreOrigianlUe, Date fechaCierreActualUe, int mesesProrrogaUe,
			Integer plazoEjecucionUe, BigDecimal montoAsignadoUe, BigDecimal desembolsoAFechaUe,
			BigDecimal montoPorDesembolsarUe, Date fechaVigencia, BigDecimal montoContratadoUsd,
			BigDecimal montoContratadoQtz, BigDecimal desembolsoAFechaUsd, BigDecimal montoPorDesembolsarUsd,
			BigDecimal montoAsignadoUeUsd, BigDecimal montoAsignadoUeQtz, BigDecimal desembolsoAFechaUeUsd,
			BigDecimal montoPorDesembolsarUeUsd, Set<ObjetoPrestamo> objetoPrestamos) {
		this.autorizacionTipo = autorizacionTipo;
		this.cooperante = cooperante;
		this.ejecucionEstado = ejecucionEstado;
		this.interesTipo = interesTipo;
		this.tipoMoneda = tipoMoneda;
		this.unidadEjecutora = unidadEjecutora;
		this.fechaCorte = fechaCorte;
		this.codigoPresupuestario = codigoPresupuestario;
		this.numeroPrestamo = numeroPrestamo;
		this.destino = destino;
		this.sectorEconomico = sectorEconomico;
		this.fechaFirma = fechaFirma;
		this.numeroAutorizacion = numeroAutorizacion;
		this.fechaAutorizacion = fechaAutorizacion;
		this.aniosPlazo = aniosPlazo;
		this.aniosGracia = aniosGracia;
		this.fechaFinEjecucion = fechaFinEjecucion;
		this.peridoEjecucion = peridoEjecucion;
		this.porcentajeInteres = porcentajeInteres;
		this.porcentajeComisionCompra = porcentajeComisionCompra;
		this.montoContratado = montoContratado;
		this.amortizado = amortizado;
		this.porAmortizar = porAmortizar;
		this.principalAnio = principalAnio;
		this.interesesAnio = interesesAnio;
		this.comisionCompromisoAnio = comisionCompromisoAnio;
		this.otrosGastos = otrosGastos;
		this.principalAcumulado = principalAcumulado;
		this.interesesAcumulados = interesesAcumulados;
		this.comisionCompromisoAcumulado = comisionCompromisoAcumulado;
		this.otrosCargosAcumulados = otrosCargosAcumulados;
		this.presupuestoAsignadoFuncionamiento = presupuestoAsignadoFuncionamiento;
		this.prespupuestoAsignadoInversion = prespupuestoAsignadoInversion;
		this.presupuestoModificadoFuncionamiento = presupuestoModificadoFuncionamiento;
		this.presupuestoModificadoInversion = presupuestoModificadoInversion;
		this.presupuestoVigenteFuncionamiento = presupuestoVigenteFuncionamiento;
		this.presupuestoVigenteInversion = presupuestoVigenteInversion;
		this.prespupuestoDevengadoFuncionamiento = prespupuestoDevengadoFuncionamiento;
		this.presupuestoDevengadoInversion = presupuestoDevengadoInversion;
		this.presupuestoPagadoFuncionamiento = presupuestoPagadoFuncionamiento;
		this.presupuestoPagadoInversion = presupuestoPagadoInversion;
		this.saldoCuentas = saldoCuentas;
		this.desembolsadoReal = desembolsadoReal;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.proyectoPrograma = proyectoPrograma;
		this.fechaDecreto = fechaDecreto;
		this.fechaSuscripcion = fechaSuscripcion;
		this.fechaElegibilidadUe = fechaElegibilidadUe;
		this.fechaCierreOrigianlUe = fechaCierreOrigianlUe;
		this.fechaCierreActualUe = fechaCierreActualUe;
		this.mesesProrrogaUe = mesesProrrogaUe;
		this.plazoEjecucionUe = plazoEjecucionUe;
		this.montoAsignadoUe = montoAsignadoUe;
		this.desembolsoAFechaUe = desembolsoAFechaUe;
		this.montoPorDesembolsarUe = montoPorDesembolsarUe;
		this.fechaVigencia = fechaVigencia;
		this.montoContratadoUsd = montoContratadoUsd;
		this.montoContratadoQtz = montoContratadoQtz;
		this.desembolsoAFechaUsd = desembolsoAFechaUsd;
		this.montoPorDesembolsarUsd = montoPorDesembolsarUsd;
		this.montoAsignadoUeUsd = montoAsignadoUeUsd;
		this.montoAsignadoUeQtz = montoAsignadoUeQtz;
		this.desembolsoAFechaUeUsd = desembolsoAFechaUeUsd;
		this.montoPorDesembolsarUeUsd = montoPorDesembolsarUeUsd;
		this.objetoPrestamos = objetoPrestamos;
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
	@JoinColumn(name = "autorizacion_tipoid")
	public AutorizacionTipo getAutorizacionTipo() {
		return this.autorizacionTipo;
	}

	public void setAutorizacionTipo(AutorizacionTipo autorizacionTipo) {
		this.autorizacionTipo = autorizacionTipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cooperanteid", nullable = false)
	public Cooperante getCooperante() {
		return this.cooperante;
	}

	public void setCooperante(Cooperante cooperante) {
		this.cooperante = cooperante;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ejecucion_estadoid")
	public EjecucionEstado getEjecucionEstado() {
		return this.ejecucionEstado;
	}

	public void setEjecucionEstado(EjecucionEstado ejecucionEstado) {
		this.ejecucionEstado = ejecucionEstado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interes_tipoid")
	public InteresTipo getInteresTipo() {
		return this.interesTipo;
	}

	public void setInteresTipo(InteresTipo interesTipo) {
		this.interesTipo = interesTipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_monedaid", nullable = false)
	public TipoMoneda getTipoMoneda() {
		return this.tipoMoneda;
	}

	public void setTipoMoneda(TipoMoneda tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unidad_ejecutoraunidad_ejecutora", nullable = false)
	public UnidadEjecutora getUnidadEjecutora() {
		return this.unidadEjecutora;
	}

	public void setUnidadEjecutora(UnidadEjecutora unidadEjecutora) {
		this.unidadEjecutora = unidadEjecutora;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_corte", length = 19)
	public Date getFechaCorte() {
		return this.fechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	@Column(name = "codigo_presupuestario", nullable = false)
	public Long getCodigoPresupuestario() {
		return this.codigoPresupuestario;
	}

	public void setCodigoPresupuestario(Long codigoPresupuestario) {
		this.codigoPresupuestario = codigoPresupuestario;
	}

	@Column(name = "numero_prestamo", nullable = false, length = 30)
	public String getNumeroPrestamo() {
		return this.numeroPrestamo;
	}

	public void setNumeroPrestamo(String numeroPrestamo) {
		this.numeroPrestamo = numeroPrestamo;
	}

	@Column(name = "destino", length = 1000)
	public String getDestino() {
		return this.destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	@Column(name = "sector_economico", length = 1000)
	public String getSectorEconomico() {
		return this.sectorEconomico;
	}

	public void setSectorEconomico(String sectorEconomico) {
		this.sectorEconomico = sectorEconomico;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_firma", length = 19)
	public Date getFechaFirma() {
		return this.fechaFirma;
	}

	public void setFechaFirma(Date fechaFirma) {
		this.fechaFirma = fechaFirma;
	}

	@Column(name = "numero_autorizacion", length = 100)
	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_autorizacion", length = 19)
	public Date getFechaAutorizacion() {
		return this.fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	@Column(name = "anios_plazo")
	public Integer getAniosPlazo() {
		return this.aniosPlazo;
	}

	public void setAniosPlazo(Integer aniosPlazo) {
		this.aniosPlazo = aniosPlazo;
	}

	@Column(name = "anios_gracia")
	public Integer getAniosGracia() {
		return this.aniosGracia;
	}

	public void setAniosGracia(Integer aniosGracia) {
		this.aniosGracia = aniosGracia;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_fin_ejecucion", length = 19)
	public Date getFechaFinEjecucion() {
		return this.fechaFinEjecucion;
	}

	public void setFechaFinEjecucion(Date fechaFinEjecucion) {
		this.fechaFinEjecucion = fechaFinEjecucion;
	}

	@Column(name = "perido_ejecucion")
	public Integer getPeridoEjecucion() {
		return this.peridoEjecucion;
	}

	public void setPeridoEjecucion(Integer peridoEjecucion) {
		this.peridoEjecucion = peridoEjecucion;
	}

	@Column(name = "porcentaje_interes", precision = 10, scale = 5)
	public BigDecimal getPorcentajeInteres() {
		return this.porcentajeInteres;
	}

	public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
		this.porcentajeInteres = porcentajeInteres;
	}

	@Column(name = "porcentaje_comision_compra", precision = 10, scale = 5)
	public BigDecimal getPorcentajeComisionCompra() {
		return this.porcentajeComisionCompra;
	}

	public void setPorcentajeComisionCompra(BigDecimal porcentajeComisionCompra) {
		this.porcentajeComisionCompra = porcentajeComisionCompra;
	}

	@Column(name = "monto_contratado", nullable = false, precision = 15)
	public BigDecimal getMontoContratado() {
		return this.montoContratado;
	}

	public void setMontoContratado(BigDecimal montoContratado) {
		this.montoContratado = montoContratado;
	}

	@Column(name = "amortizado", precision = 15)
	public BigDecimal getAmortizado() {
		return this.amortizado;
	}

	public void setAmortizado(BigDecimal amortizado) {
		this.amortizado = amortizado;
	}

	@Column(name = "por_amortizar", precision = 15)
	public BigDecimal getPorAmortizar() {
		return this.porAmortizar;
	}

	public void setPorAmortizar(BigDecimal porAmortizar) {
		this.porAmortizar = porAmortizar;
	}

	@Column(name = "principal_anio", precision = 15)
	public BigDecimal getPrincipalAnio() {
		return this.principalAnio;
	}

	public void setPrincipalAnio(BigDecimal principalAnio) {
		this.principalAnio = principalAnio;
	}

	@Column(name = "intereses_anio", precision = 15)
	public BigDecimal getInteresesAnio() {
		return this.interesesAnio;
	}

	public void setInteresesAnio(BigDecimal interesesAnio) {
		this.interesesAnio = interesesAnio;
	}

	@Column(name = "comision_compromiso_anio", precision = 15)
	public BigDecimal getComisionCompromisoAnio() {
		return this.comisionCompromisoAnio;
	}

	public void setComisionCompromisoAnio(BigDecimal comisionCompromisoAnio) {
		this.comisionCompromisoAnio = comisionCompromisoAnio;
	}

	@Column(name = "otros_gastos", precision = 15)
	public BigDecimal getOtrosGastos() {
		return this.otrosGastos;
	}

	public void setOtrosGastos(BigDecimal otrosGastos) {
		this.otrosGastos = otrosGastos;
	}

	@Column(name = "principal_acumulado", precision = 15)
	public BigDecimal getPrincipalAcumulado() {
		return this.principalAcumulado;
	}

	public void setPrincipalAcumulado(BigDecimal principalAcumulado) {
		this.principalAcumulado = principalAcumulado;
	}

	@Column(name = "intereses_acumulados", precision = 15)
	public BigDecimal getInteresesAcumulados() {
		return this.interesesAcumulados;
	}

	public void setInteresesAcumulados(BigDecimal interesesAcumulados) {
		this.interesesAcumulados = interesesAcumulados;
	}

	@Column(name = "comision_compromiso_acumulado", precision = 15)
	public BigDecimal getComisionCompromisoAcumulado() {
		return this.comisionCompromisoAcumulado;
	}

	public void setComisionCompromisoAcumulado(BigDecimal comisionCompromisoAcumulado) {
		this.comisionCompromisoAcumulado = comisionCompromisoAcumulado;
	}

	@Column(name = "otros_cargos_acumulados", precision = 15)
	public BigDecimal getOtrosCargosAcumulados() {
		return this.otrosCargosAcumulados;
	}

	public void setOtrosCargosAcumulados(BigDecimal otrosCargosAcumulados) {
		this.otrosCargosAcumulados = otrosCargosAcumulados;
	}

	@Column(name = "presupuesto_asignado_funcionamiento", precision = 15)
	public BigDecimal getPresupuestoAsignadoFuncionamiento() {
		return this.presupuestoAsignadoFuncionamiento;
	}

	public void setPresupuestoAsignadoFuncionamiento(BigDecimal presupuestoAsignadoFuncionamiento) {
		this.presupuestoAsignadoFuncionamiento = presupuestoAsignadoFuncionamiento;
	}

	@Column(name = "prespupuesto_asignado_inversion", precision = 15)
	public BigDecimal getPrespupuestoAsignadoInversion() {
		return this.prespupuestoAsignadoInversion;
	}

	public void setPrespupuestoAsignadoInversion(BigDecimal prespupuestoAsignadoInversion) {
		this.prespupuestoAsignadoInversion = prespupuestoAsignadoInversion;
	}

	@Column(name = "presupuesto_modificado_funcionamiento", precision = 15)
	public BigDecimal getPresupuestoModificadoFuncionamiento() {
		return this.presupuestoModificadoFuncionamiento;
	}

	public void setPresupuestoModificadoFuncionamiento(BigDecimal presupuestoModificadoFuncionamiento) {
		this.presupuestoModificadoFuncionamiento = presupuestoModificadoFuncionamiento;
	}

	@Column(name = "presupuesto_modificado_inversion", precision = 15)
	public BigDecimal getPresupuestoModificadoInversion() {
		return this.presupuestoModificadoInversion;
	}

	public void setPresupuestoModificadoInversion(BigDecimal presupuestoModificadoInversion) {
		this.presupuestoModificadoInversion = presupuestoModificadoInversion;
	}

	@Column(name = "presupuesto_vigente_funcionamiento", precision = 15)
	public BigDecimal getPresupuestoVigenteFuncionamiento() {
		return this.presupuestoVigenteFuncionamiento;
	}

	public void setPresupuestoVigenteFuncionamiento(BigDecimal presupuestoVigenteFuncionamiento) {
		this.presupuestoVigenteFuncionamiento = presupuestoVigenteFuncionamiento;
	}

	@Column(name = "presupuesto_vigente_inversion", precision = 15)
	public BigDecimal getPresupuestoVigenteInversion() {
		return this.presupuestoVigenteInversion;
	}

	public void setPresupuestoVigenteInversion(BigDecimal presupuestoVigenteInversion) {
		this.presupuestoVigenteInversion = presupuestoVigenteInversion;
	}

	@Column(name = "prespupuesto_devengado_funcionamiento", precision = 15)
	public BigDecimal getPrespupuestoDevengadoFuncionamiento() {
		return this.prespupuestoDevengadoFuncionamiento;
	}

	public void setPrespupuestoDevengadoFuncionamiento(BigDecimal prespupuestoDevengadoFuncionamiento) {
		this.prespupuestoDevengadoFuncionamiento = prespupuestoDevengadoFuncionamiento;
	}

	@Column(name = "presupuesto_devengado_inversion", precision = 15)
	public BigDecimal getPresupuestoDevengadoInversion() {
		return this.presupuestoDevengadoInversion;
	}

	public void setPresupuestoDevengadoInversion(BigDecimal presupuestoDevengadoInversion) {
		this.presupuestoDevengadoInversion = presupuestoDevengadoInversion;
	}

	@Column(name = "presupuesto_pagado_funcionamiento", precision = 15)
	public BigDecimal getPresupuestoPagadoFuncionamiento() {
		return this.presupuestoPagadoFuncionamiento;
	}

	public void setPresupuestoPagadoFuncionamiento(BigDecimal presupuestoPagadoFuncionamiento) {
		this.presupuestoPagadoFuncionamiento = presupuestoPagadoFuncionamiento;
	}

	@Column(name = "presupuesto_pagado_inversion", precision = 15)
	public BigDecimal getPresupuestoPagadoInversion() {
		return this.presupuestoPagadoInversion;
	}

	public void setPresupuestoPagadoInversion(BigDecimal presupuestoPagadoInversion) {
		this.presupuestoPagadoInversion = presupuestoPagadoInversion;
	}

	@Column(name = "saldo_cuentas", precision = 15)
	public BigDecimal getSaldoCuentas() {
		return this.saldoCuentas;
	}

	public void setSaldoCuentas(BigDecimal saldoCuentas) {
		this.saldoCuentas = saldoCuentas;
	}

	@Column(name = "desembolsado_real", precision = 15)
	public BigDecimal getDesembolsadoReal() {
		return this.desembolsadoReal;
	}

	public void setDesembolsadoReal(BigDecimal desembolsadoReal) {
		this.desembolsadoReal = desembolsadoReal;
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

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Column(name = "proyecto_programa", nullable = false, length = 100)
	public String getProyectoPrograma() {
		return this.proyectoPrograma;
	}

	public void setProyectoPrograma(String proyectoPrograma) {
		this.proyectoPrograma = proyectoPrograma;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_decreto", nullable = false, length = 19)
	public Date getFechaDecreto() {
		return this.fechaDecreto;
	}

	public void setFechaDecreto(Date fechaDecreto) {
		this.fechaDecreto = fechaDecreto;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_suscripcion", nullable = false, length = 19)
	public Date getFechaSuscripcion() {
		return this.fechaSuscripcion;
	}

	public void setFechaSuscripcion(Date fechaSuscripcion) {
		this.fechaSuscripcion = fechaSuscripcion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_elegibilidad_ue", nullable = false, length = 19)
	public Date getFechaElegibilidadUe() {
		return this.fechaElegibilidadUe;
	}

	public void setFechaElegibilidadUe(Date fechaElegibilidadUe) {
		this.fechaElegibilidadUe = fechaElegibilidadUe;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_cierre_origianl_ue", nullable = false, length = 19)
	public Date getFechaCierreOrigianlUe() {
		return this.fechaCierreOrigianlUe;
	}

	public void setFechaCierreOrigianlUe(Date fechaCierreOrigianlUe) {
		this.fechaCierreOrigianlUe = fechaCierreOrigianlUe;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_cierre_actual_ue", nullable = false, length = 19)
	public Date getFechaCierreActualUe() {
		return this.fechaCierreActualUe;
	}

	public void setFechaCierreActualUe(Date fechaCierreActualUe) {
		this.fechaCierreActualUe = fechaCierreActualUe;
	}

	@Column(name = "meses_prorroga_ue", nullable = false)
	public int getMesesProrrogaUe() {
		return this.mesesProrrogaUe;
	}

	public void setMesesProrrogaUe(int mesesProrrogaUe) {
		this.mesesProrrogaUe = mesesProrrogaUe;
	}

	@Column(name = "plazo_ejecucion_ue")
	public Integer getPlazoEjecucionUe() {
		return this.plazoEjecucionUe;
	}

	public void setPlazoEjecucionUe(Integer plazoEjecucionUe) {
		this.plazoEjecucionUe = plazoEjecucionUe;
	}

	@Column(name = "monto_asignado_ue", nullable = false, precision = 15)
	public BigDecimal getMontoAsignadoUe() {
		return this.montoAsignadoUe;
	}

	public void setMontoAsignadoUe(BigDecimal montoAsignadoUe) {
		this.montoAsignadoUe = montoAsignadoUe;
	}

	@Column(name = "desembolso_a_fecha_ue", precision = 15)
	public BigDecimal getDesembolsoAFechaUe() {
		return this.desembolsoAFechaUe;
	}

	public void setDesembolsoAFechaUe(BigDecimal desembolsoAFechaUe) {
		this.desembolsoAFechaUe = desembolsoAFechaUe;
	}

	@Column(name = "monto_por_desembolsar_ue", precision = 15)
	public BigDecimal getMontoPorDesembolsarUe() {
		return this.montoPorDesembolsarUe;
	}

	public void setMontoPorDesembolsarUe(BigDecimal montoPorDesembolsarUe) {
		this.montoPorDesembolsarUe = montoPorDesembolsarUe;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_vigencia", nullable = false, length = 19)
	public Date getFechaVigencia() {
		return this.fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	@Column(name = "monto_contratado_usd", nullable = false, precision = 15)
	public BigDecimal getMontoContratadoUsd() {
		return this.montoContratadoUsd;
	}

	public void setMontoContratadoUsd(BigDecimal montoContratadoUsd) {
		this.montoContratadoUsd = montoContratadoUsd;
	}

	@Column(name = "monto_contratado_qtz", nullable = false, precision = 15)
	public BigDecimal getMontoContratadoQtz() {
		return this.montoContratadoQtz;
	}

	public void setMontoContratadoQtz(BigDecimal montoContratadoQtz) {
		this.montoContratadoQtz = montoContratadoQtz;
	}

	@Column(name = "desembolso_a_fecha_usd", nullable = false, precision = 15)
	public BigDecimal getDesembolsoAFechaUsd() {
		return this.desembolsoAFechaUsd;
	}

	public void setDesembolsoAFechaUsd(BigDecimal desembolsoAFechaUsd) {
		this.desembolsoAFechaUsd = desembolsoAFechaUsd;
	}

	@Column(name = "monto_por_desembolsar_usd", nullable = false, precision = 15)
	public BigDecimal getMontoPorDesembolsarUsd() {
		return this.montoPorDesembolsarUsd;
	}

	public void setMontoPorDesembolsarUsd(BigDecimal montoPorDesembolsarUsd) {
		this.montoPorDesembolsarUsd = montoPorDesembolsarUsd;
	}

	@Column(name = "monto_asignado_ue_usd", nullable = false, precision = 15)
	public BigDecimal getMontoAsignadoUeUsd() {
		return this.montoAsignadoUeUsd;
	}

	public void setMontoAsignadoUeUsd(BigDecimal montoAsignadoUeUsd) {
		this.montoAsignadoUeUsd = montoAsignadoUeUsd;
	}

	@Column(name = "monto_asignado_ue_qtz", nullable = false, precision = 15)
	public BigDecimal getMontoAsignadoUeQtz() {
		return this.montoAsignadoUeQtz;
	}

	public void setMontoAsignadoUeQtz(BigDecimal montoAsignadoUeQtz) {
		this.montoAsignadoUeQtz = montoAsignadoUeQtz;
	}

	@Column(name = "desembolso_a_fecha_ue_usd", nullable = false, precision = 15)
	public BigDecimal getDesembolsoAFechaUeUsd() {
		return this.desembolsoAFechaUeUsd;
	}

	public void setDesembolsoAFechaUeUsd(BigDecimal desembolsoAFechaUeUsd) {
		this.desembolsoAFechaUeUsd = desembolsoAFechaUeUsd;
	}

	@Column(name = "monto_por_desembolsar_ue_usd", nullable = false, precision = 15)
	public BigDecimal getMontoPorDesembolsarUeUsd() {
		return this.montoPorDesembolsarUeUsd;
	}

	public void setMontoPorDesembolsarUeUsd(BigDecimal montoPorDesembolsarUeUsd) {
		this.montoPorDesembolsarUeUsd = montoPorDesembolsarUeUsd;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prestamo")
	public Set<ObjetoPrestamo> getObjetoPrestamos() {
		return this.objetoPrestamos;
	}

	public void setObjetoPrestamos(Set<ObjetoPrestamo> objetoPrestamos) {
		this.objetoPrestamos = objetoPrestamos;
	}

}
