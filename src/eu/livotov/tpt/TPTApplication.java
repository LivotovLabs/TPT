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
package eu.livotov.tpt;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import eu.livotov.tpt.i18n.Dictionary;
import eu.livotov.tpt.i18n.TM;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the abstract class to be used instead of regular Vaadin Application in order to
 * get some extended features, that TPT provides. Use this class absolutely the same way as you use
 * plain Application class, the only difference is that instead of init() method you need to use
 * applicationInit(). However, you may still init(), just do not forget to put super.init() at
 * the beginning (as the first line of your overridden method body).
 */
public abstract class TPTApplication extends Application
        implements ApplicationContext.TransactionListener
{

    static ThreadLocal<TPTApplication> currentApplication = new ThreadLocal<TPTApplication> ();
    private Dictionary internationalizationDictionary;
    private static AtomicBoolean firstInstanceCreated = new AtomicBoolean ( false );

    /**
     * Default application constructor. Here we additionaly put the current instance into the
     * ThreadLocal registry in case somebody will want to ask for the application istance before
     * ITMill Toolkit will call init() method.
     */
    public TPTApplication ()
    {
        super ();
        currentApplication.set ( this );
    }

    /**
     * Standard ITMill Toolkit abstract init() method is implemented here for automatic registration of our application
     * as a TransactionListener. End-users have to use applicationInit() instead of init() in
     * their applications. But nothing stops you from overriding init() in your application, just do
     * not forget to call super.init() as the first statement in your override.
     */
    public void init ()
    {
        currentApplication.set ( this );
        cleanupPreviousResources();
        getContext ().addTransactionListener ( this );
        initializeInternationalizationFramework ();
        applicationInit ();

        if ( !firstInstanceCreated.getAndSet ( true ) )
        {
            firstApplicationStartup ();
        }
    }

    /**
     * Self-provider, used to gen an actual instance of the class when it is wrapped to CDI or SessionBean proxy object.
     * @return self instance
     */
    public TPTApplication getSelf()
    {
        return this;
    }

    /**
     * Performs initialization of i18n part of TPT. It creates a dictionary, scans and loads all
     * property files from theme-name/i18n folder.
     */
    private synchronized void initializeInternationalizationFramework ()
    {
        if ( internationalizationDictionary != null )
        {
            return;
        }

        internationalizationDictionary = TM.getDictionary ( this );
        loadInternationalizationFiles ();
    }

    /**
     * Loads property files with translations from the theme-name/i18n folder (if exists)
     */
    private void loadInternationalizationFiles ()
    {
        File themeFolder = new File ( getContext ().getBaseDirectory (),
                String.format ( "VAADIN/themes/%s", getTheme () ) );

        if ( themeFolder.exists () && themeFolder.isDirectory () )
        {
            try
            {
                internationalizationDictionary.loadTranslationFilesFromThemeFolder ( themeFolder );
            }
            catch ( IOException e )
            {
                System.err.println (
                        String.format ( "Cannot load translation files from the theme folder %s",
                        themeFolder ) );
                e.printStackTrace ();
            }
        }
    }

    /**
     * This method is overridden as we want to reload theme-specific internationalization
     * translation files from the theme's i18n folder once theme is changed.
     *
     * @param themeName new theme name
     */
    @Override
    public void setTheme ( String themeName )
    {
        super.setTheme ( themeName );
        loadInternationalizationFiles ();
    }

    /**
     * This method must be implemented by a final application, exactly the same way as init() method
     * had to be implemented in a regular Vaadin Application. As init() method is used for
     * TPT specific initialization, please use applicationInit() instead. In case you want
     * to use exactly init() one, please override it but do not forget to add super.init() as
     * the first line of the overridden method.
     */
    public abstract void applicationInit ();

    /**
     * <p>Another application initialization callback. It is called after init() / applicationInit()
     * but only once per application class - e.g. this method is called when the first application
     * instance is started. All subsequent instances of the same application (until the web contaner
     * restart) will not cause this method to be called. You can place any shared or static
     * resources initialization here, that are common to all application sessions and should be
     * initialized only once.</p>
     *
     * <p>Please note, that if you're running your application on a cluster, this method will be called
     * in every cluster instance, so you have to care on this yourself.</p>
     */
    public abstract void firstApplicationStartup ();

    /**
     * Overrides ITMill Toolkit close() method to add some resources cleanup (in the future),
     * related to TPT. Please feel free to override this method in your application as well,
     * however, do not forget to call super.close() in your override.
     */
    @Override
    public void close ()
    {
        super.close();
    }

    /**
     * Adds a reference to the application instance into the current thread. This allows us to get
     * an application instance by calling ToolkitPlusApplication.getCurrentApplication() in any place of your
     * application code.
     *
     * @param application
     * @param o
     */
    public void transactionStart ( Application application, Object o )
    {
       if ( application instanceof TPTApplication && ((TPTApplication)application).getSelf() == this )
       {
           currentApplication.set ( this );
       }
    }

    /**
     * Once thread finishes, removes the application instance reference from it.
     *
     * @param application
     * @param o
     */
    public void transactionEnd ( Application application, Object o )
    {
       if (application instanceof TPTApplication && ((TPTApplication)application).getSelf() == this )
       {
           currentApplication.remove ();
       }
    }

    /**
     * Creates and starts a new thread. This method must be used instead of <code>new
     * Thread(...)</code> construction when starting server threads if you wish to access toolkit UI
     * data, i18n and be able to get the current application instance.
     *
     * @param task Runnable task to execute in a separate thread
     * @return instance of the created and started thread.
     */
    public Thread invokeLater ( Runnable task )
    {
        Thread thread = new Thread ( new TPTRunnable ( this, task ) );
        thread.start ();
        return thread;
    }

    /**
     * Provides an instance to the current application. You may use this method to get an
     * application instance from any toolkit piece of your code. However, note, that if you'll spawn
     * your own background server thread from your application and this thread will call
     * getCurrentApplication() method, it will return NULL, because thread context knows nothing
     * about toolkit. For such cases, consider explicitly passing application reference to your
     * thread object as a parameter or start threads by calling an utility "invokeLater" method of
     * the TPTApplication instance.
     *
     * @return Instance of currrent application
     */
    public static TPTApplication getCurrentApplication ()
    {
        return currentApplication.get ();
    }

    /**
     * This method cleanups resources from previous app instance. When Vaadin app is created
     * using CDI injection, http session does not die after app.close() which results the same
     * application instance to be used for the next application.
     */
    private void cleanupPreviousResources()
    {
        // Remove all old windows, if any
        Collection<Window> oldWindows = getWindows();
        for ( Window w : oldWindows)
        {
            removeWindow(w);
        }

        // Remove all i18n data
        if ( internationalizationDictionary!=null)
        {
            internationalizationDictionary.clear();
        }
    }

    private class TPTRunnable implements Runnable
    {

        private TPTApplication application;
        private Runnable actualTask;

        public TPTRunnable ( TPTApplication app, Runnable task )
        {
            application = app;
            actualTask = task;
        }

        public void run ()
        {
            try
            {
                application.currentApplication.set ( application );
                actualTask.run ();
            }
            catch ( Throwable err )
            {
                //todo: rework possible thread body exceptions handling ?
                err.printStackTrace ();
            }
            finally
            {
                if ( currentApplication != null )
                {
                    currentApplication.remove ();
                }

                application = null;
                actualTask = null;
            }
        }
    }
}
