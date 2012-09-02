/*******************************************************************************
 * Copyright 2009, Dmitri Livotov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package eu.livotov.tpt.i18n;

import com.vaadin.Application;
import eu.livotov.tpt.TPTApplication;
import java.io.Serializable;

import java.util.HashMap;

/**
 * Translation manager - a front-end for toolkit-enabled application for getting locale-dependant
 * strings for application messages. This static class should be used for both - translation queries
 * and loading/changing dictionary files.
 * <p/>
 * <p/>
 * <p/>
 * <h4>Typical use:</h4> <li>In application startup, load translation files for each language you
 * want to support. Translation files are regular java .properties files you use for SWING
 * application internationalization: TM.getDictionary().loadWords(...); <li>Use TM.get ( "xxx" )
 * instead of hardcoded string anywhere in your application code. xxx is an entry key. The method
 * will return a valid translated text for the current application instance language or the key
 * itself, if no translation for the current language is found.
 * <p/>
 * <p>For more information please see the corresponding chapter in developer documentation.
 */
public class TM implements Serializable
{

    /**
     * Repository of dictionaries for all applications that are running. Note, that single
     * dictionary is used for all instances (sessions) of such application.
     */
    protected static HashMap<String, Dictionary> translationDictionaries =
            new HashMap<String, Dictionary> ();


    /**
     * Provides the translated string by the given key. ote, that this method only works if your
     * application extends ToolkitPlusApplication. For plain ITMill Toolkit applications please use
     * another method, that accepts application instance as a parameter.
     *
     * @param key key to get string for
     * @param params - set of String.format parameters to parse
     * @return key translation, for the current application instance language
     */
    public static String get ( final String key, Object... params )
    {
        String phrase = getDictionary ().get ( key );

        if ( params != null && params.length>0)
        {
            return String.format(phrase, params);
        } else
        {
            return phrase;
        }
    }

    /**
     * Provides the translated string by the given key
     *
     * @param app application to look for the keys in
     * @param key key to get string for
     * @param params - set of String.format parameters to parse
     * @return key translation, for the current application instance language
     */
    public static String get ( Application app, final String key, Object... params )
    {
        String phrase = getDictionary ( app ).get ( app, key );

        if ( params != null && params.length>0)
        {
            return String.format(phrase, params);
        } else
        {
            return phrase;
        }
    }

    /**
     * Proxy method for easy setting the default language for the current app dictionary
     * @param lang
     */
    public static void setDefaultLanguage( String lang )
    {
        getDictionary().setDefaultLanguage(lang);
    }



    /**
     * Provides the dictionary object, used for all instances of the current application. Note, that
     * this method only works if your application extends ToolkitPlusApplication. For plain ITMill
     * Toolkit applications please use another method, that accepts application instance as a
     * parameter.
     *
     * @return dictionary object
     */
    public static Dictionary getDictionary ()
    {
        return getDictionary ( TPTApplication.getCurrentApplication () );
    }

    /**
     * Provides the dictionary object, used for all instances of the specified application
     *
     * @param app application
     * @return dictionary object
     */
    public static Dictionary getDictionary ( Application app )
    {
        if ( !translationDictionaries.containsKey ( app.getClass ().getCanonicalName () ) )
        {
            translationDictionaries.put ( app.getClass ().getCanonicalName (), new Dictionary () );
        }
        return translationDictionaries.get ( app.getClass ().getCanonicalName () );
    }

}
