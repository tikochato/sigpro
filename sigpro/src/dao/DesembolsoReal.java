package dao;

import java.math.BigDecimal;

public class DesembolsoReal {

	Integer ejercicioFiscal;
	Integer mes;
	long codigoPresupuestario;
	Integer entidad_sicoin;
	Integer unidadEjecutora_sicoin;
	String monedaDesembolso;
	BigDecimal desembolsosMesMoneda;
	BigDecimal tc_mon_usd;
	BigDecimal desembolsosMesUSD;
	BigDecimal tc_usd_gtq;
	BigDecimal desembolsosMesGTQ;
	
	
	public DesembolsoReal(Integer ejercicioFiscal, Integer mes, long codigoPresupuestario, Integer entidad_sicoin,
			Integer unidadEjecutora_sicoin, String monedaDesembolso, BigDecimal desembolsosMesMoneda,
			BigDecimal tc_mon_usd, BigDecimal desembolsosMesUSD, BigDecimal tc_usd_gtq, BigDecimal desembolsosMesGTQ) {
		super();
		this.ejercicioFiscal = ejercicioFiscal;
		this.mes = mes;
		this.codigoPresupuestario = codigoPresupuestario;
		this.entidad_sicoin = entidad_sicoin;
		this.unidadEjecutora_sicoin = unidadEjecutora_sicoin;
		this.monedaDesembolso = monedaDesembolso;
		this.desembolsosMesMoneda = desembolsosMesMoneda;
		this.tc_mon_usd = tc_mon_usd;
		this.desembolsosMesUSD = desembolsosMesUSD;
		this.tc_usd_gtq = tc_usd_gtq;
		this.desembolsosMesGTQ = desembolsosMesGTQ;
	}
	
	public Integer getEjercicioFiscal() {
		return ejercicioFiscal;
	}
	public void setEjercicioFiscal(Integer ejercicioFiscal) {
		this.ejercicioFiscal = ejercicioFiscal;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public long getCodigoPresupuestario() {
		return codigoPresupuestario;
	}
	public void setCodigoPresupuestario(long codigoPresupuestario) {
		this.codigoPresupuestario = codigoPresupuestario;
	}
	public Integer getEntidad_sicoin() {
		return entidad_sicoin;
	}
	public void setEntidad_sicoin(Integer entidad_sicoin) {
		this.entidad_sicoin = entidad_sicoin;
	}
	public Integer getUnidadEjecutora_sicoin() {
		return unidadEjecutora_sicoin;
	}
	public void setUnidadEjecutora_sicoin(Integer unidadEjecutora_sicoin) {
		this.unidadEjecutora_sicoin = unidadEjecutora_sicoin;
	}
	public String getMonedaDesembolso() {
		return monedaDesembolso;
	}
	public void setMonedaDesembolso(String monedaDesembolso) {
		this.monedaDesembolso = monedaDesembolso;
	}
	public BigDecimal getDesembolsosMesMoneda() {
		return desembolsosMesMoneda;
	}
	public void setDesembolsosMesMoneda(BigDecimal desembolsosMesMoneda) {
		this.desembolsosMesMoneda = desembolsosMesMoneda;
	}
	public BigDecimal getTc_mon_usd() {
		return tc_mon_usd;
	}
	public void setTc_mon_usd(BigDecimal tc_mon_usd) {
		this.tc_mon_usd = tc_mon_usd;
	}
	public BigDecimal getDesembolsosMesUSD() {
		return desembolsosMesUSD;
	}
	public void setDesembolsosMesUSD(BigDecimal desembolsosMesUSD) {
		this.desembolsosMesUSD = desembolsosMesUSD;
	}
	public BigDecimal getTc_usd_gtq() {
		return tc_usd_gtq;
	}
	public void setTc_usd_gtq(BigDecimal tc_usd_gtq) {
		this.tc_usd_gtq = tc_usd_gtq;
	}
	public BigDecimal getDesembolsosMesGTQ() {
		return desembolsosMesGTQ;
	}
	public void setDesembolsosMesGTQ(BigDecimal desembolsosMesGTQ) {
		this.desembolsosMesGTQ = desembolsosMesGTQ;
	}
	
}
