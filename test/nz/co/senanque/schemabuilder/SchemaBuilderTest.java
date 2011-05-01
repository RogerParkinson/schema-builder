package nz.co.senanque.schemabuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
