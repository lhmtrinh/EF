package model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import efficientFrontier.model.Portfolio;
import efficientFrontier.model.StockWrapper;

@SpringBootTest
public class PortfolioTest {
	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();
	Portfolio portfolio;
	
	@Before
	public void initiate() {
		to.set(2020,9,1);
		from.set(2020,9,1);
		from.add(Calendar.MONTH, -6);
		ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("INTC","UU.L"));
		portfolio = new Portfolio(tickers, from, to);
		portfolio.getStockWrappers().get(0).setWeight(50);
		portfolio.getStockWrappers().get(1).setWeight(50);
		
	}
	@Test
	public void eliminateAbundantAdjustedCLosesTest() {
		//Removing 2 adjusted prices of a stock to test
		StockWrapper stock1 = portfolio.getStockWrappers().get(0);
		StockWrapper stock2 = portfolio.getStockWrappers().get(0);
		stock2.getAdjustedCloses().remove(stock1.getAdjustedCloses().size()-2);
		portfolio.eliminateAbundantAdjustedCloses();
		assertTrue(stock1.getAdjustedCloses().size()==stock2.getAdjustedCloses().size());
	}
	@Test
	public void getReturnRateTest() {
		assertEquals(-1.2504, portfolio.getReturnRate(),0.00009);
	}
	@Test 
	public void getRiskRate() {
		assertEquals(5.0232, portfolio.getRiskRate(),0.00009);
	}	
}
