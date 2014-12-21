package eu.livotov.labs.vaadin.tpt.ui;

import com.vaadin.ui.Label;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 03/11/2014
 */
public class TPTUISizer extends Label
{

    public TPTUISizer(final boolean growHorizontal, final boolean growVertical)
    {
        super();

        if (growHorizontal)
        {
            setWidth(100.0f, Unit.PERCENTAGE);
        } else
        {
            setWidthUndefined();
        }

        if (growVertical)
        {
            setHeight(100.0f, Unit.PERCENTAGE);
        } else
        {
            setHeightUndefined();
        }
    }
}
