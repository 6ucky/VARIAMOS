package com.variamos.refas.core.types;

import com.variamos.syntaxsupport.semanticinterface.IntDirectRelationType;

/**
 * @author Juan Carlos Mu�oz 2014
 *  part of the PhD work at CRI - Universite Paris 1
 *
 * Definition of semantics for REFAS
 */
public enum DirectRelationType implements IntDirectRelationType {
	means_ends,
	preferred,
	required,
	conflict,
	alternative,
	implication,
	normal,
	implementation
	}
