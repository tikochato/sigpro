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

import org.hibernate.Session;
import org.hibernate.query.Query;
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
import utilities.CHibernateSession;
import utilities.CJasperReport;
import utilities.CLogger;

public class PlanEjecucionDAO {

	public PlanEjecucionDAO(){

	}

	public static double calcularEjecucionFisicaReal(Prestamo prestamo,String lineaBase){
		Set<Proyecto> proyectos = prestamo.getProyectos();
		ProyectoDAO.getProyectosPorPrestamoHistory(prestamo.getId(),null);
		List<Double> pesos = new ArrayList<Double>();
		List<Double> techos = new ArrayList<Double>();

		if(proyectos != null && proyectos.size() > 0){	
			Iterator<Proyecto> iterador = proyectos.iterator();
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();
				double peso = Math.round(proyecto.getEjecucionFisicaReal()/100.0f);
				pesos.add(peso);

				List<Componente> componentes = ComponenteDAO.getComponentesPorProyectoHistory(proyecto.getId(),lineaBase);
				if(componentes != null && componentes.size() > 0){
					Iterator<Componente> iteradorC = componentes.iterator();
					BigDecimal techoTotal = new BigDecimal(0);
					BigDecimal techoSigade = new BigDecimal(0);
					while(iteradorC.hasNext()){
						Componente componente = iteradorC.next();
						if(componente.getEsDeSigade() == 1){
							techoTotal = techoTotal.add(componente.getFuentePrestamo().add(componente.getFuenteNacional().add(componente.getFuenteDonacion())));
							ComponenteSigade cSigade = ComponenteSigadeDAO.getComponenteSigadePorIdHistory(componente.getComponenteSigade().getId(),null);
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
			if (i<techos.size()){
				numerador += techos.get(i) * pesos.get(i);
				denominador += pesos.get(i);
			}
		}

		double ret = 0;
		if(denominador != 0){
			ret = numerador/denominador;
		}

		return ret;
	}

	public static BigDecimal calcularEjecucionFinanciaeraPlanificada(Prestamo prestamo, String codigoPresupuestario,Date fecha,String lineaBase){
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
				BigDecimal desembolsoProyecto = DesembolsoDAO.getTotalDesembolsosFuturos(proyecto.getId(), fecha,lineaBase);
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

	public static Double calcularPlazoEjecucionPlanificada(Prestamo prestamo, String lineaBase){
		List<Proyecto> proyectos = ProyectoDAO.getProyectosPorPrestamoHistory(prestamo.getId(), lineaBase);

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

	public static BigDecimal calcularEjecucionFisicaPlanificada(Prestamo prestamo,String lineaBase){
		BigDecimal ejecucionFisica = new BigDecimal("0");
		List<Proyecto> proyectos = ProyectoDAO.getProyectosPorPrestamoHistory(prestamo.getId(), lineaBase);
		if(proyectos != null && proyectos.size() > 0){
			Iterator<Proyecto> iterador = proyectos.iterator();
			while(iterador.hasNext()){
				Proyecto proyecto = iterador.next();

				List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyecto.getId(), null, lineaBase);

				Date fecha = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				int anio = Integer.parseInt(sdf.format(fecha));
				sdf = new SimpleDateFormat("MM");
				int mes = Integer.parseInt(sdf.format(fecha));

				for(Producto producto : productos){
					List<Meta> metas = MetaDAO.getMetasPorObjeto(producto.getId(), 3,lineaBase);
					BigDecimal metaFinal = new BigDecimal(0);
					BigDecimal metaPlanificada = new BigDecimal(0);
					for (Meta meta : metas){
						if (meta!=null){
							metaFinal = metaFinal.add( meta.getObjetoTipo()!=null &&  meta.getObjetoTipo()==2 ? 
									new BigDecimal(meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero() : 0) : 
										(meta.getMetaFinalDecimal() != null ? meta.getMetaFinalDecimal() : new BigDecimal(0)) );

							List<MetaPlanificado> planificadas = MetaDAO.getMetasPlanificadas(meta.getId(),lineaBase);
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
				DesembolsoDAO.getTotalDesembolsosFuturos(proyectoId,   fecha,null);
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

	public static BigDecimal calcularEjecucionFisicaPlanificada(Integer proyectoId,String lineaBase){

		List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyectoId, null,lineaBase);
		BigDecimal ejecucionFisica = new BigDecimal("0");
		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));

		for(Producto producto : productos){

			List<Meta> metas = MetaDAO.getMetasPorObjeto(producto.getId(), 3,lineaBase);
			BigDecimal metaFinal = new BigDecimal(0);
			BigDecimal metaPlanificada = new BigDecimal(0);
			for (Meta meta : metas){
				if (meta!=null){
					metaFinal = metaFinal.add( meta.getObjetoTipo()!=null &&  meta.getObjetoTipo()==2 ? 
							new BigDecimal(meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero() : 0) : 
								(meta.getMetaFinalDecimal() != null ? meta.getMetaFinalDecimal() : new BigDecimal(0)) );

					List<MetaPlanificado> planificadas = MetaDAO.getMetasPlanificadas(meta.getId(),lineaBase);
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
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("proyectoId",proyectoId);
			parameters.put("usuario",usuario);
			jasperPrint = CJasperReport.reporteJasperPrint(CJasperReport.PLANTILLA_INFORMACIONGENERAL, parameters);
		}
		return jasperPrint;
	}
	
	
	public static List<?> getDatosPlan(int proyectoId,String lineaBase){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select \"Plazo Ejecucion\" categoria, ",
				"(" ,
					"select DATEDIFF(py.fecha_inicio,CURRENT_DATE)/DATEDIFF(py.fecha_inicio,py.fecha_fin)*100 ",
					"from sipro_history.proyecto py",
					"where py.id =  ?1 	",
					lineaBase != null ? "and py.linea_base like '%"+lineaBase+"%'" : "and py.actual = 1 ",
				") valor_plan,",
				"(",
					"select DATEDIFF(ps.fecha_elegibilidad_ue, CURRENT_DATE)/DATEDIFF(ps.fecha_elegibilidad_ue,ps.fecha_cierre_actual_ue)*100", 
					"from sipro_history.prestamo ps, sipro_history.proyecto py ",
					"where ps.id = py.prestamoid ",
					lineaBase != null ? "and ps.linea_base  like '%"+lineaBase+"%'" : "and ps.actual = 1",
					lineaBase != null ? "and py.linea_base  like '%"+lineaBase+"%'" : "and py.actual = 1",
					"and py.id =  ?1 		",
				") valor_real from dual",
				"union",
				"select \"Ejecucion Financiera\",", 
				"(select desembolsos_a_la_fecha/ (desembolsos_a_la_fecha+ if(desembolsos_futuros is null, 0, desembolsos_futuros)) ejecucion_financiera_planificada",
				"from (",
				"select sum(desembolsos_mes_usd) desembolsos_a_la_fecha,(",
					"select sum(d.monto)",
				    "from sipro_history.desembolso d, sipro_history.proyecto p",
				    "where d.proyectoid = p.id",
				    "and p.id = 1",
				    lineaBase != null ? "and p.linea_base like '%"+lineaBase+"%'" : "and p.actual = 1",
				    "and fecha > current_timestamp()",
				") desembolsos_futuros",
				"from sipro_analytic.dtm_avance_fisfinan_det_dti des, sipro_history.proyecto py, sipro_history.prestamo ps",
				"where des.codigo_presupuestario= ps.codigo_presupuestario",
				"and py.prestamoid = ps.id",
				"and des.unidad_ejecutora_sicoin = py.unidad_ejecutoraunidad_ejecutora",
				"and des.entidad_sicoin = py.entidad",
				"and py.id =  ?1", 	
				lineaBase != null ? "and py.linea_base  like '%"+lineaBase+"%'" : "and py.actual = 1",
				lineaBase != null ? "and ps.linea_base  like '%"+lineaBase+"%'" : "and ps.actual = 1",
				"and ( (	des.mes_desembolso<= month(current_timestamp()) and des.ejercicio_fiscal=year(current_timestamp()))",
							"OR (des.ejercicio_fiscal<year(current_timestamp())))",
				") t1),",
				 "(select sum(desembolsos_mes_usd)/", 
				"(select sum(c.fuente_prestamo)",
					"from sipro_history.componente c",
				    "where c.proyectoid = py.id",
				    lineaBase != null ? "and c.linea_base  like '%"+lineaBase+"%'" : "and c.actual = 1",
				")*100 ejecucion_financiera_real",
				"from sipro_analytic.dtm_avance_fisfinan_det_dti des, sipro_history.proyecto py, sipro_history.prestamo ps",
				"where des.codigo_presupuestario= ps.codigo_presupuestario",
				"and py.prestamoid = ps.id",
				"and des.unidad_ejecutora_sicoin = py.unidad_ejecutoraunidad_ejecutora",
				"and des.entidad_sicoin = py.entidad",
				lineaBase != null ? "and py.linea_base like '%"+lineaBase+"%'" : "and py.actual = 1",
				lineaBase != null ? "and ps.linea_base like '%"+lineaBase+"%'" : "and ps.actual = 1",
				"and py.id =  ?1 	) from dual",
				"union",
				"select \"Ejecucion Fisica\",", 
				"(select",
					"if(p_avance_entero is null, if(p_avance_decimal is null, 0, p_avance_decimal), if(p_avance_decimal is null, p_avance_entero, (p_avance_entero+p_avance_decimal)/2)) ejecucion_fisica_planificada",
					"from (",
					"select",  
					"avg(if(dato_tipoid=2,if(p_avance_entero is null, 0, if(p_avance_entero>100,100, p_avance_entero)),null)) p_avance_entero,",
					"avg(if(dato_tipoid=3,if(p_avance_decimal is null, 0, if(p_avance_decimal>100, 100, p_avance_decimal)),null)) p_avance_decimal",
					"from (",
					"select m.id, m.dato_tipoid, m.meta_final_entero, m.meta_final_decimal,",
					"sum(ma.valor_entero)/if(m.meta_final_entero>0, m.meta_final_entero, 1) p_avance_entero,",
					"sum(valor_decimal)/if(m.meta_final_decimal>0, m.meta_final_decimal, 1) p_avance_decimal",
					"from sipro_history.meta m left outer join sipro_history.meta_avance ma",
					"on ( ma.metaid = m.id ),", 
					"sipro_history.producto p",
					"where m.objeto_tipo=3 and m.objeto_id = p.id",
					"and p.treepath like  CONCAT(CAST((10000000+ ?1 ) AS char),'%')",
					"and m.dato_tipoid in (2,3)",
					lineaBase != null ? "and m.linea_base like '%"+lineaBase+"%'" : "and m.actual = 1",
					lineaBase != null ? "and ma.linea_base like '%"+lineaBase+"%'" : "and ma.actual = 1",
					lineaBase != null ? "and p.linea_base like '%"+lineaBase+"%'" : "and p.actual = 1",
					"group by m.id, m.dato_tipoid, m.meta_final_entero, m.meta_final_decimal",
					") t1",
				") t2)*100, (select py.ejecucion_fisica_real from sipro_history.proyecto py where py.id =  ?1 ",
				lineaBase != null ? "and py.linea_base like '%"+lineaBase+"%'" : "and py.actual = 1",
				")  from dual");
						
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", proyectoId);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
