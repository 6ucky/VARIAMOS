package com.variamos.dynsup.staticexprsup;

import java.util.Map;

import com.variamos.hlcl.HlclFactory;
import com.variamos.hlcl.Identifier;
import com.variamos.hlcl.NumericExpression;

/**
 * Class to create the Number expression. Part of PhD work at University of
 * Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2014-12-15
 */

public class NumberNumericExpression extends AbstractNumericExpression {
	public int getNumber() {
		return number;
	}

	public static final String TRANSFORMATION = "";
	private int number;

	public NumberNumericExpression(int number) {
		super();
		this.expressionConnectors.add(TRANSFORMATION);
		this.number = number;
	}

	@Override
	public NumericExpression transform(HlclFactory f,
			Map<String, Identifier> idMap) {
		return f.number(number);
	}

}