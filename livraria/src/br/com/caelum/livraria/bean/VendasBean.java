package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.caelum.livraria.modelo.Venda;

@Named
@ViewScoped
public class VendasBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	EntityManager manager;

	public BarChartModel getVendasModel() {
		BarChartModel model = new BarChartModel();
		model.setAnimate(true);
		
		ChartSeries vendaSerie = new ChartSeries();
		vendaSerie.setLabel("Vendas 2016");
		
		List<Venda> vendas = getVendas();
		for (Venda venda : vendas) {
			vendaSerie.set(venda.getLivro().getTitulo(), venda.getQuantidade());
		}
		
		model.addSeries(vendaSerie);
		
		model.setTitle("Vendas");
		model.setLegendPosition("ne"); //Nordeste
		
		//Setando o eixo X do gráfico
		Axis xAxis = model.getAxis(AxisType.X);
		xAxis.setLabel("Título");
		
		//Setando o eixo Y do gráfico
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel("Quantidade");
		
		return model;
	}

	public List<Venda> getVendas(){
		List<Venda> vendas = this.manager.createQuery("select v from Venda v", Venda.class).getResultList();
		return vendas;
	}
}
