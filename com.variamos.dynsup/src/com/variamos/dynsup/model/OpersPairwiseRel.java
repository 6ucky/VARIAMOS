package com.variamos.dynsup.model;


/**
 * A class to represent the edges with semantic back object. Part of PhD work at
 * University of Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2014-11-23
 * @see com.cfm.productline.
 */
public class OpersPairwiseRel extends OpersElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7976788205587295216L;

	/**
	 * 
	 */

	public OpersPairwiseRel() {
		super(null);
	}

	public OpersPairwiseRel(String identifier, boolean toSoftSemanticConcept) {
		super(identifier);
	}

}
