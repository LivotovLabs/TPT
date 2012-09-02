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
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.livotov.tpt.gui.dialogs.ui.OptionDialogButtonsComponent;

/**
 * A modal dialog that provides some pre-defined selection options or the custom interface. In all
 * cases, key ENTER, pressed during the dialog panel invocation, simulates the "OK" or "Yes" button
 * click and key ESC - "No" or "Cancel" button click.
 */
public class OptionDialog extends AbstractDialog implements Button.ClickListener
{
    protected OptionDialogButtonsComponent buttonsArea = new OptionDialogButtonsComponent ();
    protected OptionDialogResultListener resultReceiver;


    /**
     * Constructs new dialog instance. This does not show the dialog. Use extra showXXX method for
     * this.
     *
     * @param app application instance, that have a main window set and initialized.
     */
    public OptionDialog ( Application app )
    {
        this ( app.getMainWindow () );
    }

    /**
     * Constructs new dialog instance. This does not show the dialog. Use extra showXXX method for
     * this.
     *
     * @param parent window that will act as parent for this dialog
     */
    public OptionDialog ( Window parent )
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

        layout.addComponent ( buttonsArea );

        setWidth ( "450px" );
        setHeight ( "180px" );
    }

    /**
     * Displays the information only dialog, having title, info message and single "OK" button that
     * closes the dialog.
     *
     * @param title          dialog title
     * @param text           information text
     * @param resultListener dialog result listener
     */
    public void showMessageDialog ( String title, String text,
                                    OptionDialogResultListener resultListener )
    {
        buttonsArea.displayButtons ( OptionKind.OK );
        setCaption ( title );
        buttonsArea.setMessage ( text );
        resultReceiver = resultListener;
        buttonsArea.getOptionButton ( OptionKind.OK ).focus ();
        showDialog ();
    }

    /**
     * Displays the confirmation dialog, having title, info message and "OK" / "Cancel" buttons.
     *
     * @param title          dialog title
     * @param text           information text
     * @param resultListener dialog result listener
     */
    public void showConfirmationDialog ( String title, String text,
                                         OptionDialogResultListener resultListener )
    {
        buttonsArea.displayButtons ( OptionKind.OK, OptionKind.CANCEL );
        setCaption ( title );
        buttonsArea.setMessage ( text );
        resultReceiver = resultListener;
        buttonsArea.getOptionButton ( OptionKind.OK ).focus ();
        showDialog ();
    }

    /**
     * Displays the question dialog, having title, info message and "Yes" / "No" buttons.
     *
     * @param title          dialog title
     * @param text           information text
     * @param resultListener dialog result listener
     */
    public void showQuestionDialog ( String title, String text,
                                     OptionDialogResultListener resultListener )
    {
        buttonsArea.displayButtons ( OptionKind.YES, OptionKind.NO );
        setCaption ( title );
        buttonsArea.setMessage ( text );
        resultReceiver = resultListener;
        buttonsArea.getOptionButton ( OptionKind.YES ).focus ();
        showDialog ();
    }

    /**
     * Displays the cancellable question dialog, having title, info message, "Yes", "No" and
     * "Cancel" buttons.
     *
     * @param title          dialog title
     * @param text           information text
     * @param resultListener dialog result listener
     */
    public void showYesNoCancelDialog ( String title, String text,
                                        OptionDialogResultListener resultListener )
    {
        buttonsArea.displayButtons ( OptionKind.YES, OptionKind.NO, OptionKind.CANCEL );
        setCaption ( title );
        buttonsArea.setMessage ( text );
        resultReceiver = resultListener;
        buttonsArea.getOptionButton ( OptionKind.YES ).focus ();
        showDialog ();
    }

    /**
     * Displays the custom dialog, having title, custom provided component and custom buttons.
     *
     * @param title          dialog title
     * @param content        component, to be displayed as a content
     * @param resultListener dialog result listener
     * @param dialogOptions  one or more buttons to display
     */
    public void showCustomDialog ( String title, Component content,
                                   OptionDialogResultListener resultListener,
                                   OptionKind... dialogOptions )
    {
        buttonsArea.displayButtons ( dialogOptions );
        setCaption ( title );
        buttonsArea.setCustomContent ( content );
        resultReceiver = resultListener;
        buttonsArea.getOptionButton ( dialogOptions[ 0 ] ).focus ();
        showDialog ();
    }

    /**
     * Changes the text for a button
     *
     * @param optionKind  button to change text for
     * @param buttonTitle new button text
     */
    public void setButtonText ( OptionKind optionKind, String buttonTitle )
    {
        buttonsArea.setButtonTitle ( optionKind, buttonTitle );
    }

    public void buttonClick ( Button.ClickEvent clickEvent )
    {
        finihDialog ( ( OptionKind ) clickEvent.getButton ().getData () );
    }

    protected void finihDialog ( OptionKind result )
    {
        finishDialogProcessStarted = true;

        try
        {
            hideDialog ();

            if ( resultReceiver != null )
            {
                resultReceiver.dialogClosed ( result );
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
            finihDialog ( OptionKind.CANCEL );
        }
    }

    public void enterKeyPressed ()
    {
        finihDialog ( OptionKind.OK );
    }

    public void escapeKeyPressed ()
    {
        finihDialog ( OptionKind.CANCEL );
    }

    /**
     * Dialog result listener interface
     */
    public static interface OptionDialogResultListener
    {
        /**
         * Called when dialog is closed
         *
         * @param closeEvent indicates the close reason, e.g. which button was pressed.
         */
        void dialogClosed ( OptionKind closeEvent );
    }
}
