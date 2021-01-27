package efficientFrontier.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class EFRestController {
	@GetMapping("/ef")
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
			double weight = (weights[i]/sum)*100;
			BigDecimal bd = new BigDecimal(weight).setScale(2, RoundingMode.HALF_UP);
			weights[i] = bd.doubleValue();
		}
		return weights;
	}


}
