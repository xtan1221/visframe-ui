package builder.visframe.visinstance.visschemebased;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import builder.visframe.visinstance.VisInstanceBuilderBase;
import builder.visframe.visinstance.nativevi.utils.CoreShapeCFGManager;
import context.project.VisProjectDBContext;
import context.scheme.VisScheme;
import context.scheme.VisSchemeID;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.VisSchemeAppliedArchiveID;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceID;
import dependency.vccl.VSCopy;
import dependency.vcd.VCDEdgeImpl;
import dependency.vcd.VCDNodeImpl;
import dependencygraph.vccl.nodeselection.CopyLink;
import dependencygraph.vccl.nodeselection.CopyNodeSelectionManager;
import dependencygraph.vcd.VCDNodeOrderingComparatorFactory;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroupID;
import visinstance.VisSchemeBasedVisInstance;

/**
 * builder for a VisSchemeAppliedArchive with a pre-selected applied VisScheme and an assigned UID for VisSchemeAppliedArchive;
 * 
 * @author tanxu
 *
 */
public final class VisSchemeBasedVisInstanceBuilder extends VisInstanceBuilderBase<VisSchemeBasedVisInstance, VisSchemeBasedVisInstanceBuilderEmbeddedUIContentController> {
	public static final String NODE_NAME = "VisSchemeBasedVisInstance";
	public static final String NODE_DESCRIPTION = "VisSchemeBasedVisInstance";
	
	////////////////////////////
	private final VisSchemeID visSchemeID;
	private final VisSchemeAppliedArchiveID visSchemeAppliedArchiveID;
	private final VisSchemeAppliedArchiveReproducedAndInsertedInstanceID visSchemeAppliedArchiveReproducedAndInsertedInstanceID;
	
	
	///////////////////////////////
	private VisScheme visScheme;
	private VisSchemeAppliedArchive visSchemeAppliedArchive;
	private VisSchemeAppliedArchiveReproducedAndInsertedInstance visSchemeAppliedArchiveReproducedAndInsertedInstance;
	
	//////////////////////
	private CopyNodeSelectionManager<VCDNodeImpl, VCDEdgeImpl> copyNodeSelectionManager;
	
	/**
	 * the currently selected VSCopy on the VCCL graph of the {@link #visSchemeAppliedArchive} whose core ShapeCFGs are to be included in the core ShapeCFG set of the target VisSchemeBasedVisInstance;
	 * should be updated by {@link #update()} method;
	 */
	private Set<VSCopy> selectedVSCopySetInCoreShapeCFGSet;
	/**
	 * the reproduced core ShapeCFGs' CompositionFunctionGroupIDs of the currently selected VSCopys;
	 * should be updated by {@link #update()} method;
	 */
	private Set<CompositionFunctionGroupID> selectedCoreShapeCFGIDSet;
	/**
	 * the reproduced CompositionFunctionIDs of the core ShapeCFGs of the currently selected VSCopys;
	 * should be updated by {@link #update()} method;
	 */
	private Map<CompositionFunctionGroupID, Set<CompositionFunctionID>> selectedCoreShapeCFGIDCFIDSetMap;
	
	/////////////////////
	/**
	 * whether the target VisSchemeAppliedArchive is finishable to be inserted into the host VisProjectDBContext;
	 */
	private boolean finishable;
	
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param ownerCompositionFunctionGroup
	 * @param indexID
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public VisSchemeBasedVisInstanceBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			int visInstanceUID,
			
			VisSchemeID visSchemeID,
			VisSchemeAppliedArchiveID visSchemeAppliedArchiveID,
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceID visSchemeAppliedArchiveReproducedAndInsertedInstanceID
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, VisSchemeBasedVisInstanceBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING, hostVisProjectDBContext, visInstanceUID);
		
		
		this.visSchemeID = visSchemeID;
		this.visSchemeAppliedArchiveID = visSchemeAppliedArchiveID;
		this.visSchemeAppliedArchiveReproducedAndInsertedInstanceID = visSchemeAppliedArchiveReproducedAndInsertedInstanceID;
		
		this.initialize();
	}
	
	/**
	 * initialize the 
	 * 1. {@link #visScheme}
	 * 2. {@link #visSchemeAppliedArchive}
	 * 3. {@link #visSchemeAppliedArchiveReproducedAndInsertedInstance}
	 * 
	 * 4. initialize the fields related selected vccl nodes;
	 * 
	 * 5. {@link #copyNodeSelectionManager}
	 * 		note that the UI of the selection manager is done in the {@link VisSchemeBasedVisInstanceBuilderEmbeddedUIContentController#setupLogicToCheckEffectiveUIInput()} method
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		//1
		this.visScheme = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().lookup(this.visSchemeID);
		//2
		this.visSchemeAppliedArchive = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveManager().lookup(this.visSchemeAppliedArchiveID);
		
		//3
		this.visSchemeAppliedArchiveReproducedAndInsertedInstance = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveReproducedAndInsertedInstanceManager().lookup(this.visSchemeAppliedArchiveReproducedAndInsertedInstanceID);
		
		//4
		this.selectedVSCopySetInCoreShapeCFGSet = new HashSet<>();
		this.selectedCoreShapeCFGIDSet = new HashSet<>();
		this.selectedCoreShapeCFGIDCFIDSetMap = new HashMap<>();
		
		
		//5
		Map<VCDNodeImpl, Integer> VCDNodeCopyNumberMap = new HashMap<>();
		this.visSchemeAppliedArchive.getVcdNodeAssignedCopyNumberMap().forEach((node,copyNumber)->{
			VCDNodeCopyNumberMap.put(node, copyNumber);
		});
		
		Set<CopyLink<VCDNodeImpl>> copyLinkSet = new HashSet<>();
		this.visSchemeAppliedArchive.getVcclGraph().edgeSet().forEach(e->{
			VSCopy dependingCopy = e.getDependingCopy();
			VSCopy dependedCopy = e.getDependedCopy();
			
			copyLinkSet.add(
					new CopyLink<>(
							dependingCopy.getOwnerVCDNode(), dependingCopy.getIndex(), 
							dependedCopy.getOwnerVCDNode(), dependedCopy.getIndex()));
		});
		
		Runnable copySelectionChangedAction = ()->{
			this.checkFinishable();
		};
		
		this.copyNodeSelectionManager = new CopyNodeSelectionManager<>(
				this.visSchemeAppliedArchive.getVisSchemeVCDGraph(),//SimpleDirectedGraph<V,E> DAG, VCD graph of the visSchemeAppliedArchive
				VCDNodeCopyNumberMap,//Map<V, Integer> DAGNodeCopyNumberMap,
				copyLinkSet,//Set<CopyLink<V>> copyLinkSet,
				VCDNodeOrderingComparatorFactory.getComparator(),//Comparator<V> nodeOrderComparator,
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(n)->{return n.toString();},//Function<V,String> dagNodeInforStringFunction,
				copySelectionChangedAction//Runnable copySelectionChangedAction
				);
	}
	
	/**
	 * @return the copyNodeSelectionManager
	 */
	public CopyNodeSelectionManager<VCDNodeImpl, VCDEdgeImpl> getCopyNodeSelectionManager() {
		return copyNodeSelectionManager;
	}
	
	////////////////////////////////
	/**
	 * update the 
	 * 1. {@link #selectedVSCopySetInCoreShapeCFGSet} 
	 * 2. {@link #selectedCoreShapeCFGIDSet}
	 * 3. {@link #selectedCoreShapeCFGIDCFIDSetMap}
	 * 
	 * based on the currently selected copies on the {@link #copyNodeSelectionManager}
	 * 
	 * should be invoked whenever an effective change is made on the vccl copy selection;
	 */
	private void update() {
		this.selectedVSCopySetInCoreShapeCFGSet.clear();
		this.selectedCoreShapeCFGIDSet.clear();
		this.selectedCoreShapeCFGIDCFIDSetMap.clear();
		
		this.getCopyNodeSelectionManager().getSelectedDAGNodeCopyIndexPairSet().forEach(pair->{
			VCDNodeImpl vcdNode = pair.getFirst();
			int copyIndex = pair.getSecond();
			
			VSCopy copy = vcdNode.getVSCopyIndexMap().get(copyIndex);
			this.selectedVSCopySetInCoreShapeCFGSet.add(copy);
			
			//process each core ShapeCFG of the vcdNode
			vcdNode.getVSComponent().getCoreShapeCFGIDSet().forEach(originalCoreShapeCFGID->{
				CompositionFunctionGroupID reproducedID = 
						this.visSchemeAppliedArchiveReproducedAndInsertedInstance.getOriginalCFGIDCopyIndexReproducedCFGIDMapMap().get(originalCoreShapeCFGID).get(copyIndex);
				
				this.selectedCoreShapeCFGIDSet.add(reproducedID);
				
				Set<CompositionFunctionID> reproducedCoreShapeCFIDSet = new HashSet<>();
				
				this.visScheme.getCompositionFunctionIDSetOfGroupID(originalCoreShapeCFGID).forEach(originalCoreShapeCFID->{
					reproducedCoreShapeCFIDSet.add(
							this.visSchemeAppliedArchiveReproducedAndInsertedInstance.getOriginalCFIDCopyIndexReproducedCFIDMapMap().get(originalCoreShapeCFID).get(copyIndex));
				});
				
				this.selectedCoreShapeCFGIDCFIDSetMap.put(reproducedID, reproducedCoreShapeCFIDSet);
			});
		});
	}
	
	/**
	 * check if the VisSchemeBasedVisInstance is finisheable;
	 * should be invoked whenever an effect change is made;
	 */
	public void checkFinishable() {
		//must update the fields related with selected vccl copies first since they will be used to build the VisSchemeBasedVisInstance
		this.update();
		
		//try to check if the VisSchemeBasedVisInstance is buildable
		try {
			this.getEmbeddedUIContentController().build();
			this.finishable = true;
			this.getEmbeddedUIContentController().finishableLabel.setText("FINISHABLE");
		}catch (Exception e) {
			this.finishable = false;
			this.getEmbeddedUIContentController().finishableLabel.setText("UNFINISHABLE");
		}
	}

	/**
	 * @return the finishable
	 */
	public boolean isFinishable() {
		return finishable;
	}

	/**
	 * @return the selectedVSCopySetInCoreShapeCFGSet
	 */
	Set<VSCopy> getSelectedVSCopySetInCoreShapeCFGSet() {
		return selectedVSCopySetInCoreShapeCFGSet;
	}
	
	/**
	 * @return the selectedCoreShapeCFGIDSet
	 */
	Set<CompositionFunctionGroupID> getSelectedCoreShapeCFGIDSet() {
		return selectedCoreShapeCFGIDSet;
	}

	/**
	 * @return the selectedCoreShapeCFGIDCFIDSetMap
	 */
	Map<CompositionFunctionGroupID, Set<CompositionFunctionID>> getSelectedCoreShapeCFGIDCFIDSetMap() {
		return selectedCoreShapeCFGIDCFIDSetMap;
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
		
		this.copyNodeSelectionManager.setModifiable(modifiable);
	}
	
	
	
	///////////////////////////
	/**
	 * @return the visSchemeID
	 */
	public VisSchemeID getVisSchemeID() {
		return visSchemeID;
	}

	/**
	 * @return the visSchemeAppliedArchiveID
	 */
	public VisSchemeAppliedArchiveID getVisSchemeAppliedArchiveID() {
		return visSchemeAppliedArchiveID;
	}

	/**
	 * @return the visSchemeAppliedArchiveReproducedAndInsertedInstanceID
	 */
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceID getVisSchemeAppliedArchiveReproducedAndInsertedInstanceID() {
		return visSchemeAppliedArchiveReproducedAndInsertedInstanceID;
	}
	/**
	 * @return the visScheme
	 */
	public VisScheme getVisScheme() {
		return visScheme;
	}

	/**
	 * @return the visSchemeAppliedArchive
	 */
	public VisSchemeAppliedArchive getVisSchemeAppliedArchive() {
		return visSchemeAppliedArchive;
	}

	/**
	 * @return the visSchemeAppliedArchiveReproducedAndInsertedInstance
	 */
	public VisSchemeAppliedArchiveReproducedAndInsertedInstance getVisSchemeAppliedArchiveReproducedAndInsertedInstance() {
		return visSchemeAppliedArchiveReproducedAndInsertedInstance;
	}

}
