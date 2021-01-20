package co.uk.codeyogi.stockdata.service;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import efficientFrontier.controller.StockService;
import efficientFrontier.model.Portfolio;
import efficientFrontier.model.StockWrapper;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Slf4j
@SpringBootTest
public class StockServiceTest {
	@Test
	void invoke() throws IOException {
		ArrayList<String> tickers = new ArrayList<String>();
		tickers.add("INTC");
		tickers.add("UU.L");
		Map<Integer, double[]> ef = new HashMap<Integer, double[]>();
		ef= StockService.efficientFrontier(tickers);
		for (double value: ef.get(0)) {
			System.out.println(value);
		}
	}

}
