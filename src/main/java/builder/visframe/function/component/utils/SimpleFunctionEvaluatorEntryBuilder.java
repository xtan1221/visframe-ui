package builder.visframe.function.component.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.component.SimpleFunctionBuilder;
import builder.visframe.function.evaluator.delegator.SimpleFunctionEvaluatorBuilderDelegate;
import javafx.fxml.FXMLLoader;


public class SimpleFunctionEvaluatorEntryBuilder {
	private final SimpleFunctionBuilder hostSimpleFunctionBuilder;
	
	private final SimpleFunctionEvaluatorBuilderDelegate SimpleFunctionEvaluatorBuilderDelegate;
	
	private SimpleFunctionEvaluatorEntryUIController controller;
	
	/**
	 * 
	 * @param hostSimpleFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SimpleFunctionEvaluatorEntryBuilder(SimpleFunctionBuilder hostSimpleFunctionBuilder) throws SQLException, IOException{
		this.hostSimpleFunctionBuilder = hostSimpleFunctionBuilder;
		
		this.SimpleFunctionEvaluatorBuilderDelegate = new SimpleFunctionEvaluatorBuilderDelegate(
				"", "", false, null, this.hostSimpleFunctionBuilder);
		
		
	}
	
	/**
	 * @return the hostSimpleFunctionBuilder
	 */
	public SimpleFunctionBuilder getHostSimpleFunctionBuilder() {
		return hostSimpleFunctionBuilder;
	}

	
	public SimpleFunctionEvaluatorEntryUIController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(SimpleFunctionEvaluatorEntryUIController.FXML_FILE_DIR_STRING));
			
	    	try {
				loader.load();
		    	this.controller = (SimpleFunctionEvaluatorEntryUIController)loader.getController();
		    	this.controller.setOwnerBuilder(this);
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return controller;
	}

	
	public SimpleFunctionEvaluatorBuilderDelegate getSimpleFunctionEvaluatorBuilderDelegate() {
		return SimpleFunctionEvaluatorBuilderDelegate;
	}
	
	/**
	 * set whether this SimpleFunctionEvaluatorEntryBuilder can be deleted or not;
	 * @param setDeleteDisable
	 */
	public void setDeleteDisable(boolean disable) {
		this.getController().setDeleteDisable(disable);
	}
	
	/**
	 * set whether this SimpleFunctionEvaluatorEntryBuilder can be deleted or not;
	 * @param setDeleteDisable
	 */
	public void setEditDisable(boolean disable) {
		this.getController().setEditDisable(disable);
	}
	
	/**
	 * 
	 * @param modifiable
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//1. set UI controller
		this.getController().setModifiable(modifiable);
		//2. set the evaluator builder
		this.SimpleFunctionEvaluatorBuilderDelegate.setModifiable(modifiable);
	}
}
