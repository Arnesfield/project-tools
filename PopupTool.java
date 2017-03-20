package tools;

import javax.swing.JButton;

/**
 * Interface for Popup JPanels.
 * @author Arnesfield
 */
public interface PopupTool {
    /**
     * This method is used to return a list of JButtons.
     * @return list of JButtons that disposes the popup JDialog when clicked
     */
    public JButton[] getDisposeButtons();

    /**
     * This method is used to return a list of JButtons and also removes parent frame.
     * @return list of JButtons that disposes the popup JDialog and the parent frame when clicked
     */
    public JButton[] getDisposeButtonsRemoveParent();
}
