/*
 * org.openmicroscopy.shoola.agents.rnd.pane.QuantumMapping
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

package org.openmicroscopy.shoola.agents.rnd.pane;



//Java imports
import javax.swing.JPanel;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.rnd.ContrastStretchingDef;
import org.openmicroscopy.shoola.agents.rnd.PlaneSlicingDef;
import org.openmicroscopy.shoola.agents.rnd.QuantumDef;
import org.openmicroscopy.shoola.env.config.Registry;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class QuantumMapping
	extends JPanel
{
	static final int                LINEAR = 0;
	static final int                EXPONENTIAL = 1;
	static final int                LOGARITHMIC = 2;
	static final int                POLYNOMIAL = 3;
	
	private CodomainPane			codomainPane;
	private DomainPane				domainPane;
	private GraphicsRepresentation  gRepresentation;
	
	private QuantumMappingManager	manager;
	
	QuantumMapping(Registry registry, QuantumDef qDef, 
					ContrastStretchingDef csDef, PlaneSlicingDef psDef, 
					String[] waves, int mini, int maxi)
	{
		manager = new QuantumMappingManager(this);
		codomainPane = new CodomainPane(registry, manager, csDef, psDef);
		domainPane = new DomainPane(registry, manager, waves, qDef);
		gRepresentation = new GraphicsRepresentation(manager, qDef, mini, maxi);
		buildGUI();
	}

	public QuantumMappingManager getManager()
	{
		return manager;
	}
	
	/** Builds and layout the GUI. */
	private void buildGUI()
	{
		
	}
}
