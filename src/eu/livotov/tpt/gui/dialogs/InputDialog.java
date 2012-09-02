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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.livotov.tpt.gui.dialogs.ui.InputDialogButtonsComponent;

/**
 * Modal dialog, that prompts for input. Displays title, informational message and an input text
 * field, that can be used to provide some information from an application user. It also provides
 * "OK" and "Cancel" buttons as well as reacts to ENTER/ESC keys.
 */
public class InputDialog extends AbstractDialog implements Button.ClickListener
{
    protected InputDialogButtonsComponent buttonsArea = new InputDialogButtonsComponent ();
    protected InputDialogResultListener resultReceiver;


    /**
     * Constructs new dialog instance. This does not show the dialog. Use extra showXXX method for
     * this.
     *
     * @param app application instance, that have a main window set and initialized.
     */
    public InputDialog ( Application app )
    {
        this ( app.getMainWindow () );
    }

    /**
     * Constructs new dialog instance. This does not show the dialog. Use extra showXXX method for
     * this.
     *
     * @param parent window that will act as parent for this dialog
     */
    public InputDialog ( Window parent )
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
     * Shows the dialog, blocking the parent window until closed.
     *
     * @param title          dialog title
     * @param text           informational message
     * @param defaultValue   default value, can be empty or null
     * @param resultListener listener to listen for dialog result
     */
    public void showInputDialog ( String title, String text, String defaultValue,
                                  InputDialogResultListener resultListener )
    {
        setCaption ( title );
        buttonsArea.setMessage ( text );
        buttonsArea.setInputString ( defaultValue != null ? defaultValue : "" );
        resultReceiver = resultListener;
        showDialog ();
    }

    /**
     * Sets the custom text for one of dialog buttons.
     *
     * @param optionKind  button to change text for. Valid values are OK or CANCEL.
     * @param buttonTitle new button title
     */
    public void setButtonText ( OptionKind optionKind, String buttonTitle )
    {
        buttonsArea.setButtonTitle ( optionKind, buttonTitle );
    }

    public void buttonClick ( Button.ClickEvent clickEvent )
    {
        finishDialog ( ( OptionKind ) clickEvent.getButton ().getData () );
    }

    protected void finishDialog ( OptionKind result )
    {
        finishDialogProcessStarted=true;

        try
        {
            hideDialog ();

            if ( resultReceiver != null )
            {
                resultReceiver.dialogClosed ( result, buttonsArea.getInputString () );
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
            finishDialog ( OptionKind.CANCEL );
        }
    }

    @Override
    public void showDialog ()
    {
        buttonsArea.focusInputField ();
        super.showDialog ();
    }

    public void enterKeyPressed ()
    {
        finishDialog ( OptionKind.OK );
    }

    public void escapeKeyPressed ()
    {
        finishDialog ( OptionKind.CANCEL );
    }

    /**
     * Listener interface to receive dialog close event and result
     */
    public static interface InputDialogResultListener
    {
        /**
         * Called when dialog is closed by the user
         *
         * @param closeEvent   button id, that caused this dialog to be closed. Can be OK if user
         *                     pressed "OK" button or ENTER key or CANCEL - in case user pressed
         *                     "Cancel", "X" buttons or ESC key.
         * @param inputMessage input text as it was typed by a user. The text is available
         *                     regardless of how user closed the dialog.
         */
        void dialogClosed ( OptionKind closeEvent, String inputMessage );
    }
}