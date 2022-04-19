package builder.visframe.context.scheme.applier.reproduceandinserter.cf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import context.project.process.simple.CompositionFunctionInserter;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.utils.CFReproducingAndInsertionTracker;
import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
import builder.visframe.context.scheme.applier.reproduceandinserter.cf.cfd.IntegratedCFDGraphEdgeManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.cf.cfd.IntegratedCFDGraphNodeManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.cf.utils.InsertedCFManager;
import builder.visframe.context.scheme.applier.reproduceandinserter.operation.OperationReproducingAndInsertionManager;
import dependency.cfd.integrated.IntegratedCFDGraphEdge;
import dependency.cfd.integrated.IntegratedCFDGraphNode;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import function.composition.CompositionFunction;
import function.composition.CompositionFunctionID;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.AlertUtils;
import utils.FXUtils;
import utils.Pair;


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
public class CFReproducingAndInsertionManager {
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
	private CFReproducingAndInsertinoController controller;
	
	///////fields related with DAG graph layout
	private DAGNodeLevelAndOrderAssigner<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> nodeLevelAndOrderAssigner;
	
	private Map<IntegratedCFDGraphNode, IntegratedCFDGraphNodeManager> DAGNodeManagerMap;
	
	private Map<IntegratedCFDGraphEdge, IntegratedCFDGraphEdgeManager> DAGEdgeManagerMap;
	
	/////////////////////////////////////
	
//	private CFReproducingAndInsertionTracker CFReproducingAndInsertionTracker;
	/**
	 * 
	 */
	private List<Pair<CompositionFunctionID, InsertedCFManager>> reproducedAndInsertedCFIDManagerPairListOrderedByInsertion;
	
	/**
	 * if true, all CFs have been reproduced and inserted;
	 * if false, none of the CFs have been reproduced and inserted;
	 */
	private boolean allCFsInserted = false;
	
	private Iterator<CompositionFunction> reproducedCFIterator;
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
	public CFReproducingAndInsertionManager(
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
		this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion = new ArrayList<>();
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
	 * roll back all inserted CFs
	 * 
	 * the order of performing the roll back of all inserted CFs should be from the last to the first in the {@link #reproducedAndInsertedCFIDManagerPairListOrderedByInsertion}
	 * 
	 * @throws SQLException 
	 */
	public void rollbackAll(boolean showMessageAfterDone) throws SQLException {
		if(!this.allCFsInserted)
			return; //do nothing
		
		
		for(int i = this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion.size();i>0;i--){//TODO
			Pair<CompositionFunctionID, InsertedCFManager> pair = this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion.get(i-1);
			//1 roll back
			this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext()
			.getProcessLogTableAndProcessPerformerManager().rollbackProcess(pair.getFirst());
		}
		
		//remove from UI
		this.getController().getReproducedAndInsertedCFContainerHBox().getChildren().clear();
		//
		this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion.clear();
		
		this.allCFsInserted = false;
		FXUtils.set2Disable(this.getController().startButton, false);
		FXUtils.set2Disable(this.getController().rollbackAllButton, true);
		
		this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getEmbeddedUIContentController().step3StatusLabel.setText("UNFINISHED");
		
		if(showMessageAfterDone)
			AlertUtils.popAlert("Message", "All inserted CompositionFunctionGroups have been rolled back!");
	}
	
	//////////////////////
	private boolean showMessageAfterAllCFsInserted;
	/**
	 * the current CFG that is to be inserted;
	 */
	private CompositionFunction currentReproducedCF;
	
	/**
	 * insert all reproduced CFG by the {@link #CFGReproducingAndInsertionTracker} into the host VisProjectDBContext;
	 * @param showMessageAfterDone
	 * @throws SQLException 
	 */
	public void insertAll(boolean showMessageAfterDone) throws SQLException {
		if(this.allCFsInserted)
			throw new UnsupportedOperationException("allCFGsInserted is true!");
		
		//reset reproducedCFGIterator
		this.reproducedCFIterator = this.getCFReproducingAndInsertionTracker().getReproducedCompositionFunctionListOrderedByDependency().iterator();
		
		this.showMessageAfterAllCFsInserted = showMessageAfterDone;
		
		//submit the first reproduced CFG to a SingleSimpleProcessProgressManager
		//note that the insertion of remaining reproduced CFG will be triggered as the consequences of a chain reaction implemented in afterCFGSuccessfullyInsertedAction
		this.currentReproducedCF = this.reproducedCFIterator.next();
		
		CompositionFunctionInserter inserter = 
				new CompositionFunctionInserter(
						this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext(), 
						this.currentReproducedCF);
		
		SingleSimpleProcessProgressManager manager = 
				new SingleSimpleProcessProgressManager(
						inserter,
						(Stage)this.getController().getRootNodeContainer().getScene().getWindow(),
						false,//allowingCancelAndRollback not allow
						this.getAfterCFSuccessfullyInsertedAction(),
						null// additionalAfterAbortAndRollbackAction is null since not allow cancel and roll back;
						);
		
		manager.start(false);
	}
	
	/**
	 * action to take after a reproduced CF is successfully inserted;
	 * 
	 * 1. first create a {@link InsertedCFManager} with the {@link #currentReproducedCF}
	 * 		add to {@link #reproducedAndInsertedCFIDManagerPairListOrderedByInsertion} and update the UI
	 * 
	 * 2. set {@link #currentReproducedCF} to null;
	 * 
	 * 3. check if there are more CFs to be inserted in the {@link #reproducedCFIterator}
	 * if yes
	 * 		retrieve the next reproduced CF and assign it to {@link #currentReproducedCF}
	 * 		build CompositionFunctionInserter and SingleSimpleProcessProgressManager with the {@link #currentReproducedCF}
	 * 		run the SingleSimpleProcessProgressManager
	 * 
	 * if no
	 * 		run the {@link #getAfterAllCFsInsertedAction()}
	 */
	private Runnable afterCFSuccessfullyInsertedAction;
	private Runnable getAfterCFSuccessfullyInsertedAction() {
		if(this.afterCFSuccessfullyInsertedAction == null) {
			this.afterCFSuccessfullyInsertedAction = ()->{
				try {
					//1
					InsertedCFManager insertedCFManager = new InsertedCFManager(this, this.currentReproducedCF);
					
					this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion.add(new Pair<>(this.currentReproducedCF.getID(),insertedCFManager));
					//add to UI
					this.getController().getReproducedAndInsertedCFContainerHBox().getChildren().add(insertedCFManager.getController().getRootNodeContainer());
					
					//2
					this.currentReproducedCF = null;
					
					//3
					if(this.reproducedCFIterator.hasNext()) {
						this.currentReproducedCF = this.reproducedCFIterator.next();
						
						CompositionFunctionInserter inserter = 
								new CompositionFunctionInserter(
										this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext(), 
										this.currentReproducedCF);
						
						SingleSimpleProcessProgressManager manager = 
								new SingleSimpleProcessProgressManager(
										inserter,
										(Stage)this.getController().getRootNodeContainer().getScene().getWindow(),
										false,//allowingCancelAndRollback not allow
										this.getAfterCFSuccessfullyInsertedAction(),
										null// additionalAfterAbortAndRollbackAction is null since not allow cancel and roll back;
										);
						
						manager.start(false);
					}else {//
						//
						this.getAfterAllCFsInsertedAction().run();
					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
				
			};
		}
		return this.afterCFSuccessfullyInsertedAction;
	}
	
	/**
	 * 1. set the {@link #allCFsInserted} to true;
	 * 
	 * 2. notify the {@link #hostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} so that 
	 * 		the VisSchemeAppliedArchiveReproducedAndInsertedInstance can be finished and inserted into host VisProjectDBContext;
	 * 3. if {@link #showMessageAfterAllCFsInserted} is true, show the message
	 */
	private Runnable afterAllCFsInsertedAction;
	private Runnable getAfterAllCFsInsertedAction() {
		if(this.afterAllCFsInsertedAction==null) {
			this.afterAllCFsInsertedAction = ()->{
				this.allCFsInserted = true;
				
				FXUtils.set2Disable(this.getController().startButton, true);
				FXUtils.set2Disable(this.getController().rollbackAllButton, false);
				
				//
				this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().setToFinishable();
				
				//////////////////
				if(this.showMessageAfterAllCFsInserted)
					AlertUtils.popAlert("Message", "All reproduced CompositionFunctions have been inserted!");
				
			};
		}
		return this.afterAllCFsInsertedAction;
	}
	
	/**
	 * 
	 * @param cfid
	 * @param a
	 */
	public void highlightIntegratedCFDGraphNode(CompositionFunctionID cfid, boolean a) {
		//TODO
	}
	
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		//1. first set the main window
		FXUtils.set2Disable(this.getController().controlHBox,!modifiable);
		
		//2 set each InsertedCFManager;
		this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion.forEach(pair->{
				pair.getSecond().setModifiable(modifiable);
		});
	}
	
	
	///////////////////////////////
	/**
	 * only applicable for viewing an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance
	 */
	private Map<CompositionFunctionID, Map<Integer, CompositionFunctionID>> originalCFIDCopyIndexReproducedCFIDMapMap;
	
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
		this.originalCFIDCopyIndexReproducedCFIDMapMap = instance.getOriginalCFIDCopyIndexReproducedCFIDMapMap();
		
		//create InsertedCFGManagers for each reproduced and inserted CFGs with the same order as the insertion 
		for(CompositionFunctionID reproducedID:instance.getReproducedCFIDListByInsertionOrder()) {
			CompositionFunction reproducedCF = 
					this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionManager().lookup(reproducedID);
			
			InsertedCFManager manager = new InsertedCFManager(this, reproducedCF);
			
			this.reproducedAndInsertedCFIDManagerPairListOrderedByInsertion.add(new Pair<>(reproducedID, manager));
			
			this.getController().getReproducedAndInsertedCFContainerHBox().getChildren().add(manager.getController().getRootNodeContainer());
		}
		
	}
	
	/////////////////////////////////////////////
	public CFReproducingAndInsertinoController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CFReproducingAndInsertinoController.FXML_FILE_DIR_STRING));
			
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
	 * @return the cFReproducingAndInsertionTracker
	 */
	public CFReproducingAndInsertionTracker getCFReproducingAndInsertionTracker() {
		return this.getHostVisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getCFReproducingAndInsertionTracker();
	}

}
