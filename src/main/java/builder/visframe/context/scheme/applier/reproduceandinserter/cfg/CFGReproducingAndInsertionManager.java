package builder.visframe.context.scheme.applier.reproduceandinserter.cfg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
import builder.visframe.context.scheme.applier.reproduceandinserter.cfg.cfd.IntegratedCFDGraphEdgeManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.cfg.cfd.IntegratedCFDGraphNodeManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.cfg.utils.InsertedCFGManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.OperationReproducingAndInsertionManager;
import context.project.process.simple.CompositionFunctionGroupInserter;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.utils.CFGReproducingAndInsertionTracker;
import dependency.cfd.integrated.IntegratedCFDGraphEdge;
import dependency.cfd.integrated.IntegratedCFDGraphNode;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.AlertUtils;
import utils.FXUtils;


/**
 * supported actions:
 * 
 * reproduce and insert all CFGs into host VisProjectDBContext;
 * 
 * roll back all inserted CFGs;
 * 
 * @author tanxu
 * 
 */
public class CFGReproducingAndInsertionManager {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	////////////////////////////
	private final VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	
	////////////////////
	
	private final Comparator<IntegratedCFDGraphNode> nodeOrderComparator;
	
	/**
	 * distance between the layout Y of two neighboring levels!
	 */
	private final double distBetweenLevels;
	
	/**
	 * distance between the bounds of two neighboring nodes at the same level
	 */
	private final double distBetweenNodesOnSameLevel;
	
	private final Function<IntegratedCFDGraphNode,String> dagNodeInforStringFunction;
	
	//////////////////////////////
	private CFGReproducingAndInsertinoController controller;
	
	///////fields related with DAG graph layout
	private DAGNodeLevelAndOrderAssigner<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> nodeLevelAndOrderAssigner;
	
	private Map<IntegratedCFDGraphNode, IntegratedCFDGraphNodeManager> DAGNodeManagerMap;
	
	private Map<IntegratedCFDGraphEdge, IntegratedCFDGraphEdgeManager> DAGEdgeManagerMap;
	
	/////////////////////////////////////
	/**
	 * facilitate to initialize the {@link #reproducedCFGIterator}
	 */
	private List<CompositionFunctionGroup> reproducedCFGList;
	/**
	 * 
	 */
	private Iterator<CompositionFunctionGroup> reproducedCFGIterator;
	
	
	
	private Map<CompositionFunctionGroupID, InsertedCFGManager> reproducedAndInsertedCFGIDManagerMap;
	/**
	 * if true, all CFGs have been reproduced and inserted;
	 * if false, none of the CFGs have been reproduced and inserted;
	 */
	private boolean allCFGsInserted = false;
	
	/**
	 * constructor
	 * 
	 * @param underlyingSimpleDOSGraph
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 * @throws SQLException 
	 */
	public CFGReproducingAndInsertionManager(
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder,
			Comparator<IntegratedCFDGraphNode> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<IntegratedCFDGraphNode,String> dagNodeInforStringFunction
			) throws SQLException{
		
		this.hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder = hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
		this.nodeOrderComparator = nodeOrderComparator;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.dagNodeInforStringFunction = dagNodeInforStringFunction;
		
		//////////////////
		
		this.createDAGNodeManager();
		
		this.calculateAndSetDAGNodeLayoutProperty();
		
		this.createDAGEdgeManager();
		
		//
		this.layoutDAGNodeOnCanvass();
		
		this.layoutDAGEdgesOnCanvass();
		//////////////////////////
		//initialize
		this.reproducedAndInsertedCFGIDManagerMap = new LinkedHashMap<>();
		
		this.reproducedCFGList = new ArrayList<>();
		
	}
	
	
	/**
	 * 
	 * must be invoked if the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} is created to build a new VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 * DO NOT invoke this method if the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} is created to view an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance (view-only mode);
	 */
	public void initialize() {
		this.getCFGReproducingAndInsertionTracker().getOriginalCFGIDCopyIndexReproducedCFGMapMap().forEach((originalCFGID,map)->{
			map.forEach((copyIndex, reproducedCfg)->{
				this.reproducedCFGList.add(reproducedCfg);
			});
		});
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
		this.getTrimmedIntegratedCFDGraph().vertexSet().forEach(v->{
			this.DAGNodeManagerMap.put(v, new IntegratedCFDGraphNodeManager(this, v));
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.getTrimmedIntegratedCFDGraph().vertexSet().forEach(v->{
			Set<IntegratedCFDGraphNodeManager> dependedNodeManagerSet = new HashSet<>();
			this.getTrimmedIntegratedCFDGraph().outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.DAGNodeManagerMap.get(this.getTrimmedIntegratedCFDGraph().getEdgeTarget(e)));
			});
			
			this.DAGNodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<IntegratedCFDGraphNodeManager> dependingNodeManagerSet = new HashSet<>();
			this.getTrimmedIntegratedCFDGraph().incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.DAGNodeManagerMap.get(this.getTrimmedIntegratedCFDGraph().getEdgeSource(e)));
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
		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.getTrimmedIntegratedCFDGraph(), this.nodeOrderComparator);
		
		//first find out the total width for each level and find out the largest total width for a level;
		Map<Integer, Double> levelTotalWidthMap = new HashMap<>();
		double largestLevelTotalWidth = 0;
		
		for(int lvl:this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()){
			double totalWidth = 0;
			//first index is 0 on each level
			for(int index = 0;index<this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).size();index++) {
				IntegratedCFDGraphNodeManager dagNodeManager = 
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
				
				IntegratedCFDGraphNodeManager dagNodeManager = 
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
		this.getTrimmedIntegratedCFDGraph().edgeSet().forEach(e->{
			IntegratedCFDGraphNode dependingNode = this.getTrimmedIntegratedCFDGraph().getEdgeSource(e);
			IntegratedCFDGraphNode dependedNode = this.getTrimmedIntegratedCFDGraph().getEdgeTarget(e);
			
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
					new IntegratedCFDGraphEdgeManager(
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
	
	//////////////////////////
	/**
	 * roll back all inserted CFGs
	 * 
	 * the order of performing the roll back of all inserted CFGs in the {@link #reproducedAndInsertedCFGIDManagerMap} is trivial;
	 * 
	 * 1. first process the inserted CFs if any
	 * 		invoke the {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#setToNullAndDeactivateUIDOfCFReproducingAndInsertion(boolean)}
	 * 
	 * 2. roll back all inserted CFGs
	 * 		update UI
	 * 
	 * @throws SQLException 
	 */
	public void rollbackAll(boolean showMessageAfterDone) throws SQLException {
		if(!this.allCFGsInserted)
			return;
		
		//1 first invoke the method so that inserted CF (if any) will be rolled back first before CFGs
		this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().setToNullAndDeactivateUIDOfCFReproducingAndInsertion(false);//do not need to trigger the popup message for rolling back inserted CFs(if any) separately;
		
		
		//2 roll back inserted CFGs
		for(CompositionFunctionGroupID cfgID:this.reproducedAndInsertedCFGIDManagerMap.keySet()){
			//1 roll back
			this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().rollbackProcess(cfgID);
		}
		
		
		//remove from UI
		this.getController().getReproducedAndInsertedCFGContainerHBox().getChildren().clear();
		//
		this.reproducedAndInsertedCFGIDManagerMap.clear();
		
		this.allCFGsInserted = false;
		
		FXUtils.set2Disable(this.getController().startButton, false);
		FXUtils.set2Disable(this.getController().rollbackAllButton, true);
		
		this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getEmbeddedUIContentController().step2StatusLabel.setText("UNFINISHED");
		//
		if(showMessageAfterDone)
			AlertUtils.popAlert("Message", "All inserted CompositionFunctionGroups and CompositionFunctions have been rolled back!");
	}
	
	
	private boolean showMessageAfterAllCFGsInserted;
	/**
	 * the current CFG that is to be inserted;
	 */
	private CompositionFunctionGroup currentReproducedCFG;
	
	private SingleSimpleProcessProgressManager currentSingleSimpleProcessProgressManager;
	/**
	 * insert all reproduced CFG by the {@link #CFGReproducingAndInsertionTracker} into the host VisProjectDBContext;
	 * @param showMessageAfterDone
	 * @throws SQLException 
	 */
	public void insertAll(boolean showMessageAfterDone) throws SQLException {
		if(this.allCFGsInserted)
			throw new UnsupportedOperationException("allCFGsInserted is true!");
		
		//reset reproducedCFGIterator
		this.reproducedCFGIterator = this.reproducedCFGList.iterator();
		
		this.showMessageAfterAllCFGsInserted = showMessageAfterDone;
		
		
		//submit the first reproduced CFG to a SingleSimpleProcessProgressManager
		//note that the insertion of remaining reproduced CFG will be triggered as the consequences of a chain reaction implemented in afterCFGSuccessfullyInsertedAction
		this.currentReproducedCFG = this.reproducedCFGIterator.next();
		CompositionFunctionGroupInserter inserter = 
				new CompositionFunctionGroupInserter(
						this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext(), 
						this.currentReproducedCFG);
		
		currentSingleSimpleProcessProgressManager = 
				new SingleSimpleProcessProgressManager(
						inserter,
						(Stage)this.getController().getRootNodeContainer().getScene().getWindow(),
						false,//allowingCancelAndRollback not allow
						this.getAfterCFGSuccessfullyInsertedAction(),
						null// additionalAfterAbortAndRollbackAction is null since not allow cancel and roll back;
						);
		
		currentSingleSimpleProcessProgressManager.start(false);
	}
	
	/**
	 * action to take after a reproduced CFG is successfully inserted;
	 * 
	 * 1. first create a {@link InsertedCFGManager} with the {@link #currentReproducedCFG}
	 * 		add to {@link #reproducedAndInsertedCFGIDManagerMap} and update the UI
	 * 2. set {@link #currentReproducedCFG} to null;
	 * 
	 * 3. check if there are more CFGs to be inserted in the {@link #reproducedCFGIterator}
	 * if yes
	 * 		retrieve the next reproduced CFGs and assign it to {@link #currentReproducedCFG}
	 * 		build CompositionFunctionGroupInserter and SingleSimpleProcessProgressManager with the {@link #currentReproducedCFG}
	 * 		run the SingleSimpleProcessProgressManager
	 * 
	 * if no
	 * 		run the {@link #getAfterAllCFGsInsertedAction()}
	 */
	private Runnable afterCFGSuccessfullyInsertedAction;
	private Runnable getAfterCFGSuccessfullyInsertedAction() {
		if(this.afterCFGSuccessfullyInsertedAction == null) {
			this.afterCFGSuccessfullyInsertedAction = ()->{
				try {
					//1
					InsertedCFGManager insertedCFGManager = new InsertedCFGManager(this, this.currentReproducedCFG);
					
					this.reproducedAndInsertedCFGIDManagerMap.put(this.currentReproducedCFG.getID(), insertedCFGManager);
					
					//add to UI
					this.getController().getReproducedAndInsertedCFGContainerHBox().getChildren().add(insertedCFGManager.getController().getRootNodeContainer());
					
					//2
					this.currentReproducedCFG = null;
					
					//3
					if(this.reproducedCFGIterator.hasNext()) {
						this.currentReproducedCFG = this.reproducedCFGIterator.next();
						
						CompositionFunctionGroupInserter inserter = 
								new CompositionFunctionGroupInserter(
										this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext(), 
										this.currentReproducedCFG);
						
//						//first check if the currentSingleSimpleProcessProgressManager is finished or not;
//						while(!this.currentSingleSimpleProcessProgressManager.isSuccessfullyDone())
//							Thread.sleep(500);
						
						
						currentSingleSimpleProcessProgressManager = 
								new SingleSimpleProcessProgressManager(
										inserter,
										(Stage)this.getController().getRootNodeContainer().getScene().getWindow(),
										false,//allowingCancelAndRollback not allow
										this.getAfterCFGSuccessfullyInsertedAction(),
										null// additionalAfterAbortAndRollbackAction is null since not allow cancel and roll back;
										);
						
						
						currentSingleSimpleProcessProgressManager.start(false);
					}else {//
						//
						this.getAfterAllCFGsInsertedAction().run();
					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
				
			};
		}
		return this.afterCFGSuccessfullyInsertedAction;
	}
	
	/**
	 * 1. set the {@link #allCFGsInserted} to true;
	 * 
	 * 2. notify the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} so that the CF reproducing and insertion can start
	 * 		invoke the {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#setToNewAndActivateUIDOfCFReproducingAndInsertion()}
	 * 
	 * 3. if {@link #showMessageAfterAllCFGsInserted} is true, show the message
	 */
	private Runnable afterAllCFGsInsertedAction;
	
	/**
	 * 
	 * @return
	 */
	private Runnable getAfterAllCFGsInsertedAction() {
		if(this.afterAllCFGsInsertedAction==null) {
			this.afterAllCFGsInsertedAction = ()->{
				this.allCFGsInserted = true;
				
				FXUtils.set2Disable(this.getController().startButton, true);
				FXUtils.set2Disable(this.getController().rollbackAllButton, false);
				
				try {
					this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().setToNewAndActivateUIDOfCFReproducingAndInsertion();
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
				
				//////////////////
				if(this.showMessageAfterAllCFGsInserted)
					AlertUtils.popAlert("Message", "All reproduced CompositionFunctionGroups have been inserted!");
				
			};
		}
		return this.afterAllCFGsInsertedAction;
	}
	
	
	public void setModifiable(boolean modifiable) {
		//1. first set the main window
		FXUtils.set2Disable(this.getController().controlHBox,!modifiable);
		
		//2 set each InsertedCFGManager;
		this.reproducedAndInsertedCFGIDManagerMap.forEach((id,manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	
	///////////////////////////////
	/**
	 * only applicable for viewing an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 */
	private Map<CompositionFunctionGroupID, Map<Integer, CompositionFunctionGroupID>> originalCFGIDCopyIndexReproducedCFGIDMapMap;
	
	/**
	 * set the value of the reproduced and inserted CFGs
	 * 
	 * only applicable for viewing an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 * 
	 * see {@link OperationReproducingAndInsertionManager#setValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance)} for implementation strategy;
	 * 
	 * @param originalCFGIDCopyIndexReproducedCFGIDMapMap
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void setValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance instance) throws SQLException, IOException {
		this.originalCFGIDCopyIndexReproducedCFGIDMapMap = instance.getOriginalCFGIDCopyIndexReproducedCFGIDMapMap();
		
		//create InsertedCFGManagers for each reproduced and inserted CFGs with the same order as the insertion 
		for(CompositionFunctionGroupID reproducedID:instance.getReproducedCFGIDListByInsertionOrder()) {
			CompositionFunctionGroup reproducedCFG = 
					this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(reproducedID);
			
			InsertedCFGManager manager = new InsertedCFGManager(this, reproducedCFG);
			
			this.reproducedAndInsertedCFGIDManagerMap.put(reproducedID, manager);
			
			this.getController().getReproducedAndInsertedCFGContainerHBox().getChildren().add(manager.getController().getRootNodeContainer());
		}
		
	}
	
	
	/////////////////////////////////////////////
	public CFGReproducingAndInsertinoController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CFGReproducingAndInsertinoController.FXML_FILE_DIR_STRING));
			
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
	private SimpleDirectedGraph<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> getTrimmedIntegratedCFDGraph(){
		return this.hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.getVisSchemeAppliedArchive().getTrimmedIntegratedCFDGraph();
	}
	
	/**
	 * @return the hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder
	 */
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder() {
		return hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	}

	/**
	 * @return the dagNodeInforStringFunction
	 */
	public Function<IntegratedCFDGraphNode, String> getDagNodeInforStringFunction() {
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
	 * @return the cFGReproducingAndInsertionTracker
	 */
	public CFGReproducingAndInsertionTracker getCFGReproducingAndInsertionTracker() {
		return this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getCFGReproducingAndInsertionTracker();
	}

}
