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

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.*;
import eu.livotov.tpt.i18n.TM;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: dll Date: Feb 9, 2009 Time: 4:59:25 PM
 */
public class DownloadDialogButtonsComponent extends CustomComponent
{
    protected CustomLayout layout;

    protected Label message = new Label ();
    protected Link link = new Link ();
    protected Button btnClose = new Button ();

    private Application appReference;

    public DownloadDialogButtonsComponent ( Application app )
    {
        appReference = app;
        initUI ();
        updateTitles ();
    }

    public void setMessage ( String message )
    {
        this.message.setValue ( message );
    }

    public void setDownloadTarget ( Resource target, String name )
    {
        this.link.setResource ( target );
        this.link.setCaption ( " " + name );
    }

    private void initUI ()
    {
        try
        {
            layout = new CustomLayout ( DownloadDialogButtonsComponent.class.getResourceAsStream (
                    "DownloadDialogButtonsComponentLayout.html" ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException (
                    "Unexpected absense of default layout: " + e.getMessage () );
        }

        setCompositionRoot ( layout );

        layout.addComponent ( message, "ddbc.message" );
        layout.addComponent ( link, "ddbc.link" );
        layout.addComponent ( btnClose, "ddbc.btn.close" );

        link.setIcon ( new ClassResource ( DownloadDialogButtonsComponent.class, "download.png",
                appReference ) );
        link.setTargetName ( "_new" );
    }

    public void addClickListener ( Button.ClickListener lst )
    {
        btnClose.addListener ( lst );
    }

    public void removeClickListener ( Button.ClickListener lst )
    {
        btnClose.removeListener ( lst );
    }

    public void setCloseButtonTitle ( String buttonTitle )
    {
        btnClose.setCaption ( buttonTitle );
    }

    private void updateTitles ()
    {
        btnClose.setCaption ( TM.get ( "ddbc.btn.close" ) );
    }

    public void focus ()
    {
        btnClose.focus ();
    }
}