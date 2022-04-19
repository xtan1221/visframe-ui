package builder.visframe.visinstance.nativevi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import builder.visframe.visinstance.VisInstanceBuilderBase;
import builder.visframe.visinstance.nativevi.utils.CoreShapeCFGManager;
import context.project.VisProjectDBContext;
import function.group.CompositionFunctionGroupID;
import function.group.ShapeCFG;
import visinstance.NativeVisInstance;

/**
 * builder for a VisSchemeAppliedArchive with a pre-selected applied VisScheme and an assigned UID for VisSchemeAppliedArchive;
 * 
 * @author tanxu
 * 
 */
public final class NativeVisInstanceBuilder extends VisInstanceBuilderBase<NativeVisInstance, NativeVisInstanceBuilderEmbeddedUIContentController> {
	public static final String NODE_NAME = "NativeVisInstance";
	public static final String NODE_DESCRIPTION = "NativeVisInstance";
	
	////////////////////////////
	private final Set<CompositionFunctionGroupID> shapeCFGIDSetWithAllMandatoryTargetAssigned;
	
	
	////////////////////
	private Map<CompositionFunctionGroupID, CoreShapeCFGManager> selectedCoreShapeCFGIDManagerMap;
	
	private boolean finishable;
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param ownerCompositionFunctionGroup
	 * @param indexID
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public NativeVisInstanceBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			int visInstanceUID
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, NativeVisInstanceBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING, hostVisProjectDBContext, visInstanceUID);
		
		/////////////////
		this.selectedCoreShapeCFGIDManagerMap = new LinkedHashMap<>();
		this.shapeCFGIDSetWithAllMandatoryTargetAssigned = 
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().getShapeCFGIDSetWithAllMandatoryTargetsAssignedToCF();
	}
	
	/**
	 * 
	 * @param shapeCFG
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void addCoreShapeCFG(ShapeCFG shapeCFG, boolean toInvokeLayoutUpdate) throws IOException, SQLException {
		CoreShapeCFGManager manager = new CoreShapeCFGManager(this, shapeCFG);
		
		this.selectedCoreShapeCFGIDManagerMap.put(shapeCFG.getID(), manager);
		
		if(toInvokeLayoutUpdate) {
			this.getEmbeddedUIContentController().updateSelectedCoreShapeCFGLayoutOrder();
			this.checkFinishable();
		}
	}
	
	public void clearAllCoreShapeCFGs() throws SQLException {
		this.selectedCoreShapeCFGIDManagerMap.clear();
		this.getEmbeddedUIContentController().updateSelectedCoreShapeCFGLayoutOrder();
		this.checkFinishable();
	}
	
	
	public void removeSelectedShapeCFG(CompositionFunctionGroupID shapeCFGID) throws SQLException {
		this.selectedCoreShapeCFGIDManagerMap.remove(shapeCFGID);
		this.getEmbeddedUIContentController().updateSelectedCoreShapeCFGLayoutOrder();
		this.checkFinishable();
	}
	
	//////////////////////////////////////
	/**
	 * first invoker super class's method;
	 * 
	 * then invoke the {@link CoreShapeCFGManager#setModifiable(boolean)} of each selected core ShapeCFG in {@link #selectedCoreShapeCFGIDManagerMap};
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		super.setModifiable(modifiable);
		
		this.selectedCoreShapeCFGIDManagerMap.forEach((id,manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	/**
	 * invoked whenever a change is made that could result in change of the target NativeVisInstance;
	 */
	public void checkFinishable() {
		try {
			this.getEmbeddedUIContentController().build();
			this.getEmbeddedUIContentController().finishableLabel.setText("FINISHABLE");
			this.finishable = true;
		}catch(Exception e) {
			System.out.println(this.getClass().getName()+": "+e.getMessage());
			this.getEmbeddedUIContentController().finishableLabel.setText("UNFINISHABLE");
			this.finishable = false;
		}
	}

	/**
	 * @return the finishable
	 */
	public boolean isFinishable() {
		return finishable;
	}

	
	////////////////////////////////////
	/**
	 * @return the selectedCoreShapeCFGIDManagerMap
	 */
	public Map<CompositionFunctionGroupID, CoreShapeCFGManager> getSelectedCoreShapeCFGIDManagerMap() {
		return selectedCoreShapeCFGIDManagerMap;
	}


	/**
	 * @return the shapeCFGIDSetWithAllMandatoryTargetAssigned
	 */
	public Set<CompositionFunctionGroupID> getShapeCFGIDSetWithAllMandatoryTargetAssigned() {
		return shapeCFGIDSetWithAllMandatoryTargetAssigned;
	}

}
