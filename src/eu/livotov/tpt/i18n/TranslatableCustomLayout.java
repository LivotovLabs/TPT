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
import com.vaadin.ui.CustomLayout;
import eu.livotov.tpt.TPTApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * This is a wrapper for standard CustomLayout class, that will use the internationalized layout
 * file, based on a current application locale. Use it as normal CustomLayout class, however you
 * must prepare the following structure in your theme-name/layouts folder for all of your supported
 * languages:
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * <tt> + ITMILL <br/> + themes <br/> + your_theme <br/> + layouts <br/> + en <br/> my_layout.html
 * <br/> + ru <br/> my_layout.html <br/> my_layout.html <br/> </tt>
 * <p/>
 * <p>Once you call new TranslatableLayout("my_layout"), it will adjust the actual layout name
 * according to the current application locale, for instance, for Russian locale, it will load the
 * following actual layout file: layouts/ru/my_layout.html
 * <p/>
 * <p>In case no layout file for current locale is found, TranslatableLayout will try to find layout
 * file for the default language and then (in case of no success) fallback to the normal
 * CustomLayout behaviour and load layout file from base layouts folder, e.g.
 * theme-name/layouts/my_layout.html
 */
public class TranslatableCustomLayout extends CustomLayout
{

    /**
     * Creates a CustomLayout, trying to load  the appropriate layout file for the current
     * application locale. Note, that in order to use this method, your application must be extended
     * from ToolkitPlusApplication, not from the regular Application object. In case of second,
     * please use second constructor which accepts an application instance.
     *
     * @param layoutName layout template name, without any language prefixes, just like you use it
     *                   in plain CustomLayout class.
     */
    public TranslatableCustomLayout ( String layoutName )
    {
        this ( TPTApplication.getCurrentApplication (), layoutName );
    }

    /**
     * Creates a CustomLayout, trying to load  the appropriate layout file for the given application
     * locale.
     *
     * @param layoutName layout template name, without any language prefixes, just like you use it
     *                   in plain CustomLayout class.
     */
    public TranslatableCustomLayout ( Application app, String layoutName )
    {
        super ( layoutName );

        String actualTemplateName =
                resolveTemplateNameForTheApplicationLocale ( app.getLocale (), layoutName );
        if ( !isLayoutTemplateExists ( app, actualTemplateName ) )
        {
            actualTemplateName = resolveTemplateNameForTheApplicationLocale (
                    new Locale ( TM.getDictionary ().getDefaultLanguage () ), layoutName );

            if ( !isLayoutTemplateExists ( app, actualTemplateName ) )
            {
                actualTemplateName = layoutName;
            }
        }

        setTemplateName ( actualTemplateName );
    }

    /**
     * This constructor cannot be interbationalized because the pre-open input stream is provided.
     * So this will just proxy request to the original CustomLayout implementation. See Javadoc of
     * this constructor in CustomLayout for details.
     *
     * @param inputStream
     * @throws IOException
     */
    public TranslatableCustomLayout ( InputStream inputStream ) throws IOException
    {
        super ( inputStream );    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected String resolveTemplateNameForTheApplicationLocale ( Locale locale,
                                                                  final String templateName )
    {
        return locale.getLanguage ().toLowerCase () + "/" + templateName;
    }

    protected boolean isLayoutTemplateExists ( Application app, final String templateName )
    {
        final File layoutFile = new File ( app.getContext ().getBaseDirectory (),
                String.format ( "VAADIN/themes/%s/layouts/%s.html", app.getTheme (),
                        templateName ) );
        return layoutFile.exists ();
    }


}
