package eu.livotov.labs.vaadin.tpt.demo;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 03/11/2014
 */
@Theme("mh")
@PreserveOnRefresh
@Title("TPT 3.0 Demo App")
public class DemoAppUI extends UI
{

    private Navigator navigator;

    public static DemoAppUI getCurrent()
    {
        return (DemoAppUI) UI.getCurrent();
    }

    public void navigateTo(final String view)
    {
        navigateTo(view, null);
    }

    public void navigateTo(final String view, final String params)
    {
        if (view != null)
        {
            if (params == null)
            {
                navigator.navigateTo(view);
            } else
            {
                navigator.navigateTo(String.format("%s/%s", view, params));
            }
        }
    }

    protected void init(final VaadinRequest vaadinRequest)
    {
    }

    private void initNavigation()
    {
        //navigator = new Navigator(DemoAppUI.this, root.getContentContainer());
        navigator.addViewChangeListener(new ViewChangeListener()
        {

            @Override
            public boolean beforeViewChange(final ViewChangeListener.ViewChangeEvent event)
            {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event)
            {
            }
        });
    }

    @WebServlet(
                       asyncSupported = true,
                       urlPatterns = {"/*", "/VAADIN/*"},
                       initParams = {})
    @VaadinServletConfiguration(productionMode = false, ui = DemoAppUI.class)
    public static class Servlet extends VaadinServlet
    {

    }
}