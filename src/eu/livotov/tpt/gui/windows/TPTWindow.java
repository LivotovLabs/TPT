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

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Window;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Extends regular ITMill Window with some small but sometimes useful features, such as simplified
 * methods for quick invoke of various types of notifications and so on. It also does all dirty job
 * for registering action handlers for ENTER/ESC key presses listening. All you need is to override
 * the corresponding method to catch the event. Use it just like you use regular Windows class.
 */
public class TPTWindow extends Window implements Action.Handler, Window.CloseListener
{
    private Action enterKeyAction =
            new ShortcutAction ( "Default key", ShortcutAction.KeyCode.ENTER, null );
    private Action escapeKeyAction =
            new ShortcutAction ( "Default key", ShortcutAction.KeyCode.ESCAPE, null );
    private Action[] actions = new Action[]{ enterKeyAction, escapeKeyAction };


    public TPTWindow ()
    {
        super ();
        addActionHandler ( this );
        addListener ( ( CloseListener ) this );
    }

    public TPTWindow ( String title )
    {
        super ( title );
        addActionHandler ( this );
        addListener ( ( CloseListener ) this );
    }

    /**
     * Shows informational message to the user, using ITMill Toolkit notification mechanism. This is
     * actually an equivalent of calling Window.showNotification method with the flag
     * Window.Notification.TYPE_HUMANIZED_MESSAGE
     *
     * @param title  message title
     * @param info   message text
     * @param format set to true if you want your message only (not title) text be reformatted to
     *               make it html-based. Actually, reformat will replace all CR / CRLF codes to
     *               &nbsp;<code>&lt;br&gt;</code> html tags in order to save line break
     *               formatting.
     */
    public void showMessage ( final String title, final String info, boolean format )
    {
        showNotification ( title, format ? applyHtmlFormatting ( info ) : info,
                Window.Notification.TYPE_HUMANIZED_MESSAGE );
    }

    /**
     * Shows error message to the user, using ITMill Toolkit notification mechanism. This is
     * actually an equivalent of calling Window.showNotification method with the flag
     * Window.Notification.TYPE_ERROR_MESSAGE
     *
     * @param title  message title
     * @param info   message text
     * @param format set to true if you want your message only (not title) text be reformatted to
     *               make it html-based. Actually, reformat will replace all CR / CRLF codes to
     *               &nbsp;<code>&lt;br&gt;</code> html tags in order to save line break
     *               formatting.
     */
    public void showErrorMessage ( final String title, final String info, boolean format )
    {
        showNotification ( title, format ? applyHtmlFormatting ( info ) : info,
                Window.Notification.TYPE_ERROR_MESSAGE );
    }

    /**
     * Shows warning message to the user, using ITMill Toolkit notification mechanism. This is
     * actually an equivalent of calling Window.showNotification method with the flag
     * Window.Notification.TYPE_WARNING_MESSAGE
     *
     * @param title  message title
     * @param info   message text
     * @param format set to true if you want your message only (not title) text be reformatted to
     *               make it html-based. Actually, reformat will replace all CR / CRLF codes to
     *               &nbsp;<code>&lt;br&gt;</code> html tags in order to save line break
     *               formatting.
     */
    public void showWarningMessage ( final String title, final String info, boolean format )
    {
        showNotification ( title, format ? applyHtmlFormatting ( info ) : info,
                Window.Notification.TYPE_WARNING_MESSAGE );
    }

    /**
     * Shows small tray (bottom-right corner) notification message to the user, using ITMill Toolkit
     * notification mechanism. This is actually an equivalent of calling Window.showNotification
     * method with the flag Window.Notification.TYPE_TRAY_NOTIFICATION
     *
     * @param title  message title
     * @param info   message text
     * @param format set to true if you want your message only (not title) text be reformatted to
     *               make it html-based. Actually, reformat will replace all CR / CRLF codes to
     *               &nbsp;<code>&lt;br&gt;</code> html tags in order to save line break
     *               formatting.
     */
    public void showNotification ( final String title, final String info, boolean format )
    {
        showNotification ( title, format ? applyHtmlFormatting ( info ) : info,
                Window.Notification.TYPE_TRAY_NOTIFICATION );
    }

    /**
     * Applys some html formatting to the specified text. Actually, it does the following:
     * <p/>
     * <li>All CR/CRLF characters are replaced by &nbsp;<code>&lt;br&gt;</code> tags to keep
     * line-breaking formatting of original text
     *
     * @param text text to be formatted
     * @return formatted text
     */
    public static String applyHtmlFormatting ( final String text )
    {
        return "<br/>" + text.replaceAll ( "\r\n", "<br/>" ).replaceAll ( "\n", "<br/>" )
                .replaceAll ( "\r", "<br/>" );
    }

    /**
     * Fixes the getURL() method in ITMill Toolkit Window class to avoid having slash "/" as the
     * final url character. Having such character changes current web directory context so may cause
     * problems in resources lookup.
     *
     * @return Window URL, with guarantee, that it will not end with the slash character.
     */
    @Override
    public URL getURL ()
    {
        final URL url = super.getURL ();
        final String u = url.toString ();

        if ( u.endsWith ( "/" ) )
        {
            try
            {
                return new URL ( u.substring ( 0, u.length () - 1 ) );
            }
            catch ( MalformedURLException e )
            {
                e.printStackTrace ();
                throw new RuntimeException ( e );
            }
        }
        else
        {
            return url;
        }
    }

    public Action[] getActions ( Object o, Object o1 )
    {
        return actions;
    }

    public void handleAction ( Action action, Object o, Object o1 )
    {
        if ( action == enterKeyAction )
        {
            enterKeyPressed ();
        }

        if ( action == escapeKeyAction )
        {
            escapeKeyPressed ();
        }
    }

    /**
     * Called when window is closed by pressing a close icon at the top-right corner.
     *
     * @param closeEvent close event that came from toolkit
     */
    public void windowClose ( CloseEvent closeEvent )
    {
    }

    /**
     * Called when user presses an ENTER key
     */
    public void enterKeyPressed ()
    {

    }

    /**
     * Called when user presses an ESC key
     */
    public void escapeKeyPressed ()
    {

    }


}
