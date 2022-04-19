package dependencygraph.vccl.copylink;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import dependency.layout.DAGNodeLevelAndOrderAssigner;
import dependency.vccl.utils.CLCBCC2;
import dependency.vccl.utils.DAGNodeCopyLinkAssigner;
import dependency.vccl.utils.NodeCopy;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import utils.ColorUtils;

/**
 * assign copy links to all copies of all nodes of a target DAG;
 * 
 * obey the constraints
 * 
 * after all links are created, build a copy link graph;
 * 
 * 1. each copy can be selected or un-selected
 * 2. there can only be at most one copy selected at a time;
 * 3. when one copy is selected, all other copy become unselectable
 * 4. to select a selected copy, single-click it
 * 5. to de-select a selected copy, single-click it;
 * 6. when a copy of a DAG node is selected, find out the set of copies of all depended DAG nodes that are linkable to the selected copy;
 * 7. if a linkable copy is single clicked, add a link from the selected copy to the linked copy and set the status of the selected copy to unselected;
 * 
 * @author tanxu
 * 
 * @param <V>
 * @param <E>
 * @param <CV>
 * @param <CE>
 */
public class CopyLinkAssignerManager<V, E> {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	//////////////////////////////////////
	/**
	 * 
	 */
	private final SimpleDirectedGraph<V,E> dag;
	
	private final Class<E> DAGEdgeType;
	/**
	 * map from node
	 * to the map
	 * 		from the copy index
	 * 		to the NodeCopy
	 */
	private final Map<V, Map<Integer, NodeCopy<V>>> nodeCopyIndexNodeCopyMapMap;
	
	private final Comparator<V> nodeOrderComparator;
	/**
	 * distance between the layout Y of two neighboring levels!
	 */
	private final double distBetweenLevels;
	/**
	 * distance between the bounds of two neighboring nodes at the same level
	 */
	private final double distBetweenNodesOnSameLevel;
	
	private final Function<V,String> dagNodeInforStringFunction;
	
	//////////////////////////////
	private CopyLinkAssignerController<V,E> controller;
	
	private Map<V, Integer> nodeCopyNumberMap;
	///////fields related with DAG graph layout and copy node layout
	private DAGNodeLevelAndOrderAssigner<V,E> nodeLevelAndOrderAssigner;
	
	private Map<V, DAGNodeManager<V, E>> DAGNodeManagerMap;

	private Map<E, DAGEdgeManager<V,E>> DAGEdgeManagerMap;
	
	
	/////////////////////////
	//////fields related with copy link assignment
	private DAGNodeCopyLinkAssigner<V,E> DAGNodeCopyLinkAssigner;
	private Map<CLCBCC2<V,E>, Color> CLCBCC2ColorMap;
	
	
	/**
	 * currently selected {@link DAGNodeCopyManager}
	 */
	private DAGNodeCopyManager<V,E> selectedNodeCopyManager;
	
	/**
	 * map from the depending {@link NodeCopy} 
	 * to the map
	 * 		from the depended {@link NodeCopy} 
	 * 		to the CubicCurve of the link between them;
	 */
	private Map<NodeCopy<V>, Map<NodeCopy<V>, CubicCurve>> dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap;
	//////////////////////////
	/**
	 * action to take AFTER a new finished copy link assignment is saved or reset to initial empty value;
	 */
	private Runnable afterChangeMadeAction;
	
	private String beforeChangeMadeWarningInforText;
	
	/**
	 * the assignment most recently saved; could be a finished one or a default empty one;
	 * 
	 * if empty, a clear all action has been taken and no finished assignment has been saved afterwards;
	 * 
	 */
	private Map<V, Map<Integer,Map<V, Integer>>> mostRecentlySavedCopyLinkAssignment = new HashMap<>();
	/**
	 * whether or not the {@link #mostRecentlySavedCopyLinkAssignment} is a finished assignment or not;
	 */
	private Boolean mostRecentlySavedCopyLinkAssignmentFinished;
	
	/**
	 * 
	 * @param dag
	 * @param DAGEdgeType
	 * @param nodeCopyNumberMap
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 */
	public CopyLinkAssignerManager(
			SimpleDirectedGraph<V,E> dag,
			Class<E> DAGEdgeType,
			Map<V, Map<Integer, NodeCopy<V>>> nodeCopyIndexNodeCopyMapMap,
			Comparator<V> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<V,String> dagNodeInforStringFunction
			){
		this.dag = dag;
		this.DAGEdgeType = DAGEdgeType;
		this.nodeCopyIndexNodeCopyMapMap = nodeCopyIndexNodeCopyMapMap;
		this.nodeOrderComparator = nodeOrderComparator;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.dagNodeInforStringFunction = dagNodeInforStringFunction;
		//////////////////
		
		this.nodeCopyNumberMap = new HashMap<>();
		this.nodeCopyIndexNodeCopyMapMap.forEach((node,map)->{
			this.nodeCopyNumberMap.put(node, map.size());
		});
		//
		this.createDAGNodeManager();
		
		this.calculateAndSetDAGNodeLayoutProperty();
				
		this.createDAGEdgeManager();
		
		this.layoutDAGNodeOnCanvass();
		
		this.layoutDAGEdgesOnCanvass();
		//////////////////////////
		this.initializeDAGNodeCopyLinkAssigner();
//		
		this.setDAGEdgeCLCBCCColor();
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
		this.dag.vertexSet().forEach(v->{
			this.DAGNodeManagerMap.put(v, new DAGNodeManager<>(this, v, this.nodeCopyNumberMap.get(v), this.dagNodeInforStringFunction));
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.dag.vertexSet().forEach(v->{
			Set<DAGNodeManager<V,E>> dependedNodeManagerSet = new HashSet<>();
			this.dag.outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.DAGNodeManagerMap.get(this.dag.getEdgeTarget(e)));
			});
			
			this.DAGNodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<DAGNodeManager<V,E>> dependingNodeManagerSet = new HashSet<>();
			this.dag.incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.DAGNodeManagerMap.get(this.dag.getEdgeSource(e)));
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
		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.dag, this.nodeOrderComparator);
		
		//first find out the total width for each level and find out the largest total width for a level;
		Map<Integer, Double> levelTotalWidthMap = new HashMap<>();
		double largestLevelTotalWidth = 0;
		
		for(int lvl:this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()){
			double totalWidth = 0;
			//first index is 0 on each level
			for(int index = 0;index<this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).size();index++) {
				DAGNodeManager<V, E> dagNodeManager = this.DAGNodeManagerMap.get(this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).get(index));
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
				
				DAGNodeManager<V, E> dagNodeManager = this.DAGNodeManagerMap.get(this.nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(lvl).get(index));
				
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
		this.dag.edgeSet().forEach(e->{
			V dependingNode = this.dag.getEdgeSource(e);
			V dependedNode = this.dag.getEdgeTarget(e);
			
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
					new DAGEdgeManager<>(
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
	 * initialize the {@link #DAGNodeCopyLinkAssigner}
	 * 
	 * also set the NodeCopy of each {@link DAGNodeCopyManager};
	 * 
	 */
	private void initializeDAGNodeCopyLinkAssigner() {
		this.DAGNodeCopyLinkAssigner = new DAGNodeCopyLinkAssigner<>(
				this.dag,
				this.DAGEdgeType,
				this.nodeCopyIndexNodeCopyMapMap);
		
		//set the NodeCopy of each {@link CopyNodeManager}
		this.DAGNodeCopyLinkAssigner.getNodeCopyIndexNodeCopyMapMap().forEach((node, copyIndexNodeCopyMap)->{
			copyIndexNodeCopyMap.forEach((copyIndex, nodeCopy)->{
				DAGNodeCopyManager<V, E> copyNodeManager = this.DAGNodeManagerMap.get(node).getCopyIndexNodeCopyManagerMap().get(copyIndex);
				copyNodeManager.setNodeCopy(nodeCopy);
			});
		});
	}
	
	/**
	 * generate a random color for each detected {@link CLCBCC2}
	 * then DAG edges assigned to each {@link CLCBCC2} with the generated color;
	 */
	private void setDAGEdgeCLCBCCColor() {
		this.CLCBCC2ColorMap = new HashMap<>();
		this.DAGNodeCopyLinkAssigner.getClcbccDetector().getCLCBCSet().forEach(c->{
			this.CLCBCC2ColorMap.put(c, ColorUtils.randomColor());
		});
		
		this.DAGNodeCopyLinkAssigner.getClcbccDetector().getDagEdgeCLCBCCMap().forEach((DAGEdge, CLCBCC)->{
			this.DAGEdgeManagerMap.get(DAGEdge).setCLCBCCVisualEffect(this.CLCBCC2ColorMap.get(CLCBCC));
		});
		
	}
	//////////////////////////
	public CopyLinkAssignerController<V,E> getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CopyLinkAssignerController.FXML_FILE_DIR_STRING));
			
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
	 * @return the dAGNodeCopyLinkAssigner
	 */
	public DAGNodeCopyLinkAssigner<V, E> getDAGNodeCopyLinkAssigner() {
		return DAGNodeCopyLinkAssigner;
	}
	
	/**
	 * @return the selectedCopyNodeManager
	 */
	public DAGNodeCopyManager<V,E> getSelectedNodeCopyManager() {
		return selectedNodeCopyManager;
	}

	/**
	 * @param selectedCopyNodeManager the selectedCopyNodeManager to set
	 */
	public void setSelectedNodeCopyManager(DAGNodeCopyManager<V,E> selectedCopyNodeManager) {
		this.selectedNodeCopyManager = selectedCopyNodeManager;
	}

	/**
	 * @return the dAGNodeManagerMap
	 */
	public Map<V, DAGNodeManager<V, E>> getDAGNodeManagerMap() {
		return DAGNodeManagerMap;
	}
	
	
	//////////////////////////////
	/**
	 * add copy link curve from the {@link #selectedNodeCopyManager} to the given depended DAGNodeCopyManager
	 * @param dependingNodeCopyManager
	 * @param dependedNodeCopyManager
	 */
	public void addCopyLinkCurve(DAGNodeCopyManager<V,E> dependingNodeCopyManager, DAGNodeCopyManager<V,E> dependedNodeCopyManager) {
		if(this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap == null)
			this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap = new HashMap<>();
		
		///create CubicCurve
		CubicCurve copyLinkCurve = new CubicCurve();
		
		copyLinkCurve.setFill(null);
		copyLinkCurve.setStroke(Color.RED);
		copyLinkCurve.setStrokeWidth(2);
		
		
		copyLinkCurve.setStartX(dependingNodeCopyManager.getUpperCenterCoordinate().getX());
		copyLinkCurve.setStartY(dependingNodeCopyManager.getUpperCenterCoordinate().getY());
		
		copyLinkCurve.setEndX(dependedNodeCopyManager.getBottomCenterCoordinate().getX());
		copyLinkCurve.setEndY(dependedNodeCopyManager.getBottomCenterCoordinate().getY());
		
		copyLinkCurve.setControlX1(dependingNodeCopyManager.getUpperCenterCoordinate().getX());
		copyLinkCurve.setControlY1(dependingNodeCopyManager.getUpperCenterCoordinate().getY()-this.getDistBetweenLevels()/2);
		
		copyLinkCurve.setControlX2(dependedNodeCopyManager.getBottomCenterCoordinate().getX());
		copyLinkCurve.setControlY2(dependedNodeCopyManager.getBottomCenterCoordinate().getY()+this.getDistBetweenLevels()/2);
		
		////////////
		if(!this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.containsKey(dependingNodeCopyManager.getNodeCopy())){
			this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.put(dependingNodeCopyManager.getNodeCopy(), new HashMap<>());
		}
		
		this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.get(dependingNodeCopyManager.getNodeCopy()).put(
				dependedNodeCopyManager.getNodeCopy(), copyLinkCurve);
		
		this.getController().getGraphLayoutAnchorPane().getChildren().add(copyLinkCurve);
	}
	
	/**
	 * 
	 * @param dependedNodeCopyManager
	 */
	public void removeCopyLinkCurve(DAGNodeCopyManager<V,E> dependedNodeCopyManager) {
		this.getController().getGraphLayoutAnchorPane().getChildren().remove(
				this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.get(this.selectedNodeCopyManager.getNodeCopy()).get(dependedNodeCopyManager.getNodeCopy()));
		
		//
		this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.get(this.selectedNodeCopyManager.getNodeCopy()).remove(dependedNodeCopyManager.getNodeCopy());
	}
	
	/**
	 * clear all copy links
	 * 
	 * 1. clear all copy link curves from canvass
	 * 
	 * 2. clear all copy links in {@link #DAGNodeCopyLinkAssigner}
	 */
	public void clearAllCopyLinks() {
		this.clearAllCopyLinkCurves();
		this.DAGNodeCopyLinkAssigner.clearAllLinks();
	}
	
	/**
	 * clear all copy link curves from canvass and reset the {@link #dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap}
	 */
	private void clearAllCopyLinkCurves() {
		if(this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap==null)
			return;
		
		this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.forEach((dependingCopy, dependedNodeCopyLinkCurveMap)->{
			dependedNodeCopyLinkCurveMap.forEach((dependedCopy, copyLinkCurve)->{
				this.getController().getGraphLayoutAnchorPane().getChildren().remove(copyLinkCurve);
			});
		});
		
		this.dependingNodeCopyDependedNodeCopyLinkCubicCurveMapMap.clear();
	}
	
	/////////////////////
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		//set the UI
		this.getController().setModifiable(modifiable);
		
		//set each node
		this.DAGNodeManagerMap.forEach((node,manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	///////////////////////////////
	
	/**
	 * return the map containing the currently linked copies;
	 * 
	 * if there is no linked copies, the returned map will be empty!
	 * @return
	 */
	Map<V, Map<Integer,Map<V, Integer>>> getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap(){
		Map<V, Map<Integer,Map<V, Integer>>> ret = new HashMap<>();
		
		this.getDAGNodeCopyLinkAssigner().getDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().forEach((dependingNode,copyIndexDependedNodeLinkedCopyIndexMapMap)->{
			copyIndexDependedNodeLinkedCopyIndexMapMap.forEach((dependingNodeCopyIndex, dependedNodeLinkedCopyIndexMap)->{
				dependedNodeLinkedCopyIndexMap.forEach((dependedNode,dependedNodeLinkedCopyIndex)->{
					if(dependedNodeLinkedCopyIndex!=null) {
						if(!ret.containsKey(dependingNode))
							ret.put(dependingNode, new HashMap<>());
						
						if(!ret.get(dependingNode).containsKey(dependingNodeCopyIndex))
							ret.get(dependingNode).put(dependingNodeCopyIndex, new HashMap<>());
						
						ret.get(dependingNode).get(dependingNodeCopyIndex).put(dependedNode,dependedNodeLinkedCopyIndex);
					}
				});
			});
		});
		
		return ret;
		
	}
	
	
	/**
	 * set the copy links as the given ones
	 * 
	 * need to update the links in DAGNodeCopyLinkAssigner and the links in this manager
	 * 
	 * @param linkedDependingCopyDependedCopyMap
	 */
	public void setLinks(Map<V, Map<Integer,Map<V, Integer>>> dependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap) {
		this.clearAllCopyLinks();
		
		//add links
		dependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap.forEach((dependingNode, copyIndexDependedNodeLinkedCopyIndexMapMap)->{
			copyIndexDependedNodeLinkedCopyIndexMapMap.forEach((dependingNodeCopyIndex, dependedNodeLinkedCopyIndexMap)->{
				dependedNodeLinkedCopyIndexMap.forEach((dependedNode, dependedNodeCopyIndex)->{
					//add to the DAGNodeCopyLinkAssigner
					this.getDAGNodeCopyLinkAssigner().addLink(dependingNode, dependingNodeCopyIndex, dependedNode, dependedNodeCopyIndex);
					//add curve to UI
					this.addCopyLinkCurve(
							this.getDAGNodeManagerMap().get(dependingNode).getCopyIndexNodeCopyManagerMap().get(dependingNodeCopyIndex),
							this.getDAGNodeManagerMap().get(dependedNode).getCopyIndexNodeCopyManagerMap().get(dependedNodeCopyIndex)
							);
				});
			});
		});
	}
	
	////////////////////////////////
	/**
	 * @return the afterChangeMadeAction
	 */
	public Runnable getAfterChangeMadeAction() {
		return afterChangeMadeAction;
	}

	/**
	 * @param afterChangeMadeAction the changeMadeAction to set
	 */
	public void setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(Runnable afterChangeMadeAction, String beforeChangeMadeWarningInforText) {
		this.afterChangeMadeAction = afterChangeMadeAction;
		this.beforeChangeMadeWarningInforText = beforeChangeMadeWarningInforText;
	}
	
	/**
	 * @return the beforeChangeMadeWarningInforText
	 */
	public String getBeforeChangeMadeWarningInforText() {
		return beforeChangeMadeWarningInforText;
	}


	/**
	 * @return the mostRecentlySavedCopyLinkAssignment
	 */
	public Map<V, Map<Integer,Map<V, Integer>>> getMostRecentlySavedCopyLinkAssignment() {
		return mostRecentlySavedCopyLinkAssignment;
	}

	/**
	 * @param mostRecentlySavedCopyLinkAssignment the mostRecentlySavedCopyLinkAssignment to set
	 */
	public void setMostRecentlySavedCopyLinkAssignment(Map<V, Map<Integer,Map<V, Integer>>> mostRecentlySavedCopyLinkAssignment) {
		this.mostRecentlySavedCopyLinkAssignment = mostRecentlySavedCopyLinkAssignment;
	}

	/**
	 * @return the mostRecentlySavedCopyLinkAssignmentFinished
	 */
	public Boolean getMostRecentlySavedCopyLinkAssignmentFinished() {
		return mostRecentlySavedCopyLinkAssignmentFinished;
	}

	/**
	 * @param mostRecentlySavedCopyLinkAssignmentFinished the mostRecentlySavedCopyLinkAssignmentFinished to set
	 */
	public void setMostRecentlySavedCopyLinkAssignmentFinished(Boolean mostRecentlySavedCopyLinkAssignmentFinished) {
		this.mostRecentlySavedCopyLinkAssignmentFinished = mostRecentlySavedCopyLinkAssignmentFinished;
	}
	
	/**
	 * @return the dag
	 */
	public SimpleDirectedGraph<V, E> getDag() {
		return dag;
	}

	
}
