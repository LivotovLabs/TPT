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

package eu.livotov.tpt.gui.dialogs;

import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.livotov.tpt.gui.dialogs.ui.DownloadDialogButtonsComponent;

import java.io.File;

/**
 * A modal dialog that shows a title, informational message and a link to a file or resource, that
 * can be clicked by the used and downloaded. Dialog also provides the "Close" button to be closed
 * as well as reacts for ENTER and ESC keys. Dialog does not provide any result but just an event
 * that it was closed.
 */
public class DownloadDialog extends AbstractDialog implements Button.ClickListener
{
    protected DownloadDialogButtonsComponent buttonsArea;
    protected DownloadDialogResultListener resultReceiver;


    /**
     * Constructs new dialog instance. This does not show the dialog. Use extra showXXX method for
     * this.
     *
     * @param app application instance, that have a main window set and initialized.
     */
    public DownloadDialog ( Application app )
    {
        this ( app.getMainWindow () );
    }

    /**
     * Constructs new dialog instance. This does not show the dialog. Use extra showXXX method for
     * this.
     *
     * @param parent window, that will act as a parent for this dialog
     */
    public DownloadDialog ( Window parent )
    {
        super ( parent );
        initUI ();
        initActions ();
    }

    private void initActions ()
    {
        buttonsArea.addClickListener ( this );
    }

    private void initUI ()
    {
        VerticalLayout layout = new VerticalLayout ();
        setLayout ( layout );

        layout.setMargin ( true );
        layout.setSpacing ( true );
        layout.setHeight ( "100%" );
        layout.setWidth ( "100%" );

        buttonsArea = new DownloadDialogButtonsComponent ( parentWindow.getApplication () );
        layout.addComponent ( buttonsArea );

        setWidth ( "450px" );
        setHeight ( "240px" );
    }

    /**
     * Displays the dialog, blocking the parent window.
     *
     * @param title          dialog title
     * @param message        informational message
     * @param target         resource that represents the data to be downloaded
     * @param targetName     name of the resource, to be displayed as download link
     * @param resultListener dialog close event listener, that is fired once dialog is closed by the
     *                       user.
     */
    public void showDownloadDialog ( String title, String message, Resource target,
                                     String targetName,
                                     DownloadDialogResultListener resultListener )
    {
        setCaption ( title );
        buttonsArea.setMessage ( message );
        buttonsArea.setDownloadTarget ( target, targetName );
        resultReceiver = resultListener;
        showDialog ();
    }

    /**
     * Displays the dialog, blocking the parent window. In order for this method to work, an
     * Application must be associated with this dialog parent window, as in order to covert a File
     * target into the ITMill resource, parent window's .getApplication() method will be called and
     * must return not null.
     *
     * @param title          dialog title
     * @param message        informational message
     * @param target         file (at the server side) that represents the data to be downloaded
     * @param targetName     name of the resource, to be displayed as download link
     * @param resultListener dialog close event listener, that is fired once dialog is closed by the
     *                       user.
     */
    public void showDownloadDialog ( String title, String message, File target, String targetName,
                                     DownloadDialogResultListener resultListener )
    {
        showDownloadDialog ( title, message,
                new FileResource ( target, parentWindow.getApplication () ), targetName,
                resultListener );
    }

    /**
     * Simulates "Close" button click to close the dialog.
     *
     * @param clickEvent
     */
    public void buttonClick ( Button.ClickEvent clickEvent )
    {
        finishDialog ();
    }

    protected void finishDialog ()
    {
        finishDialogProcessStarted = true;
        try
        {
            hideDialog ();

            if ( resultReceiver != null )
            {
                resultReceiver.dialogClosed ();
            }
        }
        catch ( Throwable err )
        {
            throw new RuntimeException (
                    "Invalid option dialog state or buttons: " + err.getMessage () );
        }
    }

    public void windowClose ( CloseEvent closeEvent )
    {
        if (!finishDialogProcessStarted)
        {
            finishDialog ();
        }
    }

    @Override
    public void showDialog ()
    {
        buttonsArea.focus ();
        super.showDialog ();
    }

    public void enterKeyPressed ()
    {
        finishDialog ();
    }

    public void escapeKeyPressed ()
    {
        finishDialog ();
    }

    /**
     * Dialog close event listener interface
     */
    public static interface DownloadDialogResultListener
    {
        /**
         * Called when dialog is closed by the "X" icon in the upper-right cornet, "Close" button or
         * ENTER/ESC keys.
         */
        void dialogClosed ();
    }
}