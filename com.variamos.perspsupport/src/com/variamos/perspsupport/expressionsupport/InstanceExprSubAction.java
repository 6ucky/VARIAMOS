package com.variamos.perspsupport.expressionsupport;

import java.util.List;

import com.variamos.perspsupport.types.OperationSubActionExecType;

/**
 * TODO
 * A class to support instance sub actions to generalize the semantic operationalization 
 * with GUI edition. Part of PhD work at University of Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2014-02-05
 */
/**
 * @author jcmunoz
 *
 */
public class InstanceExprSubAction {
	private OperationSubAction expressionSubAction;
	private List<InstanceExpression> instanceExpressions;
	private OperationSubActionExecType expressionType;

	public OperationSubAction getExpressionSubAction() {
		return expressionSubAction;
	}

	public void setExpressionSubAction(OperationSubAction expressionSubAction) {
		this.expressionSubAction = expressionSubAction;
	}

	public List<InstanceExpression> getInstanceExpressions() {
		return instanceExpressions;
	}

	public void setInstanceExpressions(
			List<InstanceExpression> instanceExpressions) {
		this.instanceExpressions = instanceExpressions;
	}

	public OperationSubActionExecType getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(OperationSubActionExecType expressionType) {
		this.expressionType = expressionType;
	}

}
