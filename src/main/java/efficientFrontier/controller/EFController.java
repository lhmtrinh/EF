package efficientFrontier.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import efficientFrontier.model.Portfolio;
import efficientFrontier.model.StockWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
public class EFController {

	@GetMapping("/stock")
	public ArrayList<Double> getAdjustedClose(@RequestParam(value = "ticker", defaultValue = "INTC") String ticker) {
		Calendar from = Calendar.getInstance();
		from.add(Calendar.MONTH, -13);
		return new StockWrapper(ticker,from, Calendar.getInstance()).getAdjustedCloses();
	}
	
	@GetMapping("/")
	public Map<String, Object> efficientFrontier(
			@RequestParam ArrayList<String> tickers,
			@RequestParam(value = "portfolios", defaultValue = "10") int portfolios
			)
	{
		Calendar from = Calendar.getInstance();
		from.add(Calendar.MONTH, -6);
		Calendar to = Calendar.getInstance();
		Portfolio portfolio = new Portfolio(tickers, from, to);
		Map<String, Object> response = new HashMap<>();
		response.put("tickers", tickers);
		Map<Integer,Object> curveList = new HashMap<>();
		//make sure to have the same set of random weights for debugging purpose
		Random random = new Random(3000);
		for (int i = 0; i< portfolios; i++) {
			Map<String,Object> point = new HashMap<>();
			int stockNumber = portfolio.getStockWrappers().size();
			double[] weights = generateWeights(random, stockNumber);			
			for (int j = 0; j < stockNumber; j++) {
				portfolio.getStockWrappers().get(j).setWeight(weights[j]);
			}
			double returnRate = portfolio.getReturnRate();
			// calculate risk
			double riskRate = portfolio.getRiskRate();
			point.put("risk",riskRate);
			point.put("return",returnRate);
			point.put("weights", weights);
			curveList.put(i,point);
		}
		response.put("curveList", curveList);
		return response;
	}
//	@GetMapping("/")
//	public ArrayList<Portfolio> efficientFrontier(
//			@RequestParam ArrayList<String> tickers,
//			@RequestParam(value = "portfolios", defaultValue = "10") int portfolios
//			)
//	{
//		Calendar from = Calendar.getInstance();
//		from.add(Calendar.MONTH, -6);
//		Calendar to = Calendar.getInstance();
//		ArrayList<Portfolio> listPortfolios= new ArrayList<>();
//		Portfolio portfolio = new Portfolio(tickers, from, to);
//		//make sure to have the same set of random weights for debugging purpose
//		Random random = new Random(3000);
//		for (int i = 0; i< portfolios; i++) {
//			int stockNumber = portfolio.getStockWrappers().size();
//			double[] weights = generateWeights(random, stockNumber);			
//			for (int j = 0; j < stockNumber; j++) {
//				portfolio.getStockWrappers().get(j).setWeight(weights[j]);
//			}
//			portfolio.resetReturnRate();
//			portfolio.resetRiskRate();
//			listPortfolios.add(portfolio);
//		}
//		return listPortfolios;
//	}
	
	public static double[] generateWeights(Random random, int stocksNumber) {
		double sum = 0;
		double [] weights = new double[stocksNumber];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextInt(100);
		}
		for (double weight: weights) {
			sum += weight;
		}
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (weights[i]/sum)*100;
		}
		return weights;
	}


}
