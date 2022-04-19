package dependencygraph.cfd.integrated;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import dependency.cfd.integrated.IntegratedCFDGraphEdge;
import dependency.cfd.integrated.IntegratedCFDGraphNode;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

public class IntegratedCFDGraphViewerManager {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	////////////////////////////
	private final SimpleDirectedGraph<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> underlyingIntegratedCFDGraph;
	
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
	private IntegratedCFDGraphViewerController controller;
	
	///////fields related with DAG graph layout
	private DAGNodeLevelAndOrderAssigner<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> nodeLevelAndOrderAssigner;
	
	private Map<IntegratedCFDGraphNode, IntegratedCFDGraphNodeManager> DAGNodeManagerMap;
	
	private Map<IntegratedCFDGraphEdge, IntegratedCFDGraphEdgeManager> DAGEdgeManagerMap;
	
	/**
	 * 
	 * @param underlyingIntegratedCFDGraph
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 */
	public IntegratedCFDGraphViewerManager(
			SimpleDirectedGraph<IntegratedCFDGraphNode, IntegratedCFDGraphEdge> underlyingIntegratedCFDGraph,
			Comparator<IntegratedCFDGraphNode> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<IntegratedCFDGraphNode,String> dagNodeInforStringFunction
			){
		this.underlyingIntegratedCFDGraph = underlyingIntegratedCFDGraph;
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
		this.underlyingIntegratedCFDGraph.vertexSet().forEach(v->{
			this.DAGNodeManagerMap.put(v, new IntegratedCFDGraphNodeManager(this, v));
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.underlyingIntegratedCFDGraph.vertexSet().forEach(v->{
			Set<IntegratedCFDGraphNodeManager> dependedNodeManagerSet = new HashSet<>();
			this.underlyingIntegratedCFDGraph.outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.DAGNodeManagerMap.get(this.underlyingIntegratedCFDGraph.getEdgeTarget(e)));
			});
			
			this.DAGNodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<IntegratedCFDGraphNodeManager> dependingNodeManagerSet = new HashSet<>();
			this.underlyingIntegratedCFDGraph.incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.DAGNodeManagerMap.get(this.underlyingIntegratedCFDGraph.getEdgeSource(e)));
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
		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.underlyingIntegratedCFDGraph, this.nodeOrderComparator);
		
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
		this.underlyingIntegratedCFDGraph.edgeSet().forEach(e->{
			IntegratedCFDGraphNode dependingNode = this.underlyingIntegratedCFDGraph.getEdgeSource(e);
			IntegratedCFDGraphNode dependedNode = this.underlyingIntegratedCFDGraph.getEdgeTarget(e);
			
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

	//////////////////////////
	public IntegratedCFDGraphViewerController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(IntegratedCFDGraphViewerController.FXML_FILE_DIR_STRING));
			
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
}
