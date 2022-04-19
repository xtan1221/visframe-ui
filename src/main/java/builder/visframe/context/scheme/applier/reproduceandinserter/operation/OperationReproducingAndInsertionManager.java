package builder.visframe.context.scheme.applier.reproduceandinserter.operation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.dos.IntegratedDOSGraphEdgeManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.dos.IntegratedDOSGraphNodeManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.utils.InsertedOperationManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.utils.ReproducedOperationManager;
import context.project.process.simple.OperationPerformer;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.utils.OperationReproducingAndInsertionTracker;
import dependency.dos.integrated.IntegratedDOSGraphEdge;
import dependency.dos.integrated.IntegratedDOSGraphNode;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import operation.Operation;
import operation.OperationID;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.FXUtils;
import utils.Pair;


/**
 * supported actions:
 * 
 * 1. reproduce an Operation whose input Metadata all existing in the host VisProjectDBContext
 * 
 * 2. set the value of parameters dependent on input data table of a reproduced Operation before inserting it;
 * 
 * 3. insert a fully reproduced Operation
 * 
 * 4. roll back the most recently inserted Operation
 * 		only the most recently inserted Operation can be rolled back;
 * 
 * 5. roll back all inserted Operations;
 * 
 * @author tanxu
 * 
 */
public class OperationReproducingAndInsertionManager {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	////////////////////////////
	private final VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	
	////////////////////
	
	private final Comparator<IntegratedDOSGraphNode> nodeOrderComparator;
	
	/**
	 * distance between the layout Y of two neighboring levels!
	 */
	private final double distBetweenLevels;
	
	/**
	 * distance between the bounds of two neighboring nodes at the same level
	 */
	private final double distBetweenNodesOnSameLevel;
	
	private final Function<IntegratedDOSGraphNode,String> dagNodeInforStringFunction;
	
	//////////////////////////////
	///////fields related with DAG graph layout
	private DAGNodeLevelAndOrderAssigner<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> nodeLevelAndOrderAssigner;
	
	private Map<IntegratedDOSGraphNode, IntegratedDOSGraphNodeManager> DAGNodeManagerMap;
	
	private Map<IntegratedDOSGraphEdge, IntegratedDOSGraphEdgeManager> DAGEdgeManagerMap;
	
	/////////////////////////////////////

	private OperationReproducingAndInsertionController controller;
	
	
	////////////////////////
	
	/**
	 * @return the operationReproducingAndInsertionTracker
	 */
	public OperationReproducingAndInsertionTracker getOperationReproducingAndInsertionTracker() {
		return this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getOperationReproducingAndInsertionTracker();
	}

	
	/**
	 * the currently focused reproduced Operation to be inserted;
	 * 
	 * null if all operations have been successfully inserted;
	 * 
	 * should be updated after it is successfully inserted;
	 * 
	 */
	private Operation currentReproducedOperationToBeInserted;
	
	/**
	 * the ReproducedOperationManager of the {@link #currentReproducedOperationToBeInserted};
	 * null if {@link #currentReproducedOperationToBeInserted} is null;
	 */
	private ReproducedOperationManager currentReproducedOperationToBeInsertedReproducedOperationManager;
	
	/**
	 * list of pair of the reproduced and inserted OperationID and the InsertedOperationManager;
	 * the order of the list is the same with the order of insertion;
	 * 
	 * should be updated whenever an operation is successfully inserted or an inserted operation is rolled back;
	 */
	private List<Pair<OperationID, InsertedOperationManager>> reproducedAndInsertedOperationIDManagerPairList;
	
	
	private boolean allOperationReproducedAndInserted = false;
	
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
	public OperationReproducingAndInsertionManager(
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder,
			Comparator<IntegratedDOSGraphNode> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<IntegratedDOSGraphNode,String> dagNodeInforStringFunction
			) throws SQLException, IOException{
		//TODO
		this.hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder = hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
		this.nodeOrderComparator = nodeOrderComparator;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.dagNodeInforStringFunction = dagNodeInforStringFunction;
		//////////////////
		//first build and layout the trimmed integrated DOS graph;
		this.createDAGNodeManager();
		
		this.calculateAndSetDAGNodeLayoutProperty();
				
		this.createDAGEdgeManager();
		//
		this.layoutDAGNodeOnCanvass();
		
		this.layoutDAGEdgesOnCanvass();
		
		//////////////////////////
		this.reproducedAndInsertedOperationIDManagerPairList = new ArrayList<>();
		
	}
	
	/**
	 * create DAGNodeManager for each node on the dag;
	 * 
	 * 1. also create CopyNodeManager of each copy of each dag node inside the constructor of DAGNodeManager
	 * 2. after all copy node are added on UI, calculate the real size of the root FX node of the DAGNodeManager
	 */
	private void createDAGNodeManager() {
		//create DAGNodeManager for each node on dag
		this.DAGNodeManagerMap = new HashMap<>();
		this.getTrimmedIntegratedDOSGraph().vertexSet().forEach(v->{
			this.DAGNodeManagerMap.put(v, new IntegratedDOSGraphNodeManager(this, v));
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.getTrimmedIntegratedDOSGraph().vertexSet().forEach(v->{
			Set<IntegratedDOSGraphNodeManager> dependedNodeManagerSet = new HashSet<>();
			this.getTrimmedIntegratedDOSGraph().outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.DAGNodeManagerMap.get(this.getTrimmedIntegratedDOSGraph().getEdgeTarget(e)));
			});
			
			this.DAGNodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<IntegratedDOSGraphNodeManager> dependingNodeManagerSet = new HashSet<>();
			this.getTrimmedIntegratedDOSGraph().incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.DAGNodeManagerMap.get(this.getTrimmedIntegratedDOSGraph().getEdgeSource(e)));
			});
			this.DAGNodeManagerMap.get(v).setDependingDAGNodeManagerSet(dependingNodeManagerSet);
		});
	}
	
	
	
	/**
	 * 
	 * calculate and set the layout property of root StackPane of each DAG node;
	 * 		note that the layout property of StackPane is for the top-left corner;
	 * 
	 * =====================
	 * 
	 * 1. first calculate the total width for each level;
	 * 		also find out the largest total width;
	 * 
	 * 2. calculate the center x for all levels
	 * 		
	 * 3. calculate the layout x and y for each node on each level
	 * 
	 * 
	 * for a DAG node on level l with order index i
	 * 		note that the first level is 0, first order index is 0;
	 * 		1. calculate the start x of level i 
	 * 		2. calculate the layout x and y
	 * 			the layout x is calculated as
	 * 				sum(real width of all node on the same level with order index < i) + 
	 * 				(i)*{@link #distBetweenNodesOnSameLevel} + 
	 * 				(level start x)
	 * 
	 * 			the layout y is calculated as
	 * 				(l-1)*{@link #distBetweenLevels} -
	 * 				0.5*(real height of the dag node) +
 	 * 				{@link #Y_BORDER}
	 */
	private void calculateAndSetDAGNodeLayoutProperty() {
		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.getTrimmedIntegratedDOSGraph(), this.nodeOrderComparator);
		
		//first find out the total width for each level and find out the largest total width for a level;
		Map<Integer, Double> levelTotalWidthMap = new HashMap<>();
		double largestLevelTotalWidth = 0;
		
		for(int lvl:this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()){
			double totalWidth = 0;
			//first index is 0 on each level
			for(int index = 0;index<this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).size();index++) {
				IntegratedDOSGraphNodeManager dagNodeManager = 
						this.DAGNodeManagerMap.get(this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).get(index));
				if(index>0)
					totalWidth = totalWidth+this.distBetweenNodesOnSameLevel;
				
				totalWidth = totalWidth+dagNodeManager.getRealWidth();
			}
			
			levelTotalWidthMap.put(lvl, totalWidth);
			
			if(totalWidth>largestLevelTotalWidth)
				largestLevelTotalWidth = totalWidth;
		}
		
		//
		double centerX = X_BORDER + 0.5*largestLevelTotalWidth;
		
		//then calculate the layout of each node at each level
		for(int lvl:this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()){
			double levelXStart = centerX - 0.5*levelTotalWidthMap.get(lvl);
			
			double sumAllPreviousNodeWidth = 0; //reset for each level
			
			for(int index = 0;index<this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).size();index++) {
				
				IntegratedDOSGraphNodeManager dagNodeManager = 
						this.DAGNodeManagerMap.get(this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).get(index));
				
				double layoutX = sumAllPreviousNodeWidth + index*this.distBetweenNodesOnSameLevel + levelXStart;
				
				double layoutY = (lvl)*this.distBetweenLevels 
//						+ 0.5*dagNodeManager.getRealHeight() 
						+ Y_BORDER;
				
				dagNodeManager.setLayoutAndCalculateCenterCoord(layoutX, layoutY);
				
				//update
				sumAllPreviousNodeWidth = sumAllPreviousNodeWidth+dagNodeManager.getRealWidth();
			}
			
		}
		
	}
	
	
	
	
	/**
	 * create DAGEdgeManager for each edge on the DAG
	 */
	private void createDAGEdgeManager() {
		this.DAGEdgeManagerMap = new HashMap<>();
		this.getTrimmedIntegratedDOSGraph().edgeSet().forEach(e->{
			IntegratedDOSGraphNode dependingNode = this.getTrimmedIntegratedDOSGraph().getEdgeSource(e);
			IntegratedDOSGraphNode dependedNode = this.getTrimmedIntegratedDOSGraph().getEdgeTarget(e);
			
			double dependingNodeUpperCenterX = 
					this.DAGNodeManagerMap.get(dependingNode).getCenterCoord().getX();
			double dependingNodeUpperCenterY = 
					this.DAGNodeManagerMap.get(dependingNode).getCenterCoord().getY() - 0.5*this.DAGNodeManagerMap.get(dependingNode).getRealHeight();
			
			double dependedNodeBottomCenterX = 
					this.DAGNodeManagerMap.get(dependedNode).getCenterCoord().getX();
			double dependedNodeBottomCenterY = 
					this.DAGNodeManagerMap.get(dependedNode).getCenterCoord().getY() + 0.5*this.DAGNodeManagerMap.get(dependedNode).getRealHeight();;
			
			
			this.DAGEdgeManagerMap.put(
					e, 
					new IntegratedDOSGraphEdgeManager(
							this,
							e,
							new Point2D(dependingNodeUpperCenterX, dependingNodeUpperCenterY),
							new Point2D(dependedNodeBottomCenterX, dependedNodeBottomCenterY)
							));
		});
	}
	
	
	/**
	 * layout dag edges on the canvass
	 */
	private void layoutDAGEdgesOnCanvass() {
		this.DAGEdgeManagerMap.forEach((edge, manager)->{
			this.getController().getGraphLayoutAnchorPane().getChildren().add(manager.getCurve());
		});
	}
	
	
	/**
	 * layout node after the edges!
	 */
	private void layoutDAGNodeOnCanvass() {
		this.DAGNodeManagerMap.forEach((node, manager)->{
			this.getController().getGraphLayoutAnchorPane().getChildren().add(manager.getController().getRootNodeContainer());
		});
	}
	
	/////////////////////////////////////////////////
	/**
	 * initialize 
	 * invoke the {@link #reproduceNextOperation()} 
	 * 
	 * must be invoked if the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} is created to build a new VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 * DO NOT invoke this method if the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} is created to view an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance (view-only mode);
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void initialize() throws IOException, SQLException {
		
		//try to reproduce the first Operation;
		//trigger chain reactions
		this.reproduceNextOperation();
	}
	/**
	 * reproduce next operation by 
	 * 
	 * 1. check if {@link #currentReproducedOperationToBeInserted} is null
	 * 		if not, throw UnsupportedOperationException;
	 * 
	 * 2. retrieve the next reproduced Operation 
	 * 		{@link #operationReproducingAndInsertionTracker#reproduceNextOperation()};
	 * 		if null, run the {@link #getAfterAllOperationReproducedAndInsertedAction()}
	 * 		else
	 * 			1. set up the value of fields with the reproduced operation
	 * 				{@link #currentReproducedOperationToBeInserted}
	 * 				{@link #currentReproducedOperationToBeInsertedReproducedOperationManager}
	 * 
	 * 			2. and set up UI
	 * 			
	 * 			3. check whether the reproduced Operation has parameter dependent on input data table content;
	 * 				if yes
	 * 					do nothing
	 * 				if no
	 * 					check the mode
	 * 						if greedy run
	 * 							invoke the {@link #insertCurrentOperation()} method;
	 * 							note that do not need to invoke the {@link #reproduceNextOperation()} method afterwards, 
	 * 								since it is included in the {@link #afterCurrentOperationSuccessfullyInsertedAction};
	 * 						else (one-by-one)
	 * 							do nothing
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	public void reproduceNextOperation() throws IOException, SQLException {
		//1
		if(this.currentReproducedOperationToBeInserted!=null) {
			throw new UnsupportedOperationException("cannot reproduce next Operation when currentReproducedOperationToBeInserted is not null!");
		}
		
		//2
		Operation nextReproducedOperation = this.getOperationReproducingAndInsertionTracker().nextReproducedOperationToBeInserted();
		
		if(nextReproducedOperation==null) {//there is no more reproduced Operation left to be inserted;
			this.getAfterAllOperationReproducedAndInsertedAction().run();
		}else {
			//2.1
			this.currentReproducedOperationToBeInserted = nextReproducedOperation;
			this.currentReproducedOperationToBeInsertedReproducedOperationManager = new ReproducedOperationManager(this, this.currentReproducedOperationToBeInserted);
			
			//2.2
			this.getController().getCurrentReproducedOperationContainerVBox().getChildren().add(this.currentReproducedOperationToBeInsertedReproducedOperationManager.getController().getRootNodeContainer());
			
			//2.3
			if(this.currentReproducedOperationToBeInserted.hasInputDataTableContentDependentParameter()) {
				//do nothing
			}else {
				if(this.getController().isGreedyRunSelected()) //go ahead to insert the reproduced Operation if Greedy run mode is selected;
					this.insertCurrentOperation();
				//note that do not need to invoke the reproduceNextOperation() method, since it is included in the afterCurrentOperationSuccessfullyInsertedAction;
	//			this.reproduceNextOperation();
			}
		}
	}
	
	
	//////////////////////////////////////////
	/**
	 * insert the {@link #currentReproducedOperationToBeInserted} with a {@link SingleSimpleProcessProgressManager};
	 * 
	 * note that the actions to take 
	 * 1. if successfully inserted
	 * 		{@link #getAfterCurrentOperationSuccessfullyInsertedAction()}
	 * 2. if canceled and rolled back
	 * 		{@link #getAfterCurrentOperationCanceledAndRolledbackAction()}
	 * 
	 * are given as constructor parameters of {@link SingleSimpleProcessProgressManager};
	 * 
	 * thus, no explicit after-action will need to be taken;
	 * @throws SQLException 
	 */
	public void insertCurrentOperation() throws SQLException {
		Operation reproducedOperation = this.currentReproducedOperationToBeInsertedReproducedOperationManager.getInsertableReproducedOperation();
		
		OperationPerformer performer = 
				new OperationPerformer(
						this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext(), 
						reproducedOperation);
		
		//set the init window of the progress bar to the stage of the visframe session;
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						performer, 
						this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getEmbeddedUIContentController().getStage(),
						true,
						this.getAfterCurrentOperationSuccessfullyInsertedAction(),
						this.getAfterCurrentOperationCanceledAndRolledbackAction()//
						);
		//
		progressManager.start(true);
	}
	
	
	/**
	 * action to take whenever a reproduced Operation is successfully inserted into the host VisProjectDBContext;
	 */
	private Runnable afterCurrentOperationSuccessfullyInsertedAction;
	
	/**
	 * 1. create a new {@link InsertedOperationManager} with {@link #currentReproducedOperationToBeInserted}
	 * 		set the inserted Operation before the current one to NOT roll-backable;
	 * 
	 * 		add to the {@link #reproducedAndInsertedOperationIDManagerPairList}
	 * 		add to the UI
	 * 
	 * 2. update fields and UI
	 * 		remove the {@link #currentReproducedOperationToBeInsertedReproducedOperationManager} from the UI
	 * 		set {@link #currentReproducedOperationToBeInserted} to null
	 * 		set {@link #currentReproducedOperationToBeInsertedReproducedOperationManager} to null;
	 * 
	 * 3. update the {@link #operationReproducingAndInsertionTracker}
	 * 		{@link OperationReproducingAndInsertionTracker#addNextReproducedOperationToInsertedSet()};
	 * 
	 * 
	 * 4. check if there is next reproduced Operation from the {@link #operationReproducingAndInsertionTracker}
	 * 		if yes
	 * 			invoke the {@link #reproduceNextOperation()}
	 * 		if no
	 * 			all operation are reproduced and inserted, run the {@link #afterAllOperationReproducedAndInsertedAction};
	 * 
	 * @return
	 */
	private Runnable getAfterCurrentOperationSuccessfullyInsertedAction() {
		if(this.afterCurrentOperationSuccessfullyInsertedAction == null) {
			this.afterCurrentOperationSuccessfullyInsertedAction = ()->{
				try {
					//1. 
					InsertedOperationManager manager = 
							new InsertedOperationManager(this, this.currentReproducedOperationToBeInsertedReproducedOperationManager.getInsertableReproducedOperation());
					//set the previously inserted operation (if any) before the current one to not roll-backable
					if(!this.reproducedAndInsertedOperationIDManagerPairList.isEmpty())//there is a previously inserted Operation
						this.reproducedAndInsertedOperationIDManagerPairList.get(this.reproducedAndInsertedOperationIDManagerPairList.size()-1).getSecond().setToRollbackable(false);
					
					//add current one to the list;
					this.reproducedAndInsertedOperationIDManagerPairList.add(new Pair<>(this.currentReproducedOperationToBeInserted.getID(), manager));
					
					//add to UI
					this.getController().getReproducedAndInsertedOperationContainerVBox().getChildren().add(manager.getController().getRootNodeContainer());
					
					//2
					this.getController().getCurrentReproducedOperationContainerVBox().getChildren().clear();
					this.currentReproducedOperationToBeInserted = null;
					this.currentReproducedOperationToBeInsertedReproducedOperationManager = null;
					
					//3
					this.getOperationReproducingAndInsertionTracker().addNextReproducedOperationToInsertedSet();
					
					
					//4
					if(this.getOperationReproducingAndInsertionTracker().nextReproducedOperationToBeInserted()==null) {
						this.getAfterAllOperationReproducedAndInsertedAction().run();
					}else {
						this.reproduceNextOperation();
					}
					
				} catch (IOException|SQLException e) {
					e.printStackTrace();
					System.exit(1); //debug
				}
			};
		}
		
		return this.afterCurrentOperationSuccessfullyInsertedAction;
	}
	
	/**
	 * action to take after all operations are successfully reproduced and inserted into host VisProjectDBContext;
	 * 		in special case, if there is no operation to be reproduced and inserted, this should also be run;
	 * 1. set {@link #allOperationReproducedAndInserted} to true;
	 * 		
	 * 2. notify the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} 
	 * 		invoke the {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#setToNewAndActivateUIOfCFGReproducingAndInsertion()}
	 */
	private Runnable afterAllOperationReproducedAndInsertedAction;
	private Runnable getAfterAllOperationReproducedAndInsertedAction() {
		if(this.afterAllOperationReproducedAndInsertedAction==null) {
			this.afterAllOperationReproducedAndInsertedAction = ()->{
				//1
				this.allOperationReproducedAndInserted = true;
				
				//2
				try {
					this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().setToNewAndActivateUIOfCFGReproducingAndInsertion(false);
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
				
			};
		}
		
		return this.afterAllOperationReproducedAndInsertedAction;
	}
	
	/**
	 * action to take after the {@link #currentReproducedOperationToBeInserted} is canceled and rolled back during the process it is being performed and inserted into the host VisProjectDBContext;
	 * 
	 * if a running Operation is canceled and rolled back, no matter the mode is greedy-run or not, always pause the process until it is resumed;
	 * 
	 * note that there is nothing special need to do???;
	 */
	private Runnable afterCurrentOperationCanceledAndRolledbackAction;
	private Runnable getAfterCurrentOperationCanceledAndRolledbackAction() {
		if(this.afterCurrentOperationCanceledAndRolledbackAction==null) {
			this.afterCurrentOperationCanceledAndRolledbackAction = ()->{
				////do nothing ??
			};
		}
		
		return this.afterCurrentOperationCanceledAndRolledbackAction;
	}
	
	//////////////////////////////////////////////////
	/**
	 * roll back the most recently inserted operation
	 * 		specifically, the Operation of the last element in {@link #reproducedAndInsertedOperationIDManagerPairList};
	 * 
	 * 
	 * 1. remove from the {@link #reproducedAndInsertedOperationIDManagerPairList}
	 * 		remove from the UI
	 * 
	 * 2. if {@link #allOperationReproducedAndInserted} is true (all operations have been inserted)
	 * 			set {@link #allOperationReproducedAndInserted} to false
	 * 			roll back all reproduced and inserted CompositionFunctionGroups
	 * 			also set the CFGReproducingAndInsertionManager of {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} to null;
	 * 				invoke {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#setToNullAndDeactivateUIOfCFGReproducingAndInsertion(boolean)} method
	 * 
	 * 3. roll back the operation
	 * 		
	 * 4. update the {@link #operationReproducingAndInsertionTracker}
	 * 		invoke the {@link OperationReproducingAndInsertionTracker#removeMostRecentlyInsertedOperationFromInsertedSet()}
	 * 			this will remove the most recently inserted Operation and find out the available next reproduced Operation (probably the same one)
	 * 		
	 * 5. set the last inserted Operation in {@link #reproducedAndInsertedOperationIDManagerPairList} (if not empty) to roll-backable
	 * 		
	 * 6. invoke {@link #reproduceNextOperation()} to reset the newly next reproduced Operation to be inserted
	 * 		need to first clear the {@link #currentReproducedOperationToBeInsertedReproducedOperationManager} from the UI
	 * 		then set the {@link #currentReproducedOperationToBeInserted} and {@link #currentReproducedOperationToBeInsertedReproducedOperationManager} to null;
	 * 
	 * @throws SQLException 
	 * @throws IOException 
	 * 		
	 */
	public void rollbackMostRecentlyInsertedOperation() throws SQLException, IOException {
		//1
		Pair<OperationID, InsertedOperationManager> mostRecentlyInsertedOperationIDManagerPair = 
				this.reproducedAndInsertedOperationIDManagerPairList.remove(this.reproducedAndInsertedOperationIDManagerPairList.size()-1);
		
		OperationID mostRecentlyInsertedOperationID = mostRecentlyInsertedOperationIDManagerPair.getFirst();
		InsertedOperationManager mostRecentlyInsertedOperationManager = mostRecentlyInsertedOperationIDManagerPair.getSecond();
		
		this.getController().getReproducedAndInsertedOperationContainerVBox().getChildren().remove(mostRecentlyInsertedOperationManager.getController().getRootNodeContainer());
		
		//2 need to trigger rolling back of any inserted CFGs before rolling back of the Operation;
		//note that this will also trigger rolling back of inserted CF if any as a chain reaction;
		if(this.allOperationReproducedAndInserted) {
			this.allOperationReproducedAndInserted = false;
			//roll back any inserted CFGs if any (which should also trigger rolling back of inserted CFs if any)
			this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().setToNullAndDeactivateUIOfCFGReproducingAndInsertion(false);
		}
		
		//3
		this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext()
		.getProcessLogTableAndProcessPerformerManager().rollbackProcess(mostRecentlyInsertedOperationID);
		
		//4
		this.getOperationReproducingAndInsertionTracker().removeMostRecentlyInsertedOperationFromInsertedSet();
		
		
		//5 set the previous inserted operation (if any) to roll-backable
		if(!this.reproducedAndInsertedOperationIDManagerPairList.isEmpty())
			this.reproducedAndInsertedOperationIDManagerPairList.get(this.reproducedAndInsertedOperationIDManagerPairList.size()-1).getSecond().setToRollbackable(true);
		
		//6 try to get the next reproduced Operation after the roll back;
		//need to first clear UI and set the related fields to null;
		this.getController().getCurrentReproducedOperationContainerVBox().getChildren().clear();
		this.currentReproducedOperationToBeInserted = null;
		this.currentReproducedOperationToBeInsertedReproducedOperationManager = null;
		
		this.reproduceNextOperation();
	}
	
	//////////////////////////
	/**
	 * 
	 */
	public void highlightIntegratedDOSGraphEdges(OperationID reproducedOperationID, boolean highlighted) {
		OperationID originalOperationID = null; //original OperationID of the given reproducedOperationID
		Integer copyIndex = null;//copy index of the original OperationID corresponding to the given reproducedOperationID
		
		if(this.originalOperationIDCopyIndexReproducedOperationIDMapMap!=null) {//this indicates that this manager is now in a view-only mode of an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance
			for(OperationID originalID:this.originalOperationIDCopyIndexReproducedOperationIDMapMap.keySet()){
				for(int ci:this.originalOperationIDCopyIndexReproducedOperationIDMapMap.get(originalOperationID).keySet()){
					OperationID reproducedID = this.originalOperationIDCopyIndexReproducedOperationIDMapMap.get(originalOperationID).get(ci);
					if(reproducedID.equals(reproducedOperationID)) {
						originalOperationID = originalID;
						copyIndex = ci;
						break;
					}
				}
			}
		}else {//this indicates that this manager is now for building a new VisSchemeAppliedArchiveReproducedAndInsertedInstance;
			Pair<OperationID,Integer> originalOperationIDCopyIndexPair = 
					this.getOperationReproducingAndInsertionTracker().getReproducedAndInsertedOperationIDOriginalOperationIDCopyIndexPairMap().get(reproducedOperationID);
			originalOperationID = originalOperationIDCopyIndexPair.getFirst();
			copyIndex = originalOperationIDCopyIndexPair.getSecond();
		}
		
		this.getOperationReproducingAndInsertionTracker().getIntegratedDOSGraphEdgeSet(originalOperationID, copyIndex).forEach(e->{
			this.DAGEdgeManagerMap.get(e).setToHighlighted(highlighted);
		});
	}
	
	
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		//1. first set the main window
		FXUtils.set2Disable(this.getController().controlHBox, !modifiable);
		FXUtils.set2Disable(this.getController().rollbackAllButton, !modifiable);
		
		
		//2 set each InsertedOperationManager;
		this.reproducedAndInsertedOperationIDManagerPairList.forEach(pair->{
			pair.getSecond().setModifiable(modifiable);
		});
	}
	
	
	//////////////////////////////////////////////
	/**
	 * only be set for view-only mode of an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * if this is non-null,  
	 */
	private Map<OperationID, Map<Integer, OperationID>> originalOperationIDCopyIndexReproducedOperationIDMapMap;
	
	/**
	 * set the reproduced Operations 
	 * 
	 * only applicable to view an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 * @param originalOperationIDCopyIndexReproducedOperationIDMapMap
	 * @throws SQLException
	 * @throws IOException
	 */
	public void setValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance instance) throws SQLException, IOException {
		this.originalOperationIDCopyIndexReproducedOperationIDMapMap = instance.getOriginalOperationIDCopyIndexReproducedOperationIDMapMap();
		
		//create InsertedOperationManagers and add to UI with the same insertion order;
		for(OperationID reproducedID:instance.getReproducedOperationIDListByInsertionOrder()) {
			Operation reproducedAndInsertedOperation = this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getOperationManager().lookup(reproducedID);
			
			InsertedOperationManager manager = new InsertedOperationManager(this, reproducedAndInsertedOperation);
			
			//add to reproducedAndInsertedOperationIDManagerPairList;
			this.reproducedAndInsertedOperationIDManagerPairList.add(new Pair<>(reproducedID, manager));
			
			//add to UI
			this.getController().getReproducedAndInsertedOperationContainerVBox().getChildren().add(manager.getController().getRootNodeContainer());
		}
	}
	
	/////////////////////////
	public OperationReproducingAndInsertionController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(OperationReproducingAndInsertionController.FXML_FILE_DIR_STRING));
			
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

	
	////////////////////////////////////////

	private SimpleDirectedGraph<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> getTrimmedIntegratedDOSGraph(){
		return this.hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.getVisSchemeAppliedArchive().getTrimmedIntegratedDOSGraph();
	}
	
	/**
	 * @return the dagNodeInforStringFunction
	 */
	public Function<IntegratedDOSGraphNode, String> getDagNodeInforStringFunction() {
		return dagNodeInforStringFunction;
	}
	
	/**
	 * @return the distBetweenLevels
	 */
	public double getDistBetweenLevels() {
		return distBetweenLevels;
	}

	/**
	 * @return the distBetweenNodesOnSameLevel
	 */
	public double getDistBetweenNodesOnSameLevel() {
		return distBetweenNodesOnSameLevel;
	}
	
	/**
	 * @return the hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder
	 */
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder() {
		return hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	}
	

	
	/**
	 * @return the reproducedAndInsertedOperationIDManagerPairList
	 */
	public List<Pair<OperationID, InsertedOperationManager>> getReproducedAndInsertedOperationIDManagerPairList() {
		return reproducedAndInsertedOperationIDManagerPairList;
	}

}
