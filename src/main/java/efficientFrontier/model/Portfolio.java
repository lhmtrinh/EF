package efficientFrontier.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.math3.stat.correlation.Covariance;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Portfolio {
	private ArrayList<StockWrapper> stockWrappers;
	private double returnRate;
	private double riskRate;
	
	//Constructor
	public Portfolio() {
		stockWrappers = new ArrayList<>();
	}
	
	//Parameterized constructor
	public Portfolio(ArrayList<String> tickers, Calendar from, Calendar to) {
		stockWrappers = new ArrayList<>();
		for (String ticker : tickers) {
			//Adding new stockWrapper objects to the instance variable stockWrappers
			this.stockWrappers.add(new StockWrapper(ticker, from, to));
		}
		eliminateAbundantAdjustedCloses();

	}
	
	//Make sure all stocks have the same number of adjusted close price observations
	//Helpful to calculate portfolio risk rate which involves covariance
	public void eliminateAbundantAdjustedCloses() {
		//Set min number very high
		int minAdjustedSize = 1000000000;
		//Find the smallest size out of all assets
		for (StockWrapper stockWrapper: stockWrappers) {
			if ( minAdjustedSize >stockWrapper.getAdjustedCloses().size()) {
				minAdjustedSize = stockWrapper.getAdjustedCloses().size();
			}
		}
		//For every stocks, removing the last elements of the list if the list size is larger than min value
		for (StockWrapper stockWrapper: stockWrappers) {
			if ( minAdjustedSize < stockWrapper.getAdjustedCloses().size()) {
				int diff = stockWrapper.getAdjustedCloses().size()-minAdjustedSize;
				//Remove a number of elements based on how many more observations the list has than the minimum list
				for (int i = 0; i<diff; i++) {
					stockWrapper.getAdjustedCloses().remove(stockWrapper.getAdjustedCloses().size()-1);
				}
			}
			//After processing adjustedCloses, set return, risk rate of each stock
			stockWrapper.populateReturnAndRisk();		
		}
	}
	
	//Reset portfolio return rate
	public void resetReturnRate() {
		double returnRate=0;
		//Return rate = sum of all (stock weight* stock return)
		for (StockWrapper stockWrapper: stockWrappers) {
			returnRate += stockWrapper.getReturnRate()*stockWrapper.getWeight();
		}
		//Round return rate
		BigDecimal bd = new BigDecimal(returnRate).setScale(4, RoundingMode.HALF_UP);
		this.returnRate= bd.doubleValue();
	}
	
	//Get portfolio return rate
	public double getReturnRate() {
		//Reset portfolio return rate every stime a set of new weights is added
		resetReturnRate();
		return this.returnRate;
	}
	
	//Reset portfolio risk rate
	public void resetRiskRate() {
		double portfolioVariance = 0;
		//Nested loop to find porfolio variance
		for (int i = 0; i < this.stockWrappers.size(); i++) {
			for (int j = 0; j< this.stockWrappers.size(); j++) {
				if (i==j) {
					//When i = j, increase the variance by the squared value of (stock risk * stock weight)
					double weight = this.stockWrappers.get(i).getWeight();
					double risk = this.stockWrappers.get(i).getRiskRate();
					portfolioVariance += weight*weight*risk*risk;
				} else {
					//When i != j, increase the variance by stock 1 weight * stock 2 weight * covariance(stock 1, stock 2)
					try {
						Covariance cov = new Covariance();
						double[] arr1 = this.stockWrappers.get(i).getReturnRates().stream().mapToDouble(Double::doubleValue).toArray();
						double[] arr2 = this.stockWrappers.get(j).getReturnRates().stream().mapToDouble(Double::doubleValue).toArray();
						double weight1 = this.stockWrappers.get(i).getWeight();
						double weight2 = this.stockWrappers.get(j).getWeight();					
						portfolioVariance += cov.covariance(arr1, arr2)*weight1*weight2;
					} catch (Exception e) {
						//Handle exception when input arrays have different sizes
						log.info("Cant handle exception when calculating portfolio covariance");
					} 
				}
			}
		}
		//Risk rate is the square root of the variance
		double riskRate = Math.sqrt(portfolioVariance);
		//Round the number
		BigDecimal bd = new BigDecimal(riskRate).setScale(4, RoundingMode.HALF_UP);
		riskRate = bd.doubleValue();
		this.riskRate = riskRate; 
	}
	//Get portfolio risk rate
	public double getRiskRate() {
		//Reset portfolio risk rate every time a set of new weights is added
		resetRiskRate();
		return this.riskRate;
	}


}
