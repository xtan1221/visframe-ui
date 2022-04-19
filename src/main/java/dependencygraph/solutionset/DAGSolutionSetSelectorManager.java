package dependencygraph.solutionset;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;
import dependency.dos.integrated.solutionset.DAGSolutionSetSelector;
import dependency.dos.integrated.solutionset.DAGSolutionSetSelector.RootPath;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import dependencygraph.solutionset.DAGNodeManager.Status;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.ColorUtils;

/**
 * manager class for selection of a solution set from a DAG;
 * 
 * 
 * @author tanxu
 *
 * @param <V>
 * @param <E>
 * @param <CV>
 * @param <CE>
 */
public class DAGSolutionSetSelectorManager<V, E> {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	//////////////////////////////////////
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
	
	private final Function<V,String> dagNodeInforStringFunction;
	
	//////////////////////////////
	private DAGSolutionSetSelectorController<V,E> controller;
	
	
	///////fields related with DAG graph layout and copy node layout
	private DAGNodeLevelAndOrderAssigner<V,E> nodeLevelAndOrderAssigner;
	
	private Map<V, DAGNodeManager<V, E>> DAGNodeManagerMap;

	private Map<E, DAGEdgeManager<V,E>> DAGEdgeManagerMap;
	
	
	/////////////////////////
	//////fields related with copy link assignment
	private DAGSolutionSetSelector<V,E> DAGSolutionSetSelector;
	private Map<RootPath<V>, Color> rootPathAssignedEdgeColorMap;
	//////////////////////////
	/**
	 * action to take AFTER a new finished solution set selection is saved or reset to initial empty value;
	 */
	private Runnable afterChangeMadeAction;
	
	private String beforeChangeMadeWarningInforText;
	
	/**
	 * the set most recently saved; could be a finished solution set or a default empty one;
	 * 
	 * if empty, a clear all action has been taken and no finished assignment has been saved afterwards;
	 */
	private Set<V> mostRecentlySavedSelectedSet = new HashSet<>();
	/**
	 * whether or not the {@link #mostRecentlySavedSelectedSet} is a finished complete solution set or not;
	 */
	private Boolean mostRecentlySavedSelectedSetFinished;
	
	
	/**
	 * constructor
	 * @param dag
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 */
	public DAGSolutionSetSelectorManager(
			SimpleDirectedGraph<V,E> dag,
//			Class<E> DAGEdgeType,
			Comparator<V> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<V,String> dagNodeInforStringFunction
			){
		this.dag = dag;
//		this.DAGEdgeType = DAGEdgeType;
		this.nodeOrderComparator = nodeOrderComparator;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.dagNodeInforStringFunction = dagNodeInforStringFunction;
		//////////////////
		
		this.createDAGNodeManager();
		
		this.calculateAndSetDAGNodeLayoutProperty();
				
		this.createDAGEdgeManager();
		
		///layout edges after nodes;
		this.layoutDAGNodeOnCanvass();
		
		this.layoutDAGEdgesOnCanvass();
		
		//////////////////////////
		this.initializeDAGSolutionSetSelector();
		
		//must be invoked after DAGSolutionSetSelector is initialized
		this.colorRootPathEdges();
		
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
			this.DAGNodeManagerMap.put(v, new DAGNodeManager<>(this, v, this.dagNodeInforStringFunction));
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
	

	private void colorRootPathEdges() {
		this.rootPathAssignedEdgeColorMap = new HashMap<>();
		
		this.DAGSolutionSetSelector.getRootPathSet().forEach(rp->{
			Color color = ColorUtils.randomColor();
			this.rootPathAssignedEdgeColorMap.put(rp, color);
			
			
			for(int i=0;i<rp.getNodeList().size()-1;i++) {
				V dependedNode =rp.getNodeList().get(i);
				V dependingNode = rp.getNodeList().get(i+1);
				
				E edge = this.dag.getEdge(dependingNode, dependedNode);
				
				this.DAGEdgeManagerMap.get(edge).setVisualEffect(color, 5); //stroke width of edge curves on a root path
			}
		});
	}
	
	//////////////////////////
	/**
	 * initialize the {@link #DAGNodeCopyLinkAssigner}
	 * 
	 * also initialize the status of each DAGNodeManager to either {@link Status#SELECTABLE} or {@link Status#UNSELECTABLE}
	 * 
	 */
	private void initializeDAGSolutionSetSelector() {
		this.DAGSolutionSetSelector = new DAGSolutionSetSelector<>(this.dag);
		
		this.DAGNodeManagerMap.forEach((v, manager)->{
			if(this.DAGSolutionSetSelector.getNodeAssignedRootPathSetMap().containsKey(v)) {
				manager.setStatus(Status.SELECTABLE);
			}else {
				manager.setStatus(Status.UNSELECTABLE);
			}
		});
	}
	//////////////////////////
	public DAGSolutionSetSelectorController<V,E> getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGSolutionSetSelectorController.FXML_FILE_DIR_STRING));
			
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

	
	///////////////////////////
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
	 * @return the dAGNodeManagerMap
	 */
	public Map<V, DAGNodeManager<V, E>> getDAGNodeManagerMap() {
		return DAGNodeManagerMap;
	}
	
	
	////////////////////////////
	/**
	 * update status of all DAGNodeManager based on the current {@link #DAGSolutionSetSelector};
	 * 
	 * for nodes not on any RootPath, do nothing;
	 * for other nodes
	 * 		if the node is selected, set to SELECTED
	 * 		
	 * invoked after the selected node set of {@link #DAGSolutionSetSelector} is changed;
	 * 
	 */
	public void updateAllNodeStatus() {
		dag.vertexSet().forEach(v->{
			if(!this.DAGSolutionSetSelector.getNodeAssignedRootPathSetMap().containsKey(v)) {
				//skip nodes not on any RootPath
			}else {
				if(this.DAGSolutionSetSelector.getCurrentlySelectedNodeSet().contains(v)) {//node is currently selected
					this.DAGNodeManagerMap.get(v).setStatus(Status.SELECTED);
				}else {
					//if one rootpath of the node is in ... REPRESENTED
					if(this.DAGSolutionSetSelector.nodeIsRepresented(v))
						this.DAGNodeManagerMap.get(v).setStatus(Status.REPRESENTED);
					else
						this.DAGNodeManagerMap.get(v).setStatus(Status.SELECTABLE);
				}
			}
		});
	}
	
	
	/**
	 * clear all selection in the {@link #DAGSolutionSetSelector}
	 * then update the UI visual effect;
	 */
	public void clearAllSelection() {
		this.DAGSolutionSetSelector.clearAllSelectedNodes();
		this.updateAllNodeStatus();
	}

	/**
	 * @return the dAGSolutionSetSelector
	 */
	public DAGSolutionSetSelector<V,E> getDAGSolutionSetSelector() {
		return DAGSolutionSetSelector;
	}
	
	
	/**
	 * 
	 * @return
	 */
	Set<V> getCurrentlySelectedSet() {
		Set<V> ret = new HashSet<>();
		
		this.DAGSolutionSetSelector.getCurrentlySelectedNodeSet().forEach(n->{
			ret.add(n);
		});
		
		return ret;
	}
	
	/**
	 * set the solution set as the given one;
	 * 
	 * @param solutionSet
	 */
	public void setSelectedSet(Set<V> solutionSet) {
		this.clearAllSelection();
		//select all nodes in the DAGSolutionSetSelector
		solutionSet.forEach(n->{
			this.getDAGSolutionSetSelector().selectNode(n);
		});
		//update the UI
		this.updateAllNodeStatus();
	}
	
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
	 * @return the changeMadeInforText
	 */
	public String getBeforeChangeMadeWarningInforText() {
		return beforeChangeMadeWarningInforText;
	}
	

	/**
	 * @return the mostRecentlySavedSelectedSetFinished
	 */
	public Boolean getMostRecentlySavedSelectedSetFinished() {
		return mostRecentlySavedSelectedSetFinished;
	}

	/**
	 * @param mostRecentlySavedSelectedSetFinished the mostRecentlySavedSelectedSetFinished to set
	 */
	public void setMostRecentlySavedSelectedSetFinished(Boolean mostRecentlySavedSelectedSetFinished) {
		this.mostRecentlySavedSelectedSetFinished = mostRecentlySavedSelectedSetFinished;
	}

	/**
	 * @return the mostRecentlySavedSelectedSet
	 */
	public Set<V> getMostRecentlySavedSelectedSet() {
		return mostRecentlySavedSelectedSet;
	}

	/**
	 * @param mostRecentlySavedSelectedSet the mostRecentlySavedSelectedSet to set
	 */
	public void setMostRecentlySavedSelectedSet(Set<V> mostRecentlySavedSelectedSet) {
		this.mostRecentlySavedSelectedSet = mostRecentlySavedSelectedSet;
	}
	
	/////////////////////////
	
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
		
		this.getDAGNodeManagerMap().forEach((node, manager)->{
			manager.setModifiable(modifiable);
		});
	}

}
