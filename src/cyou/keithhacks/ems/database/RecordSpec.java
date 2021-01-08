package cyou.keithhacks.ems.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Function;

public class RecordSpec {
	
	static class FieldSpec {
		
		Class<?> type;
		
		boolean optional;
		/**
		 * Function that calculates a default value if none is provided.
		 * Not required for all optional fields - they will simply default to null.
		 */
		Function<Record, ?> defaultValue;
		
		boolean readOnly;
		boolean unique;
		
		/**
		 * Value is generated by a function each time it's read.
		 * Implies read-only. Cannot be unique or a key.
		 */
		boolean virtual;
		
		/**
		 * Field is a primary or alternate key.
		 */
		boolean key;
		
	}
	
	protected HashMap<String, FieldSpec> fields;
	
	/**
	 * Name of the primary key field.
	 * Primary key fields are required, read-only, and unique.
	 */
	protected String primaryKey;
	
	public RecordSpec(Class<? extends Record> recordClass) {
		fields = new HashMap<String, FieldSpec>();
		
		for (Field field : recordClass.getDeclaredFields()) {
			DBField annotation = field.getAnnotation(DBField.class);
			if (annotation == null)
				continue;
			
			FieldSpec spec = new FieldSpec();
			spec.type = field.getType();
			spec.optional = annotation.optional();
			spec.defaultValue = null; // TODO make this actually work
			spec.readOnly = annotation.readOnly();
			spec.unique = annotation.unique();
			spec.virtual = false;
			spec.key = annotation.alternateKey();
			
			if (annotation.primaryKey()) {
				if (annotation.alternateKey())
					throw new RuntimeException("Field cannot be a primary key and an alternate key at the same time");
				
				if (primaryKey != null)
					throw new RuntimeException("Record cannot have more than one primary key field");
				
				spec.key = true;
				spec.optional = false;
				spec.readOnly = true;
				spec.unique = true;
				
				primaryKey = field.getName();
			}
			
			fields.put(field.getName(), spec);
		}
		
		for (Method method : recordClass.getDeclaredMethods()) {
			VirtualDBField annotation = method.getAnnotation(VirtualDBField.class);
			if (annotation == null)
				continue;
			
			FieldSpec spec = new FieldSpec();
			spec.type = method.getReturnType();
			spec.optional = false;
			spec.defaultValue = null;
			spec.readOnly = true;
			spec.unique = false;
			spec.virtual = true;
			spec.key = false;
			
			fields.put(method.getName(), spec);
		}
		
		if (primaryKey == null)
			throw new RuntimeException("Record must have a primary key");
	}
	
}
