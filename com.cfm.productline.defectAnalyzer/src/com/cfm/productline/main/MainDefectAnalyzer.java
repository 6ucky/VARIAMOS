package com.cfm.productline.main;

import com.cfm.productline.defectAnalyzer.DefectAnalyzerController;
import com.cfm.productline.dto.DefectAnalyzerControllerInDTO;
import com.cfm.productline.dto.DefectAnalyzerControllerOutDTO;
import com.cfm.productline.dto.VMTransformerInDTO;
import com.cfm.productline.exceptions.FunctionalException;
import com.cfm.productline.model.defectAnalyzerModel.VariabilityModel;
import com.cfm.productline.model.enums.CorrectionSetIdentificationType;
import com.cfm.productline.model.enums.NotationType;
import com.cfm.productline.model.enums.SolverEditorType;
import com.cfm.productline.transformer.VariabilityModelTransformer;

public class MainDefectAnalyzer {

	/**
	 * M�todo para ejecutar el an�lisis de los defectos de un feature model
	 * desde c�digo ( sin pantalla gr�fica)
	 * 
	 * @param modelName
	 * @param modelPath
	 * @throws FunctionalException
	 */

	public DefectAnalyzerControllerOutDTO analyzeSplotFM(String modelName,
			String modelPath, String outputDirectoryPath)
			throws FunctionalException {

		try {
			// Variables
			VariabilityModel variabilityModel = null;
			DefectAnalyzerController defectAnalyzer = null;
			
			if(!outputDirectoryPath.endsWith("\\")){
				//Si el directorio no tiene \ entonces se adiciona
				outputDirectoryPath=outputDirectoryPath+"\\";
			}
					

			// Tipo de solver
			SolverEditorType prologEditorType = SolverEditorType.SWI_PROLOG;

			// Se instancia el transformador
			VMTransformerInDTO transformerInDTO = new VMTransformerInDTO();
			transformerInDTO.setNotationType(NotationType.FEATURES_MODELS);
			transformerInDTO.setPathToTransform(modelPath);
			VariabilityModelTransformer transformer = new VariabilityModelTransformer(
					transformerInDTO);
			variabilityModel = transformer.transformToVariabilityModel();

			// Se instancia el analizador de los defectos
			DefectAnalyzerControllerInDTO defectAnalyzerInDTO = new DefectAnalyzerControllerInDTO();

			// PARAMETRIZACI�N
			// Defectos que se desean verificar
			defectAnalyzerInDTO.setVerifyDeadFeatures(Boolean.TRUE);
			defectAnalyzerInDTO.setVerifyFalseOptionalElement(Boolean.TRUE);
			defectAnalyzerInDTO.setVerifyNonAttainableDomains(Boolean.FALSE);
			defectAnalyzerInDTO.setVerifyFalseProductLine(Boolean.TRUE);
			defectAnalyzerInDTO.setVerifyRedundancies(Boolean.TRUE);

			// Defectos para los que se desean analizar las causas y
			// correcciones
			defectAnalyzerInDTO.setAnalyzeDeadFeatures(Boolean.TRUE);
			defectAnalyzerInDTO.setAnalyzeFalseOptional(Boolean.TRUE);
			defectAnalyzerInDTO.setAnalyzeVoidModel(Boolean.TRUE);
			defectAnalyzerInDTO.setIdentifyCausesFalseProductLine(Boolean.TRUE);
			defectAnalyzerInDTO.setAnalyzeRedundancies(Boolean.TRUE);

			// Modelo transformado, editor de prolog y tipo de identificaci�n:
			// completa o parcial
			defectAnalyzerInDTO.setVariabilityModel(variabilityModel);
			defectAnalyzerInDTO.setPrologEditorType(prologEditorType);
			defectAnalyzerInDTO
					.setCorrectionSetIdentificationType(CorrectionSetIdentificationType.PARTIAL);

			defectAnalyzer = new DefectAnalyzerController();
			DefectAnalyzerControllerOutDTO outDTO = defectAnalyzer
					.analyzeModel(defectAnalyzerInDTO,
							modelName + System.currentTimeMillis(),
							outputDirectoryPath);

			System.out
					.println("El resultado del an�lisis se export� exitosamente en la ruta "
							+ outputDirectoryPath);

			return outDTO;
		} catch (FunctionalException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
