package builder.visframe.context.scheme.applier.archive;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.graph.SimpleDirectedGraph;

import context.project.VisProjectDBContext;
import context.scheme.VisScheme;
import context.scheme.appliedarchive.TrimmedIntegratedDOSAndCFDGraphUtils;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.builder.LeafNodeBuilder;
import dependency.cfd.SimpleCFDGraph;
import dependency.cfd.SimpleCFDGraphBuilder;
import dependency.cfd.integrated.IntegratedCFDGraphBuilder;
import dependency.cfd.integrated.IntegratedCFDGraphTrimmer;
import dependency.dos.SimpleDOSGraph;
import dependency.dos.SimpleDOSGraphBuilder;
import dependency.dos.integrated.IntegratedDOSGraphBuilder;
import dependency.dos.integrated.IntegratedDOSGraphEdge;
import dependency.dos.integrated.IntegratedDOSGraphNode;
import dependency.dos.integrated.IntegratedDOSGraphTrimmer;
import dependency.vccl.VCCLEdge;
import dependency.vccl.VCCLGraphBuilder;
import dependency.vccl.VSCopy;
import dependency.vcd.VCDEdgeImpl;
import dependency.vcd.VCDGraph;
import dependency.vcd.VCDGraphBuilder;
import dependency.vcd.VCDNodeImpl;
import dependencygraph.cfd.integrated.IntegratedCFDGraphViewerManager;
import dependencygraph.dos.integrated.IntegratedDOSGraphViewerManager;
import dependencygraph.mapping.SolutionSetMetadataMappingManager;
import dependencygraph.solutionset.DAGSolutionSetSelectorManager;
import dependencygraph.vccl.copylink.CopyLinkAssignerManager;
import dependencygraph.vccl.copynumber.DAGNodeCopyNumberAssignmentManager;
import dependencygraph.vcd.VCDNodeOrderingComparatorFactory;
import utils.FXUtils;

/**
 * builder for a VisSchemeAppliedArchive with a pre-selected applied VisScheme and an assigned UID for VisSchemeAppliedArchive;
 * 
 * @author tanxu
 *
 */
public final class VisSchemeAppliedArchiveBuilder extends LeafNodeBuilder<VisSchemeAppliedArchive, VisSchemeAppliedArchiveBuilderEmbeddedUIContentController> {
	public static final String NODE_NAME = "VisSchemeAppliedArchive";
	public static final String NODE_DESCRIPTION = "VisSchemeAppliedArchive";
	
	////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	private final VisScheme appliedVisScheme;
	
	private final int visSchemeAppliedArchiveUID;
	
	/////////////////////////////
	/**
	 * VCD graph of the {@link #appliedVisScheme};
	 */
	private VCDGraph fullVCDGraph;
	/**
	 * cfd graph of the {@link #appliedVisScheme}
	 */
	private SimpleCFDGraph fullCFDGraph;
	/**
	 * dos graph of the {@link #appliedVisScheme}
	 */
	private SimpleDOSGraph fullDOSGraph;
	
	
	////////////////////////////
	/**
	 * vccl graph based on the assigned copy number and copy links;
	 * should be null if copy link assignment step is not finished;
	 */
	private SimpleDirectedGraph<VSCopy, VCCLEdge> vcclGraph;
	
	/////////////////////////////
	///step 1
	private DAGNodeCopyNumberAssignmentManager<VCDNodeImpl,VCDEdgeImpl> vcdNodeCopyNumberAssignmentManager;
	private Runnable DAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction;
	private final String DAGNodeCopyNumberAssignmentManagerBeforeChangeMadeWarningInforText = "perform this will reset all following steps (if already started)!";
	
	////step 2
	private CopyLinkAssignerManager<VCDNodeImpl,VCDEdgeImpl> copyLinkAssignerManager;
	private Runnable copyLinkAssignerManagerAfterChangeMadeAction;
	private final String copyLinkAssignerManagerBeforeChangeMadeWarningInforText = "perform this will reset all following steps (if already started)!";
	
	///step 3
	private IntegratedCFDGraphBuilder integratedCFDGraphBuilder;
	private IntegratedCFDGraphTrimmer integratedCFDGraphTrimmer;
	private IntegratedCFDGraphViewerManager integratedCFDGraphViewerManager;
	
	///step 4
	private IntegratedDOSGraphBuilder integratedDOSGraphBuilder;
	private IntegratedDOSGraphTrimmer integratedDOSGraphTrimmer;
	private IntegratedDOSGraphViewerManager integratedDOSGraphViewerManager;
	
	///step 5
	private DAGSolutionSetSelectorManager<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> solutionSetSelectorManager;
	private Runnable solutionSetSelectorManagerAfterChangeMadeAction;
	private final String solutionSetSelectorManagerBeforeChangeMadeWarningInforText = "perform this will reset all following steps (if already started)!";
	
	///step 6
	private SolutionSetMetadataMappingManager solutionSetMetadataMappingManager;
	private Runnable solutionSetMetadataMappingManagerAfterChangeMadeAction;
	private final String solutionSetMetadataMappingManagerBeforeChangeMadeWarningInforText = "perform this will update the MetadataMapping to the current ones!!";
	
	/////////////////
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
	 */
	public VisSchemeAppliedArchiveBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			VisScheme appliedVisScheme,
			int visSchemeAppliedArchiveUID
			) throws SQLException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, VisSchemeAppliedArchiveBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		if(appliedVisScheme==null)
			throw new IllegalArgumentException("given appliedVisScheme cannot be null!");
		
		//
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.appliedVisScheme = appliedVisScheme;
		this.visSchemeAppliedArchiveUID = visSchemeAppliedArchiveUID;
		
		this.initialize();
	}
	
	
	///////////////////step 1
	/**
	 * 
	 * 1. build the {@link #fullVCDGraph}, {@link #fullCFDGraph}, {@link #fullDOSGraph}
	 * 
	 * 
	 * 2. initialize the {@link #vcdNodeCopyNumberAssignmentManager}
	 * 		invoke the {@link #setToNewAndActivateUIOfCopyNumberAssignmentStep()}
	 * @throws SQLException 
	 * 
	 */
	private void initialize() throws SQLException {
		//1
		VCDGraphBuilder vcdGraphBuilder = new VCDGraphBuilder(this.appliedVisScheme, this.appliedVisScheme.getVSComponentPrecedenceList());
		this.fullVCDGraph = vcdGraphBuilder.getVCDGraph();
		
		//
		SimpleCFDGraphBuilder cfdGraphBuilder = 
				new SimpleCFDGraphBuilder(this.appliedVisScheme, this.appliedVisScheme.getCompositionFunctionLookup().getMap().keySet());
		this.fullCFDGraph = cfdGraphBuilder.getBuiltGraph();
		//
		SimpleDOSGraphBuilder dosGraphBuilder = 
				new SimpleDOSGraphBuilder(this.appliedVisScheme, this.appliedVisScheme.getMetadataLookup().getMap().keySet());
		this.fullDOSGraph = dosGraphBuilder.getBuiltGraph();
		
		//////////////////////////
		//2
		this.setToNewAndActivateUIOfCopyNumberAssignmentStep();
	}
	
	
	/**
	 * action to take AFTER the copy numbers assignment has been changed to a new value or reset to initial default value;
	 * 
	 * 1. either from a saved value or default value to a saved value
	 * 2. or from a saved value to default value (all 0)
	 * 
	 * @return the dAGNodeCopyNumberAssignmentManagerChangeMadeAction
	 */
	public Runnable getDAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction() {
		if(this.DAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction == null) {
			this.DAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction = ()->{
				if(this.vcdNodeCopyNumberAssignmentManager.currentCopyNumberAssignmentIsFinishable()) {
					//SAVE button clicked;
					//from default initial value or a previously saved value to newly saved value;
					//next step should be activated and updated based on the newly saved copy numbers;
					//all other following steps after next step should be deactivated;
					//need to set the VSCopy map for each VCDNode
					this.vcdNodeCopyNumberAssignmentManager.getCurrentNodeCopyIndexNodeCopyMap().forEach((vcdNode,copyIndexNodeCopyMap)->{
						Map<Integer, VSCopy> vscopyIndexMap = new HashMap<>();
						copyIndexNodeCopyMap.forEach((copyIndex, nodeCopy)->{
							vscopyIndexMap.put(copyIndex, new VSCopy(vcdNode, copyIndex, nodeCopy.getNotes()));
						});
						vcdNode.setVscopyIndexMap(vscopyIndexMap);
					});
					
					this.getEmbeddedUIContentController().step1StatusLabel.setText("FINISHED");
					this.setToNewAndActivateUIOfCopyLinkAssignmentStep();
				}else {
					//clear all button clicked
					//from a previously saved value to default value (all 0), all following steps should be deactivated;
					//need to set the VSCopy map for each VCDNode to empty map
					this.vcdNodeCopyNumberAssignmentManager.getCurrentNodeCopyIndexNodeCopyMap().forEach((vcdNode,copyIndexNodeCopyMap)->{
						vcdNode.setVscopyIndexMap(new HashMap<>());
					});
					
					this.getEmbeddedUIContentController().step1StatusLabel.setText("UNFINISHED");
					this.setToNullAndDeactivateUIOfCopyLinkAssignmentStep();
				}
			};
		}
		
		return DAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction;
	}
	
	//////////////step 2
	/**
	 * Runnable action to be taken AFTER the copy link assignment has been changed
	 * 1. from a previously saved finished one to a new finished one
	 * 2. from a previously saved finished one to default status (no link created yet)
	 * 3. from default status to a finished one
	 * 
	 * if the copy link assignment is successfully finished, 
	 * 		build the {@link #vcclGraph} and set the dependedVCDNodeLinkedCopyMap of each {@link VSCopy} node on the built {@link #vcclGraph};
	 * Otherwise (copy link assignment has been cleared)
	 * 		set the {@link #vcclGraph} to null;
	 * 
	 * @return the copyLinkAssignerManagerChangeMadeAction
	 */
	public Runnable getCopyLinkAssignerManagerAfterChangeMadeAction() {
		if(this.copyLinkAssignerManagerAfterChangeMadeAction==null) {
			this.copyLinkAssignerManagerAfterChangeMadeAction = ()->{
				if(this.copyLinkAssignerManager.getDAGNodeCopyLinkAssigner().copyLinkGraphIsFullyBuilt()) {//
					//SAVE button clicked;
					//from default initial value or a previously saved value to newly saved value;
					//next step should be activated and updated based on the newly saved copy numbers;
					//all other following steps after next step should be deactivated;
					
					//build the {@link #vcclGraph} and set the dependedVCDNodeLinkedCopyMap of each {@link VSCopy} node on the built {@link #vcclGraph} with VCCLGraphBuilder;
					VCCLGraphBuilder vcclGraphBuilder = 
							new VCCLGraphBuilder(this.copyLinkAssignerManager.getDAGNodeCopyLinkAssigner().buildFullCopyLinkGraph());
					//
					this.vcclGraph = vcclGraphBuilder.getBuiltVCCLGraph();
					//
					
					this.getEmbeddedUIContentController().step2StatusLabel.setText("FINISHED");
					this.setToNewAndActivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep();
				}else {//
					//clear all button clicked
					//from a previously saved value to default value (all 0), all following steps should be deactivated;
					
					//set the {@link #vcclGraph} to null;
					this.vcclGraph = null;
					
					this.getEmbeddedUIContentController().step2StatusLabel.setText("UNFINISHED");
					this.setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep();
				}
			};
		}
		return copyLinkAssignerManagerAfterChangeMadeAction;
	}
	
	////////////////////////////step 5
	/**
	 * @return the solutionSetSelectorManagerChangeMadeAction
	 */
	public Runnable getSolutionSetSelectorManagerChangeMadeAction() {
		if(this.solutionSetSelectorManagerAfterChangeMadeAction==null) {
			this.solutionSetSelectorManagerAfterChangeMadeAction = ()->{
				if(this.solutionSetSelectorManager.getDAGSolutionSetSelector().allRootPathsHaveOneNodeSelected()) {
					//SAVE button clicked;
					//from default initial value or a previously saved value to newly saved value;
					//next step should be activated and updated based on the newly saved copy numbers;
					//all other following steps after next step should be deactivated;
					this.getEmbeddedUIContentController().step5StatusLabel.setText("FINISHED");
					this.setToNewAndActivateUIOfMetadataMappingStep();
				}else {
					//clear all button clicked
					//from a previously saved value to default value (all 0), all following steps should be deactivated;
					this.getEmbeddedUIContentController().step5StatusLabel.setText("UNFINISHED");
					this.setToNullAndDeactivateUIOfMetadataMappingStep();
				}
			};
		}
		return solutionSetSelectorManagerAfterChangeMadeAction;
	}
	
	
	/////////////////////step 6
	/**
	 * @return the solutionSetMetadataMappingManagerChangeMadeAction
	 */
	public Runnable getSolutionSetMetadataMappingManagerChangeMadeAction() {
		if(this.solutionSetMetadataMappingManagerAfterChangeMadeAction==null) {
			this.solutionSetMetadataMappingManagerAfterChangeMadeAction = ()->{
				if(this.solutionSetMetadataMappingManager.allSolutionSetNodeMappingFinished()) {
					//SAVE button clicked;
					//from default initial value or a previously saved value to newly saved value;
					//next step should be activated and updated based on the newly saved copy numbers;
					//all other following steps after next step should be deactivated;
					this.getEmbeddedUIContentController().step6StatusLabel.setText("FINISHED");
					this.setToFinishable();
				}else {
					//clear all button clicked
					//from a previously saved value to default value (all 0), all following steps should be deactivated;
					this.getEmbeddedUIContentController().step6StatusLabel.setText("UNFINISHED");
					this.setToUnfinishable();
				}
			};
		}
		
		return solutionSetMetadataMappingManagerAfterChangeMadeAction;
	}
	
	
	//////////////////////////////chains of reactions
	///////////step 1
	/**
	 * 1. initialize the {@link #vcdNodeCopyNumberAssignmentManager} to a new value thus to restart the step;
	 * 2. set the UI to activated
	 * 3. also invoke {@link #setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep()}
	 * 
	 * =====================
	 * this method should be invoked when the whole process of building VisSchemeAppliedArchive should be restarted;
	 * 
	 */
	void setToNewAndActivateUIOfCopyNumberAssignmentStep() {
		//1
		this.vcdNodeCopyNumberAssignmentManager = new DAGNodeCopyNumberAssignmentManager<>(
				this.fullVCDGraph.getUnderlyingGraph(),//SimpleDirectedGraph<V,E> dag, 
				VCDNodeOrderingComparatorFactory.getComparator(),//Comparator<V> nodeOrderComparator, comparator for VCDNode at the same level TODO
				300,//double distBetweenLevels,  TODO
				200,//double distBetweenNodesOnSameLevel, TODO
				(n)->{return n.toString();}//Function<V,String> nodeInforStringFunction TODO
				);
		//
		this.vcdNodeCopyNumberAssignmentManager.setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(
				this.getDAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction(), 
				this.DAGNodeCopyNumberAssignmentManagerBeforeChangeMadeWarningInforText);
		
		//set the UI to this step
		this.getEmbeddedUIContentController().showStep1ButtonOnAction(null);
		
		//2
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step1RootContainerVBox, false);
		
		//3
		this.setToNullAndDeactivateUIOfCopyLinkAssignmentStep();
	}
	///////////////////////////step 2
	/**
	 * 1. set the {@link #copyLinkAssignerManager} with the currently assigned copy number
	 * 		set the change made action and infor text;
	 * 2. set the UI to activated
	 * 3. also invoke {@link #setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep()}
	 */
	private void setToNewAndActivateUIOfCopyLinkAssignmentStep() {
		//1
		this.copyLinkAssignerManager = new CopyLinkAssignerManager<>(
				this.fullVCDGraph.getUnderlyingGraph(),
				VCDEdgeImpl.class,
				this.vcdNodeCopyNumberAssignmentManager.buildNodeCopyIndexNodeCopyMap(),
				VCDNodeOrderingComparatorFactory.getComparator(),//Comparator<V> nodeOrderComparator, comparator for VCDNode at the same level TODO
				300,//double distBetweenLevels, TODO
				200,//double distBetweenNodesOnSameLevel, TODO
				(n)->{return n.toString();}//Function<V,String> dagNodeInforStringFunction TODO
				);
		
		this.copyLinkAssignerManager.setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(
				this.getCopyLinkAssignerManagerAfterChangeMadeAction(), 
				this.copyLinkAssignerManagerBeforeChangeMadeWarningInforText);
		
		//2
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step2RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep2Button, false);
		
		//3
		this.setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep();
	}
	
	
	/**
	 * 1. set {@link #copyLinkAssignerManager} to null
	 * 2. disable the UI for this step
	 * 3. invoke {@link #setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep()}
	 * 
	 */
	private void setToNullAndDeactivateUIOfCopyLinkAssignmentStep() {
		if(this.copyLinkAssignerManager!=null) {
			this.copyLinkAssignerManager = null;
			//
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step2RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep2Button, true);
			//
			this.setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep();
		}
	}
	
	/////////////////////step 3
	/**
	 * 1. set the {@link #integratedCFDGraphBuilder} and {@link #integratedCFDGraphTrimmer} to new value based on the current copy link assignment
	 * 2. set the {@link #integratedCFDGraphViewerManager} based on new trimmed integrated cfd graph;
	 * 3. set the UI to activated
	 * 4. invoke the {@link #setToNewAndActivateUIOfIntegratedDOSGraphBuildingAndTrimmingStep()}
	 */
	private void setToNewAndActivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep() {
		this.integratedCFDGraphBuilder = 
				new IntegratedCFDGraphBuilder(this.fullVCDGraph, this.fullCFDGraph, this.vcclGraph);
		
		this.integratedCFDGraphTrimmer = 
				new IntegratedCFDGraphTrimmer(this.appliedVisScheme, this.integratedCFDGraphBuilder.getBuiltIntegratedCFDUnderlyingGraph());
		
		//2
		this.integratedCFDGraphViewerManager = new IntegratedCFDGraphViewerManager(
				this.integratedCFDGraphTrimmer.getTrimmedIntegratedCFDUnderlyingGraph(),//SimpleDirectedGraph<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> underlyingIntegratedDOSGraph,
				(a,b)->{
					if(a.getAssignedVCDNode().getPrecedenceIndex()!=b.getAssignedVCDNode().getPrecedenceIndex()) {
						return a.getAssignedVCDNode().getPrecedenceIndex() - b.getAssignedVCDNode().getPrecedenceIndex();
					}else if(a.getCopyIndex()!=b.getCopyIndex()) {
						return a.getCopyIndex() - b.getCopyIndex();
					}else {
						if(!a.getCfID().getHostCompositionFunctionGroupID().equals(b.getCfID().getHostCompositionFunctionGroupID())){
							return a.getCfID().getHostCompositionFunctionGroupID().getName().compareTo(b.getCfID().getHostCompositionFunctionGroupID().getName());
						}else {//belong to the same host CFG
							return a.getCfID().getIndexID()-b.getCfID().getIndexID();
						}
					}
				},//Comparator<IntegratedCFDGraphNode> nodeOrderComparator, TODO
				300,//double distBetweenLevels, TODO
				200,//double distBetweenNodesOnSameLevel, TODO
				(a)->{return a.toString();}//Function<IntegratedCFDGraphNode,String> dagNodeInforStringFunction TODO
				);
		
		//3
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step3RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep3Button, false);
		
		//4
		this.setToNewAndActivateUIOfIntegratedDOSGraphBuildingAndTrimmingStep();
		
		//
		this.getEmbeddedUIContentController().step3StatusLabel.setText("FINISHED");
	}
	
	/**
	 * 1. set {@link #integratedCFDGraphBuilder} and {@link #integratedCFDGraphTrimmer} to null
	 * 2. set {@link #integratedCFDGraphViewerManager} to null;
	 * 
	 * 3. deactivate the UI
	 * 4. invoke {@link #setToNullAndDeactivateUIOfIntegratedDOSGraphBuildingAndTrimmingStep()}
	 */
	private void setToNullAndDeactivateUIOfIntegratedCFDGraphBuildingAndTrimmingStep() {
		if(this.integratedCFDGraphBuilder!=null) {
			this.integratedCFDGraphBuilder = null;
			this.integratedCFDGraphTrimmer = null;
			//
			this.integratedCFDGraphViewerManager = null;
			//
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step3RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep3Button, true);

			////
			this.getEmbeddedUIContentController().step3StatusLabel.setText("UNFINISHED");
			//
			this.setToNullAndDeactivateUIOfIntegratedDOSGraphBuildingAndTrimmingStep();
		}
	}
	
	
	/////////////////////step 4
	/**
	 * 1. set the {@link #integratedDOSGraphBuilder} and {@link #integratedDOSGraphTrimmer} to new value based on the current trimmed integrated cfd graph;
	 * 2. set the {@link #integratedDOSGraphViewerManager} based on the new trimmed integrated DOS graph;
	 * 3. set the UI to activated
	 * 4. invoke the {@link #setToNewAndActivateUIOfSolutionSetSelectionStep()}
	 */
	private void setToNewAndActivateUIOfIntegratedDOSGraphBuildingAndTrimmingStep() {
		//1
		this.integratedDOSGraphBuilder  = new IntegratedDOSGraphBuilder(this.fullVCDGraph, this.fullDOSGraph, this.vcclGraph);
		
		this.integratedDOSGraphTrimmer = 
				new IntegratedDOSGraphTrimmer(this.appliedVisScheme, this.fullVCDGraph, this.integratedCFDGraphTrimmer, this.integratedDOSGraphBuilder.getBuiltIntegratedDOSUnderlyingGraph());
		//2
		this.integratedDOSGraphViewerManager = new IntegratedDOSGraphViewerManager(
				this.integratedDOSGraphTrimmer.getTrimmedIntegratedDOSUnderlyingGraph(),//SimpleDirectedGraph<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> underlyingIntegratedDOSGraph,
				(a,b)->{
					if(a.getAssignedVCDNode().getPrecedenceIndex()!=b.getAssignedVCDNode().getPrecedenceIndex()) {
						return a.getAssignedVCDNode().getPrecedenceIndex() - b.getAssignedVCDNode().getPrecedenceIndex();
					}else if(a.getCopyIndex()!=b.getCopyIndex()) {
						return a.getCopyIndex() - b.getCopyIndex();
					}else {
						if(!a.getMetadataID().getDataType().equals(b.getMetadataID().getDataType())) {
							return a.getMetadataID().getDataType().compareTo(b.getMetadataID().getDataType());
						}else {
							return a.getMetadataID().getName().compareTo(b.getMetadataID().getName());
						}
					}
				},//Comparator<IntegratedDOSGraphNode> nodeOrderComparator, TODO
				300,//double distBetweenLevels, TODO
				200,//double distBetweenNodesOnSameLevel, TODO
				(a)->{return a.toString();}//Function<IntegratedDOSGraphNode,String> dagNodeInforStringFunction TODO
				);
		
		//3
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step4RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep4Button, false);
		
		//4
		this.setToNewAndActivateUIOfSolutionSetSelectionStep();
		
		//
		this.getEmbeddedUIContentController().step4StatusLabel.setText("FINISHED");
	}
	
	/**
	 * 1. set {@link #integratedDOSGraphBuilder} and {@link #integratedDOSGraphTrimmer} to null
	 * 2. set {@link #integratedDOSGraphViewerManager} to null;
	 * 3. deactivate the UI
	 * 4. invoke {@link #setToNullAndDeactivateUIOfSolutionSetSelectionStep()}
	 */
	private void setToNullAndDeactivateUIOfIntegratedDOSGraphBuildingAndTrimmingStep() {
		if(this.integratedDOSGraphBuilder!=null) {
			this.integratedDOSGraphBuilder = null;
			this.integratedDOSGraphTrimmer = null;
			//
			this.integratedDOSGraphViewerManager = null;
			//
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step4RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep4Button, true);
			
			//
			this.getEmbeddedUIContentController().step4StatusLabel.setText("UNFINISHED");
			//
			this.setToNullAndDeactivateUIOfSolutionSetSelectionStep();
		}
	}
	
	//////////////////////step 5
	/**
	 * 1. set the value of {@link #solutionSetSelectorManager} based on current trimmed integrated DOS graph
	 * 		set up the changeMadeAction and infor text
	 * 2. activate the UI
	 * 3. invoke {@link #setToNullAndDeactivateUIOfMetadataMappingStep()}
	 */
	private void setToNewAndActivateUIOfSolutionSetSelectionStep() {
		//1
		this.solutionSetSelectorManager = new DAGSolutionSetSelectorManager<>(
				this.integratedDOSGraphTrimmer.getTrimmedIntegratedDOSUnderlyingGraph(),//SimpleDirectedGraph<V,E> dag,
				(a,b)->{
					if(a.getAssignedVCDNode().getPrecedenceIndex()!=b.getAssignedVCDNode().getPrecedenceIndex()) {
						return a.getAssignedVCDNode().getPrecedenceIndex() - b.getAssignedVCDNode().getPrecedenceIndex();
					}else if(a.getCopyIndex()!=b.getCopyIndex()) {
						return a.getCopyIndex() - b.getCopyIndex();
					}else {
						if(!a.getMetadataID().getDataType().equals(b.getMetadataID().getDataType())) {
							return a.getMetadataID().getDataType().compareTo(b.getMetadataID().getDataType());
						}else {
							return a.getMetadataID().getName().compareTo(b.getMetadataID().getName());
						}
					}
				},//Comparator<V> nodeOrderComparator, TODO
				300,//double distBetweenLevels, TODO
				200,//double distBetweenNodesOnSameLevel, TODO
				(a)->{return a.toString();}//Function<V,String> dagNodeInforStringFunction TODO
				);
		this.solutionSetSelectorManager.setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(
				this.getSolutionSetSelectorManagerChangeMadeAction(), this.solutionSetSelectorManagerBeforeChangeMadeWarningInforText);
		
		//2
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step5RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep5Button, false);
		
		//3
		this.setToNullAndDeactivateUIOfMetadataMappingStep();
	}
	
	/**
	 * 1. set {@link #solutionSetSelectorManager} to null
	 * 2. deactivate the UI
	 * 3. invoke {@link #setToNullAndDeactivateUIOfMetadataMappingStep()}
	 */
	private void setToNullAndDeactivateUIOfSolutionSetSelectionStep() {
		if(this.solutionSetSelectorManager!=null) {
			this.solutionSetSelectorManager = null;
			
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step5RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep5Button, true);
			
			this.getEmbeddedUIContentController().step5StatusLabel.setText("UNFINISHED");
			
			this.setToNullAndDeactivateUIOfMetadataMappingStep();
		}
	}
	
	////////////////////step 6
	/**
	 * 
	 * 1. set the value of {@link #solutionSetMetadataMappingManager} based on current selected solution set;
	 * 		set up the change made action and infor text
	 * 2. activate the UI
	 * 3. invoke {@link #setToUnfinishable()}
	 * 
	 * 
	 */
	private void setToNewAndActivateUIOfMetadataMappingStep() {
		//1
		TrimmedIntegratedDOSAndCFDGraphUtils trimmedIntegratedDOSAndCFDGraphUtils = 
				new TrimmedIntegratedDOSAndCFDGraphUtils(
						this.appliedVisScheme,//VisScheme visScheme,
						this.fullVCDGraph.getUnderlyingGraph(),//SimpleDirectedGraph<VCDNodeImpl, VCDEdgeImpl> visSchemeVCDGraph,
						this.vcclGraph,//SimpleDirectedGraph<VSCopy, VCCLEdge> vcclGraph,
						this.integratedCFDGraphTrimmer.getTrimmedIntegratedCFDUnderlyingGraph(),//SimpleDirectedGraph<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> trimmedIntegratedCFDGraph,
						this.integratedDOSGraphTrimmer.getTrimmedIntegratedDOSUnderlyingGraph()//SimpleDirectedGraph<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> trimmedIntegratedDOSGraph
						);
		try {
			this.solutionSetMetadataMappingManager = 
					new SolutionSetMetadataMappingManager(
							this.hostVisProjectDBContext,//VisProjectDBContext hostVisProjectDBContext,
							this.integratedDOSGraphTrimmer.getTrimmedIntegratedDOSUnderlyingGraph(),//SimpleDirectedGraph<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> underlyingIntegratedDOSGraph,
							this.solutionSetSelectorManager.getDAGSolutionSetSelector().getSelectedSolutionSet(),//Set<IntegratedDOSGraphNode> selectedSolutionSet,
							trimmedIntegratedDOSAndCFDGraphUtils,//TrimmedIntegratedDOSAndCFDGraphUtils trimmedIntegratedDOSAndCFDGraphUtils,
							(a,b)->{
								if(a.getAssignedVCDNode().getPrecedenceIndex()!=b.getAssignedVCDNode().getPrecedenceIndex()) {
									return a.getAssignedVCDNode().getPrecedenceIndex() - b.getAssignedVCDNode().getPrecedenceIndex();
								}else if(a.getCopyIndex()!=b.getCopyIndex()) {
									return a.getCopyIndex() - b.getCopyIndex();
								}else {
									if(!a.getMetadataID().getDataType().equals(b.getMetadataID().getDataType())) {
										return a.getMetadataID().getDataType().compareTo(b.getMetadataID().getDataType());
									}else {
										return a.getMetadataID().getName().compareTo(b.getMetadataID().getName());
									}
								}
							},//Comparator<IntegratedDOSGraphNode> nodeOrderComparator, TODO
							300,//double distBetweenLevels, TODO
							200,//double distBetweenNodesOnSameLevel, TODO
							(a)->{return a.toString();}//Function<IntegratedDOSGraphNode,String> dagNodeInforStringFunction TODO
							);
			this.solutionSetMetadataMappingManager.setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(
					this.getSolutionSetMetadataMappingManagerChangeMadeAction(), this.solutionSetMetadataMappingManagerBeforeChangeMadeWarningInforText);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1); 
		}
		
		//2
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step6RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep6Button, false);
		
		//3
		this.setToUnfinishable();
	}
	
	/**
	 * 1. set {@link #solutionSetMetadataMappingManager} to null
	 * 2. deactivate the UI
	 * 3. invoke {@link #setToUnfinishable()}
	 */
	private void setToNullAndDeactivateUIOfMetadataMappingStep() {
		if(this.solutionSetMetadataMappingManager!=null) {
			this.solutionSetMetadataMappingManager = null;
			
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step6RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep6Button, true);
			
			this.setToUnfinishable();
		}
	}
	
	
	///////////////////////
	private void setToFinishable() {
		this.finishable = true;
		//TODO UI???
		this.getEmbeddedUIContentController().finishableLabel.setText("FINISHABLE");
	}
	
	private void setToUnfinishable() {
		this.finishable = false;
		//TODO UI???
		this.getEmbeddedUIContentController().finishableLabel.setText("UNFINISHABLE");
	}
	
	/**
	 * @return the finishable
	 */
	public boolean isFinishable() {
		return finishable;
	}
	
	/////////////////////////////
	/**
	 * @return the vcdNodeCopyNumberAssignmentManager
	 */
	public DAGNodeCopyNumberAssignmentManager<VCDNodeImpl, VCDEdgeImpl> getVcdNodeCopyNumberAssignmentManager() {
		return vcdNodeCopyNumberAssignmentManager;
	}
	
	/**
	 * @return the copyLinkAssignerManager
	 */
	public CopyLinkAssignerManager<VCDNodeImpl, VCDEdgeImpl> getCopyLinkAssignerManager() {
		return copyLinkAssignerManager;
	}

	/**
	 * @return the integratedCFDGraphBuilder
	 */
	public IntegratedCFDGraphBuilder getIntegratedCFDGraphBuilder() {
		return integratedCFDGraphBuilder;
	}

	/**
	 * @return the integratedCFDGraphViewerManager
	 */
	public IntegratedCFDGraphViewerManager getIntegratedCFDGraphViewerManager() {
		return integratedCFDGraphViewerManager;
	}

	/**
	 * @return the integratedDOSGraphViewerManager
	 */
	public IntegratedDOSGraphViewerManager getIntegratedDOSGraphViewerManager() {
		return integratedDOSGraphViewerManager;
	}

	/**
	 * @return the solutionSetSelectorManager
	 */
	public DAGSolutionSetSelectorManager<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> getSolutionSetSelectorManager() {
		return solutionSetSelectorManager;
	}

	/**
	 * @return the solutionSetMetadataMappingManager
	 */
	public SolutionSetMetadataMappingManager getSolutionSetMetadataMappingManager() {
		return solutionSetMetadataMappingManager;
	}

	/**
	 * @return the visSchemeAppliedArchiveUID
	 */
	public int getVisSchemeAppliedArchiveUID() {
		return visSchemeAppliedArchiveUID;
	}


	/**
	 * @return the appliedVisScheme
	 */
	public VisScheme getAppliedVisScheme() {
		return appliedVisScheme;
	}


	/**
	 * @return the fullVCDGraph
	 */
	public VCDGraph getFullVCDGraph() {
		return fullVCDGraph;
	}


	/**
	 * @return the fullCFDGraph
	 */
	public SimpleCFDGraph getFullCFDGraph() {
		return fullCFDGraph;
	}


	/**
	 * @return the fullDOSGraph
	 */
	public SimpleDOSGraph getFullDOSGraph() {
		return fullDOSGraph;
	}


	/**
	 * @return the vcclGraph
	 */
	public SimpleDirectedGraph<VSCopy, VCCLEdge> getVcclGraph() {
		return vcclGraph;
	}


	/**
	 * @return the integratedCFDGraphTrimmer
	 */
	public IntegratedCFDGraphTrimmer getIntegratedCFDGraphTrimmer() {
		return integratedCFDGraphTrimmer;
	}


	/**
	 * @return the integratedDOSGraphTrimmer
	 */
	public IntegratedDOSGraphTrimmer getIntegratedDOSGraphTrimmer() {
		return integratedDOSGraphTrimmer;
	}


	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

}
