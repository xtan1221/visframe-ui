package builder.visframe.context.scheme.utils;

import java.io.IOException;

import builder.visframe.context.scheme.VSComponentBuilder;
import function.group.ShapeCFG;
import javafx.fxml.FXMLLoader;

/**
 * manager class for a ShapeCFG entry of a {@link VSComponentBuilder}
 * @author tanxu
 *
 */
public class CoreShapeCFGEntryManager {
	private final VSComponentBuilder ownerVSComponentBuilder;
	private final ShapeCFG coreShapeCFG;
	
	////////////
	private CoreShapeCFGEntryController controller;
	
	
	public CoreShapeCFGEntryManager(VSComponentBuilder ownerVSComponentBuilder, ShapeCFG coreShapeCFG){
		this.ownerVSComponentBuilder = ownerVSComponentBuilder;
		this.coreShapeCFG = coreShapeCFG;
	}


	/**
	 * @return the controller
	 */
	public CoreShapeCFGEntryController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CoreShapeCFGEntryController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
			
		}
		
		return this.controller;
		
	}
	
	
	
	
	/**
	 * @return the ownerVSComponentBuilder
	 */
	public VSComponentBuilder getOwnerVSComponentBuilder() {
		return ownerVSComponentBuilder;
	}


	/**
	 * @return the coreShapeCFG
	 */
	public ShapeCFG getCoreShapeCFG() {
		return coreShapeCFG;
	}

}
