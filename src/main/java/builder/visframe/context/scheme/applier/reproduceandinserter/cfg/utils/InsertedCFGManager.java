package builder.visframe.context.scheme.applier.reproduceandinserter.cfg.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.applier.reproduceandinserter.cfg.CFGReproducingAndInsertionManager;
import builder.visframe.function.group.AbstractCompositionFunctionGroupBuilder;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import function.group.CompositionFunctionGroup;
import javafx.fxml.FXMLLoader;


/**
 * @author tanxu
 * 
 */
public class InsertedCFGManager {
	////////////////////////////
	private final CFGReproducingAndInsertionManager hostCFGReproducingAndInsertionManager;
	
	private final CompositionFunctionGroup insertedCFG;
	
	///////////////////////
	private InsertedCFGController controller;
	
	//////////////
	private AbstractCompositionFunctionGroupBuilder<?> cfgBuilder;
	
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
	public InsertedCFGManager(
			CFGReproducingAndInsertionManager hostCFGReproducingAndInsertionManager,
			CompositionFunctionGroup insertedCFG
			) throws IOException, SQLException{
		//TODO 
		this.hostCFGReproducingAndInsertionManager = hostCFGReproducingAndInsertionManager;
		this.insertedCFG = insertedCFG;
		
		//
		this.initialize();
	}
	
	private void initialize() throws IOException, SQLException {
		this.cfgBuilder = 
				CompositionFunctionGroupBuilderFactory.singleton(this.hostCFGReproducingAndInsertionManager.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext())
				.build(this.insertedCFG.getClass());
		
		this.cfgBuilder.setValue(this.insertedCFG, false);
		
	}
	
	public void setModifiable(boolean modifiable) {
		//do nothing
	}
	//////////////////////////
	public InsertedCFGController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(InsertedCFGController.FXML_FILE_DIR_STRING));
			
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
	 * @return the insertedCFG
	 */
	public CompositionFunctionGroup getInsertedCompositionFunctionGroup() {
		return insertedCFG;
	}



	/**
	 * @return the cfgBuilder
	 */
	public AbstractCompositionFunctionGroupBuilder<?> getCompositionFunctionGroupBuilder() {
		return cfgBuilder;
	}

	/**
	 * @return the hostOperationReproducingAndInsertionManager
	 */
	public CFGReproducingAndInsertionManager getHostCFGReproducingAndInsertionManager() {
		return this.hostCFGReproducingAndInsertionManager;
	}


}
