package nz.co.senanque.schemabuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * You can use this test to generate an xsd file from a database if you want to set the system properties:
 * eg jdbc.driver=oracle.jdbc.driver.OracleDriver 
 * jdbc.url=jdbc:oracle:thin:@localhost:1521:xe 
 * out.File.Name=target/out.xsd 
 * dbpassword=whatever 
 * schema.Name=whatever (optional, depends on db type)
 * dbuser=whatever
 * typeFile=somefile (optional)
 * 
 * @author Roger Parkinson
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SchemaBuilderTest{
    
    @Autowired private SchemaBuilderTask m_schemaBuilderTask;

    @Test
	public void testExecute() throws Exception
	{
        m_schemaBuilderTask.execute();
	}

    public SchemaBuilderTask getSchemaBuilderTask()
    {
        return m_schemaBuilderTask;
    }

    public void setSchemaBuilderTask(SchemaBuilderTask schemaBuilderTask)
    {
        m_schemaBuilderTask = schemaBuilderTask;
    }

}
