package com.variamos.perspsupport.syntaxsupport;

import com.variamos.hlcl.Domain;

/**
 * @author Juan Carlos Mu�oz 2014 part of the PhD work at CRI - Universite Paris
 *         1
 *
 *         Definition of semantics for REFAS
 */
public class ExecCurrentStateAttribute extends ExecutionAttribute {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7430454253717334119L;

	public ExecCurrentStateAttribute() {
		super();
	}

	/**
	 * 
	 */
	public ExecCurrentStateAttribute(String name, String type,
			boolean affectProperties, String displayName, Object defaultValue,
			int defaultGroup) {
		super(name, type, affectProperties, displayName, defaultValue,
				defaultGroup);
	}

	public ExecCurrentStateAttribute(String name, String type,
			boolean affectProperties, String displayName, Object defaultValue,
			Domain domain, int defaultGroup) {
		super(name, type, affectProperties, displayName, defaultValue, domain,
				defaultGroup);
	}

	public ExecCurrentStateAttribute(String name, String type,
			boolean affectProperties, String displayName, String enumType,
			Object defaultValue, int defaultGroup) {
		super(name, type, affectProperties, displayName, enumType,
				defaultValue, defaultGroup);
	}

	public ExecCurrentStateAttribute(String name, String type,
			boolean affectProperties, String displayName, String enumType,
			Object defaultValue, Domain domain, int defaultGroup) {
		super(name, type, affectProperties, displayName, defaultValue, domain,
				defaultGroup);
	}

}
