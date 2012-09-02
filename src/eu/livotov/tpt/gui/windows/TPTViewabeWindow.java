/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.livotov.tpt.gui.windows;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import eu.livotov.tpt.gui.widgets.TPTMultiView;

/**
 *
 * @author dlivotov
 */
public class TPTViewabeWindow extends Window
{
    private TPTMultiView controller = null;


    public TPTViewabeWindow()
    {
        super();
        controller = new TPTMultiView(true);
        controller.setSizeFull();
        setContent(controller);
    }

    public TPTViewabeWindow( String title, boolean navigational )
    {
        super(title);
        controller = new TPTMultiView(navigational);
        controller.setSizeFull();
        setContent(controller);
    }

    public TPTMultiView getController()
    {
        return controller;
    }

    public void switchView(String id, String params)
    {
        if ( params == null || params.isEmpty())
        {
            controller.switchView(id);
        } else
        {
            controller.switchView( String.format("%s/%s", id, params));
        }
    }

    public void addView(String id, Component view)
    {
        controller.addView(id, view);
    }

    public void removeView(String id)
    {
        controller.removeView(id);
    }

    public void replaceView(String id, Component newView)
    {
        controller.replaceView(id, newView);
    }

    public void setFailsafeView(String id)
    {
        controller.setFailsafeViewName(id);
    }

    public String getCurrentViewId()
    {
        return controller.getCurrentViewName();
    }

    public Component getCurrentView()
    {
        return controller.getCurrentView();
    }
}
