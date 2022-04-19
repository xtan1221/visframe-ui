package builder.visframe.context.scheme.applier.reproduceandinserter.operation.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.applier.reproduceandinserter.operation.OperationReproducingAndInsertionManager;
import builder.visframe.operation.AbstractOperationBuilder;
import builder.visframe.operation.OperationBuilderFactory;
import javafx.fxml.FXMLLoader;
import operation.Operation;
import utils.FXUtils;


/**
 * manager class for a reproduced Operation that needs to be inserted;
 * 
 * also provide utilities to set parameter values if the operation has parameter dependent on input data table content;
 * 
 * @author tanxu
 * 
 */
public class ReproducedOperationManager {
	////////////////////////////
	private final OperationReproducingAndInsertionManager hostOperationReproducingAndInsertionManager;
	
	/**
	 * the original reproduced Operation;
	 * 
	 * for Operation with NO parameter dependent on input data table content, the reproduced operation will stay the same through the whole process;
	 * 		and the inserted Operation will be this one;
	 * 
	 * for Operation with parameter dependent on input data table content, those parameters' values should be set before insertion;
	 * 		only after a valid Operation with all such parameters assigned valid values (with the help of {@link #operationBuilder}) and saved to {@link #reproducedOperationWithValueOfParametersDependentOnInputTableContentSet}, can the operation be inserted;
	 * 		also, in this case, the inserted Operation should be the {@link #reproducedOperationWithValueOfParametersDependentOnInputTableContentSet};
	 */
	private final Operation originalReproducedOperation;
	
	
	///////////////////////
	private ReproducedOperationController controller;
	
	//////////////
	/**
	 * 
	 */
	private AbstractOperationBuilder<?> operationBuilder;
	/**
	 * only relevant if the {@link #originalReproducedOperation} has parameter dependent on input data table content;
	 */
	private Operation reproducedOperationWithValueOfParametersDependentOnInputTableContentSet;
	
	/**
	 * 
	 */
	private boolean operationInsertable;
	

	/**
	 * constructor
	 * 
	 * @param underlyingSimpleDOSGraph
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public ReproducedOperationManager(
			OperationReproducingAndInsertionManager hostOperationReproducingAndInsertionManager,
			Operation reproducedOperation
			) throws IOException, SQLException{
		//TODO 
		this.hostOperationReproducingAndInsertionManager = hostOperationReproducingAndInsertionManager;
		this.originalReproducedOperation = reproducedOperation;
		
		//
		this.initialize();
	}
	
	/**
	 * initialize the {@link #operationBuilder} and set up the status change event action if has parameter dependent on input data table content;
	 * @throws IOException
	 * @throws SQLException
	 */
	void initialize() throws IOException, SQLException {
		this.operationBuilder = 
				OperationBuilderFactory.singleton(this.hostOperationReproducingAndInsertionManager.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext())
				.build(this.originalReproducedOperation, true);//forReproducing is true
		
		if(this.originalReproducedOperation.hasInputDataTableContentDependentParameter()) {
			this.operationBuilder.addStatusChangedAction(
					()->{
						if(this.operationBuilder.getCurrentStatus().hasValidValue()) {
							this.setInsertable(true);
							this.reproducedOperationWithValueOfParametersDependentOnInputTableContentSet = this.operationBuilder.getCurrentValue();
						}else {
							this.setInsertable(false);
							this.reproducedOperationWithValueOfParametersDependentOnInputTableContentSet = null;
						}
					});
			
			this.setInsertable(false);
			FXUtils.set2Disable(this.getController().getEditButton(), false); //can be edited or reset
			FXUtils.set2Disable(this.getController().getResetButton(), false);
		}else {
			this.setInsertable(true);
			FXUtils.set2Disable(this.getController().getEditButton(), true); //cannot be edited
			FXUtils.set2Disable(this.getController().getResetButton(), true); //cannot be reset
		}
	}
	
	
	/**
	 * update the UI
	 * @param insertable
	 */
	void setInsertable(boolean insertable) {
		this.operationInsertable = insertable;
		//update the UI
		//insert button 
		FXUtils.set2Disable(this.getController().getInsertButton(), !this.operationInsertable);
		//
		if(this.operationInsertable) {
			this.getController().statusLabel.setText("INSERTABLE");
		}else {
			this.getController().statusLabel.setText("UNFINISHED");
		}
	}
	
	/**
	 * return the insertable reproduced Operation;
	 * 
	 * @return
	 */
	public Operation getInsertableReproducedOperation() {
		if(this.originalReproducedOperation.hasInputDataTableContentDependentParameter()) {
			if(this.reproducedOperationWithValueOfParametersDependentOnInputTableContentSet == null)
				throw new UnsupportedOperationException("reproduced Operation has parameter dependent on input data table content but their values are not set properly!");
			return this.reproducedOperationWithValueOfParametersDependentOnInputTableContentSet;
		}else {
			return this.originalReproducedOperation;
		}
	}
	//////////////////////////
	public ReproducedOperationController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(ReproducedOperationController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}


	/**
	 * @return the reproducedOperation
	 */
	public Operation getReproducedOperation() {
		return originalReproducedOperation;
	}

	
	/**
	 * @return the operationBuilder
	 */
	public AbstractOperationBuilder<?> getOperationBuilder() {
		return operationBuilder;
	}


	/**
	 * @return the hostOperationReproducingAndInsertionManager
	 */
	public OperationReproducingAndInsertionManager getHostOperationReproducingAndInsertionManager() {
		return hostOperationReproducingAndInsertionManager;
	}



}
