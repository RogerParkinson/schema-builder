/*
 * Created on 12/04/2011
 *
 * Copyright 2011 Prometheus Consulting.
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

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


public class SchemaBuilder
{
    private final String m_schemaName;
    private final String m_outFileName;
    private final TypeMapper m_typeMapper;

    
    public SchemaBuilder(String schemaName, String outFileName, TypeMapper typeMapper)
    {
        m_schemaName = schemaName;
        m_outFileName = outFileName;
        m_typeMapper = typeMapper;
    }
    
    public void build(Connection connection) throws Exception
    {
        Map<String,TableDescriptor> tableMap = new HashMap<String,TableDescriptor>();
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet rs = metadata.getTables(null, m_schemaName, "%", new String[]{"TABLE"});
        while (rs.next())
        {
           String tableName = rs.getString(3);
           // log.info("name: {}",tableName);
            TableDescriptor tableDescriptor = new TableDescriptor(tableName);
            tableMap.put(tableName, tableDescriptor);
            ResultSet rs1 = metadata.getImportedKeys(null, m_schemaName, tableName);
            while (rs1.next())
            {
                int seq = rs1.getInt(9);
                String foreignTable = rs1.getString(3);
                String foreignColumn = rs1.getString(4);
                String localColumn = rs1.getString(8);
                tableDescriptor.addImportedKey(new ForeignKey(foreignTable,foreignColumn,localColumn,foreignTable));
            }
            rs1.close();
            rs1 = metadata.getExportedKeys(null, m_schemaName, tableName);
            while (rs1.next())
            {
                String foreignTable = rs1.getString(7);
                String foreignColumn = rs1.getString(8);
                String localColumn = rs1.getString(4);
                tableDescriptor.addExportedKey(new ForeignKey(foreignTable,foreignColumn,localColumn,foreignTable));
            }
            rs1.close();
            rs1 = metadata.getPrimaryKeys(null, m_schemaName, tableName);
            while (rs1.next())
            {
                String localColumn = rs1.getString(4);
                tableDescriptor.addPrimaryKey(localColumn);
            }
            rs1.close();
        }
        rs.close();
        rs = metadata.getColumns(null, m_schemaName, "%", "%");
        while (rs.next())
        {
            String tableName = rs.getString(3);
            String columnName = rs.getString(4);
            boolean nullable = rs.getBoolean(11);
            int type = rs.getInt(5);
            int decimalDigits = rs.getInt(9);
            int columnSize = rs.getInt(7);
            TableDescriptor tableDescriptor = tableMap.get(tableName);
            if (tableDescriptor == null)
            {
                continue;
            }
            tableDescriptor.addColumn(new ColumnDescriptor(columnName,type,columnSize,nullable,decimalDigits,tableName,m_typeMapper));
        }
        rs.close();

        
        connection.close();
        PrintStream ps = new PrintStream(new FileOutputStream(m_outFileName));
        ps.println(generateFile(tableMap));
    }
    
    private String generateFile(Map<String,TableDescriptor> tableMap)
    {
        StringBuilder ret = new StringBuilder();
        ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        ret.append("<schema xmlns=\"http://www.w3.org/2001/XMLSchema\" \n");
        ret.append("    targetNamespace=\"http://www.example.org/sandbox\" \n");
        ret.append("    xmlns:tns=\"http://www.example.org/sandbox\" \n");
        ret.append("    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n");
        ret.append("    xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" jaxb:version=\"2.0\"\n");
        ret.append("    xmlns:xjc=\"http://java.sun.com/xml/ns/jaxb/xjc\" jaxb:extensionBindingPrefixes=\"xjc hj annox orm\" \n");
        ret.append("    xmlns:hj=\"http://hyperjaxb3.jvnet.org/ejb/schemas/customizations\" \n");
        ret.append("    xmlns:annox=\"http://annox.dev.java.net\"\n");
        ret.append("    xmlns:orm=\"http://java.sun.com/xml/ns/persistence/orm\" \n");
        ret.append("    elementFormDefault=\"qualified\">\n");
        ret.append("\n");
        ret.append("<xsd:annotation>\n");
        ret.append("<xsd:appinfo>\n");
//        ret.append("  <jaxb:globalBindings generateIsSetMethod=\"false\" localScoping=\"toplevel\">\n");
//        ret.append("  <jaxb:javaType \n");
//        ret.append("    name=\"java.util.Date\" xmlType=\"xsd:date\" \n");
//        ret.append("    parseMethod=\"nz.co.senanque.schemabuilder.JAXBDataAdapter.parseDate\" \n");
//        ret.append("    printMethod=\"nz.co.senanque.schemabuilder.JAXBDataAdapter.printDate\" /> \n");
//        ret.append("    <xjc:serializable uid=\"100\"/>\n");
//        ret.append("  </jaxb:globalBindings>\n");
//        ret.append("  <jaxb:schemaBindings>\n");
//        ret.append("    <jaxb:package name=\"com.sandbox\"/>\n");
//        ret.append("  </jaxb:schemaBindings>\n");
        ret.append("</xsd:appinfo>\n");
        ret.append("</xsd:annotation>\n");
        
        for (TableDescriptor tableDescriptor: tableMap.values())
        {
            tableDescriptor.resolveKeys();
        }
        for (TableDescriptor tableDescriptor: tableMap.values())
        {
            tableDescriptor.resolveExportedKeys(tableMap);
        }
        for (TableDescriptor tableDescriptor: tableMap.values())
        {
            ret.append(tableDescriptor.render());
        }
        
        ret.append("</schema>\n");
        return ret.toString();
    }

}
