/*
 * org.openmicroscopy.shoola.agents.rnd.Quantization_8_16_bit
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

package org.openmicroscopy.shoola.agents.rnd;

//Java imports

//Third-party libraries

//Application-internal dependencies

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
public class Quantization_8_16_bit
	extends QuantumStrategy
{

	//	current lookup table
	private byte[]      LUT;
	private int         min;
	private int         max;
	private QuantumMap	qMap;
	
	/**
	 * @param qd 	Quantum definition object, contained mapping data.
	 * @param qMap	QuantumMap object.	
	 */
	public Quantization_8_16_bit(QuantumDef qd, QuantumMap qMap)
	{
		super(qd);
		this.qMap = qMap;
	}
	
	/**
	 * Initializes the LUT. 
	 * Comparable getGlobalMin and getGlobalMax
	 * assumed to be Integer, QuantumStrategy enforces min < max.
	 * QuantumFactory makes sure 0 < max-min< 2^N where N = 8 or N = 16.
	 * LUT size is at most 256 bytes if N = 8 or 
	 * 2^16 bytes = 2^6Kb = 64Kb if N = 16.
	 */
	private void init()
	{	
		min = ((Integer) getGlobalMin()).intValue();  
		max = ((Integer) getGlobalMax()).intValue();
		LUT = new byte[max-min+1];  
	}

	/** 
	 * Maps the input interval onto the codomain [cdStart, cdEnd] sub-interval
	 * of [0, 255].
	 * Since the user can select the bitResolution 2^n-1 where n = 1..8, 
	 * 2 steps.
	 *
	 */
	private void buildLUT()
	{
		if( LUT == null )   init();
		// Comparable assumed to be Integer
		//domain
		int dStart = ((Integer) getWindowStart()).intValue(),
			dEnd = ((Integer)getWindowEnd()).intValue();
		//codomain  
		int cdStart = qDef.cdStart, cdEnd = qDef.cdEnd;
		double ys = qMap.transform(dStart);
		double ye = qMap.transform(dEnd);
		double a0 = qDef.bitResolution/(ye-ys);
		double a1 = (dEnd-dStart)/qDef.bitResolution;
		
		int x = min;
		
		for(; x < dStart; ++x)   LUT[x-min] = (byte) cdStart;
		
		for(; x < dEnd; ++x) { 
			double v;
			v = Approximation.nearestInteger(a0*(qMap.transform(x)-ys));
			v = Approximation.nearestInteger(a1*v+dStart);
			LUT[x-min] = (byte) v;
		}
		
		for(; x<=max; ++x)   LUT[x-min] = (byte) cdEnd; 
	}

	/**
	 * The input window size changed, rebuild the LUT
	 */
	protected void onWindowChange()
	{
		buildLUT();
	}

	int quantize(Object value)
	{
		int x = ((Integer) value).intValue();
	   	int i = LUT[x-min];
	   	return (i & 0xFF);  //assumed x in [min, max]
	}

}
