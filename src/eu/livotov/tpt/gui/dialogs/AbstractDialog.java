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
import com.vaadin.ui.Window;
import eu.livotov.tpt.gui.windows.TPTWindow;
import eu.livotov.tpt.i18n.Dictionary;
import eu.livotov.tpt.i18n.TM;

import java.io.IOException;

/**
 * Abstract dialog, that is used for all option dialogs in this package. Provides support for
 * translation files initialization as well as show/hide methods.
 */
public abstract class AbstractDialog extends TPTWindow
{
    protected Window parentWindow;
    protected volatile boolean finishDialogProcessStarted;

    public AbstractDialog ( Application app )
    {
        this ( app.getMainWindow () );
    }

    public AbstractDialog ( Window parent )
    {
        super ();
        this.parentWindow = parent;
        setModal ( true );
        initTranslatoins ();
    }

    private void initTranslatoins ()
    {
        Dictionary dict = TM.getDictionary ();

        try
        {
            dict.loadWords ( "en", "us",
                    AbstractDialog.class.getResource ( "i18n/en_us.properties" ), false );
            dict.loadWords ( "ru", "ru",
                    AbstractDialog.class.getResource ( "i18n/ru_ru.properties" ), false );
        }
        catch ( IOException io )
        {
            //no-op
        }
    }

    public void showDialog ()
    {
        finishDialogProcessStarted = false;
        parentWindow.addWindow ( this );
        center ();
    }

    public void hideDialog ()
    {
        try
        {
            parentWindow.removeWindow ( this );
        } catch (Throwable err)
        {
            err.printStackTrace();
        }
    }

}
