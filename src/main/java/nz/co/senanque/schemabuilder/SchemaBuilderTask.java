/*
 * Created on 8/05/2010
 *
 * Copyright 2010 Prometheus Consulting.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.senanque.schemabuilder;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 
 * Ant task wrapper for the schema builder
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.9 $
 */
public class SchemaBuilderTask extends Task
{
    private static final Logger logger = LoggerFactory.getLogger(SchemaBuilderTask.class);
    String m_schemaName;
    String m_typeFile;
    String m_outFileName;
    String m_user;
    String m_password;
    String m_driver;
    String m_jdbcURL;
    boolean m_debug;

    public void execute() {
        registerDriver();
        m_schemaName = (!StringUtils.hasText(m_schemaName))?"%":m_schemaName;
        TypeMapper typeMapper = new TypeMapper();
        if (StringUtils.hasText(m_typeFile))
        {
        	Properties types = new Properties();
        	try {
				types.load(new FileReader(m_typeFile));
			} catch (IOException e) {
				throw new RuntimeException("Failed to open typeFile",e);
			}
        	typeMapper.addTypes(types);
        }
        SchemaBuilder schemaBuilder = new SchemaBuilder(m_schemaName, m_outFileName,typeMapper);
        Connection connection = null;
        try
        {
        	connection = getJDBCConnection(m_user,m_password);
        }
        catch (Exception e)
        {
        	throw new RuntimeException("Failed to connect to database",e);
        }
        try {
			schemaBuilder.build(connection);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

    }
    private void registerDriver()
    {
         try {
            Driver driver = (Driver)Class.forName(getDriver()).newInstance();
             DriverManager.registerDriver(driver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Connection getJDBCConnection(String user, String password) {
        try {
            debug(MessageFormat.format("connecting to {0} {1}", getJdbcURL(),user));
            return DriverManager.getConnection(getJdbcURL(), user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void debug(Object message)
    {
        if (m_debug)
        {
            logger.info("[debug] "+message.toString());
        }
    }
    public String getSchemaName()
    {
        return m_schemaName;
    }
    public void setSchemaName(String schemaName)
    {
        m_schemaName = schemaName;
    }
    public String getOutFileName()
    {
        return m_outFileName;
    }
    public void setOutFileName(String outFileName)
    {
        m_outFileName = outFileName;
    }
    public String getDriver()
    {
        return m_driver;
    }
    public void setDriver(String driver)
    {
        m_driver = driver;
    }
    public String getJdbcURL()
    {
        return m_jdbcURL;
    }
    public void setJdbcURL(String jdbcURL)
    {
        m_jdbcURL = jdbcURL;
    }
    public String getUser()
    {
        return m_user;
    }
    public void setUser(String user)
    {
        m_user = user;
    }
    public String getPassword()
    {
        return m_password;
    }
    public void setPassword(String password)
    {
        m_password = password;
    }
    public boolean isDebug()
    {
        return m_debug;
    }
    public void setDebug(boolean debug)
    {
        m_debug = debug;
    }
	public String getTypeFile() {
		return m_typeFile;
	}
	public void setTypeFile(String typeFile) {
		m_typeFile = typeFile;
	}

}
