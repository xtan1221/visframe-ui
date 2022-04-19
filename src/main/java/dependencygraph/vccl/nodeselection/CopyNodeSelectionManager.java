package dependencygraph.vccl.nodeselection;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import dependency.layout.DAGNodeLevelAndOrderAssigner;
import dependencygraph.vccl.nodeselection.utils.DAGEdgeManager;
import dependencygraph.vccl.nodeselection.utils.DAGNodeCopyManager;
import dependencygraph.vccl.nodeselection.utils.DAGNodeManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import utils.Pair;

/**
 * 
 * input 
 * 		1. a DAG with each node assigned a copy number;
 * 
 * 		2. also a set of copy links between the 
 * 
 * build a copy link graph;
 * 
 * then select a subset of copies;
 * 
 * 
 * =================================================
 * facilitate to select a subset of VSCopies from a VCCL graph in the process of building a VisSchemeBasedVisInstance;
 * 
 * 
 * @author tanxu
 *
 */
public class CopyNodeSelectionManager<V,E> {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	
	///////////////////////////
	private final SimpleDirectedGraph<V,E> DAG;
	
	private final Map<V, Integer> DAGNodeCopyNumberMap;
	
	private final Set<CopyLink<V>> copyLinkSet;
	
	///////////////////////
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
	
	/**
	 * action to take whenever a node copy's selection is changed (from selected to unselected or from unselected to selected);
	 */
	private final Runnable copySelectionChangedAction;
	
	/////////////////////////////
	private CopyNodeSelectionController<V,E> controller;
	
	
	////////////////
	private DAGNodeLevelAndOrderAssigner<V,E> nodeLevelAndOrderAssigner;
	
	private Map<V, DAGNodeManager<V, E>> DAGNodeManagerMap;

	private Map<E, DAGEdgeManager<V,E>> DAGEdgeManagerMap;

	
	///////////////////
	
	/**
	 * 
	 * @param DAG
	 * @param DAGNodeCopyNumberMap
	 * @param copyLinkSet
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 * @param copySelectionChangedAction
	 */
	public CopyNodeSelectionManager(
			SimpleDirectedGraph<V,E> DAG,
			Map<V, Integer> DAGNodeCopyNumberMap,
			Set<CopyLink<V>> copyLinkSet,
			Comparator<V> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<V,String> dagNodeInforStringFunction,
			Runnable copySelectionChangedAction
			) {
		//TODO
		
		
		//
		this.DAG = DAG;
		this.DAGNodeCopyNumberMap = DAGNodeCopyNumberMap;
		this.copyLinkSet = copyLinkSet;
		this.nodeOrderComparator = nodeOrderComparator;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.dagNodeInforStringFunction = dagNodeInforStringFunction;
		this.copySelectionChangedAction = copySelectionChangedAction;
		
		//
		this.createDAGNodeManager();
		
		this.calculateAndSetDAGNodeLayoutProperty();
				
		this.createDAGEdgeManager();
		
		this.layoutDAGNodeOnCanvass();
		
		this.layoutDAGEdgesOnCanvass();
		
		this.addCopyLinkCurves();
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
		this.DAG.vertexSet().forEach(v->{
			this.DAGNodeManagerMap.put(v, new DAGNodeManager<>(this, v, this.DAGNodeCopyNumberMap.get(v), this.dagNodeInforStringFunction));
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.DAG.vertexSet().forEach(v->{
			Set<DAGNodeManager<V,E>> dependedNodeManagerSet = new HashSet<>();
			this.DAG.outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.DAGNodeManagerMap.get(this.DAG.getEdgeTarget(e)));
			});
			
			this.DAGNodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<DAGNodeManager<V,E>> dependingNodeManagerSet = new HashSet<>();
			this.DAG.incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.DAGNodeManagerMap.get(this.DAG.getEdgeSource(e)));
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
		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.DAG, this.nodeOrderComparator);
		
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
		this.DAG.edgeSet().forEach(e->{
			V dependingNode = this.DAG.getEdgeSource(e);
			V dependedNode = this.DAG.getEdgeTarget(e);
			
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
	
	/**
	 * add copy link curves based on {@link #copyLinkSet}
	 * @param dependingNodeCopyManager
	 * @param dependedNodeCopyManager
	 */
	public void addCopyLinkCurves() {
		this.copyLinkSet.forEach(link->{
			DAGNodeCopyManager<V,E> dependingNodeCopyManager = 
					this.DAGNodeManagerMap.get(link.getDependingNode()).getCopyIndexNodeCopyManagerMap().get(link.getDependingNodeCopyIndex());
			DAGNodeCopyManager<V,E> dependedNodeCopyManager = 
					this.DAGNodeManagerMap.get(link.getDependedNode()).getCopyIndexNodeCopyManagerMap().get(link.getDependedNodeCopyIndex());
			
			
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
			copyLinkCurve.setControlY1(dependingNodeCopyManager.getUpperCenterCoordinate().getY()-this.distBetweenLevels/2);
			
			copyLinkCurve.setControlX2(dependedNodeCopyManager.getBottomCenterCoordinate().getX());
			copyLinkCurve.setControlY2(dependedNodeCopyManager.getBottomCenterCoordinate().getY()+this.distBetweenLevels/2);
			
			////////////
			this.getController().getGraphLayoutAnchorPane().getChildren().add(copyLinkCurve);
		});
		
	}
	////////////////////
	/**
	 * 
	 * @return
	 */
	public Set<Pair<V,Integer>> getSelectedDAGNodeCopyIndexPairSet(){
		Set<Pair<V,Integer>> ret = new LinkedHashSet<>();
		
		this.DAGNodeManagerMap.forEach((node,manager)->{
			manager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex,copyManager)->{
				if(copyManager.isSelected())
					ret.add(new Pair<>(node, copyIndex));
			});
		});
		
		return ret;
	}
	
	/**
	 * 
	 * @param selectedDAGNodeCopyIndexPairSet
	 */
	public void setSelectedDAGNodeCopyIndexPairSet(Set<Pair<V,Integer>> selectedDAGNodeCopyIndexPairSet) {
		//first set all to unselected;
		this.clearAll();
		
		//set given copies to selected
		selectedDAGNodeCopyIndexPairSet.forEach(pair->{
			this.DAGNodeManagerMap.get(pair.getFirst()).getCopyIndexNodeCopyManagerMap().get(pair.getSecond()).setSelected(true);
		});
	}
	
	public void selectAll() {
		this.DAGNodeManagerMap.forEach((node,manager)->{
			manager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex,copyManager)->{
				copyManager.setSelected(true);
			});
		});
		if(this.copySelectionChangedAction!=null)
			this.copySelectionChangedAction.run();
	}
	
	public void clearAll() {
		this.DAGNodeManagerMap.forEach((node,manager)->{
			manager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex,copyManager)->{
				copyManager.setSelected(false);
			});
		});
		
		if(this.copySelectionChangedAction!=null)
			this.copySelectionChangedAction.run();
	}
	/////////////////////
	
	public CopyNodeSelectionController<V,E> getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CopyNodeSelectionController.FXML_FILE_DIR_STRING));
			
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

	

	///////////////////
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
	
	///////////////////////////

	
	/**
	 * @return the dAG
	 */
	public SimpleDirectedGraph<V, E> getDAG() {
		return DAG;
	}

	/**
	 * @return the dAGNodeCopyIndexMap
	 */
	public Map<V, Integer> getDAGNodeCopyNumberMap() {
		return DAGNodeCopyNumberMap;
	}

	/**
	 * @return the copyLinkSet
	 */
	public Set<CopyLink<V>> getCopyLinkSet() {
		return copyLinkSet;
	}

	/**
	 * @return the nodeOrderComparator
	 */
	public Comparator<V> getNodeOrderComparator() {
		return nodeOrderComparator;
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
	 * @return the dagNodeInforStringFunction
	 */
	public Function<V, String> getDagNodeInforStringFunction() {
		return dagNodeInforStringFunction;
	}

	/**
	 * @return the nodeLevelAndOrderAssigner
	 */
	public DAGNodeLevelAndOrderAssigner<V, E> getNodeLevelAndOrderAssigner() {
		return nodeLevelAndOrderAssigner;
	}

	/**
	 * @return the dAGNodeManagerMap
	 */
	public Map<V, DAGNodeManager<V, E>> getDAGNodeManagerMap() {
		return DAGNodeManagerMap;
	}

	/**
	 * @return the dAGEdgeManagerMap
	 */
	public Map<E, DAGEdgeManager<V, E>> getDAGEdgeManagerMap() {
		return DAGEdgeManagerMap;
	}

	/**
	 * @return the copySelectionChangedAction
	 */
	public Runnable getCopySelectionChangedAction() {
		return copySelectionChangedAction;
	}
	
}
