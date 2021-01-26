package model;


import org.junit.*;

import static org.junit.Assert.assertArrayEquals;
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
		to.set(2020,9,1);
		from.set(2020,9,1);
		from.add(Calendar.MONTH, -6);
		stock = new StockWrapper("INTC", from, to);
		stock.populateReturnAndRisk();
	}
	
	@Test
	public void setAdjustedClosesTest() {
		ArrayList<Double> expList = new ArrayList<Double>(Arrays.asList(58.812981, 61.705582, 58.997292, 47.065693, 50.240879,51.406097));
		double[] expArr = expList.stream().mapToDouble(Number::doubleValue).toArray();
		double[] actArr = stock.getAdjustedCloses().stream().mapToDouble(Number::doubleValue).toArray();
		assertArrayEquals(expArr,actArr, 0.00000001);
	}
	@Test
	public void setReturnRatesTest() {
		ArrayList<Double> expList = new ArrayList<Double>(Arrays.asList(0.049183037,-0.043890519,-0.202239774,0.067462855,0.023192628));
		double[] expArr = expList.stream().mapToDouble(Number::doubleValue).toArray();
		double[] actArr = stock.getReturnRates().stream().mapToDouble(Number::doubleValue).toArray();
		assertArrayEquals(expArr,actArr, 0.00000001);
	}
	@Test
	public void setReturnRateTest() {
		//maximum difference equal to 0.0000000009
		assertEquals(-0.021258355, stock.getReturnRate(),0.00009);
	}
	@Test
	public void setRiskRateTest() {
		//maximum difference equal to 0.0000000009
		assertEquals(0.109630996, stock.getRiskRate(),0.00009);
	}
	
	
	// Test on the period. HOW many observations do we get from how many months. Does that affect the adjusted close prices list
	// Test null input
}
