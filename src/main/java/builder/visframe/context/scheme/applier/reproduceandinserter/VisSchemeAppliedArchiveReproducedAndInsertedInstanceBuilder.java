package builder.visframe.context.scheme.applier.reproduceandinserter;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.context.scheme.applier.reproduceandinserter.cf.CFReproducingAndInsertionManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.cfg.CFGReproducingAndInsertionManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.OperationReproducingAndInsertionManager;
import context.project.VisProjectDBContext;
import context.scheme.VisScheme;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducerAndInserter;
import core.builder.LeafNodeBuilder;
import core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps.BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager;
import exception.VisframeException;
import utils.FXUtils;

/**
 * builder for a VisSchemeAppliedArchive with a pre-selected applied VisScheme and an assigned UID for VisSchemeAppliedArchive;
 * 
 * @author tanxu
 *
 */
public final class VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder 
	extends LeafNodeBuilder<VisSchemeAppliedArchiveReproducedAndInsertedInstance, VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderEmbeddedUIContentController> {
	public static final String NODE_NAME = "VisSchemeAppliedArchiveReproducedAndInsertedInstance";
	public static final String NODE_DESCRIPTION = "VisSchemeAppliedArchiveReproducedAndInsertedInstance";
	
	////////////////////////////
	private final VisSchemeAppliedArchiveReproducerAndInserter visSchemeAppliedArchiveReproducerAndInserter;
	
	////////////////////////
	
	private OperationReproducingAndInsertionManager operationReproducingAndInsertionManager;
	
	/**
	 * should be re-initialized with constructor when any inserted Operation in {@link #operationReproducingAndInsertionManager} is rolled back;
	 */
	private CFGReproducingAndInsertionManager CFGReproducingAndInsertionManager;
	/**
	 * should be re-initialized with constructor when any inserted CFG in {@link #CFGReproducingAndInsertionManager} is rolled back;
	 */
	private CFReproducingAndInsertionManager CFReproducingAndInsertionManager;
	
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
	 * @throws IOException 
	 */
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder(
			VisSchemeAppliedArchiveReproducerAndInserter visSchemeAppliedArchiveReproducerAndInserter
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		
		//
		this.visSchemeAppliedArchiveReproducerAndInserter = visSchemeAppliedArchiveReproducerAndInserter;
		
		
//		this.initialize();
	}

	
	///////////////////step 1
	/**
	 * initialize 
	 * {@link #operationReproducingAndInsertionManager}
	 * 
	 * must be invoked immediately after the constructor if this builder is created to build a new VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * 		see {@link BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager#getStepRootNode()}
	 * 
	 * DO NOT invoke this method if the builder is created for viewing an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 * 		see {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory#build(VisSchemeAppliedArchiveReproducedAndInsertedInstance)};
	 * 		
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	public void initialize() throws SQLException, IOException {
		//initialize the 
		this.setToNewAndActivateUIOfOperationReproducingAndInsertion(false);
		
		//set the UI to the first step
		this.getEmbeddedUIContentController().showStep1ButtonOnAction(null);
	}
	////////////////////
	/**
	 * invoked whenever this builder is initialized;
	 * 
	 * 1. initialize the OperationReproducingAndInsertionTracker in the {@link #visSchemeAppliedArchiveReproducerAndInserter}
	 * 
	 * 2. initialize the {@link #operationReproducingAndInsertionManager}
	 * 
	 * @param viewOnlyMode whether the {@link #operationReproducingAndInsertionManager} is initialized for viewing an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance or not (create a new VisSchemeAppliedArchiveReproducedAndInsertedInstance)
	 * @throws SQLException
	 * @throws IOException 
	 */
	public void setToNewAndActivateUIOfOperationReproducingAndInsertion(boolean viewOnlyMode) throws SQLException, IOException {
		//1 Initialize the OperationReproducingAndInsertionTracker in the VisSchemeAppliedArchiveReproducerAndInserter
		this.getVisSchemeAppliedArchiveReproducerAndInserter().initializeOperationReproducingAndInsertionTracker();
		
		//2
		this.operationReproducingAndInsertionManager = 
				new OperationReproducingAndInsertionManager(
						this,
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
		
		//
		if(!viewOnlyMode) //build a new VisSchemeAppliedArchiveReproducedAndInsertedInstance
			this.operationReproducingAndInsertionManager.initialize();
	}
	/**
	 * invoked whenever all Operations are successfully reproduced and inserted;
	 * 
	 * if {@link #CFGReproducingAndInsertionManager} is not null, throw exception;
	 * else
	 * 		1. re-initialize the CFGReproducingAndInsertionTracker in the {@link #visSchemeAppliedArchiveReproducerAndInserter}
	 * 		2. initialize {@link #CFGReproducingAndInsertionManager}
	 * 		
	 * 		3. enable corresponding buttons on the UI
	 * 		4. set the status label of operation reproducing and insertion to finished
	 * @throws SQLException 
	 */
	public void setToNewAndActivateUIOfCFGReproducingAndInsertion(boolean viewOnlyMode) throws SQLException {
		if(this.CFGReproducingAndInsertionManager!=null)
			throw new VisframeException("cannot restart CFGReproducingAndInsertionManager when it is non-null!");
		//1
		this.getVisSchemeAppliedArchiveReproducerAndInserter().initializeCFGReproducingAndInsertionTracker();
		
		//2
		this.CFGReproducingAndInsertionManager = new CFGReproducingAndInsertionManager(
				this,
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
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return a.toString();}//Function<IntegratedCFDGraphNode,String> dagNodeInforStringFunction TODO
				);
		
		if(!viewOnlyMode)
			this.CFGReproducingAndInsertionManager.initialize();
		
		//3
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep2Button,false);
		this.getEmbeddedUIContentController().step1StatusLabel.setText("FINISHED");
	}
	
	/**
	 * invoked whenever an inserted Operation is rolled back;
	 * if {@link #CFGReproducingAndInsertionManager} is null, return;
	 * else
	 * 		
	 * 		1. invoke {@link CFGReproducingAndInsertionManager#rollbackAll(boolean)}
	 * 			this will invoke the {@link #setToNullAndDeactivateUIDOfCFReproducingAndInsertion()}
	 * 
	 * 		2. set {@link #CFGReproducingAndInsertionManager} to null;
	 * 			
	 * 		3. disable the corresponding buttons and set the status label on the UI
	 * 
	 * 		4. set the step scroll pane to step 1
	 * @throws SQLException 
	 */
	public void setToNullAndDeactivateUIOfCFGReproducingAndInsertion(boolean showMessageAfterDone) throws SQLException {
		if(this.CFGReproducingAndInsertionManager==null)
			return;
		
		//note that this will trigger rolling back of any inserted CF
		this.CFGReproducingAndInsertionManager.rollbackAll(showMessageAfterDone);
		
		this.CFGReproducingAndInsertionManager = null;
		
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep2Button, true);
		
		this.getEmbeddedUIContentController().step2StatusLabel.setText("UNFINISHED");
		
		this.getEmbeddedUIContentController().showStep1ButtonOnAction(null);
	}
	
	/**
	 * invoked whenever all CFGs are successfully reproduced and inserted;
	 * 
	 * if {@link #CFReproducingAndInsertionManager} is not null, throw exception;
	 * else
	 * 		1. re-initialize the CFReproducingAndInsertionManager in the {@link #visSchemeAppliedArchiveReproducerAndInserter}
	 * 		2. initialize {@link #CFReproducingAndInsertionManager}
	 * 		3. enable corresponding buttons on the UI
	 * 		4. set the status label of CFG reproducing and insertion to finished
	 * @throws SQLException 
	 */
	public void setToNewAndActivateUIDOfCFReproducingAndInsertion() throws SQLException {
		if(this.CFReproducingAndInsertionManager!=null)
			throw new VisframeException("cannot restart CFReproducingAndInsertionManager when it is non-null");
		
		//1
		this.getVisSchemeAppliedArchiveReproducerAndInserter().initializeCFReproducingAndInsertionTracker();
		
		//2
		this.CFReproducingAndInsertionManager = new CFReproducingAndInsertionManager(
				this,
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
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return a.toString();}//Function<IntegratedCFDGraphNode,String> dagNodeInforStringFunction TODO
				);
		
		//3
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep3Button, false);
		
		this.getEmbeddedUIContentController().step2StatusLabel.setText("FINISHED");
	}
	
	
	/**
	 * invoked whenever an inserted CFG is rolled back;
	 * 
	 * if {@link #CFReproducingAndInsertionManager} is null, return;
	 * else
	 * 		1. invoke {@link CFReproducingAndInsertionManager#rollbackAll(boolean)}
	 * 			
	 * 		2. set {@link #CFReproducingAndInsertionManager} to null;
	 * 			
	 * 		3. disable the corresponding buttons and set the status label on the UI
	 * 
	 * 		4. set the step anchorpane to step 1
	 * @throws SQLException 
	 */
	public void setToNullAndDeactivateUIDOfCFReproducingAndInsertion(boolean showMessageAfterDone) throws SQLException {
		if(this.CFReproducingAndInsertionManager==null)
			return;
		
		this.CFReproducingAndInsertionManager.rollbackAll(showMessageAfterDone);
		
		this.CFReproducingAndInsertionManager = null;
		
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep3Button, true);
		
		this.getEmbeddedUIContentController().step3StatusLabel.setText("UNFINISHED");
		
		this.getEmbeddedUIContentController().showStep1ButtonOnAction(null);
		
		this.setToUnfinishable();
	}
	
	
	///////////////////////
	/**
	 * invoked after all CFs are inserted;
	 */
	public void setToFinishable() {
		this.finishable = true;
		//TODO UI???
		this.getEmbeddedUIContentController().step3StatusLabel.setText("FINISHED");
		this.getEmbeddedUIContentController().finishableLabel.setText("FINISHABLE");
	}
	
	/**
	 * 
	 */
	private void setToUnfinishable() {
		this.finishable = false;
		this.getEmbeddedUIContentController().finishableLabel.setText("UNFINISHABLE");
	}
	
	/**
	 * @return the finishable
	 */
	public boolean isFinishable() {
		return finishable;
	}

	
	public VisSchemeAppliedArchiveReproducerAndInserter getVisSchemeAppliedArchiveReproducerAndInserter() {
		return this.visSchemeAppliedArchiveReproducerAndInserter;
	}

	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return this.visSchemeAppliedArchiveReproducerAndInserter.getHostVisProjectDBContext();
	}


	/**
	 * @return the visSchemeAppliedArchive
	 */
	public VisSchemeAppliedArchive getVisSchemeAppliedArchive() {
		return this.visSchemeAppliedArchiveReproducerAndInserter.getAppliedArchive();
	}

	
	public VisScheme getVisScheme() throws SQLException {
		return this.getVisSchemeAppliedArchiveReproducerAndInserter().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().lookup(
				this.getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive().getAppliedVisSchemeID());
	}
	
	/**
	 * @return the visSchemeAppliedArchiveReproducedAndInsertedInstanceUID
	 */
	public int getVisSchemeAppliedArchiveReproducedAndInsertedInstanceUID() {
		return this.visSchemeAppliedArchiveReproducerAndInserter.getVisSchemeAppliedArchiveReproducedAndInsertedInstanceUID();
	}
	

	/**
	 * @return the operationReproducingAndInsertionManager
	 */
	public OperationReproducingAndInsertionManager getOperationReproducingAndInsertionManager() {
		return operationReproducingAndInsertionManager;
	}


	/**
	 * @return the cFGReproducingAndInsertionManager
	 */
	public CFGReproducingAndInsertionManager getCFGReproducingAndInsertionManager() {
		return CFGReproducingAndInsertionManager;
	}


	/**
	 * @return the cFReproducingAndInsertionManager
	 */
	public CFReproducingAndInsertionManager getCFReproducingAndInsertionManager() {
		return CFReproducingAndInsertionManager;
	}

}
