/*
 * org.openmicroscopy.shoola.agents.annotator.Annotator
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

/*------------------------------------------------------------------------------
 *
 * Written by:    Jeff Mellen <jeffm@alum.mit.edu>
 *
 *------------------------------------------------------------------------------
 */
 
package org.openmicroscopy.shoola.agents.annotator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openmicroscopy.shoola.agents.annotator.events.AnnotateImage;
import org.openmicroscopy.shoola.env.Agent;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.data.DSAccessException;
import org.openmicroscopy.shoola.env.data.DSOutOfServiceException;
import org.openmicroscopy.shoola.env.data.SemanticTypesService;
import org.openmicroscopy.shoola.env.event.AgentEvent;
import org.openmicroscopy.shoola.env.event.AgentEventListener;
import org.openmicroscopy.shoola.env.event.EventBus;
import org.openmicroscopy.shoola.env.event.ResponseEvent;
import org.openmicroscopy.shoola.env.ui.TopFrame;
import org.openmicroscopy.shoola.env.ui.UserNotifier;

/**
 * Model and agent for the Annotator set of widgets.  The annotator makes
 * it so multiple annotation windows can be opened at the same time.  There are
 * also two alternate windows, as the annotator both handles text annotations
 * and scalar attribute definitions.  The text annotations are inherently
 * (from a UI perspective) different, and are thus shown in a different
 * format.
 *
 * @author Jeff Mellen, <a href="mailto:jeffm@alum.mit.edu">jeffm@alum.mit.edu</a><br>
 * <b>Internal version:</b> $Revision$ $Date$
 * @version 2.2
 * @since OME2.2
 */
public class Annotator implements Agent, AgentEventListener
{
    private Registry registry;
    
    private TopFrame topFrame;
    
    private List activeControls;
    
    public Annotator()
    {
        activeControls = new ArrayList();
    }
    
    /**
     * Process and handle events destined for the Annotator.  NB to JM/Andrea:
     * I *believe* that there would only be one real event: EditAnnotation, or
     * the like, but there may be a distinction between a dataset and image
     * annotation, so... we'll likely talk about this on 3/9.
     * 
     * @param e The event to be captured by the annotator.
     */
    public void eventFired(AgentEvent e)
    {
        if(e instanceof AnnotateImage)
        {
            AnnotateImage event = (AnnotateImage)e;
            showAnnotationDialog(event);
        }
    }
    
    /**
     * Don't do anything, because of nature of annotator (triggered explicitly,
     * not when the application becomes active)
     * 
     * @see org.openmicroscopy.shoola.env.Agent#activate()
     */
    public void activate()
    {
        // do nothing
    }
    
    /**
     * Checks any open annotator windows to see if there is unsaved
     * information.  On a cancel, will return false.  Otherwise, if there is
     * no unsaved information or if the user makes a decisive choice to save
     * or not save an annotation, returns true.
     * 
     * @return Whether or not the agent can be terminated.
     * @see org.openmicroscopy.shoola.env.Agent#canTerminate()
     */
    public boolean canTerminate()
    {
        for(Iterator iter = activeControls.iterator(); iter.hasNext();)
        {
            AnnotationCtrl control = (AnnotationCtrl)iter.next();
            if(!control.canExit()) return false;
        }
        return true;
    }
    
    /**
     * Does nothing.
     * 
     * @see org.openmicroscopy.shoola.env.Agent#terminate()
     */
    public void terminate()
    {
        // do nothing.
    }
    
    /**
     * Sets the registry (and operating context) of the Annotator.
     * 
     * @see org.openmicroscopy.shoola.env.Agent#setContext(org.openmicroscopy.shoola.env.config.Registry)
     * @throws IllegalArgumentException If the parameter is null.
     */
    public void setContext(Registry ctx)
        throws IllegalArgumentException
    {
        if(ctx == null)
        {
            throw new IllegalArgumentException("Annotator cannot operate " +
                "in null context");
        }
        this.registry = ctx;
        topFrame = registry.getTopFrame();
        EventBus bus = registry.getEventBus();
        bus.register(this,AnnotateImage.class);
    }

    /**
     * Retrieve the dataset annotation information from the database.
     * @param datasetID the ID of the dataset to retrieve annotations from.
     * @return A list of annotations for that dataset.
     */
    List getDatasetAnnotations(int datasetID)
    {
        // TODO call registry to find out this information and change this
        return new ArrayList();
    }
    
    /**
     * Retrieve the semantic attributes associated with this database from
     * the dataset.
     * @param datasetID the ID of the dataset to retrieve attributes from.
     * @return A list of attributes for that dataset.
     */
    List getDatasetAttributes(int datasetID)
    {
        SemanticTypesService sts = registry.getSemanticTypesService();
        try
        {
            return sts.retrieveImageAttributes("DatasetAnnotation",datasetID);
        }
        catch(DSAccessException dsa)
        {
            UserNotifier un = registry.getUserNotifier();
            un.notifyError("Server Error",dsa.getMessage(),dsa);
        }
        catch(DSOutOfServiceException dso)
        {
            UserNotifier un = registry.getUserNotifier();
            un.notifyError("Communication Error",dso.getMessage(),dso);
        }
        return null;
    }
    
    /**
     * Retrieve the image annotation information from the database.
     * @param imageID the ID of the image to retrieve annotations from.
     * @return A list of annotations for that image.
     */
    List getImageAnnotations(int imageID)
    {
        SemanticTypesService sts = registry.getSemanticTypesService();
        try
        {
            return sts.retrieveImageAttributes("ImageAnnotation",imageID);
        }
        catch(DSAccessException dsa)
        {
            UserNotifier un = registry.getUserNotifier();
            un.notifyError("Server Error",dsa.getMessage(),dsa);
        }
        catch(DSOutOfServiceException dso)
        {
            UserNotifier un = registry.getUserNotifier();
            un.notifyError("Communication Error",dso.getMessage(),dso);
        }
        return null;
    }
    
    /**
     * Retrieve the image attribute information from the database.
     *
     * @param imageID The ID of the image to retrieve attributes from.
     * @return A list of attributes for that image.
     */
    List getImageAttributes(int imageID)
    {
        // TODO call registry to find out this information and change this
        return new ArrayList();
    }
    
    /**
     * Show the annotation dialog.
     * @param summary The image summary to wrap the dialog around.
     */
    void showAnnotationDialog(AnnotateImage requestEvent)
    {
        ImageAnnotationCtrl iac =
            new ImageAnnotationCtrl(this,requestEvent.getImageInfo(),
                                    requestEvent);
        activeControls.add(iac);
        TextAnnotationUIF tif = new TextAnnotationUIF(iac,registry);
        tif.show();
    }
    
    /**
     * Tells the annotator agent to respond with the specified event.
     * @param re
     */
    void respondWithEvent(ResponseEvent re)
    {
        EventBus eventBus = registry.getEventBus();
        eventBus.post(re);
    }
    
    /**
     * Indicates a close event; removes this control from the active.
     * @param control
     */
    void close(AnnotationCtrl control)
    {
        activeControls.remove(control);
    }
    
    /**
     * Commits all the attributes to the specified values.
     * @param attributes The attributes to update (and specified values)
     */
    boolean updateDatasetInfo(List attributes)
    {
        // TODO call registry to get STS handle to do update.
        // return false on error
        return true;
    }
    

}
