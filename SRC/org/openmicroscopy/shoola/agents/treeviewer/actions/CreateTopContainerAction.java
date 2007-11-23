/*
 * org.openmicroscopy.shoola.agents.treeviewer.actions.CreateTopContainerAction
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.treeviewer.actions;




//Java imports
import java.awt.event.ActionEvent;

import javax.swing.Action;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.IconManager;
import org.openmicroscopy.shoola.agents.treeviewer.browser.Browser;
import org.openmicroscopy.shoola.agents.treeviewer.cmd.CreateCmd;
import org.openmicroscopy.shoola.agents.treeviewer.view.TreeViewer;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * Creates a top container either a project or a categoryGroup.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $ $Date: $)
 * </small>
 * @since OME2.2
 */
public class CreateTopContainerAction
    extends TreeViewerAction
{

    /** The name of the action for the creation of a <code>Project</code>. */
    private static final String NAME = "New...";
    
    /** The name of the action for the creation of a <code>Project</code>. */
    private static final String NAME_PROJECT = "New Project...";
    
    /** 
     * The name of the action for the creation of a <code>CategoryGroup</code>.
     */
    private static final String NAME_CATEGORY_GROUP = "New Tag Set...";
    
    /** Description of the action for a <code>Project</code> . */
    private static final String DESCRIPTION_PROJECT = "Create a new Project.";
    
    /** Description of the action for a <code>CategoryGroup</code> . */
    private static final String DESCRIPTION_CATEGORY_GROUP = 
                                "Create a new Tag Set.";
    
    /** The type of node to create. */
    private int nodeType;
    
    /** 
     * Sets the action enabled depending on the state of the {@link Browser}.
     * @see TreeViewerAction#onBrowserStateChange(Browser)
     */
    protected void onBrowserStateChange(Browser browser)
    {
        if (browser == null) return;
        switch (browser.getState()) {
            case Browser.LOADING_DATA:
            case Browser.LOADING_LEAVES:
            case Browser.COUNTING_ITEMS:  
                setEnabled(false);
                break;
            default:
            	setEnabled(true);
                break;
        }
    }
    
    /**
     * Modifies the name of the action when a new browser is selected.
     * @see TreeViewerAction#onBrowserSelection(Browser)
     */
    protected void onBrowserSelection(Browser browser)
    {
        nodeType = -1;
        if (browser == null) {
            setEnabled(false);
            name = NAME;
        } else {
            switch (browser.getBrowserType()) {
                case Browser.PROJECT_EXPLORER:
                    setEnabled(true);
                    name = NAME_PROJECT; 
                    nodeType = CreateCmd.PROJECT;
                    putValue(Action.SHORT_DESCRIPTION, 
                            UIUtilities.formatToolTipText(DESCRIPTION_PROJECT));
                    break;
                case Browser.CATEGORY_EXPLORER:
                	setEnabled(true);
                    name = NAME_CATEGORY_GROUP; 
                    nodeType = CreateCmd.CATEGORY_GROUP;
                    putValue(Action.SHORT_DESCRIPTION, 
                            UIUtilities.formatToolTipText(
                                    DESCRIPTION_CATEGORY_GROUP));
                    break;
                case Browser.IMAGES_EXPLORER:
                    //setEnabled(true);
                    setEnabled(false);
                    name = NAME;
            }
        }
        description = (String) getValue(Action.SHORT_DESCRIPTION);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param model Reference to the Model. Mustn't be <code>null</code>.
     */
    public CreateTopContainerAction(TreeViewer model)
    {
        super(model);
        onBrowserSelection(model.getSelectedBrowser());
        putValue(Action.SHORT_DESCRIPTION, 
                UIUtilities.formatToolTipText(DESCRIPTION_PROJECT));
        IconManager im = IconManager.getInstance();
        putValue(Action.SMALL_ICON, im.getIcon(IconManager.ADD_CONTAINER));
    } 
    
    /**
     * Creates a {@link CreateCmd} command to execute the action.
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        if (nodeType == -1) return;
        CreateCmd cmd = new CreateCmd(model, nodeType);
        cmd.execute();
    }
    
}
