package dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import pojo.Componente;
import pojo.ComponenteSigade;
import pojo.Meta;
import pojo.MetaPlanificado;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CJasperReport;

public class PlanEjecucionDAO {

	public PlanEjecucionDAO(){

	}

	public static double calcularEjecucionFisicaReal(Prestamo prestamo){
		Set<Proyecto> proyectos = prestamo.getProyectos();
		List<Double> pesos = new ArrayList<Double>();
		List<Double> techos = new ArrayList<Double>();

		if(proyectos != null && proyectos.size() > 0){	
			Iterator<Proyecto> iterador = proyectos.iterator();
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();
				double peso = Math.round(proyecto.getEjecucionFisicaReal()/100.0f);
				pesos.add(peso);

				Set<Componente> componentes = proyecto.getComponentes();
				if(componentes != null && componentes.size() > 0){
					Iterator<Componente> iteradorC = componentes.iterator();
					BigDecimal techoTotal = new BigDecimal(0);
					BigDecimal techoSigade = new BigDecimal(0);
					while(iteradorC.hasNext()){
						Componente componente = iteradorC.next();
						if(componente.getEsDeSigade() == 1){
							techoTotal = techoTotal.add(componente.getFuentePrestamo().add(componente.getFuenteNacional().add(componente.getFuenteDonacion())));
							ComponenteSigade cSigade = ComponenteSigadeDAO.getComponenteSigadePorId(componente.getComponenteSigade().getId());
							techoSigade = techoSigade.add(cSigade.getMontoComponente());
						}
					}
					techos.add(techoTotal.doubleValue()/techoSigade.doubleValue());
				}
			}
		}

		//Calcular media ponderada;
		double numerador = 0;
		double denominador = 0;
		for(int i = 0; i < pesos.size(); i++){
			numerador += techos.get(i) * pesos.get(i);
			denominador += pesos.get(i); 
		}

		double ret = 0;
		if(denominador != 0){
			ret = numerador/denominador;
		}

		return ret;
	}

	public static BigDecimal calcularEjecucionFinanciaeraPlanificada(Prestamo prestamo, String codigoPresupuestario,Date fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));
		BigDecimal totalDesembolsosReales = 
				DataSigadeDAO.totalDesembolsadoAFechaReal(codigoPresupuestario,new Long( anio), mes);

		BigDecimal totalDesembolsosPlanificados = new BigDecimal(0);
		Set<Proyecto> proyectos = prestamo.getProyectos();
		if(proyectos != null && proyectos.size() > 0){	
			Iterator<Proyecto> iterador = proyectos.iterator();
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();
				BigDecimal desembolsoProyecto = DesembolsoDAO.getTotalDesembolsosFuturos(proyecto.getId(), fecha);
				totalDesembolsosPlanificados = totalDesembolsosPlanificados.add(desembolsoProyecto != null ? desembolsoProyecto : new BigDecimal(0));
			}
		}
		BigDecimal  ejecucionFinanciera = new BigDecimal("0");
		if (totalDesembolsosReales!= null && totalDesembolsosPlanificados != null){
			BigDecimal total = totalDesembolsosReales.add( totalDesembolsosPlanificados);
			ejecucionFinanciera = totalDesembolsosReales.divide(total,2,BigDecimal.ROUND_HALF_UP);
		}
		return ejecucionFinanciera;
	}

	public static Double calcularPlazoEjecucionPlanificada(Prestamo prestamo){
		Set<Proyecto> proyectos = prestamo.getProyectos();

		DateTime fechaActual = null;
		DateTime fechaMinima = new DateTime();
		DateTime fechaMaxima = new DateTime();
		if(proyectos != null && proyectos.size() > 0){
			Iterator<Proyecto> iterador = proyectos.iterator();
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();
				if(proyecto.getFechaFin() != null){
					if(fechaActual == null)
						fechaActual = new DateTime(proyecto.getFechaFin());
					else{
						fechaMaxima = new DateTime(proyecto.getFechaFin());

						if(fechaActual.isBefore(fechaMaxima))
							fechaActual = fechaMaxima;
					}	
				}
			}

			iterador = proyectos.iterator();
			fechaMaxima = fechaActual;
			fechaActual = null;
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();
				if(fechaActual == null)
					fechaActual = new DateTime(proyecto.getFechaInicio());
				else{
					fechaMinima = new DateTime(proyecto.getFechaInicio());

					if(fechaActual.isAfter(fechaMinima))
						fechaActual = fechaMinima;
				}
			}
		}

		fechaMinima = fechaActual;
		Long hoy = new Date().getTime();
		Long inicio = fechaMinima.getMillis();			
		Long fin = fechaMaxima.getMillis();

		Long total = fin - inicio;

		Long transcurrido = hoy - inicio;
		return transcurrido*1.0/total;

	}

	public static BigDecimal calcularEjecucionFisicaPlanificada(Prestamo prestamo){
		BigDecimal ejecucionFisica = new BigDecimal("0");
		Set<Proyecto> proyectos = prestamo.getProyectos();
		if(proyectos != null && proyectos.size() > 0){
			Iterator<Proyecto> iterador = proyectos.iterator();
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();

				List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyecto.getId(), null);

				Date fecha = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				int anio = Integer.parseInt(sdf.format(fecha));
				sdf = new SimpleDateFormat("MM");
				int mes = Integer.parseInt(sdf.format(fecha));

				for(Producto producto : productos){
					List<Meta> metas = MetaDAO.getMetasPorObjeto(producto.getId(), 3);
					BigDecimal metaFinal = new BigDecimal(0);
					BigDecimal metaPlanificada = new BigDecimal(0);
					for (Meta meta : metas){
						if (meta!=null){
							metaFinal = metaFinal.add( meta.getObjetoTipo()!=null &&  meta.getObjetoTipo()==2 ? 
									new BigDecimal(meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero() : 0) : 
										(meta.getMetaFinalDecimal() != null ? meta.getMetaFinalDecimal() : new BigDecimal(0)) );

							List<MetaPlanificado> planificadas = MetaDAO.getMetasPlanificadas(meta.getId());
							for (MetaPlanificado planificado : planificadas){
								if (planificado.getId().getEjercicio() < anio){
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getEneroEntero()!= null ? planificado.getEneroEntero() : 0) :
												(planificado.getEneroDecimal() !=null ? planificado.getEneroDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getFebreroEntero()!= null ? planificado.getFebreroEntero() : 0) :
												(planificado.getFebreroDecimal() !=null ? planificado.getFebreroDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getMarzoEntero()!= null ? planificado.getMarzoEntero() : 0) :
												(planificado.getMarzoDecimal() !=null ? planificado.getMarzoDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getAbrilEntero()!= null ? planificado.getAbrilEntero() : 0) :
												(planificado.getAbrilDecimal() !=null ? planificado.getAbrilDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getMayoEntero()!= null ? planificado.getMayoEntero() : 0) :
												(planificado.getMayoDecimal() !=null ? planificado.getMayoDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getJunioEntero()!= null ? planificado.getJunioEntero() : 0) :
												(planificado.getJunioDecimal() !=null ? planificado.getJunioDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getJulioEntero()!= null ? planificado.getJulioEntero() : 0) :
												(planificado.getJulioDecimal() !=null ? planificado.getJulioDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getAgostoEntero()!= null ? planificado.getAgostoEntero() : 0) :
												(planificado.getAgostoDecimal() !=null ? planificado.getAgostoDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getSeptiembreEntero()!= null ? planificado.getSeptiembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getOctubreEntero()!= null ? planificado.getOctubreEntero() : 0) :
												(planificado.getOctubreDecimal() !=null ? planificado.getOctubreDecimal() : new BigDecimal(0)));

									metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));

									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
								}else if (planificado.getId().getEjercicio() == anio) {
									for (int i = 1 ; i <= mes;i++){
										switch (i){
										case 1:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getEneroEntero()!= null ? planificado.getEneroEntero() : 0) :
														(planificado.getEneroDecimal() !=null ? planificado.getEneroDecimal() : new BigDecimal(0)));
											break;
										case 2:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getFebreroEntero()!= null ? planificado.getFebreroEntero() : 0) :
														(planificado.getFebreroDecimal() !=null ? planificado.getFebreroDecimal() : new BigDecimal(0)));
											break;
										case 3:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getMarzoEntero()!= null ? planificado.getMarzoEntero() : 0) :
														(planificado.getMarzoDecimal() !=null ? planificado.getMarzoDecimal() : new BigDecimal(0)));
											break;
										case 4:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getAbrilEntero()!= null ? planificado.getAbrilEntero() : 0) :
														(planificado.getAbrilDecimal() !=null ? planificado.getAbrilDecimal() : new BigDecimal(0)));
											break;
										case 5:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getMayoEntero()!= null ? planificado.getMayoEntero() : 0) :
														(planificado.getMayoDecimal() !=null ? planificado.getMayoDecimal() : new BigDecimal(0)));
											break;
										case 6:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getJunioEntero()!= null ? planificado.getJunioEntero() : 0) :
														(planificado.getJunioDecimal() !=null ? planificado.getJunioDecimal() : new BigDecimal(0)));
											break;
										case 7:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getJulioEntero()!= null ? planificado.getJulioEntero() : 0) :
														(planificado.getJulioDecimal() !=null ? planificado.getJulioDecimal() : new BigDecimal(0)));
											break;
										case 8:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getAgostoEntero()!= null ? planificado.getAgostoEntero() : 0) :
														(planificado.getAgostoDecimal() !=null ? planificado.getAgostoDecimal() : new BigDecimal(0)));
											break;
										case 9:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getSeptiembreEntero()!= null ? planificado.getSeptiembreEntero() : 0) :
														(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
											break;
										case 10:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getOctubreEntero()!= null ? planificado.getOctubreEntero() : 0) :
														(planificado.getOctubreDecimal() !=null ? planificado.getOctubreDecimal() : new BigDecimal(0)));
											break;
										case 11:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
														(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
											break;
										case 12:
											metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
													new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
														(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
											break;
										}
									}
								}	
							}	
						}

						if (metaPlanificada!=null && metaFinal!=null 
								&& !metaFinal.equals(new BigDecimal(0)) && productos.size() > 0  ){
							ejecucionFisica = ejecucionFisica.add(metaPlanificada.divide(metaFinal, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(
									(producto.getPeso() !=null ? (double) producto.getPeso() : (Double) (100.0 / productos.size())) / 100)));
						}
					}
				}
			}
		}

		return ejecucionFisica; 
	}

	public static BigDecimal calcularEjecucionFisicaReal(Proyecto proyecto){
		BigDecimal ejecucionFisica = new BigDecimal(proyecto.getEjecucionFisicaReal()!= null ? 
				proyecto.getEjecucionFisicaReal():0);
		return ejecucionFisica;
	}

	public BigDecimal calcularEjecucionFinanciaeraPlanificada(int proyectoId, String codigoPresupuestario,Date fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));
		BigDecimal totalDesembolsosReales = 
				DataSigadeDAO.totalDesembolsadoAFechaReal(codigoPresupuestario,new Long( anio), mes);
		BigDecimal totalDesembolsosPlanificados = 
				DesembolsoDAO.getTotalDesembolsosFuturos(proyectoId,   fecha);
		BigDecimal  ejecucionFinanciera = new BigDecimal("0");
		if (totalDesembolsosReales!= null && totalDesembolsosPlanificados != null){
			BigDecimal total = totalDesembolsosReales.add( totalDesembolsosPlanificados);
			ejecucionFinanciera = totalDesembolsosReales.divide(total,2,BigDecimal.ROUND_HALF_UP);
		}
		return ejecucionFinanciera;
	}

	public static Double calcularPlazoEjecucionPlanificada(Proyecto proyecto){	
		if (proyecto!=null && proyecto.getFechaInicio()!=null && proyecto.getFechaFin()!=null ){

			Long hoy = new Date().getTime();
			Long inicio = proyecto.getFechaInicio().getTime();			
			Long fin = proyecto.getFechaFin().getTime();

			Long total = fin - inicio;

			Long transcurrido = hoy - inicio;
			return transcurrido*1.0/total;

		}else
			return 0.0;

	}

	public static BigDecimal calcularEjecucionFisicaPlanificada(Integer proyectoId){

		List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyectoId, null);
		BigDecimal ejecucionFisica = new BigDecimal("0");
		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));

		for(Producto producto : productos){

			List<Meta> metas = MetaDAO.getMetasPorObjeto(producto.getId(), 3);
			BigDecimal metaFinal = new BigDecimal(0);
			BigDecimal metaPlanificada = new BigDecimal(0);
			for (Meta meta : metas){
				if (meta!=null){
					metaFinal = metaFinal.add( meta.getObjetoTipo()!=null &&  meta.getObjetoTipo()==2 ? 
							new BigDecimal(meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero() : 0) : 
								(meta.getMetaFinalDecimal() != null ? meta.getMetaFinalDecimal() : new BigDecimal(0)) );

					List<MetaPlanificado> planificadas = MetaDAO.getMetasPlanificadas(meta.getId());
					for (MetaPlanificado planificado : planificadas){
						if (planificado.getId().getEjercicio() < anio){
							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getEneroEntero()!= null ? planificado.getEneroEntero() : 0) :
										(planificado.getEneroDecimal() !=null ? planificado.getEneroDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getFebreroEntero()!= null ? planificado.getFebreroEntero() : 0) :
										(planificado.getFebreroDecimal() !=null ? planificado.getFebreroDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getMarzoEntero()!= null ? planificado.getMarzoEntero() : 0) :
										(planificado.getMarzoDecimal() !=null ? planificado.getMarzoDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getAbrilEntero()!= null ? planificado.getAbrilEntero() : 0) :
										(planificado.getAbrilDecimal() !=null ? planificado.getAbrilDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getMayoEntero()!= null ? planificado.getMayoEntero() : 0) :
										(planificado.getMayoDecimal() !=null ? planificado.getMayoDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getJunioEntero()!= null ? planificado.getJunioEntero() : 0) :
										(planificado.getJunioDecimal() !=null ? planificado.getJunioDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getJulioEntero()!= null ? planificado.getJulioEntero() : 0) :
										(planificado.getJulioDecimal() !=null ? planificado.getJulioDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getAgostoEntero()!= null ? planificado.getAgostoEntero() : 0) :
										(planificado.getAgostoDecimal() !=null ? planificado.getAgostoDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getSeptiembreEntero()!= null ? planificado.getSeptiembreEntero() : 0) :
										(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getOctubreEntero()!= null ? planificado.getOctubreEntero() : 0) :
										(planificado.getOctubreDecimal() !=null ? planificado.getOctubreDecimal() : new BigDecimal(0)));

							metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
										(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));

							metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
									new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
										(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
						}else if (planificado.getId().getEjercicio() == anio) {
							for (int i = 1 ; i <= mes;i++){
								switch (i){
								case 1:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getEneroEntero()!= null ? planificado.getEneroEntero() : 0) :
												(planificado.getEneroDecimal() !=null ? planificado.getEneroDecimal() : new BigDecimal(0)));
									break;
								case 2:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getFebreroEntero()!= null ? planificado.getFebreroEntero() : 0) :
												(planificado.getFebreroDecimal() !=null ? planificado.getFebreroDecimal() : new BigDecimal(0)));
									break;
								case 3:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getMarzoEntero()!= null ? planificado.getMarzoEntero() : 0) :
												(planificado.getMarzoDecimal() !=null ? planificado.getMarzoDecimal() : new BigDecimal(0)));
									break;
								case 4:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getAbrilEntero()!= null ? planificado.getAbrilEntero() : 0) :
												(planificado.getAbrilDecimal() !=null ? planificado.getAbrilDecimal() : new BigDecimal(0)));
									break;
								case 5:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getMayoEntero()!= null ? planificado.getMayoEntero() : 0) :
												(planificado.getMayoDecimal() !=null ? planificado.getMayoDecimal() : new BigDecimal(0)));
									break;
								case 6:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getJunioEntero()!= null ? planificado.getJunioEntero() : 0) :
												(planificado.getJunioDecimal() !=null ? planificado.getJunioDecimal() : new BigDecimal(0)));
									break;
								case 7:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getJulioEntero()!= null ? planificado.getJulioEntero() : 0) :
												(planificado.getJulioDecimal() !=null ? planificado.getJulioDecimal() : new BigDecimal(0)));
									break;
								case 8:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getAgostoEntero()!= null ? planificado.getAgostoEntero() : 0) :
												(planificado.getAgostoDecimal() !=null ? planificado.getAgostoDecimal() : new BigDecimal(0)));
									break;
								case 9:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getSeptiembreEntero()!= null ? planificado.getSeptiembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
									break;
								case 10:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getOctubreEntero()!= null ? planificado.getOctubreEntero() : 0) :
												(planificado.getOctubreDecimal() !=null ? planificado.getOctubreDecimal() : new BigDecimal(0)));
									break;
								case 11:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
									break;
								case 12:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
									break;
								}
							}
						}	
					}	
				}

				if (metaPlanificada!=null && metaFinal!=null 
						&& !metaFinal.equals(new BigDecimal(0)) && productos.size() > 0  ){
					ejecucionFisica = ejecucionFisica.add(metaPlanificada.divide(metaFinal, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(
							(producto.getPeso() !=null ? (double) producto.getPeso() : (Double) (100.0 / productos.size())) / 100)));
				}
			}
		}
		return ejecucionFisica; 

	}

	public static JasperPrint generarJasper(Integer proyectoId, String usuario) throws JRException, SQLException{
		JasperPrint jasperPrint = null;
		Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
		if (proyecto!=null){
//			BigDecimal ejecucionFisicaReal = calcularEjecucionFisicaReal(proyecto);
//			BigDecimal ejecucionFinancieraPlanificada = calcularEjecucionFinanciaeraPlanificada(proyecto.getPrestamo(), proyecto.getPrestamo().getCodigoPresupuestario()+"", new Date());
//			Double plazoEjecucionPlanificada = calcularPlazoEjecucionPlanificada(proyecto);
//			BigDecimal ejecucionFisicaPlanificada = calcularEjecucionFisicaPlanificada(proyecto.getPrestamo());

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("proyectoId",proyectoId);
			parameters.put("usuario",usuario);
//			parameters.put("ejecucionFisicaReal",(ejecucionFisicaReal.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
//			parameters.put("ejecucionFinancieraPlanificada",df.format((ejecucionFisicaReal * 100.00)) );
//			parameters.put("plazoEjecucionPlanificada",df.format((ejecucionFisicaReal * 100.00)) );
//			parameters.put("ejecucionFisicaPlanificada",df.format((ejecucionFisicaReal * 100.00)) );

			jasperPrint = CJasperReport.reporteJasperPrint(CJasperReport.PLANTILLA_INFORMACIONGENERAL, parameters);
		}
		return jasperPrint;
	}
}
