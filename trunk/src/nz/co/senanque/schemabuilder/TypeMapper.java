package nz.co.senanque.schemabuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roger Parkinson
 *
 */
public class TypeMapper {
	
    private static final Logger log = LoggerFactory.getLogger(ColumnDescriptor.class);
	private Map<Integer,String> m_map = new HashMap<Integer,String>();
	
	public TypeMapper()
	{
		initialize();
	}
	
	public TypeMapper(Properties properties)
	{
		initialize();
		if (properties != null)
		{
			for (Entry<Object,Object> entry: properties.entrySet())
			{
				Integer key = new Integer((String)entry.getKey());
				String value = (String)entry.getValue();
				m_map.put(key, value);
			}
		}
	}
	
	private void initialize()
	{
		m_map.put(new Integer(2003), "ARRAY:string");
		m_map.put(new Integer(-5), "BIGINT:long");
		m_map.put(new Integer(-2), "BINARY:base64Binary");
		m_map.put(new Integer(-7), "BIT:boolean"); // short?
		m_map.put(new Integer(2004), "BLOB:base64Binary");
		m_map.put(new Integer(16), "BOOLEAN:boolean");
		m_map.put(new Integer(1), "CHAR:string");
		m_map.put(new Integer(2005), "CLOB:string");
		m_map.put(new Integer(70), "DATALINK:anyURI");
		m_map.put(new Integer(91), "DATE:date");
		m_map.put(new Integer(3), "DECIMAL:decimal");
		m_map.put(new Integer(2001), "DISTINCT:string");
		m_map.put(new Integer(8), "DOUBLE:double");
		m_map.put(new Integer(6), "FLOAT:float");
		m_map.put(new Integer(4), "INTEGER:int");
		m_map.put(new Integer(2000), "JAVA_OBJECT:string");
		m_map.put(new Integer(-4), "LONGVARBINARY:base64Binary");
		m_map.put(new Integer(-1), "LONGVARCHAR:string");
		m_map.put(new Integer(0), "NULL:string");
		m_map.put(new Integer(2), "NUMERIC:decimal");
		m_map.put(new Integer(1111), "OTHER:string");
		m_map.put(new Integer(7), "REAL:float");
		m_map.put(new Integer(2006), "REF:string");
		m_map.put(new Integer(5), "SMALLINT:short");
		m_map.put(new Integer(2002), "STRUCT:string");
		m_map.put(new Integer(92), "TIME:time");
		m_map.put(new Integer(93), "TIMESTAMP:dateTime");
		m_map.put(new Integer(-6), "TINYINT:short");
		m_map.put(new Integer(-3), "VARBINARY:base64Binary");
		m_map.put(new Integer(12), "VARCHAR:string");
		m_map.put(new Integer(100), "DB2:anyType"); // DB2
		m_map.put(new Integer(2009), "DB2:string"); // DB2
	}

	public String getType(int type, String name) {
		String ret = m_map.get(new Integer(type));
		if (ret == null)
		{
            log.warn("Unknown type {} on {}",type,name);
			ret = "cannot resolve datatype "+type;
		}
		else
		{
			ret = ret.substring(ret.indexOf(':')+1);
		}
		return ret;
	}

	public void addTypes(Properties properties) {
		if (properties != null)
		{
			for (Entry<Object,Object> entry: properties.entrySet())
			{
				Integer key = new Integer((String)entry.getKey());
				String value = (String)entry.getValue();
				m_map.put(key, value);
			}
		}
	}

	public String getJDBCType(int type, String name) {
		String ret = m_map.get(new Integer(type));
		if (ret == null)
		{
            log.warn("Unknown type {} on {}",type,name);
			ret = "cannot resolve datatype "+type;
		}
		else
		{
			ret = ret.substring(0,ret.indexOf(':'));
		}
		return ret;
	}
}
