package viewer.visframe.function.target;

import function.target.IndependentPrimitiveAttributeTarget;

/**
 * 
 */
public class IndependentPrimitiveAttributeTargetViewer extends CFGTargetViewerBase<IndependentPrimitiveAttributeTarget<?>, IndependentPrimitiveAttributeTargetViewerController>{
	
	public IndependentPrimitiveAttributeTargetViewer(IndependentPrimitiveAttributeTarget<?> value) {
		super(value, IndependentPrimitiveAttributeTargetViewerController.FXML_FILE_DIR_STRING);
	}
	
}
