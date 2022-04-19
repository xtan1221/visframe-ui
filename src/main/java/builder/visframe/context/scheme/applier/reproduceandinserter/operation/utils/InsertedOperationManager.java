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
 * @author tanxu
 * 
 */
public class InsertedOperationManager {
	////////////////////////////
	private final OperationReproducingAndInsertionManager hostOperationReproducingAndInsertionManager;
	
	private final Operation insertedOperation;
	
	///////////////////////
	private InsertedOperationController controller;
	
	//////////////
	private AbstractOperationBuilder<?> operationBuilder;
	
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
	public InsertedOperationManager(
			OperationReproducingAndInsertionManager hostOperationReproducingAndInsertionManager,
			Operation insertedOperation
			) throws IOException, SQLException{
		//TODO 
		this.hostOperationReproducingAndInsertionManager = hostOperationReproducingAndInsertionManager;
		this.insertedOperation = insertedOperation;
		
		//
		this.initialize();
	}
	
	private void initialize() throws IOException, SQLException {
		this.operationBuilder = 
				OperationBuilderFactory.singleton(this.hostOperationReproducingAndInsertionManager.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext())
				.build(this.insertedOperation, false); //not for reproducing, only for view-mode
	
		
		//TODO
	
	}
	
	/**
	 * set this inserted Operation manager's UI to roll backable or not;
	 * 
	 * note that only when this operation is the most recently inserted one, it is roll-backable;
	 * otherwise, it is not;
	 * 
	 * by default, it is roll-backable, until there is another operation inserted;
	 * 
	 * @param rollbackable
	 */
	public void setToRollbackable(boolean rollbackable) {
		FXUtils.set2Disable(this.getController().getRollbackButton(),!rollbackable);
	}
	
	
	/**
	 * set modifiable
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.getController().rollbackButton, !modifiable);
	}
	//////////////////////////
	public InsertedOperationController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(InsertedOperationController.FXML_FILE_DIR_STRING));
			
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
	 * @return the insertedOperation
	 */
	public Operation getInsertedOperation() {
		return insertedOperation;
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
