package efficientFrontier.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import efficientFrontier.model.Portfolio;
import efficientFrontier.model.StockWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Slf4j
@AllArgsConstructor
@Service
public class StockService {
	public static Map<Integer, double[]> efficientFrontier(@RequestParam ArrayList<String> tickers)
	{
		Calendar from = Calendar.getInstance();
		from.add(Calendar.MONTH, -6);
		Calendar to = Calendar.getInstance();
		Portfolio portfolio = new Portfolio(tickers, from, to);
		Map<Integer, double[]> ef = new HashMap<Integer,double[]>();
		for (int i = 0; i< 1; i++) {
			int stockNumber = portfolio.getStockWrappers().size();
			double[] weights = generateWeights(stockNumber);
			//log.info("Weights of stocks: "+Double.toString(weights[0])+" "+Double.toString(weights[1]));			
			for (int j = 0; j < stockNumber; j++) {
				portfolio.getStockWrappers().get(j).setWeight(weights[j]);
			}
			double returnRate = portfolio.getReturnRate();
			// calculate risk
			double riskRate = portfolio.getRiskRate();
			double[] values = new double[] {returnRate, riskRate};
			ef.put(i,values);
//			System.out.println("Weight 1: "+portfolio.getStockWrappers().get(0).getWeight());
//			System.out.println("Adjusted Closes: "+portfolio.getStockWrappers().get(0).getAdjustedCloses());
//			System.out.println("Return rates: "+portfolio.getStockWrappers().get(0).getReturnRates());
//			System.out.println("Return rates: "+portfolio.getStockWrappers().get(0).getReturnRate());
//			System.out.println("Risk: "+portfolio.getStockWrappers().get(0).getRiskRate());
//			
//			System.out.println("Weight 2: "+portfolio.getStockWrappers().get(1).getWeight());
//			System.out.println("Adjusted Closes: "+portfolio.getStockWrappers().get(1).getAdjustedCloses());
//			System.out.println("Return rates: "+portfolio.getStockWrappers().get(1).getReturnRates());
//			System.out.println("Return rates: "+portfolio.getStockWrappers().get(1).getReturnRate());
//			System.out.println("Risk: "+portfolio.getStockWrappers().get(1).getRiskRate());
		}
		return ef;
	}
	
	public static double[] generateWeights(int stocksNumber) {
		Random random = new Random(3000);
		double sum = 0;
		double [] weights = new double[stocksNumber];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextInt(100);
		}
		for (double weight: weights) {
			sum += weight;
		}
		for (int i = 0; i < weights.length; i++) {
			double weight=weights[i]/sum;
			BigDecimal bd = new BigDecimal(weight).setScale(4, RoundingMode.HALF_UP);
			weights[i] = bd.doubleValue();
		}
		return weights;
	}
}
