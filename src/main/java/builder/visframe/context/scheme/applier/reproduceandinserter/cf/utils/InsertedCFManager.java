package builder.visframe.context.scheme.applier.reproduceandinserter.cf.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.applier.reproduceandinserter.cf.CFReproducingAndInsertionManager;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilderFactory;
import function.composition.CompositionFunction;
import javafx.fxml.FXMLLoader;


/**
 * @author tanxu
 * 
 */
public class InsertedCFManager {
	////////////////////////////
	private final CFReproducingAndInsertionManager hostCFReproducingAndInsertionManager;
	
	private final CompositionFunction insertedCF;
	
	///////////////////////
	private InsertedCFController controller;
	
	//////////////
	private CompositionFunctionBuilder cfBuilder;
	
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
	public InsertedCFManager(
			CFReproducingAndInsertionManager hostCFReproducingAndInsertionManager,
			CompositionFunction insertedCF
			) throws IOException, SQLException{
		//TODO 
		this.hostCFReproducingAndInsertionManager = hostCFReproducingAndInsertionManager;
		this.insertedCF = insertedCF;
		
		//
		this.initialize();
	}
	
	private void initialize() throws IOException, SQLException {
		this.cfBuilder = 
				CompositionFunctionBuilderFactory.singleton(this.hostCFReproducingAndInsertionManager.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext())
				.build(this.insertedCF);
	}

	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		//do nothing
	}
	//////////////////////////
	public InsertedCFController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(InsertedCFController.FXML_FILE_DIR_STRING));
			
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
	 * @return the hostCFReproducingAndInsertionManager
	 */
	public CFReproducingAndInsertionManager getHostCFReproducingAndInsertionManager() {
		return hostCFReproducingAndInsertionManager;
	}

	/**
	 * @return the insertedCF
	 */
	public CompositionFunction getInsertedCompositionFunction() {
		return insertedCF;
	}

	/**
	 * @return the cfBuilder
	 */
	public CompositionFunctionBuilder getCompositionFunctionBuilder() {
		return cfBuilder;
	}

	
	
}
