package efficientFrontier.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import efficientFrontier.model.Portfolio;

public class EF {
	public static Map<Integer, Double> efficientFrontier(Portfolio portfolio, int portfolioNumber){
		Map<Integer, Double> ef = new HashMap<Integer,Double>();
		for (int i = 0; i< portfolioNumber; i++) {
			int stockNumber = portfolio.getStockWrappers().size();
			double[] weights = generateWeights(stockNumber);
			for (int j = 0; j < stockNumber; j++) {
				portfolio.getStockWrappers().get(j).setWeight(weights[j]);
			}
			double returnRate = portfolio.getReturnRate();
			ef.put(i, returnRate);
		}
		return ef;
	}
	
	public static double[] generateWeights(int stocksNumber) {
		Random random = new Random();
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
