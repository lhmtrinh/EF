package efficientFrontier.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import efficientFrontier.model.Portfolio;
import efficientFrontier.model.StockWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EFController {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	@GetMapping("/stock")
	public ArrayList<Double> getAdjustedClose(@RequestParam(value = "ticker", defaultValue = "INTC") String ticker) {
		Calendar from = Calendar.getInstance();
		from.add(Calendar.MONTH, -13);
		return new StockWrapper(ticker,from, Calendar.getInstance()).getAdjustedCloses();
	}
	
	@GetMapping("/ef")
	public Map<Integer, double[]> efficientFrontier(
			@RequestParam ArrayList<String> tickers,
			@RequestParam(value = "portfolios", defaultValue = "10") int portfolios
			)
	{
		Calendar from = Calendar.getInstance();
		from.add(Calendar.MONTH, -6);
		Calendar to = Calendar.getInstance();
		Portfolio portfolio = new Portfolio(tickers, from, to);
		Map<Integer, double[]> ef = new HashMap<Integer,double[]>();
		for (int i = 0; i< portfolios; i++) {
			int stockNumber = portfolio.getStockWrappers().size();
			double[] weights = generateWeights(stockNumber);			
			for (int j = 0; j < stockNumber; j++) {
				portfolio.getStockWrappers().get(j).setWeight(weights[j]);
			}
			double returnRate = portfolio.getReturnRate();
			// calculate risk
			double riskRate = portfolio.getRiskRate();
			double[] values = new double[] {returnRate, riskRate};
			ef.put(i,values);
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
			weights[i] = (weights[i]/sum)*100;
		}
		return weights;
	}


}
