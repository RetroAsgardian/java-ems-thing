package cyou.keithhacks.ems.database;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface DBField {
	boolean optional() default false;
	boolean readOnly() default false;
	boolean unique() default false;
	boolean primaryKey() default false;
	boolean alternateKey() default false;
}
