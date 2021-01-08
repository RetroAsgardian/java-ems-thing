package cyou.keithhacks.ems.database;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A database table. Stores records based on their primary key, and stores mappings of auxiliary keys to primary keys.
 * @author Keith
 */
public class Table {
	
	/**
	 * Mapping of primary keys to records. Primary keys must be unique.
	 */
	protected HashMap<Object, Record> records;
	
	/**
	 * Mappings of alternate keys to primary keys.
	 */
	protected HashMap<String, HashMap<Object, HashSet<Object>>> altKeys;
	
	
	
	public Table() {
		this.records = new HashMap<Object, Record>();
		this.altKeys = new HashMap<String, HashMap<Object, HashSet<Object>>>();
	}
	
	/**
	 * Verify and repair database state.
	 */
	public void fsck() {
		
	}
}
