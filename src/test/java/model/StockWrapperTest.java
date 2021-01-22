package model;


import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import org.springframework.boot.test.context.SpringBootTest;

import efficientFrontier.model.StockWrapper;


@SpringBootTest
public class StockWrapperTest {
	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();
	StockWrapper stock;
	
	@Before
	public void initiate() {
		to.set(2021,1,20);
		from.set(2021,1,20);
		from.add(Calendar.MONTH, -6);
		stock = new StockWrapper("INTC", from, to);
	}
	
	@Test
	public void setAdjustedClosesTest() {
		ArrayList<Double> exp = new ArrayList<Double>(Arrays.asList(51.406097, 43.960255, 48.000862, 49.82, 62.459999,56.66));
		ArrayList<Double> act = stock.getAdjustedCloses();
		assertEquals(exp, act);
	}
	@Test
	public void setReturnRateTest() {
		stock.setReturnRate();
		//maximum difference equal to 0.0000000009
		assertEquals(0.029164681, stock.getReturnRate(),0.0000000009);
	}
	@Test
	public void setRiskRateTest() {
		stock.setRiskRate();
		//maximum difference equal to 0.0000000009
		assertEquals(0.157801082, stock.getRiskRate(),0.0000000009);
		
	}
}
