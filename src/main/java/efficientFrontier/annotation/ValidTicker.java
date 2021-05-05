package efficientFrontier.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { ValidTickerValidator.class})
public @interface ValidTicker {
	String message();
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
