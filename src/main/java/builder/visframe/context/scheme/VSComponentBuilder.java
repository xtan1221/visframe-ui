package builder.visframe.context.scheme;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import builder.visframe.context.scheme.utils.CoreShapeCFGEntryManager;
import context.scheme.VSComponent;
import context.scheme.VisScheme;
import core.builder.LeafNodeBuilder;
import function.group.CompositionFunctionGroupID;
import function.group.ShapeCFG;

public class VSComponentBuilder extends LeafNodeBuilder<VSComponent, VSComponentBuilderEmbeddedUIContentController>{
	private final VisSchemeBuilder ownerVisSchemeBuilder;
	
	//////////////////////////
	private int precedenceIndex;
	private Map<CompositionFunctionGroupID, CoreShapeCFGEntryManager> selectedCoreShapeCFGIDEntryManagerMap;
	
	///////////////////
	/**
	 * temporary VisScheme;
	 * only be used when host VisSchemeBuilder is used to view an existing VisScheme;
	 * cannot be used when creating a new VisScheme;
	 */
	private VisScheme tempVisScheme;
	
	/**
	 * constructor
	 * @param ownerVisSchemeBuilder
	 */
	protected VSComponentBuilder(VisSchemeBuilder ownerVisSchemeBuilder) {
		super("VSComponent", "VSComponent", false, null, VSComponentBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		this.ownerVisSchemeBuilder = ownerVisSchemeBuilder;
		
		this.selectedCoreShapeCFGIDEntryManagerMap = new HashMap<>();
		
	}
	
	
	public VisSchemeBuilder getOwnerVisSchemeBuilder() {
		return ownerVisSchemeBuilder;
	}
	
	public int getPrecedenceIndex() {
		return precedenceIndex;
	}

	/**
	 * set the precedence index of this VSComponentBuilder;
	 * also update the UI
	 * @param precedenceIndex
	 */
	public void setPrecedenceIndex(int precedenceIndex) {
		this.precedenceIndex = precedenceIndex;
		this.getEmbeddedUIContentController().getPrecedenceIndexTextField().setText(Integer.toString(this.precedenceIndex));
	}
	
	
	public Map<CompositionFunctionGroupID, CoreShapeCFGEntryManager> getSelectedCoreShapeCFGIDEntryManagerMap() {
		return selectedCoreShapeCFGIDEntryManagerMap;
	}
	
	
	//////////////////////
	/**
	 * invoked when the controller is first time created and whenever  selectedCoreShapeCFGIDEntryManagerMap is changed;
	 */
	void updateSelectedCoreShapeCFGNumTextField() {
		this.getEmbeddedUIContentController().getSelectedCoreShapeCFGNumTextField().setText(Integer.toString(this.selectedCoreShapeCFGIDEntryManagerMap.size()));
//		
	}
	
	/**
	 * remove the given core ShapeCFG's ID from the selected set and the UI;
	 * @param id
	 * @throws SQLException 
	 */
	public void removeCoreShapeCFG(CompositionFunctionGroupID id) throws SQLException {
		//remove from the UI
		this.getEmbeddedUIContentController().getSelectedCoreShapeCFGEntrySetContainerVBox().getChildren().remove(this.selectedCoreShapeCFGIDEntryManagerMap.get(id).getController().getRootContainerPane());
		//remove from the selectedCoreShapeCFGIDEntryManagerMap
		this.selectedCoreShapeCFGIDEntryManagerMap.remove(id);
		
		//update the selected core ShapeCFG number text field
		this.updateSelectedCoreShapeCFGNumTextField();
		
		//update owner VisSchemeBuilder
		this.getOwnerVisSchemeBuilder().getSelectedShapeCFGIDSet().remove(id);
		
		//update the current value of this builder
		this.getEmbeddedUIContentController().update();
		
		this.getOwnerVisSchemeBuilder().updateVCDGraphLayoutAndInvolvedVisframeEntities();
	}
	
	/**
	 * remove all selected core ShapeCFGs
	 * @throws SQLException 
	 */
	public void removeAllCoreShapeCFGs() throws SQLException {
		this.getEmbeddedUIContentController().getSelectedCoreShapeCFGEntrySetContainerVBox().getChildren().clear();
		
		this.selectedCoreShapeCFGIDEntryManagerMap.keySet().forEach(e->{
			this.getOwnerVisSchemeBuilder().getSelectedShapeCFGIDSet().remove(e);
		});
		
		this.selectedCoreShapeCFGIDEntryManagerMap.clear();
		
		//update the selected core ShapeCFG number text field
		this.updateSelectedCoreShapeCFGNumTextField();
		
		//update the current value of this builder
		this.getEmbeddedUIContentController().update();
				
				
		this.getOwnerVisSchemeBuilder().updateVCDGraphLayoutAndInvolvedVisframeEntities();
	}
	
	/**
	 * add the given core ShapeCFG to the selected and the UI;
	 * 
	 * also update the {@link #ownerVisSchemeBuilder}'s selected set of core shapeCFG set
	 * 
	 * invoke the {@link #ownerVisSchemeBuilder}'s {@link VisSchemeBuilder#updateVCDGraphLayoutAndInvolvedVisframeEntities()} method 
	 * 
	 * @param shapeCFGSet
	 * @throws SQLException 
	 */
	public void addCoreShapeCFG(ShapeCFG shapeCFG) throws SQLException {
		CoreShapeCFGEntryManager entryManager = new CoreShapeCFGEntryManager(this, shapeCFG);
		
		this.selectedCoreShapeCFGIDEntryManagerMap.put(shapeCFG.getID(), entryManager);
		
		this.getEmbeddedUIContentController().getSelectedCoreShapeCFGEntrySetContainerVBox().getChildren().add(entryManager.getController().getRootContainerPane());
		
		//update the selected core ShapeCFG number text field
		this.updateSelectedCoreShapeCFGNumTextField();
		
		
		this.getOwnerVisSchemeBuilder().getSelectedShapeCFGIDSet().add(shapeCFG.getID());
		
		//update the current value of this builder
		this.getEmbeddedUIContentController().update();
				
		
		this.getOwnerVisSchemeBuilder().updateVCDGraphLayoutAndInvolvedVisframeEntities();
	}

	
	/**
	 * @return the tempVisScheme
	 */
	public VisScheme getTempVisScheme() {
		return tempVisScheme;
	}


	/**
	 * @param tempVisScheme the tempVisScheme to set
	 */
	public void setTempVisScheme(VisScheme tempVisScheme) {
		this.tempVisScheme = tempVisScheme;
	}

}
