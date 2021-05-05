package efficientFrontier.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import efficientFrontier.annotation.ValidTicker;
import efficientFrontier.annotation.ValidYear;
import efficientFrontier.model.Portfolio;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@Validated
public class EFRestController {
	@GetMapping("/api/ef")
	public Map<String, Object> efficientFrontier(
			@RequestParam @ValidTicker(message="Must input a valid ticker") ArrayList<String> tickers,
			@RequestParam(defaultValue = "10000") @Positive @Min(1) @Max(100000) int portfolios,
			@RequestParam(value = "period") @ValidYear(message = "Must be a year in the past") @Size(min=2,max=2) ArrayList<Integer> years
			)
	{
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		if (years.get(0)<years.get(1)) {
			from.set(years.get(0),Calendar.JANUARY,1,0,0,0);
			to.set(years.get(1),Calendar.JANUARY,1,0,0,0);
		} else {
			from.set(years.get(1),Calendar.JANUARY,1,0,0,0);
			to.set(years.get(0),Calendar.JANUARY,1,0,0,0);
		}

		
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
