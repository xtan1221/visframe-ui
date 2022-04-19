package builder.visframe.visinstance.nativevi.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilderFactory;
import function.composition.CompositionFunction;
import javafx.fxml.FXMLLoader;


/**
 * UI manager for a CompositionFunction of a core ShapeCFG;
 * 
 * @author tanxu
 * 
 */
public class CoreShapeCFManager {
	////////////////////////////
	private final CoreShapeCFGManager hostCoreShapeCFGManager;
	
	private final CompositionFunction compositionFunction;
	
	private final boolean assignedMandataryTargets;
	
	///////////////////////
	private CoreShapeCFController controller;
	
	//////////////
	private CompositionFunctionBuilder cfBuilder;
	
	public CoreShapeCFManager(
			CoreShapeCFGManager hostCoreShapeCFGManager,
			CompositionFunction compositionFunction,
			boolean assignedMandataryTargets
			) throws IOException, SQLException{
		//TODO 
		
		
		
		this.hostCoreShapeCFGManager = hostCoreShapeCFGManager;
		this.compositionFunction = compositionFunction;
		this.assignedMandataryTargets = assignedMandataryTargets;
		//
		this.initialize();
	}
	
	/**
	 * build the {@link #cfBuilder}
	 * 
	 * set the initial selected or not
	 * @throws IOException
	 * @throws SQLException
	 */
	private void initialize() throws IOException, SQLException {
		this.cfBuilder = 
				CompositionFunctionBuilderFactory.singleton(this.hostCoreShapeCFGManager.getHostNativeVisInstanceBuilder().getHostVisProjectDBContext())
				.build(this.compositionFunction);
		
		//
	}
	
	////////////////////////
	/**
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return this.getController().getToIncludeInVisInstanceCheckBox().isSelected();
	}
	
	/**
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		if(this.assignedMandataryTargets) {
			//do nothing
		}else {
			this.getController().getToIncludeInVisInstanceCheckBox().setSelected(selected);
		}
	}
	
	///////////////////////////
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
	}
	
	
	//////////////////////////
	public CoreShapeCFController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CoreShapeCFController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);//debug
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}

	

	/**
	 * @return the hostCoreShapeCFGManager
	 */
	public CoreShapeCFGManager getHostCoreShapeCFGManager() {
		return hostCoreShapeCFGManager;
	}

	/**
	 * @return the compositionFunction
	 */
	public CompositionFunction getCompositionFunction() {
		return compositionFunction;
	}

	/**
	 * @return the assignedMandataryTargets
	 */
	public boolean isAssignedMandataryTargets() {
		return assignedMandataryTargets;
	}

	/**
	 * @return the cfBuilder
	 */
	public CompositionFunctionBuilder getCompositionFunctionBuilder() {
		return cfBuilder;
	}



}
