package efficientFrontier.annotation;

import java.util.ArrayList;
import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidYearValidator implements ConstraintValidator<ValidYear, ArrayList<Integer>> {
	public void inititalize(ValidYear constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(ArrayList<Integer> value, ConstraintValidatorContext context) {
		for(int year: value) {
			if (year>Calendar.getInstance().get(Calendar.YEAR)&& year>=0) {
				return false;
			}
		}
		return true;
	}
}
