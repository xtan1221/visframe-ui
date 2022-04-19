package builder.visframe.visinstance.run.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.run.VisInstanceRunBuilder;
import function.composition.CompositionFunction;
import function.variable.independent.IndependentFreeInputVariableType;
import javafx.fxml.FXMLLoader;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


public class IndependentFreeInputVariableTypeValueAssignerManager {
	private final VisInstanceRunBuilder ownerVisInstanceRunBuilder;
	private final IndependentFreeInputVariableType indieFIVType;
	/////////////////
	private IndependentFreeInputVariableTypeValueAssignerController controller;
	
	private CompositionFunction ownerCompositionFunction;
	/**
	 * 
	 * @param indieFIVType
	 * @throws SQLException 
	 */
	public IndependentFreeInputVariableTypeValueAssignerManager(
			VisInstanceRunBuilder ownerVisInstanceRunBuilder,
			IndependentFreeInputVariableType indieFIVType) throws SQLException{
		
		this.ownerVisInstanceRunBuilder = ownerVisInstanceRunBuilder;
		this.indieFIVType = indieFIVType;
		
		//
		this.ownerCompositionFunction = this.getOwnerVisInstanceRunBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionManager().lookup(this.indieFIVType.getOwnerCompositionFunctionID());
	}
	
	
	public boolean hasValidValueAssigned() {
		return ((VfDefinedPrimitiveSQLDataType)this.indieFIVType.getSQLDataType()).isValidStringValue(this.getController().getValueStringTextField().getText());
	}
	
	public String getStringValue() {
		return this.getController().getValueStringTextField().getText();
	}
	
	public void setStringValue(String value) {
		this.getController().setStringValue(value);
	}
	
	public void setModifiable(boolean modifiable) {
		this.getController().getValueStringTextField().setEditable(modifiable);
	}
	
	/**
	 * @return the indieFIVType
	 */
	public IndependentFreeInputVariableType getIndieFIVType() {
		return indieFIVType;
	}
	
	/**
	 * @return the ownerVisInstanceRunBuilder
	 */
	public VisInstanceRunBuilder getOwnerVisInstanceRunBuilder() {
		return ownerVisInstanceRunBuilder;
	}
	
	/**
	 * @return the controller
	 */
	public IndependentFreeInputVariableTypeValueAssignerController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(IndependentFreeInputVariableTypeValueAssignerController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return controller;
	}


	/**
	 * @return the ownerCompositionFunction
	 */
	public CompositionFunction getOwnerCompositionFunction() {
		return ownerCompositionFunction;
	}


}
