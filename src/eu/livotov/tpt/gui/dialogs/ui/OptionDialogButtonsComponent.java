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
public class OptionDialogButtonsComponent extends CustomComponent
{
    protected CustomLayout layout;

    protected Label message = new Label ();
    protected Button btnOk = new Button ();
    protected Button btnCancel = new Button ();
    protected Button btnYes = new Button ();
    protected Button btnNo = new Button ();

    public OptionDialogButtonsComponent ()
    {
        initUI ();
        updateTitles ();
        displayButtons ( OptionKind.OK, OptionKind.CANCEL );
    }

    public void displayButtons ( OptionKind... buttonIds )
    {
        hideAllButtons ();
        for ( OptionKind buttonId : buttonIds )
        {
            setButtonVisibility ( buttonId, true );
        }
    }

    public void setMessage ( String message )
    {
        this.message.setValue ( message );
    }

    private void hideAllButtons ()
    {
        btnOk.setVisible ( false );
        btnCancel.setVisible ( false );
        btnYes.setVisible ( false );
        btnNo.setVisible ( false );
    }

    private void setButtonVisibility ( OptionKind buttonId, boolean visible )
    {
        switch ( buttonId )
        {
            case OK:
                btnOk.setVisible ( visible );
                break;
            case CANCEL:
                btnCancel.setVisible ( visible );
                break;
            case YES:
                btnYes.setVisible ( visible );
                break;
            case NO:
                btnNo.setVisible ( visible );
                break;
        }
    }


    private void initUI ()
    {
        try
        {
            layout = new CustomLayout ( OptionDialogButtonsComponent.class.getResourceAsStream (
                    "OptionDialogButtonsComponentLayout.html" ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException (
                    "Unexpected absense of default layout: " + e.getMessage () );
        }

        setCompositionRoot ( layout );

        layout.addComponent ( message, "odbc.message" );
        layout.addComponent ( btnOk, "odbc.btn.ok" );
        layout.addComponent ( btnCancel, "odbc.btn.cancel" );
        layout.addComponent ( btnYes, "odbc.btn.yes" );
        layout.addComponent ( btnNo, "odbc.btn.no" );

        btnOk.setData ( OptionKind.OK );
        btnCancel.setData ( OptionKind.CANCEL );
        btnYes.setData ( OptionKind.YES );
        btnNo.setData ( OptionKind.NO );
    }

    public void addClickListener ( Button.ClickListener lst )
    {
        btnOk.addListener ( lst );
        btnCancel.addListener ( lst );
        btnYes.addListener ( lst );
        btnNo.addListener ( lst );
    }

    public void removeClickListener ( Button.ClickListener lst )
    {
        btnOk.removeListener ( lst );
        btnCancel.removeListener ( lst );
        btnNo.removeListener ( lst );
        btnYes.removeListener ( lst );
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
            case YES:
                return btnYes;
            case NO:
                return btnNo;
        }

        throw new IllegalArgumentException ( "Unsupported button id: " + kind.name () );
    }

    private void updateTitles ()
    {
        btnOk.setCaption ( TM.get ( "odbc.btn.ok" ) );
        btnCancel.setCaption ( TM.get ( "odbc.btn.cancel" ) );
        btnYes.setCaption ( TM.get ( "odbc.btn.yes" ) );
        btnNo.setCaption ( TM.get ( "odbc.btn.no" ) );
    }

    public void setCustomContent ( Component content )
    {
        layout.removeComponent ( message );
        layout.addComponent ( content, "odbc.message" );
    }
}
