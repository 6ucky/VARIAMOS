package com.variamos.reasoning.core.transformer;

import com.variamos.common.core.exceptions.TransformerException;
import com.variamos.reasoning.defectAnalyzer.model.dto.VMTransformerInDTO;
import com.variamos.reasoning.defectAnalyzer.model.transformation.VariabilityModel;


public interface ITransformer {

	public abstract VariabilityModel transform (VMTransformerInDTO inDTO) throws TransformerException;
		
	
}
