package efficientFrontier.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.math3.stat.correlation.Covariance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;

@Getter
@Setter
@Slf4j
public class Portfolio {
	private ArrayList<StockWrapper> stockWrappers;
	private double returnRate;
	private double riskRate;
	
	public Portfolio() {
		stockWrappers = new ArrayList<>();
	}
	public Portfolio(ArrayList<String> tickers, Calendar from, Calendar to) {
		stockWrappers = new ArrayList<>();
		for (String ticker : tickers) {
			this.stockWrappers.add(new StockWrapper(ticker, from, to));
		}
		//Making sure all stocks has the same amount of adjusted closes
		int minAdjustedSize = 1000000000;
		for (StockWrapper stockWrapper: stockWrappers) {
			if ( minAdjustedSize >stockWrapper.getAdjustedCloses().size()) {
				minAdjustedSize = stockWrapper.getAdjustedCloses().size();
			}
		}
		for (StockWrapper stockWrapper: stockWrappers) {
			if ( minAdjustedSize < stockWrapper.getAdjustedCloses().size()) {
				int diff = stockWrapper.getAdjustedCloses().size()-minAdjustedSize;
				//remove the last elements
				for (int i = 0; i<diff; i++) {
					stockWrapper.getAdjustedCloses().remove(stockWrapper.getAdjustedCloses().size()-1);
				}
			}
			stockWrapper.populateReturnAndRisk();		
		}
	}
	
	public void setReturnRate() {
		double returnRate=0;
		for (StockWrapper stockWrapper: stockWrappers) {
			returnRate += stockWrapper.getReturnRate()*stockWrapper.getWeight();
		}
		BigDecimal bd = new BigDecimal(returnRate).setScale(4, RoundingMode.HALF_UP);
		this.returnRate= bd.doubleValue();
	}
	public double getReturnRate() {
		setReturnRate();
		return this.returnRate;
	}
	
	public void setRiskRate() {
		double portfolioVariance = 0;
		for (int i = 0; i < this.stockWrappers.size(); i++) {
			for (int j = 0; j< this.stockWrappers.size(); j++) {
				if (i==j) {
					//Squared of variance
					double weight = this.stockWrappers.get(i).getWeight();
					double risk = this.stockWrappers.get(i).getRiskRate();
					portfolioVariance += weight*weight*risk*risk;
				} else {
					//Covariance
					try {
						Covariance cov = new Covariance();
						double[] arr1 = this.stockWrappers.get(i).getReturnRates().stream().mapToDouble(Double::doubleValue).toArray();
						double[] arr2 = this.stockWrappers.get(j).getReturnRates().stream().mapToDouble(Double::doubleValue).toArray();
						double weight1 = this.stockWrappers.get(i).getWeight();
						double weight2 = this.stockWrappers.get(j).getWeight();					
						portfolioVariance += cov.covariance(arr1, arr2)*weight1*weight2;
//						System.out.println("w*w*cov is: "+cov.covariance(arr1, arr2)*weight1*weight2);
					} catch (Exception e) {
						log.info("Cant handle exception when calculating portfolio covariance");
					} 
				}
			}
		}
		double riskRate = Math.sqrt(portfolioVariance);
		BigDecimal bd = new BigDecimal(riskRate).setScale(4, RoundingMode.HALF_UP);
		riskRate = bd.doubleValue();
		this.riskRate = riskRate; 
	}
	
	public double getRiskRate() {
		setRiskRate();
		return this.riskRate;
	}


}
