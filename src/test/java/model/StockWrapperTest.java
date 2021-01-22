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
		to.set(2021,1,20);
		from.set(2021,1,20);
		from.add(Calendar.MONTH, -6);
		stock = new StockWrapper("INTC", from, to);
		stock.populateReturnAndRisk();
	}
	
	@Test
	public void setAdjustedClosesTest() {
		ArrayList<Double> expList = new ArrayList<Double>(Arrays.asList(51.406097, 43.960255, 48.000862, 49.82, 62.459999,56.66));
		double[] expArr = expList.stream().mapToDouble(Number::doubleValue).toArray();
		double[] actArr = stock.getAdjustedCloses().stream().mapToDouble(Number::doubleValue).toArray();
		assertArrayEquals(expArr,actArr, 0.00000001);
	}
	@Test
	public void setReturnRatesTest() {
		ArrayList<Double> expList = new ArrayList<Double>(Arrays.asList(-0.144843558,0.091915004,0.037898028,0.253713348,-0.092859416));
		double[] expArr = expList.stream().mapToDouble(Number::doubleValue).toArray();
		double[] actArr = stock.getReturnRates().stream().mapToDouble(Number::doubleValue).toArray();
		assertArrayEquals(expArr,actArr, 0.00000001);
	}
	@Test
	public void setReturnRateTest() {
		//maximum difference equal to 0.0000000009
		assertEquals(0.029164681, stock.getReturnRate(),0.0000000009);
	}
	@Test
	public void setRiskRateTest() {
		//maximum difference equal to 0.0000000009
		assertEquals(0.157801082, stock.getRiskRate(),0.0000000009);
	}
	
	
	// Test on the period. HOW many observations do we get from how many months. Does that affect the adjusted close prices list
	//
}
