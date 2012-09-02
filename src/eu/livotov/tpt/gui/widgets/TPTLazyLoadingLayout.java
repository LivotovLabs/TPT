/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.livotov.tpt.gui.widgets;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import eu.livotov.tpt.TPTApplication;

/**
 * <p>
 * This layout allows easy implementation of heavy components initialization or data loading. If your component
 * initialization process takes significant time or you need to load some data from a database lenghty query,
 * TPTLazyLoadingLayout can simplify your task. It runs your component initialization or data loading process in a
 * separate thread on the server side and while the thread is running, it displays a progress indicator and optional
 * message. When initialization of your component is finished, it appears in place of progress bar. This all happens
 * without blocking the entire UI, so while your data is loading, application interface is alive and responsive.
 * </p>
 * <p>
 *
 * </p>
 *
 * @author dll
 */
public class TPTLazyLoadingLayout extends VerticalLayout implements Runnable
{

    private LazyLoader loader;
    private ProgressIndicator progressBar = new ProgressIndicator ();
    private Label loadingLabel = new Label ();
    private Label errorLabel = new Label ();

    /**
     * Constructs lazy loadig layout
     * @param loader loader interface, which actually loads the data and constructs an actual component
     * @param loadImmideately if set to <code>true</code> immideately starts a background thread to construct actual component. Otherwise
     * you must explicitly call <code>loadComponent()</code> method to initiate this process. Until called, component displays empty panel.
     */
    public TPTLazyLoadingLayout ( LazyLoader loader, boolean loadImmideately )
    {
        this.loader = loader;
        initUI ();

        if ( loadImmideately )
        {
            loadComponent ();
        }
    }

    /**
     * Initiates the process of loading an actual component via provided LazyLoader interface. Once this mehtod is called,
     * UI start displaying a progress indicator and actual component loading process begins. Once process completed, a progress
     * indicator will be automatically replaced by a constructed component. In case of any uncaught error, it will be also
     * displayed instead of progress indicator.
     */
    public void loadComponent ()
    {
        activateLoadingView ();
        TPTApplication.getCurrentApplication ().invokeLater ( this );
    }

    /**
     * Do not call this method manually. It will be automatically called in a separate thread by a TPTApplication instance.
     */
    public void run ()
    {
        try
        {
            activateLoadedView ( loader.lazyLoad ( this ) );
        }
        catch ( Throwable err )
        {
            activateErrorView ( err );
        }
    }

    /**
     * Allow to update the progress status of the current component loading process. As the instance of TPTLazyLoader is
     * passed to your LazyLoader implementation, you may update the progress from there.
     * @param progress new progress. Must be specified from 0.0 to 1.0, which reflects 0% ... 100% interval. If set to 0.0,
     * a progress indicator will be swithced to indeterminate mode, displaying an endless wheel.
     */
    public void setLoadingProgress ( float progress )
    {
        if ( progress == 0.0f )
        {
            progressBar.setIndeterminate ( true );
        }
        else
        {
            progressBar.setIndeterminate ( false );
            progressBar.setValue ( progress );
        }
    }

    /**
     * Returns an instance of progress indicator component for further customization
     * @return ProgressIndicator instance that is used to display a progress indicator during the loading process.
     */
    public ProgressIndicator getProgressIndicator ()
    {
        return progressBar;
    }

    /**
     * Returns an instance of loading label, which is shown together with a progress indicator during the loading process. Use this
     * for further customization of the UI (set styles, sizes, etc...)
     * @return Label instance
     */
    public Label getLoadingLabel ()
    {
        return loadingLabel;
    }

    /**
     * Returns an instance of the label which is used to display an unexpected error message if the loading process throws an
     * exception. Use it for further customization of the UI (set styles, sizes, etc)
     * @return Label instance
     */
    public Label getErrorLabel ()
    {
        return errorLabel;
    }

    private void activateLoadedView ( Component lazyLoad )
    {
        removeAllComponents ();
        addComponent ( lazyLoad );
    }

    private void activateErrorView ( Throwable err )
    {
        errorLabel.setValue ( err.getMessage () );
        removeAllComponents ();
        addComponent ( errorLabel );
        setComponentAlignment ( errorLabel, Alignment.MIDDLE_CENTER );
    }

    private void activateLoadingView ()
    {
        removeAllComponents ();

        TPTSizer sz1 = new TPTSizer ( null, "50%" );
        TPTSizer sz2 = new TPTSizer ( null, "50%" );

        addComponent ( sz1 );

        if ( loader.getLazyLoadingMessage () != null)
        {
            loadingLabel.setValue ( loader.getLazyLoadingMessage () );
            addComponent ( loadingLabel );
        }
        
        addComponent ( progressBar );
        addComponent ( sz2 );
        setComponentAlignment ( loadingLabel, Alignment.MIDDLE_CENTER );
        setComponentAlignment ( progressBar, Alignment.MIDDLE_CENTER);
        setExpandRatio ( sz1, 0.5f );
        setExpandRatio ( sz2, 0.5f );
    }

    private void initUI ()
    {
        setSizeFull ();
        errorLabel.setWidth ( null );
        loadingLabel.setWidth ( null );
        progressBar.setIndeterminate ( true );
        progressBar.setPollingInterval ( 500 );
        progressBar.setWidth ( "50%" );
        progressBar.setImmediate ( true );
    }

    /**
     * Implement this interface in your long initialing component and move all lenghty stuff into the <code>lazyLoad</code>
     * method, returning the fully initializing component at the end
     */
    public interface LazyLoader
    {

        /**
         * Provides an informational message, which is shown together with a progress indicator during the loading process.
         * @return message or null. If null is returned, the text label will not be shown.
         */
        String getLazyLoadingMessage ();

        /**
         * Implement all your lenghty stuff here. For instance, query database and fill up the UI fields with the actual data.
         * This method will be invoked automatically in the separate server thread. In this method you feel free to use
         * TPTApplication.getCurrentApplication() method to get an instance of your application, if needed.
         *
         * If you need to report any error and you do not want to handle it yourself - simply throw an exception. Its message
         * (via getMessage() method) will be displayed automatically.
         * @param layout instance of TPTLazyLoadingLayout. You can use this instance to update a progress status or customize any informational components on the fly.
         * @return actual component, which will be shown instead of the progress indicator when loading process is finished.
         */
        Component lazyLoad ( TPTLazyLoadingLayout layout );
    }
}
