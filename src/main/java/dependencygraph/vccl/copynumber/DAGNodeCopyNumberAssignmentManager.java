package dependencygraph.vccl.copynumber;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.SimpleDirectedGraph;

import basic.VfNotes;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import dependency.vccl.utils.NodeCopy;
import dependencygraph.vccl.copynumber.edge.DAGEdgeManager;
import dependencygraph.vccl.copynumber.node.DAGNodeManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

/**
 * assigner for a copy number for each node on a target SimpleDirectedGraph;
 * 
 * then build the map from each node to the assigned copy number;
 * 
 * the constraints must be obeyed;
 * 
 * facilitate creating a {@link VisSchemeAppliedArchive}
 * 
 * note that the edge and node type E and V must override the {@link #equals(Object)} and {@link #hashCode()} methods!
 * 
 * @author tanxu
 *
 * @param <V>
 * @param <E>
 */
public class DAGNodeCopyNumberAssignmentManager<V,E>{
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	//////////////////////
	private final SimpleDirectedGraph<V,E> dag;
	private final Comparator<V> nodeOrderComparator;
	/**
	 * distance between the layout Y of two neighboring levels!
	 */
	private final double distBetweenLevels;
	/**
	 * distance between the bounds of two neighboring nodes at the same level
	 */
	private final double distBetweenNodesOnSameLevel;
	private final Function<V,String> nodeInforStringFunction;
	//////////////////
	private DAGNodeCopyNumberAssignmentController<V,E> controller;
	private Map<V, DAGNodeManager<V,E>> nodeManagerMap;
	private Map<E, DAGEdgeManager<V,E>> edgeManagerMap;
	
	
	/////////////////////
	/**
	 * 
	 */
	private String beforeChangeMadeWarningInforText;
	
	/**
	 * action to take AFTER a new copy number assignment of all nodes is saved or reset to initial empty value;
	 */
	private Runnable afterChangeMadeAction;
	
	/**
	 * the assignment most recently saved; could be a finished one or a default empty one;
	 * 
	 * if empty, a clear all action has been taken and no finished assignment has been saved afterwards;
	 * 
	 * null only if this manager is just initialized;
	 */
	private Map<V,Map<Integer, NodeCopy<V>>> mostRecentlySavedNodeCopyIndexNodeCopyMap = new HashMap<>();
	/**
	 * whether or not the {@link #mostRecentlySavedNodeCopyNumberAssignment} is a finished assignment or not;
	 */
	private Boolean mostRecentlySavedAssignmentFinished;
	
	/**
	 * 
	 * @param dag
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param nodeInforStringFunction
	 */
	public DAGNodeCopyNumberAssignmentManager(
			SimpleDirectedGraph<V,E> dag, 
			Comparator<V> nodeOrderComparator,
			double distBetweenLevels, 
			double distBetweenNodesOnSameLevel,
			Function<V,String> nodeInforStringFunction
			) {
		//
		if(dag==null)
			throw new IllegalArgumentException("given dag cannot be null!");
		if(nodeOrderComparator==null)
			throw new IllegalArgumentException("given nodeOrderComparator cannot be null!");
		if(nodeInforStringFunction==null)
			throw new IllegalArgumentException("given nodeInforStringFunction cannot be null!");
		
		CycleDetector<V,E> cd = new CycleDetector<>(dag);
		if(cd.detectCycles())
			throw new IllegalArgumentException("Cycle found in the given DAG!");
		
		
		
		/////////////////////////
		this.dag = dag;
		this.nodeOrderComparator = nodeOrderComparator;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.nodeInforStringFunction = nodeInforStringFunction;
		
		//////////////////
		this.createNodeManager();
		
		this.calculateCoordinate();
		
		this.createEdgeManager();
		
		this.layoutNodesAndEdges();
	}
	
	/**
	 * 1. create DAGNodeManager for each node on the {@link #dag}
	 * 
	 * 2. set the depended and depending DAGNodeManager set for each DAGNodeManager;
	 * 
	 * 3. set copy number change event listener of each DAGNodeManager
	 * 
	 * 4. Initialize all DAGNodeManager's copy number to 0 
	 * 
	 * 5. note that the real height and width of the root fx node of each DAG node will be calculated in the constructor of each DAGNodeManager object;
	 * 
	 * 
	 * must be invoked before {@link #calculateCoordinate()} because the real height and width of each DAG Node is needed to calculate the layout of each node on the canvass;
	 * 
	 */
	private void createNodeManager() {
		//create DAGNodeManager for each node on dag
		this.nodeManagerMap = new HashMap<>();
		this.dag.vertexSet().forEach(v->{
			this.nodeManagerMap.put(v, new DAGNodeManager<>(this, v, nodeInforStringFunction.apply(v)));
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.dag.vertexSet().forEach(v->{
			Set<DAGNodeManager<V,E>> dependedNodeManagerSet = new HashSet<>();
			this.dag.outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.nodeManagerMap.get(this.dag.getEdgeTarget(e)));
			});
			
			this.nodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<DAGNodeManager<V,E>> dependingNodeManagerSet = new HashSet<>();
			this.dag.incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.nodeManagerMap.get(this.dag.getEdgeSource(e)));
			});
			this.nodeManagerMap.get(v).setDependingDAGNodeManagerSet(dependingNodeManagerSet);
		});
		
		
		//set the copy number change event listener for each DAGNodeManager;
		this.nodeManagerMap.forEach((node, manager)->{
			manager.setCopyNumberChangeEventListener();
		});
		
		//initialize copy number to 0
		this.nodeManagerMap.forEach((node, manager)->{
			
			manager.setCopyIndexNodeCopyMap(new HashMap<>());//set to 0 copy number
			
		});
		
		//initialize status
		this.nodeManagerMap.forEach((node, manager)->{
			manager.updateStatus();
		});
		
		
	}
	
	
	
	/**
	 * first calculate the coordinate for each node on the {@link #dag}
	 * 
	 */
	private void calculateCoordinate() {
		
		DAGNodeLevelAndOrderAssigner<V,E> nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.dag, this.nodeOrderComparator);
		
		//first find out the total width for each level and find out the largest total width for a level;
		Map<Integer, Double> levelTotalWidthMap = new HashMap<>();
		double largestLevelTotalWidth = 0;
		
		for(int lvl:nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()){
			double totalWidth = 0;
			//first index is 0 on each level
			for(int index = 0;index<nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).size();index++) {
				DAGNodeManager<V, E> dagNodeManager = this.nodeManagerMap.get(nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).get(index));
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
		for(int lvl:nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()){
			double levelXStart = centerX - 0.5*levelTotalWidthMap.get(lvl);
			
			double sumAllPreviousNodeWidth = 0; //reset for each level
			
			for(int index = 0;index<nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).size();index++) {
				
				DAGNodeManager<V, E> dagNodeManager = this.nodeManagerMap.get(nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).get(index));
				
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
	 * 
	 */
	private void createEdgeManager() {
		this.edgeManagerMap = new HashMap<>();
		this.dag.edgeSet().forEach(e->{
			V dependingNode = this.dag.getEdgeSource(e);
			V dependedNode = this.dag.getEdgeTarget(e);
			
			double dependingNodeUpperCenterX = 
					this.nodeManagerMap.get(dependingNode).getCenterCoord().getX();
			double dependingNodeUpperCenterY = 
					this.nodeManagerMap.get(dependingNode).getCenterCoord().getY() - 0.5*this.nodeManagerMap.get(dependingNode).getRealHeight();
			
			double dependedNodeBottomCenterX = 
					this.nodeManagerMap.get(dependedNode).getCenterCoord().getX();
			double dependedNodeBottomCenterY = 
					this.nodeManagerMap.get(dependedNode).getCenterCoord().getY() + 0.5*this.nodeManagerMap.get(dependedNode).getRealHeight();;
			
			
			this.edgeManagerMap.put(
					e, 
					new DAGEdgeManager<>(
							this,
							e,
							new Point2D(dependingNodeUpperCenterX, dependingNodeUpperCenterY),
							new Point2D(dependedNodeBottomCenterX, dependedNodeBottomCenterY)
							));
		});
	}
	
	
	
	/**
	 * add the edges and nodes to the layout AnchorPane;
	 * note that add node first;
	 */
	private void layoutNodesAndEdges() {
		//nodes
		this.nodeManagerMap.forEach((k,v)->{
			
			this.getController().getGraphLayoutAnchorPane().getChildren().add(v.getController().getRootNodeContainer());
			
		});
		
		//edges
		this.edgeManagerMap.forEach((k,v)->{
			this.getController().getGraphLayoutAnchorPane().getChildren().add(v.getCurve());
		});
		
	}
	
	/////////////////////////////
	/**
	 * check if current copy number assignment can be finished
	 * 
	 * equivalently to checking whether there is at least one vertex been assigned a positive copy number;
	 * 
	 * if yes,
	 * 		the copy number assignment is finishable;
	 * otherwise,
	 * 		the copy number cannot be finished;
	 * @return
	 */
	public boolean currentCopyNumberAssignmentIsFinishable() {
		for(V node:this.nodeManagerMap.keySet()){
			if(this.nodeManagerMap.get(node).getCopyNumber()>0)
				return true;
		}
		return false;
	}
	
	/**
	 * build and return the map of current copy number for each vertex in the {@link #dag}
	 * @return
	 */
	public Map<V,Map<Integer,NodeCopy<V>>> buildNodeCopyIndexNodeCopyMap(){
		return this.getCurrentNodeCopyIndexNodeCopyMap();
	}
	
	
	/////////////////////////////////////
	/**
	 * @return the dag
	 */
	public SimpleDirectedGraph<V, E> getDag() {
		return dag;
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
	 * @return the nodeManagerMap
	 */
	public Map<V, DAGNodeManager<V, E>> getNodeManagerMap() {
		return nodeManagerMap;
	}
	
	////////////////////////////////
	/**
	 * @return the changeMadeAction
	 */
	public Runnable getAfterChangeMadeAction() {
		return afterChangeMadeAction;
	}

	/**
	 * 
	 * @param afterChangeMadeAction
	 * @param beforeChangeMadeWarningInforText
	 */
	public void setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(Runnable afterChangeMadeAction, String beforeChangeMadeWarningInforText) {
		this.afterChangeMadeAction = afterChangeMadeAction;
		this.beforeChangeMadeWarningInforText = beforeChangeMadeWarningInforText;
	}
	
	/**
	 * @return the changeMadeInforText
	 */
	public String getBeforeChangeMadeWarningInforText() {
		return beforeChangeMadeWarningInforText;
	}


	/**
	 * @return the mostRecentlySavedNodeCopyIndexNodeCopyMap
	 */
	public Map<V,Map<Integer,NodeCopy<V>>> getMostRecentlySavedNodeCopyIndexNodeCopyMap() {
		return mostRecentlySavedNodeCopyIndexNodeCopyMap;
	}

	/**
	 * @param mostRecentlySavedNodeCopyIndexNodeCopyMap the mostRecentlySavedNodeCopyIndexNodeCopyMap to set
	 */
	public void setMostRecentlySavedNodeCopyIndexNodeCopyMap(Map<V,Map<Integer,NodeCopy<V>>> mostRecentlySavedNodeCopyIndexNodeCopyMap) {
		this.mostRecentlySavedNodeCopyIndexNodeCopyMap = mostRecentlySavedNodeCopyIndexNodeCopyMap;
	}
	
	/**
	 * @return the mostRecentlySavedAssignmentFinished
	 */
	public Boolean getMostRecentlySavedAssignmentFinished() {
		return mostRecentlySavedAssignmentFinished;
	}
	
	/**
	 * @param mostRecentlySavedAssignmentFinished the mostRecentlySavedAssignmentFinished to set
	 */
	public void setMostRecentlySavedAssignmentFinished(Boolean mostRecentlySavedAssignmentFinished) {
		this.mostRecentlySavedAssignmentFinished = mostRecentlySavedAssignmentFinished;
	}
	
	
	/**
	 * @return the currentNodeCopyNumberMap
	 */
	public Map<V,Map<Integer,NodeCopy<V>>> getCurrentNodeCopyIndexNodeCopyMap() {
		Map<V,Map<Integer,NodeCopy<V>>> ret = new HashMap<>();
		
		this.getNodeManagerMap().forEach((node,manager)->{
			Map<Integer,NodeCopy<V>> copyIndexNodeCopyMap = new HashMap<>();
			
			manager.getNotesAssignmentManager().getCopyIndexDAGNodeCopyNotesAssignmentManagerMap().forEach((index, m2)->{
				
				copyIndexNodeCopyMap.put(
						index, 
						new NodeCopy<>(
								node,
								index,
								new VfNotes(m2.getController().getNodeCopyNotesTextArea().getText())));
			});
			
			ret.put(node, copyIndexNodeCopyMap);
		});
		
		return ret;
	}
	
	
	/**
	 * set the copy number of all nodes as the map
	 * @param nodeCopyNumberMap
	 */
	public void setNodeCopyIndexNodeCopyMap(Map<V,Map<Integer,NodeCopy<V>>> nodeCopyIndexNodeCopyMap) {
		this.getNodeManagerMap().forEach((node,manager)->{
			manager.setCopyIndexNodeCopyMap(nodeCopyIndexNodeCopyMap.get(node));
		});
	}
	
	
	//////////////////////////
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		//set the controller buttons
		this.getController().setModifiable(modifiable);
		
		
		//set each node manager
		this.nodeManagerMap.forEach((node, manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	//////////////////////////
	public DAGNodeCopyNumberAssignmentController<V,E> getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGNodeCopyNumberAssignmentController.FXML_FILE_DIR_STRING));
			
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


}
