/*
 * Created on 13/04/2011
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Short description
 * 
 * @author Roger Parkinson
 * @version $Revision:$
 */
public class TableDescriptor
{

    private final String m_name;
    private final List<ColumnDescriptor> m_columns = new ArrayList<ColumnDescriptor>();
    private final List<ForeignKey> m_exportedKeys = new ArrayList<ForeignKey>();
    private final List<ForeignKey> m_importedKeys = new ArrayList<ForeignKey>();
    private final List<String> m_primaryKeys = new ArrayList<String>();
    
    public TableDescriptor(String name)
    {
        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }

    public void addColumn(ColumnDescriptor columnDescriptor)
    {
        m_columns.add(columnDescriptor);
        
    }

    public void addImportedKey(ForeignKey foreignKey)
    {
        m_importedKeys.add(foreignKey);
        
    }

    public void addExportedKey(ForeignKey foreignKey)
    {
        m_exportedKeys.add(foreignKey);
        
    }

    public String render()
    {
        boolean compositeKey = m_primaryKeys.size()>1;
        StringBuilder ret = new StringBuilder();
        ret.append("<complexType name=\""+NameHandler.translateToJavaType(m_name)+"\">\n");
        ret.append("    <xsd:annotation>\n");
        ret.append("        <xsd:appinfo>\n");
        ret.append("            <hj:entity>\n");
        ret.append("                <orm:table name=\""+m_name+"\"/>\n");
        ret.append("            </hj:entity>    \n");
//        ret.append("            <annox:annotate>\n");
//        ret.append("            <Cache include=\"all\" usage=\"NONSTRICT_READ_WRITE\" region=\"RuntimeCatalogueCache\" xmlns=\"http://annox.dev.java.net/org.hibernate.annotations\"/> \n");
//        ret.append("            </annox:annotate>\n");
        ret.append("        </xsd:appinfo>\n");
        ret.append("   </xsd:annotation>\n");
        ret.append("   <sequence>\n");
        for (ColumnDescriptor columnDescriptor: m_columns)
        {
            ret.append(columnDescriptor.render(compositeKey));
        }
        ret.append("   </sequence>\n");
        if (compositeKey)
        {
            for (String primaryKeyName: m_primaryKeys)
            {
                for (ColumnDescriptor columnDescriptor: m_columns)
                {
                    if (columnDescriptor.getName().equals(primaryKeyName))
                    {
                        ret.append(columnDescriptor.renderAsAttribute());
                    }
                }
                
            }
        }
        ret.append("</complexType>\n");
        return ret.toString();
    }

    public void addPrimaryKey(String localColumn)
    {
        m_primaryKeys.add(localColumn);
    }
    
    public void resolveKeys()
    {
        List<String> tableNames = new ArrayList<String>();
        Set<String> duplicateTableNames = new HashSet<String>();
        for (ForeignKey fk: m_importedKeys)
        {
            String n = fk.getName();
            if (tableNames.contains(n))
            {
                duplicateTableNames.add(n);
                continue;
            }
            tableNames.add(n);
        }
        for (ForeignKey fk: m_exportedKeys)
        {
            if (tableNames.contains(fk.getName()))
            {
                duplicateTableNames.add(fk.getName());
                continue;
            }
            tableNames.add(fk.getName());
        }
        for (String dname: duplicateTableNames)
        {
            int i = 0;
            for (ForeignKey fk: m_importedKeys)
            {
                if (dname.equals(fk.getName()))
                {
                    fk.duplicateName(String.valueOf(i++));
                }
            }
        }
        for (ColumnDescriptor columnDescriptor: m_columns)
        {
            if (m_primaryKeys.contains(columnDescriptor.getName()))
            {
                columnDescriptor.setIdentifier(true);
            }
            for (ForeignKey fk: m_importedKeys)
            {
                if (fk.getLocalColumn().equals(columnDescriptor.getName()))
                {
                    // this field is an imported key
                    columnDescriptor.setImportedKey(fk);
                    break;
                }
           }
            for (ForeignKey fk: m_exportedKeys)
            {
                if (fk.getLocalColumn().equals(columnDescriptor.getName()))
                {
                    // this field is an imported key
                    columnDescriptor.addExportedKey(fk);
                    break;
                }
           }
        }
    }

    public void resolveExportedKeys(Map<String, TableDescriptor> tableMap)
    {
        for (ForeignKey fk: m_exportedKeys)
        {
            TableDescriptor foreignTable = tableMap.get(fk.getForeignTable());
            for (ForeignKey fk1: foreignTable.m_importedKeys)
            {
                if (fk.getForeignColumn().equals(fk1.getLocalColumn()))
                {
                    fk.setForeignColumnJava(fk1.getName());
                    break;
                }
           }
       }
    }

}
