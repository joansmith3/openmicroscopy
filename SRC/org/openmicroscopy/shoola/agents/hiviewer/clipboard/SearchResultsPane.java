/*
 * org.openmicroscopy.shoola.agents.hiviewer.clipboard.SearchResultsPane
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.hiviewer.clipboard;

//Java imports
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.hiviewer.util.TreeCellRenderer;
import org.openmicroscopy.shoola.env.data.model.DataObject;
import org.openmicroscopy.shoola.env.data.model.DatasetSummary;
import org.openmicroscopy.shoola.env.data.model.ImageSummary;
import org.openmicroscopy.shoola.env.data.model.ProjectSummary;

/** 
 * 
 *
 * @author  Barry Anderson &nbsp;&nbsp;&nbsp;&nbsp;
 *              <a href="mailto:banderson@computing.dundee.ac.uk">
 *              banderson@comnputing.dundee.ac.uk
 *              </a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class SearchResultsPane
    extends JTree
{

    /** The View hosting this component. */
    private CBSearchTabView     view;
    
    /** Initializes the component. */
    private void initialize()
    {
        putClientProperty("JTree.lineStyle", "Angled");
        setCellRenderer(new TreeCellRenderer());
        getSelectionModel().setSelectionMode(
                                    TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param view The <code>CBSearchTabView</code> hosting the component
     * @param nodes The set of nodes to display.
     */
    SearchResultsPane(CBSearchTabView view, Set nodes)
    {
        this.view = view;
        initialize();
        SearchResultsPaneMng manager = new SearchResultsPaneMng(this);
        manager.buildTree(nodes);
    }
    
    /**
     * Displays the popup menu at the specified location.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    void showMenu(int x, int y)
    {
        TreePopupMenu menu = view.getPopupMenu();
        DataObject target = getDataObject();
        String txt = TreePopupMenu.BROWSE;
        boolean b = false;
        if (target instanceof ImageSummary) {
            txt = TreePopupMenu.VIEW;
            b = true;
        }
        menu.setViewText(txt);
        menu.setClassifyEnabled(b);
        menu.show(this, x, y);
    }
    
    /**
     * Sets the tree model when the root node is created.
     * 
     * @param root The root node.
     */
    void setTreeModel(DefaultMutableTreeNode root)
    {
        DefaultTreeModel dtm = new DefaultTreeModel(root);
        setModel(dtm);
        setShowsRootHandles(true);
        expandPath(new TreePath(root.getPath()));
    }
      
    /**
     * Returns the selected data object.
     * 
     * @return See above.
     */
    DataObject getDataObject()
    {
        DataObject target = null;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                            getLastSelectedPathComponent();
        if (node != null) {
            Object  usrObj = node.getUserObject();
            if (usrObj instanceof ProjectSummary || 
                usrObj instanceof DatasetSummary ||
                usrObj instanceof ImageSummary)
                target = (DataObject) usrObj;
        }
        return target;
    }
    
}
