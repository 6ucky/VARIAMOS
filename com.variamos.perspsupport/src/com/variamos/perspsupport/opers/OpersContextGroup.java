package com.variamos.perspsupport.opers;

import com.variamos.perspsupport.syntaxsupport.SemanticAttribute;
import com.variamos.semantic.types.AttributeType;

/**
 * A class to represent the edges at semantic level. Part of PhD work at
 * University of Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2014-12-08
 * @see com.cfm.productline.
 */
public class OpersContextGroup extends OpersAbstractVertex {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5538738414024566452L;

	private static final String VAR_VARIABLENAME = "name",
			VAR_VARIABLENAMENAME = "Group Name",

			VAR_INSTANCENUMBER = "instances",
			VAR_INSTANCENUMBERNAME = "Number of Instances",

			VAR_EXTVISIBLE = "ExtVisible",
			VAR_EXTVISIBLENAME = "Externally Visible",

			VAR_EXTCONTROL = "ExtControl",
			VAR_EXTCONTROLNAME = "Externally Controlled";

	public OpersContextGroup() {
		super();
		defineSemanticAttributes();
	}

	public OpersContextGroup(String name) {
		super(name, true);
		defineSemanticAttributes();
	}

	/*
	 * public OpersContextGroup(OpersAbstractVertex parentConcept, String name)
	 * { super( name, true); defineSemanticAttributes(); }
	 */

	private void defineSemanticAttributes() {

		putSemanticAttribute(VAR_VARIABLENAME, new SemanticAttribute(
				VAR_VARIABLENAME, "String", AttributeType.OPERATION, false,
				VAR_VARIABLENAMENAME, "<<new>>", 0, 1, "", "", 1, "", ""));
		putSemanticAttribute(VAR_INSTANCENUMBER, new SemanticAttribute(
				VAR_INSTANCENUMBER, "Integer", AttributeType.OPERATION, false,
				VAR_INSTANCENUMBERNAME, "1", 0, 7, "", "", -1, "", ""));
		putSemanticAttribute(VAR_EXTVISIBLE, new SemanticAttribute(
				VAR_EXTVISIBLE, "Boolean", AttributeType.OPERATION, false,
				VAR_EXTVISIBLENAME, false, 0, 8, "", "", -1, "", ""));
		putSemanticAttribute(VAR_EXTCONTROL, new SemanticAttribute(
				VAR_EXTCONTROL, "Boolean", AttributeType.OPERATION, false,
				VAR_EXTCONTROLNAME, false, 0, 9, "", "", -1, "", ""));

		this.addPropEditableAttribute("01#" + VAR_VARIABLENAME);
		this.addPropEditableAttribute("07#" + VAR_INSTANCENUMBER);
		this.addPropEditableAttribute("08#" + VAR_EXTVISIBLE);
		this.addPropEditableAttribute("09#" + VAR_EXTCONTROL);

		this.addPropVisibleAttribute("01#" + VAR_VARIABLENAME);
		this.addPropVisibleAttribute("07#" + VAR_INSTANCENUMBER);
		this.addPropVisibleAttribute("08#" + VAR_EXTVISIBLE);
		this.addPropVisibleAttribute("09#" + VAR_EXTCONTROL);

		// this.addPanelVisibleAttribute("01#" + VAR_VARIABLENAME);

	}

	public String toString() {

		return " VAR: " + super.toString();
	}
}
