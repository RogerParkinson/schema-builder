/*
 * Created on 14/04/2011
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

/**
 * 
 * Short description
 * 
 * @author Roger Parkinson
 * @version $Revision:$
 */
public class NameHandler
{

    public static String translateToJavaField(String name)
    {
        return initialLowerCase(fixCase(name));
    }
    public static String translateToJavaCollection(String name)
    {
        return pluralize(initialLowerCase(fixCase(StringUtils.remove(name,"_ID"))));
    }
    public static String translateToJavaType(String name)
    {
        return singularize(fixCase(name));
    }

    private static String singularize(String ret)
    {
        if (ret.endsWith("ss"))
        {
            return ret;
        }
        if (ret.endsWith("es"))
        {
            return ret.substring(0,ret.length()-1);
        }
        if (ret.endsWith("s"))
        {
            return ret.substring(0,ret.length()-1);
        }
        return ret;
    }
    private static String pluralize(String name)
    {
        String ret = name;
        if (ret.endsWith("s"))
        {
            ret = ret + "es";
        }
        else
        {
            ret = ret + "s";
        }
        return ret;
    }
    private static String initialLowerCase(String str)
    {
        if (str == null)
        {
            return str;
        }
        if (str.length() < 2)
        {
            return StringUtils.lowerCase(str);
        }
        return Character.toLowerCase(str.charAt(0))+str.substring(1);
    }
    private static String fixCase(String str)
    {
        StringBuilder buff = new StringBuilder();

        String[] tokens = str.split("_");
        for (String i : tokens) {
            buff.append(StringUtils.capitalize(i.toLowerCase()));
        }
        return buff.toString();
    }
    public static String translateToJavaTypeProperty(String name)
    {
        return initialLowerCase(translateToJavaType(name));
    }

}
