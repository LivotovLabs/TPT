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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * A dictionary object, that holds translation data for all instances of a single application. In
 * most cases this class shoud not be instantiated directly, use a TM front-end instead.
 */
public class Dictionary implements Serializable
{
    /**
     * Contains references to loaded properties files for the set of languages
     */
    protected HashMap<String, Properties> languages = new HashMap<String, Properties> ();

    /**
     * Contains references to loaded properties files for the set of countries
     */
    protected HashMap<String, Properties> countries = new HashMap<String, Properties> ();

    /**
     * Default language. If translation key for the specified language is not found, a default
     * language will be tried and only if it also fails, a key name is returned instead.
     */
    private String defaultLanguage = "en";


    /**
     * Provides a translation of the given key for the current application instance, using this
     * instance language (locale). Note, that this method will only work if your application extends
     * ToolkitPlusApplication. If your application is based in plain ITMill Toolkit Application,
     * please use another version of this method witth the additional parameter.
     *
     * @param key key to get translated string for
     * @return translated string or the key name itself, if no translated string found
     */
    public String get ( final String key )
    {
        return get ( TPTApplication.getCurrentApplication (), key );
    }

    /**
     * Provides a translation of the given key for the given application instance, using this
     * instance language (locale).
     *
     * @param app application instance. It is used to extact locale to be used in translation
     * @param key key to get translated string for
     * @return translated string or the key name itself, if no translated string found
     */
    public String get ( Application app, final String key )
    {
        final Locale locale = app.getLocale ();
        return get ( locale.getLanguage (), locale.getCountry (), key );
    }

    /**
     * Provides a translation of the given key for the given locale
     *
     * @param locale locale
     * @param key    key to get translated string for
     * @return translated string or the key name itself, if no translated string found
     */
    public String get ( Locale locale, final String key )
    {
        return get ( locale.getLanguage (), locale.getCountry (), key );
    }

    /**
     * Provides a translation of the given key for the given language and country codes
     *
     * @param lang    language 2 letter code. Cannot be nullor empty
     * @param country country 2 letter code. Can be null or empty, in this case, generic translation
     *                for the country is used.
     * @param key     key to get translated string for
     * @return translated string or the key name itself, if no translated string found
     */
    public String get ( final String lang, final String country, final String key )
    {
        Properties bundle = getLanguageBundle ( lang, country );
        return bundle.getProperty ( key,
                getLanguageBundle ( getDefaultLanguage (), "" ).getProperty ( key, key ) );
    }

    /**
     * Loads data into dictionary from a property file.
     *
     * @param lang      2 letter language code, the file being loaded refers to. Cannot be null or
     *                  empty.
     * @param country   2 letter country code, the file being loaded rerefs to. Can be null or
     *                  empty.
     * @param resource  URL to a property file from classpath resource. Note, that resource contents must
     *                  be in UTF-8 encoding
     * @param overwrite if set to false, dictionary will allow any unique url to be loaded only
     *                  once. This is useful ehrn you load translations in the application init or
     *                  constructor method, which is called for all sessions. Having overwrite
     *                  parameter set to false, you prevents spending system resources for unneeded
     *                  loads of the same data.
     * @throws IOException if any IO error occurs
     */
    public void loadWords ( final String lang, final String country, URL resource,
                            boolean overwrite ) throws IOException
    {
        Properties language = getLanguageBundle ( lang, country );

        if ( !language.containsKey ( resource.toString () ) || overwrite )
        {
            language.put ( resource.toString (), "1" );
            loadWords ( lang, country, new InputStreamReader( resource.openStream (), "utf-8" ));
        }
    }

    /**
     * Loads data into dictionary from a property file.
     *
     * @param lang    2 letter language code, the file being loaded refers to. Cannot be null or
     *                empty.
     * @param country 2 letter country code, the file being loaded rerefs to. Can be null or empty.
     * @param data    input stream for the translation property file contents to be loaded.
     * @throws IOException if any IO error occurs
     */
    public void loadWords ( final String lang, final String country, InputStream data )
            throws IOException
    {
        Properties language = getLanguageBundle ( lang, country );

        language.load ( data );

        try
        {
            data.close ();
        }
        catch ( Throwable err )
        {
            // no-op
        }
    }

    /**
     * Loads data into dictionary from a reader object. This allow to eliminate using native2ascii
     * utility for non-english contents of i18n files
     *
     * @param lang    2 letter language code, the file being loaded refers to. Cannot be null or
     *                empty.
     * @param country 2 letter country code, the file being loaded rerefs to. Can be null or empty.
     * @param data    reader instance for the translation property file contents to be loaded.
     * @throws IOException if any IO error occurs
     */
    public void loadWords ( final String lang, final String country, Reader data )
            throws IOException
    {
        Properties language = getLanguageBundle ( lang, country );

        language.load ( data );

        try
        {
            data.close ();
        }
        catch ( Throwable err )
        {
            // no-op
        }
    }

    /**
     * Loads data into dictionary from a reader object. This allow to eliminate using native2ascii
     * utility for non-english contents of i18n files
     *
     * @param lang    2 letter language code, the file being loaded refers to. Cannot be null or
     *                empty.
     * @param country 2 letter country code, the file being loaded rerefs to. Can be null or empty.
     * @param file    i18n file. Note, the contents must be in UTF-8 encoding.
     * @throws IOException if any IO error occurs
     */
    public void loadWords ( String lang, String country, File file, boolean overwrite )
            throws IOException
    {
        Properties language = getLanguageBundle ( lang, country );

        if ( !language.containsKey ( file.getAbsolutePath () ) || overwrite )
        {
            language.put ( file.getAbsolutePath (), "1" );
            loadWords ( lang, country, new InputStreamReader (new FileInputStream ( file ), "utf-8") );
        }

    }


    private Properties getLanguageBundle ( String lang, String country )
    {
        Properties bundle;

        if ( lang == null || "".equalsIgnoreCase ( lang ) )
        {
            throw new IllegalArgumentException ( "Language code cannot be null or empty. " + lang );
        }

        final String _language = lang.toLowerCase ();
        final String _country = country == null ? "" : country.toLowerCase ();

        bundle = countries.get ( _language + "_" + _country );

        if ( bundle == null )
        {
            bundle = languages.get ( _language );
        }

        if ( bundle == null )
        {
            bundle = new Properties ();
            languages.put ( _language, bundle );
            if ( !"".equals ( _country ) )
            {
                countries.put ( _language + "_" + _country, bundle );
            }
        }

        return bundle;
    }

    /**
     * Provides current default language 2 letter code that is used by the dictionary in case key
     * being queried is not found in the target language.
     *
     * @return current language 2 letter code
     */
    public String getDefaultLanguage ()
    {
        return defaultLanguage;
    }

    /**
     * Sets the new current language 2 letter code.
     *
     * @param defaultLanguage new 2 letter code to be used as a current language.
     */
    public void setDefaultLanguage ( String defaultLanguage )
    {
        this.defaultLanguage = defaultLanguage;
    }

    public void loadTranslationFilesFromThemeFolder ( File themeFolder ) throws IOException
    {
        File I18NFolder = new File ( themeFolder, "i18n" );

        if ( I18NFolder.exists () && I18NFolder.isDirectory () )
        {
            File[] dirs = I18NFolder.listFiles ();

            for ( File dir : dirs )
            {
                if ( dir.isDirectory () )
                {
                    loadTranslationsFromLanguageDirectory ( dir );
                }
            }
        }
    }

    public void loadTranslationsFromLanguageDirectory ( File folder ) throws IOException
    {
        File[] files = folder.listFiles ();

        String languageCode = extractLanguageCodeFromString ( folder.getName () );
        String countryCode = extractCountryCodeFromString ( folder.getName () );

        for ( File file : files )
        {
            if ( file.getName ().toLowerCase ().endsWith ( ".properties" ) )
            {
                loadWords ( languageCode, countryCode, file, true );
            }
        }
    }

    protected String extractCountryCodeFromString ( String name )
    {
        try
        {
            StringTokenizer tok = new StringTokenizer ( name, "_", false );
            tok.nextToken ();
            return tok.nextToken ();
        }
        catch ( Throwable err )
        {
            return null;
        }
    }

    protected String extractLanguageCodeFromString ( String name )
    {
        try
        {
            StringTokenizer tok = new StringTokenizer ( name, "_", false );
            return tok.nextToken ();
        }
        catch ( Throwable err )
        {
            return null;
        }
    }

    public void clear()
    {
        languages.clear();
        countries.clear();
    }

}
