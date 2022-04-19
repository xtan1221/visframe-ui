package builder.visframe.visinstance.nativevi.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.function.group.AbstractCompositionFunctionGroupBuilder;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import builder.visframe.visinstance.nativevi.NativeVisInstanceBuilder;
import function.composition.CompositionFunction;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroup;
import javafx.fxml.FXMLLoader;


/**
 * @author tanxu
 * 
 */
public class CoreShapeCFGManager {
	////////////////////////////
	private final NativeVisInstanceBuilder hostNativeVisInstanceBuilder;
	
	private final CompositionFunctionGroup coreShapeCFG;
	
	///////////////////////
	private CoreShapeCFGController controller;
	
	//////////////
	private AbstractCompositionFunctionGroupBuilder<?> coreShapeCFGBuilder;
	
	private Map<Integer, CoreShapeCFManager> CFIndexCoreShapeCFManagerMap;
	
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
	public CoreShapeCFGManager(
			NativeVisInstanceBuilder hostNativeVisInstanceBuilder,
			CompositionFunctionGroup coreShapeCFG
			) throws IOException, SQLException{
		// 
		if(hostNativeVisInstanceBuilder==null)
			throw new IllegalArgumentException("given hostNativeVisInstanceBuilder cannot be null!");
		if(coreShapeCFG==null)
			throw new IllegalArgumentException("given coreShapeCFG cannot be null!");
		
		
		
		this.hostNativeVisInstanceBuilder = hostNativeVisInstanceBuilder;
		this.coreShapeCFG = coreShapeCFG;
		
		//
		this.initialize();
	}
	
	/**
	 * initialize the 
	 * 1. {@link #coreShapeCFGBuilder}
	 * 2. {@link #CFIndexCoreShapeCFManagerMap}
	 * 		also add to the UI
	 * 			this is done in {@link CoreShapeCFGController#setManager(CoreShapeCFGManager)};
	 * @throws IOException
	 * @throws SQLException
	 */
	private void initialize() throws IOException, SQLException {
		//1
		this.coreShapeCFGBuilder = 
				CompositionFunctionGroupBuilderFactory.singleton(this.getHostNativeVisInstanceBuilder().getHostVisProjectDBContext())
				.build(this.coreShapeCFG.getClass());
		
		this.coreShapeCFGBuilder.setValue(this.coreShapeCFG, false);
		
		
		//2
		this.CFIndexCoreShapeCFManagerMap = new HashMap<>();
		for(CompositionFunctionID cfid:this.getHostNativeVisInstanceBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController()
		.getCompositionFunctionManager().getCompositionFunctionIDSetOfGroupID(this.coreShapeCFG.getID())){
			CompositionFunction cf = 
					this.getHostNativeVisInstanceBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController()
					.getCompositionFunctionManager().lookup(cfid);
			
			boolean assignedMandatoryTarget = false;
			for(SimpleName assignedTargetName:cf.getAssignedTargetNameSet()) {
				if(this.coreShapeCFG.getMandatoryTargetNameSet().contains(assignedTargetName)) {
					assignedMandatoryTarget = true;
					break;
				}
			}
			
			CoreShapeCFManager manager = new CoreShapeCFManager(this, cf, assignedMandatoryTarget);
			this.CFIndexCoreShapeCFManagerMap.put(cf.getIndexID(), manager);
		}
		
		
		//3 add CompositionFunctions to the UI
		//this will be done in the {@link CoreShapeCFGController#setManager(CoreShapeCFGManager)};
	}
	
	void updateSelectedCFNumber() {
		int selectedCFNum = 0;
		for(int index:this.CFIndexCoreShapeCFManagerMap.keySet()){
			if(this.CFIndexCoreShapeCFManagerMap.get(index).isSelected())
				selectedCFNum++;
		}
		
		this.getController().setSelectedCFNumber(selectedCFNum);
	}
	
	/**
	 * set the UI;
	 * then set each CoreShapeCFManager
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		//1
		this.getController().setModifiable(modifiable);
		//2
		this.getCfIndexCoreShapeCFManagerMap().forEach((index,manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	
	//////////////////////////
	public CoreShapeCFGController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CoreShapeCFGController.FXML_FILE_DIR_STRING));
			
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
	 * @return the hostNativeVisInstanceBuilder
	 */
	public NativeVisInstanceBuilder getHostNativeVisInstanceBuilder() {
		return hostNativeVisInstanceBuilder;
	}

	/**
	 * @return the coreShapeCFG
	 */
	public CompositionFunctionGroup getCoreShapeCFG() {
		return coreShapeCFG;
	}

	/**
	 * @return the cfIndexCoreShapeCFManagerMap
	 */
	public Map<Integer, CoreShapeCFManager> getCfIndexCoreShapeCFManagerMap() {
		return CFIndexCoreShapeCFManagerMap;
	}

	/**
	 * @return the cfgBuilder
	 */
	public AbstractCompositionFunctionGroupBuilder<?> getCoreShapeCFGBuilder() {
		return coreShapeCFGBuilder;
	}

}
