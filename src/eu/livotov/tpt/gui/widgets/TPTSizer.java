/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.livotov.tpt.gui.widgets;

import com.vaadin.ui.Label;

/**
 * This is a dummy component, based on a Label (because it is most lightweight and does not perform any JS computations),
 * which can be used for quick insertions of a gaps between components. It's main purpose to speed-up UI construction code,
 * as writing new TPTSizer("20px",null) is easier (in lines of code) than instantiating a label and then calling setWidth and setHeight methods.
 * @author dll
 */
public class TPTSizer extends Label {

    /**
     * Main and only constructor. Constructs a sizer with the specified width and height. As anywhere in Vaadin,
     * use <code>null</code> to specify an undefined dimension.
     * @param width width of the sizer or <code>null</code> for undefined width.
     * @param height height of the sizer or <code>null</code> for undefined height
     */
    public TPTSizer( String width, String height )
    {
        super();
        setWidth ( width );
        setHeight ( height );
    }
}
