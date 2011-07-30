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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Short description
 * 
 * @author Roger Parkinson
 * @version $Revision:$
 */
public class ColumnDescriptor
{
    private static final Logger log = LoggerFactory.getLogger(ColumnDescriptor.class);

    private final String m_name;
    private final int m_columnSize;
    private final int m_type;
    private final boolean m_nullable;
    private final int m_decimalDigits;
    private boolean m_identifier;
    private ForeignKey m_importedKey;

    private List<ForeignKey> m_exportedKeys = new ArrayList<ForeignKey>();

    private final String m_tableName;
    
    public ColumnDescriptor(String name, int type, int columnSize, boolean nullable, int decimalDigits, String tableName)
    {
        m_name = name;
        m_tableName = tableName;
        m_type = type;
        m_columnSize = columnSize;
        m_nullable = nullable;
        m_decimalDigits = decimalDigits;
    }

    public String getName()
    {
        return m_name;
    }

    public int getColumnSize()
    {
        return m_columnSize;
    }

    public int getType()
    {
        return m_type;
    }
    
    private String renderImportedKey(ForeignKey fk)
    {
        StringBuilder ret = new StringBuilder();
      if (m_identifier)
      {
          ret.append(renderNormalField(false));
      }
        ret.append("<element name=\""+NameHandler.translateToJavaTypeProperty(fk.getName())+"\" type=\"tns:"+NameHandler.translateToJavaType(fk.getForeignTable())+"\">\n");
        
        ret.append("    <xsd:annotation>\n");
        ret.append("        <xsd:appinfo>\n");
        //ret.append("            <xsd:documentation>Imported key ForeignColumn: "+fk.getForeignColumn()+" LocalColumn: "+fk.getLocalColumn()+" name: "+fk.getName()+" </xsd:documentation>\n");
        ret.append("            <hj:many-to-one>\n");
        ret.append("                <orm:join-column name=\""+fk.getLocalColumn()+"\" />\n");
        ret.append("            </hj:many-to-one>    \n");
        ret.append("        </xsd:appinfo>\n");
        ret.append("   </xsd:annotation>\n");
        ret.append("</element>\n");
        return ret.toString();
    }

    private String renderExportedKey(ForeignKey fk)
    {
        StringBuilder ret = new StringBuilder();
        ret.append("<element name=\""+NameHandler.translateToJavaCollection(m_name)+"\" type=\"tns:"+NameHandler.translateToJavaType(fk.getForeignTable())+"\" maxOccurs=\"unbounded\" minOccurs=\"0\">\n");
        ret.append("    <xsd:annotation>\n");
        ret.append("        <xsd:appinfo>\n");
        //ret.append("            <xsd:documentation>Exported key ForeignColumn: "+fk.getForeignColumn()+" LocalColumn: "+fk.getLocalColumn()+" name: "+fk.getName()+" </xsd:documentation>\n");
//        ret.append("            <hj:one-to-many mapped-by=\""+NameHandler.translateToJavaTypeProperty(fk.getForeignColumnJava())+"\" fetch=\"LAZY\">\n");
        ret.append("            <hj:one-to-many fetch=\"LAZY\">\n");
        ret.append("                <orm:join-column name=\""+fk.getForeignColumn()+"\" />\n");
        ret.append("            </hj:one-to-many>    \n");
        ret.append("        </xsd:appinfo>\n");
        ret.append("   </xsd:annotation>\n");
        ret.append("</element>\n");
        return ret.toString();
    }

    private String renderNormalField(boolean attribute)
    {
        String type = ""+m_type;
        String precision = "";
        switch(m_type)
        {
        case 91:
            type="date";
            break;
        case 1111:
        case 12:
            type="string";
            break;
        case 3:
            if (m_decimalDigits==0)
            {
                type="long";
                precision = "precision=\"38\"";
            }
            else
            {
                type="decimal";
            }
            break;
        case 1:
            type="byte";
            break;
        case -7:
            type="boolean";
            break;
        case 4:
            type="integer";
            break;
        case 93:
            type="dateTime";
            break;
        default:
            log.warn("Unknown type {} on {}",type,m_name);
        }
        String elementType = (attribute?"attribute":"element");
        StringBuilder ret = new StringBuilder();
        ret.append("<xsd:"+elementType+" name=\""+NameHandler.translateToJavaField(m_name)+"\" >\n");
        ret.append("    <xsd:annotation>\n");
        ret.append("        <xsd:appinfo>\n");
        if (m_identifier)
        {
            ret.append("            <hj:id>\n");
            ret.append("                <hj:generated-value strategy=\"AUTO\"/>\n");
            ret.append("                <orm:column name=\""+m_name+"\" nullable=\""+((m_nullable)?"true":"false")+"\" "+precision+"/>\n");
//            <orm:generated-value strategy="SEQUENCE" generator="my-sequence"/>
//            <orm:sequence-generator name="my-sequence" sequence-name="MY_SEQ"/>

            ret.append("            </hj:id>    \n");
        }
        else
        {
            ret.append("            <hj:basic>\n");
            ret.append("                <orm:column name=\""+m_name+"\" nullable=\""+((m_nullable)?"true":"false")+"\" "+precision+"/>\n");
            ret.append("            </hj:basic>    \n");
            
        }
        ret.append("        </xsd:appinfo>\n");
        ret.append("   </xsd:annotation>\n");
        ret.append("   <simpleType>\n");
        ret.append("   <restriction base=\""+type+"\">\n");
        if ((m_type == 1111 || m_type == 12 ) && m_columnSize != 0)
        {
            ret.append("        <maxLength value=\""+m_columnSize+"\"/>\n");  
        }
        if (m_type == 3 && m_decimalDigits != 0)
        {
            ret.append("       <xsd:totalDigits value=\""+m_columnSize+"\"/>\n");
            ret.append("       <xsd:fractionDigits value=\""+m_decimalDigits+"\"/>\n");
        }
        ret.append("   </restriction>\n");
        ret.append("   </simpleType>\n");
        ret.append("</xsd:"+elementType+">\n");
        return ret.toString();
    }

    public String render(boolean compositeKey)
    {
        if ((m_identifier && compositeKey))
        {
            return "";
        }
        if (m_importedKey != null)
        {
            return renderImportedKey(m_importedKey);
        }
        StringBuilder ret = new StringBuilder();
        ret.append(renderNormalField(false));
        for (ForeignKey fk: m_exportedKeys)
        {
            ret.append(renderExportedKey(fk));
        }
        return ret.toString();
    }
    public String renderAsAttribute()
    {
        return renderNormalField(true);
    }

    public boolean isNullable()
    {
        return m_nullable;
    }

    public int getDecimalDigits()
    {
        return m_decimalDigits;
    }

    public void setIdentifier(boolean b)
    {
        m_identifier = b;
        
    }

    public void setImportedKey(ForeignKey fk)
    {
       m_importedKey = fk;
        
    }

    public void addExportedKey(ForeignKey fk)
    {
        m_exportedKeys.add(fk);
        
    }

    public boolean isIdentifier()
    {
        return m_identifier;
    }


}
