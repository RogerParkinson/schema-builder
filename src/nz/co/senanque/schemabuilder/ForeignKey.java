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
public class ForeignKey
{

    private static final Logger log = LoggerFactory.getLogger(ForeignKey.class);
    private final String m_localColumn;
    private final String m_foreignColumn;
    private final String m_foreignTable;
    private String m_name;
    private String m_foreignColumnJava;

    /**
     * @param localColumn 
     * @param foreignColumn 
     * @param foreignTable 
     * 
     */
    public ForeignKey(String foreignTable, String foreignColumn, String localColumn, String name)
    {
        m_foreignTable = foreignTable;
        m_foreignColumn = foreignColumn;
        m_localColumn = localColumn;
        m_name = name;
    }

    public String getLocalColumn()
    {
        return m_localColumn;
    }

    public String getForeignColumn()
    {
        return m_foreignColumn;
    }

    public String getForeignTable()
    {
        return m_foreignTable;
    }

    public String getName()
    {
        return m_name;
    }

    public void duplicateName(String ref)
    {
        m_name = m_foreignTable+"_"+StringUtils.remove(m_localColumn, "_ID");//+"_"+ref;
    }
    
    public String getForeignColumnJava()
    {
        return m_foreignColumnJava;
    }

    public void setForeignColumnJava(String foreignColumnJava)
    {
        m_foreignColumnJava = foreignColumnJava;
    }

}
