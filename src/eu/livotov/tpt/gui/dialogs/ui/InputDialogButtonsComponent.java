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

package eu.livotov.tpt.gui.dialogs.ui;

import com.vaadin.ui.*;
import eu.livotov.tpt.gui.dialogs.OptionKind;
import eu.livotov.tpt.i18n.TM;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: dll Date: Feb 9, 2009 Time: 4:59:25 PM
 */
public class InputDialogButtonsComponent extends CustomComponent
{
    protected CustomLayout layout;

    protected Label message = new Label ();
    protected TextField input = new TextField ();
    protected Button btnOk = new Button ();
    protected Button btnCancel = new Button ();

    public InputDialogButtonsComponent ()
    {
        initUI ();
        updateTitles ();
    }

    public void setMessage ( String message )
    {
        this.message.setValue ( message );
    }

    public void setInputString ( String inputString )
    {
        this.input.setValue ( inputString );
    }

    public String getInputString ()
    {
        return ( String ) this.input.getValue ();
    }

    private void initUI ()
    {
        try
        {
            layout = new CustomLayout ( InputDialogButtonsComponent.class.getResourceAsStream (
                    "InputDialogButtonsComponentLayout.html" ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException (
                    "Unexpected absense of default layout: " + e.getMessage () );
        }

        setCompositionRoot ( layout );

        layout.addComponent ( message, "idbc.message" );
        layout.addComponent ( btnOk, "idbc.btn.ok" );
        layout.addComponent ( btnCancel, "idbc.btn.cancel" );
        layout.addComponent ( input, "idbc.input" );

        input.setWidth ( "100%" );

        btnOk.setData ( OptionKind.OK );
        btnCancel.setData ( OptionKind.CANCEL );
    }

    public void addClickListener ( Button.ClickListener lst )
    {
        btnOk.addListener ( lst );
        btnCancel.addListener ( lst );
    }

    public void removeClickListener ( Button.ClickListener lst )
    {
        btnOk.removeListener ( lst );
        btnCancel.removeListener ( lst );
    }

    public void setButtonTitle ( OptionKind optionKind, String buttonTitle )
    {
        getOptionButton ( optionKind ).setCaption ( buttonTitle );
    }


    public Button getOptionButton ( OptionKind kind )
    {
        switch ( kind )
        {
            case OK:
                return btnOk;
            case CANCEL:
                return btnCancel;
        }

        throw new IllegalArgumentException ( "Unsupported button id: " + kind.name () );
    }

    private void updateTitles ()
    {
        btnOk.setCaption ( TM.get ( "idbc.btn.ok" ) );
        btnCancel.setCaption ( TM.get ( "idbc.btn.cancel" ) );
    }

    public void focusInputField ()
    {
        input.focus ();
    }
}