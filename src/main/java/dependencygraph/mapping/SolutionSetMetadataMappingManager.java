package dependencygraph.mapping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import builder.visframe.context.scheme.applier.archive.mapping.GenericGraphMappingBuilder;
import builder.visframe.context.scheme.applier.archive.mapping.MetadataMappingBuilder;
import builder.visframe.context.scheme.applier.archive.mapping.RecordMappingBuilder;
import builder.visframe.context.scheme.applier.archive.mapping.VfTreeMappingBuilder;
import context.project.VisProjectDBContext;
import context.scheme.appliedarchive.TrimmedIntegratedDOSAndCFDGraphUtils;
import context.scheme.appliedarchive.mapping.GenericGraphMappingHelper;
import context.scheme.appliedarchive.mapping.MetadataMapping;
import context.scheme.appliedarchive.mapping.RecordMappingHelper;
import context.scheme.appliedarchive.mapping.VfTreeMappingHelper;
import dependency.dos.integrated.IntegratedDOSGraphEdge;
import dependency.dos.integrated.IntegratedDOSGraphNode;
import dependency.layout.DAGNodeLevelAndOrderAssigner;
import exception.VisframeException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import metadata.DataType;

public class SolutionSetMetadataMappingManager {
	static final double Y_BORDER = 100;
	static final double X_BORDER = 100;
	
	////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	private final SimpleDirectedGraph<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> underlyingIntegratedDOSGraph;
	/**
	 * set of IntegratedDOSGraphNode in the solution set;
	 */
	private final Set<IntegratedDOSGraphNode> selectedSolutionSet;
	/**
	 * 
	 */
	private final TrimmedIntegratedDOSAndCFDGraphUtils trimmedIntegratedDOSAndCFDGraphUtils;
	/////////////////////
	//
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
	private SolutionSetMetadataMappingController controller;
	
	///////fields related with DAG graph layout
	private DAGNodeLevelAndOrderAssigner<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> nodeLevelAndOrderAssigner;
	
	private Map<IntegratedDOSGraphNode, IntegratedDOSGraphNodeManager> DAGNodeManagerMap;
	
	private Map<IntegratedDOSGraphEdge, IntegratedDOSGraphEdgeManager> DAGEdgeManagerMap;
	///////////////////////
	
	private Map<IntegratedDOSGraphNode, MetadataMappingBuilder<?>> solutionSetNodeMetadataMappingBuilderMap;
	/**
	 * 
	 */
//	private Map<IntegratedDOSGraphNode, MappingBuilderWindowManager> solutionSetNodeMappingBuilderWindowManagerMap;


	//////////////////////////
	/**
	 * action to take AFTER a new finished mapping for all nodes in solution set is saved or all nodes' mapping are reset to initial empty value;
	 */
	private Runnable afterChangeMadeAction;
	
	private String beforeChangeMadeWarningInforText;
	
	
	
	/**
	 * the MetadataMappings most recently saved; could be a finished one for all nodes in solution set or one with all MetadataMapping with default empty value;
	 * 
	 * if empty, a clear all action has been taken and no finished assignment has been saved afterwards;
	 */
	private Map<IntegratedDOSGraphNode, MetadataMapping> mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap = new HashMap<>();
	
	/**
	 * whether or not the {@link #mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap} contains a finished complete MetadataMapping for all solution set or not;
	 */
	private Boolean mostRecentlySavedMetadataMappingFinished;
	
	/**
	 * 
	 * @param underlyingIntegratedDOSGraph
	 * @param nodeOrderComparator
	 * @param distBetweenLevels
	 * @param distBetweenNodesOnSameLevel
	 * @param dagNodeInforStringFunction
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SolutionSetMetadataMappingManager(
			VisProjectDBContext hostVisProjectDBContext,
			SimpleDirectedGraph<IntegratedDOSGraphNode, IntegratedDOSGraphEdge> underlyingIntegratedDOSGraph,
			Set<IntegratedDOSGraphNode> selectedSolutionSet,
			TrimmedIntegratedDOSAndCFDGraphUtils trimmedIntegratedDOSAndCFDGraphUtils,
			
			Comparator<IntegratedDOSGraphNode> nodeOrderComparator,
			double distBetweenLevels,
			double distBetweenNodesOnSameLevel,
			Function<IntegratedDOSGraphNode,String> dagNodeInforStringFunction
			) throws SQLException, IOException{
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.underlyingIntegratedDOSGraph = underlyingIntegratedDOSGraph;		
		this.selectedSolutionSet = selectedSolutionSet;
		this.trimmedIntegratedDOSAndCFDGraphUtils = trimmedIntegratedDOSAndCFDGraphUtils;
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
		this.initializeMetadataMappingBuilders();
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
		this.underlyingIntegratedDOSGraph.vertexSet().forEach(v->{
			IntegratedDOSGraphNodeManager manager = new IntegratedDOSGraphNodeManager(this, v);
			manager.setInSolutionSet(this.selectedSolutionSet.contains(v));
			this.DAGNodeManagerMap.put(v, manager);
			
		});
		
		//set the depended and depending DAGNodeManager set for each DAGNodeManager of each node on dag;
		this.underlyingIntegratedDOSGraph.vertexSet().forEach(v->{
			Set<IntegratedDOSGraphNodeManager> dependedNodeManagerSet = new HashSet<>();
			this.underlyingIntegratedDOSGraph.outgoingEdgesOf(v).forEach(e->{
				dependedNodeManagerSet.add(this.DAGNodeManagerMap.get(this.underlyingIntegratedDOSGraph.getEdgeTarget(e)));
			});
			
			this.DAGNodeManagerMap.get(v).setDependedDAGNodeManagerSet(dependedNodeManagerSet);
			
			Set<IntegratedDOSGraphNodeManager> dependingNodeManagerSet = new HashSet<>();
			this.underlyingIntegratedDOSGraph.incomingEdgesOf(v).forEach(e->{
				dependingNodeManagerSet.add(this.DAGNodeManagerMap.get(this.underlyingIntegratedDOSGraph.getEdgeSource(e)));
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
		this.nodeLevelAndOrderAssigner = new DAGNodeLevelAndOrderAssigner<>(this.underlyingIntegratedDOSGraph, this.nodeOrderComparator);
		
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
		this.underlyingIntegratedDOSGraph.edgeSet().forEach(e->{
			IntegratedDOSGraphNode dependingNode = this.underlyingIntegratedDOSGraph.getEdgeSource(e);
			IntegratedDOSGraphNode dependedNode = this.underlyingIntegratedDOSGraph.getEdgeTarget(e);
			
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
	
	//////////////////////////////////////
	/**
	 * initialize the {@link #solutionSetNodeMetadataMappingBuilderMap} by creating the MetadataMappingBuilder for each IntegratedDOSGraphNode in the {@link #selectedSolutionSet};
	 * 
	 * also add the status changed action to each created MetadataMappingBuilder so that whenever the status changes, this manager will react accordingly;
	 *  
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private void initializeMetadataMappingBuilders() throws SQLException, IOException {
		this.solutionSetNodeMetadataMappingBuilderMap = new HashMap<>();
		
		for(IntegratedDOSGraphNode n:this.selectedSolutionSet){
			
			if(n.getMetadataID().getDataType().equals(DataType.RECORD)) {
				RecordMappingHelper helper = new RecordMappingHelper(this.trimmedIntegratedDOSAndCFDGraphUtils, n);
				RecordMappingBuilder builder = new RecordMappingBuilder(this.hostVisProjectDBContext, helper);
				
				//whenever the status of the RecordMappingBuilder is changed, invoke the 
				builder.addStatusChangedAction(
						()->{this.getController().enableFinishButton();}
						);
				
				this.solutionSetNodeMetadataMappingBuilderMap.put(n, builder);
				
			}else {//graph or vftree
				if(n.getMetadataID().getDataType().equals(DataType.vfTREE) && this.trimmedIntegratedDOSAndCFDGraphUtils.vfTreeContainingIntegratedDOSGraphNodeUsedByAtLeastOneOperationsRequiringVfTreeAsInput(n)) {
					//target Metadata is vftree data type and is used as input of at least one vftree-specific operation
					//thus source metadata must be of vftree
					VfTreeMappingHelper helper = new VfTreeMappingHelper(this.trimmedIntegratedDOSAndCFDGraphUtils, n);
					VfTreeMappingBuilder builder = new VfTreeMappingBuilder(this.hostVisProjectDBContext, helper);
					
					//whenever the status of the RecordMappingBuilder is changed, invoke the 
					builder.addStatusChangedAction(
							()->{this.getController().enableFinishButton();}
							);
					
					this.solutionSetNodeMetadataMappingBuilderMap.put(n, builder);
					
				}else {
					GenericGraphMappingHelper helper = new GenericGraphMappingHelper(this.trimmedIntegratedDOSAndCFDGraphUtils, n);
					GenericGraphMappingBuilder builder = new GenericGraphMappingBuilder(this.hostVisProjectDBContext, helper);
					
					//whenever the status of the RecordMappingBuilder is changed, invoke the 
					builder.addStatusChangedAction(
							()->{this.getController().enableFinishButton();}
							);
					
					this.solutionSetNodeMetadataMappingBuilderMap.put(n, builder);
				}
			}
			
		}
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
	 * @return the mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap
	 */
	public Map<IntegratedDOSGraphNode, MetadataMapping> getMostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap() {
		return mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap;
	}

	/**
	 * @param mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap the mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap to set
	 */
	public void setMostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap(
			Map<IntegratedDOSGraphNode, MetadataMapping> mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap) {
		this.mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap = mostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap;
	}

	/**
	 * @return the mostRecentlySavedMetadataMappingFinished
	 */
	public Boolean getMostRecentlySavedMetadataMappingFinished() {
		return mostRecentlySavedMetadataMappingFinished;
	}

	/**
	 * @param mostRecentlySavedMetadataMappingFinished the mostRecentlySavedMetadataMappingFinished to set
	 */
	public void setMostRecentlySavedMetadataMappingFinished(Boolean mostRecentlySavedMetadataMappingFinished) {
		this.mostRecentlySavedMetadataMappingFinished = mostRecentlySavedMetadataMappingFinished;
	}

	
	///////////////////////////////////
	/**
	 * check and return whether all nodes in the solution set have been assigned a valid metadata mapping;
	 * @return
	 */
	public boolean allSolutionSetNodeMappingFinished() {
		for(IntegratedDOSGraphNode node:this.solutionSetNodeMetadataMappingBuilderMap.keySet()) {
			if(!this.solutionSetNodeMetadataMappingBuilderMap.get(node).getCurrentStatus().hasValidValue())
				return false;
		}
		
		return true;
	}
	
	/**
	 * set all mappings to default empty
	 */
	public void clearAllMappings() {
		this.solutionSetNodeMetadataMappingBuilderMap.forEach((node,mappingBuilder)->{
			try {
				mappingBuilder.setToDefaultEmpty(); //TODO
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	
	/**
	 * set the MetadataMapping of all nodes in the solution set as the ones in the given map
	 * @param solutionSetNodeMetadataMappingMap
	 */
	public void setAllMappings(Map<IntegratedDOSGraphNode, MetadataMapping> solutionSetNodeMetadataMappingMap) {
		this.clearAllMappings();
		
		this.solutionSetNodeMetadataMappingBuilderMap.forEach((node,mappingBuilder)->{
			try {
				mappingBuilder.setValue(solutionSetNodeMetadataMappingMap.get(node), false);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * return the map from IntegratedDOSGraphNode to the current MetadataMapping;
	 * note that the MetadataMapping may be finished (non-null valid value) or null value;
	 * @return
	 */
	public Map<IntegratedDOSGraphNode, MetadataMapping> getCurrentIntegratedDOSGraphNodeMetadataMappingMap(){
		Map<IntegratedDOSGraphNode, MetadataMapping> ret = new HashMap<>();
		this.solutionSetNodeMetadataMappingBuilderMap.forEach((node, mappingBuilder)->{
			ret.put(node, mappingBuilder.getCurrentValue());
		});
		return ret;
	}
	
	/**
	 * build and return the map from IntegratedDOSGraphNode in solution set to the created MetadataMapping;
	 * 
	 * if not all mappings are finished, throw exception;
	 * 
	 * @return
	 */
	public Map<IntegratedDOSGraphNode, MetadataMapping> buildIntegratedDOSGraphNodeMetadataMappingMap(){
		if(!this.allSolutionSetNodeMappingFinished())
			throw new VisframeException("not all mappings are finished!");
		
		Map<IntegratedDOSGraphNode, MetadataMapping> ret = new HashMap<>();
		this.solutionSetNodeMetadataMappingBuilderMap.forEach((node, mappingBuilder)->{
			ret.put(node, mappingBuilder.getCurrentValue());
		});
		return ret;
	}
	
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//
		this.getController().setModifiable(modifiable);
		
		//
		for(IntegratedDOSGraphNode node:this.solutionSetNodeMetadataMappingBuilderMap.keySet()){
			this.solutionSetNodeMetadataMappingBuilderMap.get(node).setModifiable(modifiable);
			this.DAGNodeManagerMap.get(node).setModifiable(modifiable);
		}
	}
	
	//////////////////////////
	public SolutionSetMetadataMappingController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(SolutionSetMetadataMappingController.FXML_FILE_DIR_STRING));
			
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
	 * @return the distBetweenLevels
	 */
	public double getDistBetweenLevels() {
		return distBetweenLevels;
	}

	
	/**
	 * @return the dagNodeInforStringFunction
	 */
	public Function<IntegratedDOSGraphNode, String> getDagNodeInforStringFunction() {
		return dagNodeInforStringFunction;
	}
	
	/**
	 * @return the distBetweenNodesOnSameLevel
	 */
	public double getDistBetweenNodesOnSameLevel() {
		return distBetweenNodesOnSameLevel;
	}

	
	/**
	 * @return the solutionSetNodeMetadataMappingBuilderMap
	 */
	public Map<IntegratedDOSGraphNode, MetadataMappingBuilder<?>> getSolutionSetNodeMetadataMappingBuilderMap() {
		return solutionSetNodeMetadataMappingBuilderMap;
	}
}
