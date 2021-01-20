package efficientFrontier.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import efficientFrontier.controller.StockService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class StockWrapper {
	private String ticker;
	private double weight;
	private double returnRate;
	private ArrayList<Double> returnRates;
	private ArrayList<Double> adjustedCloses;
	private double riskRate;
	
	public StockWrapper(String ticker, Calendar from, Calendar to) {
		this.ticker = ticker;
		setAdjustedCloses(from,to);
	}
	public void setAdjustedCloses(Calendar from, Calendar to) {
		try {
			//create a list of adjusted close price
			ArrayList<Double> adjustedCloses = new ArrayList<>();
			//returning the historical quotes of stock from .... to .... with a monthly interval
			List<HistoricalQuote> quotes= YahooFinance.get(this.ticker).getHistory(from, to, Interval.MONTHLY);
			//add adjusted close price to the adjustedClose
			for (HistoricalQuote quote: quotes) {
				adjustedCloses.add(quote.getAdjClose().doubleValue());
			}
			//set the list
			this.adjustedCloses = adjustedCloses;
		} catch (IOException e) {
			//log any exception generated
			log.info(e.toString());
		}
	}
	public void populateReturnAndRisk() {
		setReturnRate();
		setRiskRate();
	}
	public void setReturnRate() {
		ArrayList<Double> returnRates = new ArrayList<>();
		double sum = 0;
		for (int i = 0; i < this.adjustedCloses.size()-1; i++) {
			double returnRate = (this.adjustedCloses.get(i+1) - this.adjustedCloses.get(i))/this.adjustedCloses.get(i);
			returnRates.add(returnRate);
		}
		this.returnRates = returnRates;
		for (double returnRate: returnRates) {
			sum+= returnRate;
		}
		this.returnRate = sum/returnRates.size();
	}	
	public void setRiskRate() {
		StandardDeviation std = new StandardDeviation();
		this.riskRate=std.evaluate(this.returnRates.stream().mapToDouble(Double::doubleValue).toArray());
	}
}




