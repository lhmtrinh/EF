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
		to.set(2020, Calendar.JANUARY, 1, 0, 0, 0);
		from.set(2019, Calendar.JANUARY, 1, 0, 0, 0);
		stock = new StockWrapper("NFLX", from, to);
		stock.populateReturnAndRisk();
	}

	@Test
	public void setAdjustedClosesTest() {
		ArrayList<Double> expList = new ArrayList<Double>(Arrays.asList(44.587032, 50.113102, 51.13541, 48.60244,
				41.936749, 45.862999, 48.430641, 45.422287, 49.702877, 54.525593, 55.991699, 58.045586, 62.002583));
		double[] expArr = expList.stream().mapToDouble(Number::doubleValue).toArray();
		double[] actArr = stock.getAdjustedCloses().stream().mapToDouble(Number::doubleValue).toArray();
		assertArrayEquals(expArr, actArr, 0.00000001);
	}

	@Test
	public void setReturnRatesTest() {
		ArrayList<Double> expList = new ArrayList<Double>(
				Arrays.asList(0.123938952, 0.020400014, -0.049534559, -0.13714725, 0.093623137, 0.055985044,
						-0.06211675, 0.094239861, 0.097030922, 0.026888401, 0.036681991, 0.068170507));
		double[] expArr = expList.stream().mapToDouble(Number::doubleValue).toArray();
		double[] actArr = stock.getReturnRates().stream().mapToDouble(Number::doubleValue).toArray();
		assertArrayEquals(expArr, actArr, 0.00000001);
	}


	@Test
	public void setReturnRateTest() {
		// maximum difference equal to 0.0000000009
		assertEquals(0.030680022, stock.getReturnRate(), 0.00009);
	}

	@Test
	public void setRiskRateTest() {
		// maximum difference equal to 0.0000000009
		assertEquals(0.077761249, stock.getRiskRate(), 0.00009);
	}
}
