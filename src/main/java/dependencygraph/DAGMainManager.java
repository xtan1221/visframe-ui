package dependencygraph;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.SimpleDirectedGraph;

import dependency.layout.DAGNodeLevelAndOrderAssigner;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;


/**
 * manager class for a layout of a DAG on a UI;
 * 
 * @author tanxu
 *
 * @param <V>
 * @param <E>
 */
public class DAGMainManager<V,E> {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	////////////////////////////////////////////////
	private final SimpleDirectedGraph<V,E> dag;
	/**
	 * comparator for vertex on the same level
	 */
	private final Comparator<V> nodeOrderComparator;
	
	/**
	 * 
	 */
	private final DAGNodeManagerFactory<V, ?> nodeManagerFactory;
	/**
	 * 
	 */
	private final DAGEdgeManagerFactory<E> edgeManagerFactory;
	
	/**
	 * distance between two neighboring levels; 
	 * in details, the distance between center's y coordinate of nodes at two neighboring levels;
	 */
	private final double distBetweenLevels;
	
	/**
	 * distance between two neighboring nodes on the same level;
	 * in details, the distance between center's x coordinate of two neighboring nodes at the same level;
	 */
	private final double distBetweenNodesOnSameLevel;
	
	/**
	 * the height of the root JAVAFX Node that contains the DAG node
	 */
	private final double nodeRootFXNodeHeight;
	
	/**
	 * the width of the root JAVAFX Node that contains the DAG node
	 */
	private final double nodeRootFXNodeWidth;
	
	
	////////////////////////////
	private DAGMainController<V,E> controller;
	
//	/**
//	 * 
//	 */
//	private DAGNodeLevelAndOrderAssigner<V,E> nodeLevelAndOrderAssigner;
	
	
	/**
	 * map for the calculated coordinate of the center of each node;
	 */
	private Map<V, Point2D> nodeCenterCoordinateMap;
	private Map<V, DAGNodeManager<V,?>> nodeManagerMap;
	private Map<E, DAGEdgeManager<E>> edgeManagerMap;
	
	
	protected DAGMainManager(
			SimpleDirectedGraph<V,E> dag,
			Comparator<V> nodeOrderComparator,
			DAGNodeManagerFactory<V, ?> nodeManagerFactory,
			DAGEdgeManagerFactory<E> edgeManagerFactory,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			double nodeRootNodeHeight,
			double nodeRootNodeWidth
			){
		CycleDetector<V,E> cd = new CycleDetector<>(dag);
		if(cd.detectCycles())
			throw new IllegalArgumentException("Cycle found in the given DAG!");
		
		
		this.dag = dag;
		this.nodeOrderComparator = nodeOrderComparator;
		this.nodeManagerFactory = nodeManagerFactory;
		this.edgeManagerFactory = edgeManagerFactory;
		this.distBetweenLevels = distBetweenLevels;
		this.distBetweenNodesOnSameLevel = distBetweenNodesOnSameLevel;
		this.nodeRootFXNodeHeight = nodeRootNodeHeight;
		this.nodeRootFXNodeWidth = nodeRootNodeWidth;
		
		
		////////////////////////////
//		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.dag, this.nodeOrderComparator);
		
		this.calculateCoordinate();
		
		this.createNodeManager();
		
		this.createEdgeManager();
		
		this.layout();
	}
	
	/**
	 * first calculate the coordinate for each node on the {@link #dag}
	 * 
	 */
	private void calculateCoordinate() {
		this.nodeCenterCoordinateMap = new HashMap<>();
		
		DAGNodeLevelAndOrderAssigner<V,E> nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.dag, this.nodeOrderComparator);
		
		//first find out the center x for the graph
		double maxLevelX = X_BORDER + (nodeLevelAndOrderAssigner.getLargetNodeNumLevelSize()-1)*this.distBetweenNodesOnSameLevel;
		
		for(int level:nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().keySet()) {
			double levelTotalX = X_BORDER + (nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(level).size()-1)*this.distBetweenNodesOnSameLevel;
			
			double levelShiftX = (maxLevelX-levelTotalX)/2;
			
			double levelY = this.distBetweenLevels*(level-1) + Y_BORDER;
			
			List<V> nodeList = nodeLevelAndOrderAssigner.getLevelIndexOrderedNodeListMap().get(level);
			
			for(int i=0;i<nodeList.size();i++) {
				double nodeX = X_BORDER + i*this.distBetweenNodesOnSameLevel + levelShiftX;
				
				this.nodeCenterCoordinateMap.put(nodeList.get(i), new Point2D(nodeX, levelY));
			}
		}
	}
	
	/**
	 * 
	 */
	private void createNodeManager() {
		this.nodeManagerMap = new HashMap<>();
		
		this.nodeCenterCoordinateMap.forEach((k,v)->{
			this.nodeManagerMap.put(k, this.nodeManagerFactory.build(this, k, v, this.nodeRootFXNodeHeight, this.nodeRootFXNodeWidth));
		});
	}
	
	/**
	 * 
	 */
	private void createEdgeManager() {
		this.edgeManagerMap = new HashMap<>();
		
		this.dag.edgeSet().forEach(e->{
			V sourceNode = this.dag.getEdgeSource(e);
			V sinkNode = this.dag.getEdgeTarget(e);
			
			this.edgeManagerMap.put(
					e, 
					this.edgeManagerFactory.build(
							this,
							e, 
							this.nodeCenterCoordinateMap.get(sourceNode), 
							this.nodeCenterCoordinateMap.get(sinkNode))
			);
			
		});
		
		
	}
	
	
	private void layout() {
		
		//edges
		this.edgeManagerMap.forEach((k,v)->{
			this.getController().getGraphLayoutAnchorPane().getChildren().add(v.getCurve());
		});
		
		//nodes
		this.nodeManagerMap.forEach((k,v)->{
			
			this.getController().getGraphLayoutAnchorPane().getChildren().add(v.getController().getRootNodeContainer());
			
		});
				
				
	}
	
	/**
	 * return the controller;
	 * @return
	 */
	public DAGMainController<V,E> getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGMainController.FXML_FILE_DIR_STRING));
			
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
	
	
	///////////////////////////////

	/**
	 * @return the dag
	 */
	public SimpleDirectedGraph<V, E> getDag() {
		return dag;
	}

	/**
	 * @return the nodeOrderComparator
	 */
	public Comparator<V> getNodeOrderComparator() {
		return nodeOrderComparator;
	}

	/**
	 * @return the nodeManagerFactory
	 */
	public DAGNodeManagerFactory<V, ?> getNodeManagerFactory() {
		return nodeManagerFactory;
	}

	/**
	 * @return the edgeManagerFactory
	 */
	public DAGEdgeManagerFactory<E> getEdgeManagerFactory() {
		return edgeManagerFactory;
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
	 * @return the nodeRootNodeHeight
	 */
	public double getNodeRootFXNodeHeight() {
		return nodeRootFXNodeHeight;
	}

	/**
	 * @return the nodeRootNodeWidth
	 */
	public double getNodeRootFXNodeWidth() {
		return nodeRootFXNodeWidth;
	}

//	/**
//	 * @return the nodeLevelAndOrderAssigner
//	 */
//	public DAGNodeLevelAndOrderAssigner<V, E> getNodeLevelAndOrderAssigner() {
//		return nodeLevelAndOrderAssigner;
//	}

	/**
	 * @return the nodeCenterCoordinateMap
	 */
	public Map<V, Point2D> getNodeCenterCoordinateMap() {
		return nodeCenterCoordinateMap;
	}

	/**
	 * @return the nodeManagerMap
	 */
	public Map<V, DAGNodeManager<V, ?>> getNodeManagerMap() {
		return nodeManagerMap;
	}

	/**
	 * @return the edgeManagerMap
	 */
	public Map<E, DAGEdgeManager<E>> getEdgeManagerMap() {
		return edgeManagerMap;
	}

	
}
