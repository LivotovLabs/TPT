/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.livotov.tpt.gui.widgets;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Message panel allow you quickly construct an empty panel with 100% width and height with
 * the message text or any Vaadin widget at the panel center. This widget just saves you
 * a 10-15 lines of code every time you want to inject a panel with a message text.
 *
 * This widget inhertis VerticalLayout, so if you need something unusual later - feel free
 * to call layout methods directly, manipulate its children tree and so on.
 * @author dlivotov
 */
public class TPTMessagePanel extends VerticalLayout
{
 
    /**
     * Constructs an empty (with no message) panel.
     */
    public TPTMessagePanel()
    {
        super();
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }
    
    /**
     * Constructs a panel with a specified message at the center
     * @param message message text
     */
    public TPTMessagePanel( String message )
    {
        this( null, message);
    }

    /**
     * Constructs a panel with the specified message and an icon
     * @param icon icon to display at the left of the message
     * @param message message text
     */
    public TPTMessagePanel( Resource icon, String message )
    {
        this();

        Label msg = new Label(message);
        msg.setWidth(null);

        TPTSizer s1 = new TPTSizer(null, "100%");
        TPTSizer s2 = new TPTSizer(null, "100%");

        addComponent(s1);
        addComponent(msg);
        addComponent(s2);

        setComponentAlignment(msg, Alignment.MIDDLE_CENTER);

        setExpandRatio(s1, 0.5f);
        setExpandRatio(s2, 0.5f);

        if ( icon != null)
        {
            msg.setIcon(icon);
        }
    }

    /**
     * Constructs a panel with the arbitrary Vaadin widget at the center. Widget will be
     * centered.
     * @param component widget to use as a panel centered content
     */
    public TPTMessagePanel( Component component )
    {
        this();

        TPTSizer s1 = new TPTSizer(null, "100%");
        TPTSizer s2 = new TPTSizer(null, "100%");

        addComponent(s1);
        addComponent(component);
        addComponent(s2);

        setComponentAlignment(component, Alignment.MIDDLE_CENTER);

        setExpandRatio(s1, 0.5f);
        setExpandRatio(s2, 0.5f);
    }

}
