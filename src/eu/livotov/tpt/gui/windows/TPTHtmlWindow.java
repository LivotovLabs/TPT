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

package eu.livotov.tpt.gui.windows;

import com.vaadin.ui.Component;
import eu.livotov.tpt.i18n.TranslatableCustomLayout;

import java.io.IOException;
import java.io.InputStream;

/**
 * A Window, that contains an HTML code as its background, with an actual dynamic compoment,
 * inserted at the specified location. Location must be specified using ITMill custom layout naming
 * rule, e.g. by providing an exact line &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
 * somewhere in the html code, supplied as background.
 */
public class TPTHtmlWindow extends TPTWindow
{
    public static final String CONTENT_LOCATION_ID = "content";

    private TranslatableCustomLayout layout;
    private Component currentMainContent;

    /**
     * Constructs window
     *
     * @param name           window name, that is used in URLs
     * @param htmlBackground custom layout file name (without extension) to use for both layout and
     *                       background of this window. The specified layout file must contain a
     *                       &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
     *                       declaration, where the actual content will be inserted.
     */
    public TPTHtmlWindow ( final String name, final String htmlBackground )
    {
        super ();
        setName ( name );
        setWindowBackground ( htmlBackground );
    }

    /**
     * Constructs window also providing the content
     *
     * @param name           window name, that is used in URLs
     * @param htmlBackground custom layout file name (without extension) to use for both layout and
     *                       background of this window. The specified layout file must contain a
     *                       &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
     *                       declaration, where the actual content will be inserted.
     * @param content        actual component to be displayed in the window in the location,
     *                       specified by &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
     *                       tag of custom layout file.
     */
    public TPTHtmlWindow ( final String name, String htmlBackground, Component content )
    {
        super ();
        setName ( name );
        setWindowBackground ( htmlBackground );
        setContent ( content );
    }

    /**
     * Constructs window
     *
     * @param name           window name, that is used in URLs
     * @param htmlBackground custom layout data stream to use for both layout and background of this
     *                       window. The specified layout file must contain a
     *                       &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
     *                       declaration, where the actual content will be inserted.
     */
    public TPTHtmlWindow ( final String name, InputStream htmlBackground ) throws IOException
    {
        super ();
        setName ( name );
        setWindowBackground ( htmlBackground );
    }

    /**
     * Constructs window also providing the content
     *
     * @param name           window name, that is used in URLs
     * @param htmlBackground custom layout data stream to use for both layout and background of this
     *                       window. The specified layout file must contain a
     *                       &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
     *                       declaration, where the actual content will be inserted.
     * @param content        actual component to be displayed in the window in the location,
     *                       specified by &nbsp;<code>&lt;div&nbsp;location="content"&gt;&lt;/div&gt;</code>
     *                       tag of custom layout file.
     */
    public TPTHtmlWindow ( final String name, InputStream htmlBackground, Component content )
            throws IOException
    {
        super ();
        setName ( name );
        setWindowBackground ( htmlBackground );
        setContent ( content );
    }

    /**
     * Sets new or changes the active component of the window. Component is placed into the
     * location, described with attribute location="content"
     *
     * @param content new component that is used as window main component
     */
    public void setContent ( Component content )
    {
        if ( content != null )
        {
            removeComponent ( CONTENT_LOCATION_ID );
            addComponent ( content, CONTENT_LOCATION_ID );
            currentMainContent = content;
        }
    }

    /**
     * Allows to add more components into the window's background layout by providing custom layout
     * connection slot name (location name)
     *
     * @param component  component to be added to the window
     * @param locationId name of the location where component should be inserted
     */
    public void addComponent ( Component component, String locationId )
    {
        layout.addComponent ( component, locationId );
    }

    /**
     * Removes component by it's location id
     *
     * @param locationId
     */
    public void removeComponent ( String locationId )
    {
        layout.removeComponent ( locationId );
    }

    /**
     * Sets new background layout for the window. This will transfer your main active component as
     * well but will not transfer all extra components, added to previoud background by
     * addComponent(...) method.
     *
     * @param htmlCode layout file name (without extension)
     */
    public void setWindowBackground ( final String htmlCode )
    {
        if ( layout != null )
        {
            layout.removeAllComponents ();
        }

        layout = new TranslatableCustomLayout ( htmlCode );
        setLayout ( layout );
        setContent ( currentMainContent );
    }

    /**
     * Sets new background layout for the window. This will transfer your main active component as
     * well but will not transfer all extra components, added to previoud background by
     * addComponent(...) method.
     *
     * @param htmlCode layout data stream
     * @throws IOException in case of layout stream loading errors
     */
    public void setWindowBackground ( InputStream htmlCode ) throws IOException
    {
        if ( layout != null )
        {
            layout.removeAllComponents ();
        }

        layout = new TranslatableCustomLayout ( htmlCode );
        setLayout ( layout );
        setContent ( currentMainContent );
    }

}
