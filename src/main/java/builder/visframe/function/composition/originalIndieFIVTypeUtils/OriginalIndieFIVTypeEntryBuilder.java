package builder.visframe.function.composition.originalIndieFIVTypeUtils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.variable.IndependentFreeInputVariableTypeBuilder;
import javafx.fxml.FXMLLoader;

public class OriginalIndieFIVTypeEntryBuilder {
	private final CompositionFunctionBuilder hostCompositionFunctionBuilder;
	
	
	private final IndependentFreeInputVariableTypeBuilder independentFreeInputVariableTypeBuilder;
	
	
	private OriginalIndieFIVTypeEntryUIController controller;
	
	/**
	 * 
	 * @param hostCompositionFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public OriginalIndieFIVTypeEntryBuilder(CompositionFunctionBuilder hostCompositionFunctionBuilder) throws SQLException, IOException{
		this.hostCompositionFunctionBuilder = hostCompositionFunctionBuilder;
		
		this.independentFreeInputVariableTypeBuilder = new IndependentFreeInputVariableTypeBuilder(
				"originalIndependentFreeInputVariableType", "originalIndependentFreeInputVariableType", false, null,
				this.hostCompositionFunctionBuilder, 
				e->{return true;}//Predicate<SQLDataType> dataTypeConstraints
				);
		
	}


	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	public CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return hostCompositionFunctionBuilder;
	}


	/**
	 * @return the independentFreeInputVariableTypeBuilder
	 */
	public IndependentFreeInputVariableTypeBuilder getIndependentFreeInputVariableTypeBuilder() {
		return independentFreeInputVariableTypeBuilder;
	}


	/**
	 * @return the controller
	 */
	public OriginalIndieFIVTypeEntryUIController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(OriginalIndieFIVTypeEntryUIController.FXML_FILE_DIR_STRING));
			
	    	try {
				loader.load();
		    	this.controller = (OriginalIndieFIVTypeEntryUIController)loader.getController();
		    	this.controller.setOwnerBuilder(this);
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return controller;
	}
	
	/**
	 * set whether this SimpleFunctionEvaluatorEntryBuilder can be deleted or not;
	 * @param setDeleteDisable
	 */
	public void setDeleteDisable(boolean disable) {
		this.getController().setDeleteDisable(disable);
	}


	public void setEmployerFreeInputVariableNum(int num) {
		this.getController().setEmployerFreeInputVariableNum(num);
	}
	

	public void setTypeName(String typeName) {
		this.getController().setTypeName(typeName);
	}


	public void setDeleteButtonDisable(boolean disable) {
		this.getController().setDeleteButtonDisable(disable);
	}
	
//	/**
//	 * set whether this SimpleFunctionEvaluatorEntryBuilder can be deleted or not;
//	 * @param setDeleteDisable
//	 */
//	public void setEditDisable(boolean disable) {
//		this.controller.setEditDisable(disable);
//	}
}
