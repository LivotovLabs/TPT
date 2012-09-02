/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.livotov.tpt.gui.blocks;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.livotov.tpt.gui.widgets.TPTSizer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author dlivotov
 */
public class TPTHeadingPanel extends VerticalLayout implements ClickListener
{
    private Label titleLabel = new Label("Title Text");
    
    private Label versionLabel = new Label("version 1.2.3");
    
    private Button btnExit = new Button("Exit");
    
    private HorizontalLayout notificationBar = new HorizontalLayout();
    
    private HorizontalLayout buttonsBar = new HorizontalLayout();
    
    private Map<String,Button> userToolButtons = new HashMap<String,Button>();

    private HeadingPanelActionListener listener = null;


    public TPTHeadingPanel()
    {
        super();
        initUI();
        initActions();
        applyBlackStyle();
    }

    public void addToolButton( String id, String name )
    {
        addToolButton(id, name, null);
    }

    public void addToolButton( String id, String name, String tooltip )
    {
        if ( id == null || "".equals(id) || name == null || "".equals(name))
        {
            throw new IllegalArgumentException("Tool button id or name parameters cannot be empty or null.");
        }

        Button tool = new Button(name);
        tool.setData(id);
        tool.setDescription( tooltip==null ? "" : tooltip );
        tool.setStyleName(Button.STYLE_LINK);
        tool.addListener((ClickListener)this);
        buttonsBar.addComponent(tool);
    }

    public void addToolButton( Component btn )
    {
        buttonsBar.addComponent(btn);
    }

    public HeadingPanelNotification postNotification( Resource icon, String title, String details, boolean autoremove )
    {
        HeadingPanelNotification n = new HeadingPanelNotification(icon, title, details, autoremove);

        Button nb = new Button(n.getTitle());
        nb.setDescription(nb.getDescription());
        nb.setIcon(nb.getIcon());
        nb.setData(n);
        nb.setStyleName(Button.STYLE_LINK);

        nb.addListener( new ClickListener()
        {
            public void buttonClick(ClickEvent event)
            {
                Button b = event.getButton();
                if ( b.getData() != null & b.getData() instanceof HeadingPanelNotification)
                {
                    HeadingPanelNotification notification = (HeadingPanelNotification)b.getData();

                    if ( notification.isAutoremove())
                    {
                        b.setData(null);
                        notificationBar.removeComponent(b);
                    }

                    if ( listener != null)
                    {
                        listener.notificationIconPressed(notification);
                    }
                }
            }
        });

        notificationBar.addComponent(nb);
        return n;
    }

    public void clearNotification( HeadingPanelNotification n)
    {
        Iterator<Component> notifs = notificationBar.getComponentIterator();
        while ( notifs.hasNext())
        {
            Component nc = notifs.next();
            if ( nc instanceof Button )
            {
                Button ncb = (Button)nc;
                if ( ncb.getData() != null && ncb.getData().equals(n))
                {
                    ncb.setData(null);
                    notificationBar.removeComponent(nc);
                    break;
                }
            }
        }
    }

    public void clearAllNotifications()
    {
        Iterator<Component> notifs = notificationBar.getComponentIterator();
        while ( notifs.hasNext())
        {
            Component nc = notifs.next();
            if ( nc instanceof Button )
            {
                ((Button)nc).setData(null);
            }
        }

        removeAllComponents();
    }

    public void setTitle( String text )
    {
        titleLabel.setValue( "" + text);
    }

    public String getTitle()
    {
        return "" + titleLabel.getValue();
    }

    public void setVersion( String text )
    {
        versionLabel.setValue( "" + text );
    }

    public String getVersion()
    {
        return "" + versionLabel.getValue();
    }

    public void setExitButtonTitle( String text )
    {
        btnExit.setCaption("" + text);
    }

    public String getExitButtonTitle()
    {
        return "" + btnExit.getCaption();
    }

    public void setExitButtonVisibility( boolean v )
    {
        btnExit.setVisible(v);
    }

    public Label getTitleComponent()
    {
        return titleLabel;
    }

    public Label getVersionComponent()
    {
        return versionLabel;
    }

    public Button getExitButtonComponent()
    {
        return btnExit;
    }

    public void setActionListener(HeadingPanelActionListener l)
    {
        listener = l;
    }

    private void initUI()
    {
        setWidth("100%");
        setHeight(null);
        setMargin(false);
        setSpacing(true);

        notificationBar.setWidth(null);
        notificationBar.setMargin(false);
        notificationBar.setSpacing(true);

        buttonsBar.setWidth(null);
        buttonsBar.setHeight(null);
        buttonsBar.setMargin(false);
        buttonsBar.setSpacing(true);

        titleLabel.setWidth(null);
        versionLabel.setWidth(null);

        TPTSizer sz1 = new TPTSizer("100%", null);
        TPTSizer sz2 = new TPTSizer("100%", null);

        HorizontalLayout titleHeader = new HorizontalLayout();
        titleHeader.setMargin(true);
        titleHeader.setSpacing(true);
        titleHeader.setWidth("100%");
        titleHeader.setHeight(null);
        titleHeader.addComponent(titleLabel);
        titleHeader.addComponent(sz1);
        titleHeader.addComponent(notificationBar);
        titleHeader.addComponent(versionLabel);
        titleHeader.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);
        titleHeader.setComponentAlignment(versionLabel, Alignment.TOP_RIGHT);
        titleHeader.setComponentAlignment(notificationBar, Alignment.TOP_RIGHT);
        titleHeader.setExpandRatio(sz1, 1.0f);

        HorizontalLayout toolsHeader = new HorizontalLayout();
        toolsHeader.setMargin(true);
        toolsHeader.setSpacing(true);
        toolsHeader.setWidth("100%");
        toolsHeader.setHeight(null);
        toolsHeader.addComponent(buttonsBar);
        toolsHeader.addComponent(sz2);
        toolsHeader.addComponent(btnExit);
        toolsHeader.setExpandRatio(sz2, 1.0f);

        addComponent(titleHeader);
        addComponent(toolsHeader);
    }

    private void initActions()
    {
        btnExit.addListener( new ClickListener()
        {
            public void buttonClick(ClickEvent event)
            {
                if ( listener != null)
                {
                    listener.exitButtonPressed();
                }
            }
        });
    }

    public void buttonClick(ClickEvent event)
    {
        Button tool = event.getButton();
        if ( tool.getData() != null && tool.getData() instanceof String && listener != null)
        {
            listener.toolButtonPressed( tool.getData().toString() );
        }
    }

    private void applyBlackStyle()
    {
        setStyleName("black");
        btnExit.setStyleName("small");
        titleLabel.setStyleName("h1");
    }

    public interface HeadingPanelActionListener
    {
        void toolButtonPressed( String id );

        void exitButtonPressed();

        void notificationIconPressed( HeadingPanelNotification n );
    }

    public class HeadingPanelNotification implements Serializable
    {
        private String id = UUID.randomUUID().toString();

        private String title = "";

        private Resource icon;

        private String details = "";

        private boolean autoremove = true;


        public HeadingPanelNotification(Resource icon, String title, String details, boolean autoremove)
        {
            this.icon = icon;
            this.title = "" + title;
            this.details = "" + details;
            this.autoremove = autoremove;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Resource getIcon() {
            return icon;
        }

        public void setIcon(Resource icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public boolean isAutoremove() {
            return autoremove;
        }

        public void setAutoremove(boolean autoremove) {
            this.autoremove = autoremove;
        }
    }
}
