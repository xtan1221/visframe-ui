package builder.visframe.generic.tree.trim.helper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import context.project.VisProjectDBContext;
import generic.tree.VfTree;
import generic.tree.calculation.VfCalculatorTree;
import generic.tree.calculation.VfCalculatorTreeNode;
import generic.tree.reader.projectbased.VfDataTreeReader;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import metadata.MetadataID;
import metadata.graph.vftree.VfTreeDataMetadata;
import rdb.table.data.DataTableColumnName;
import utils.AlertUtils;

/**
 * builder for an interactive VfTree 
 * @author tanxu
 *
 */
public class InteractiveRectangleVfTreeGraphics {
	public static final double ORIGIN_X = 100;
	public static final double ORIGIN_Y = 100;
	
	public static final double DEFAULT_DRAWING_AREA_WIDTH = 1000;
	public static final double DEFAULT_DRAWING_AREA_HEIGHT = 1000;
	
	public static final double ZOOM_FACTOR = 0.05;
	
	
	public static final double DEFAULT_NODE_CIRCLE_RADIUS = 10d;
	public static List<Double> NODE_CIRCLE_RADIUS_SET(){
		List<Double> ret = new ArrayList<>();
		ret.add(5d);
		ret.add(10d);
		ret.add(15d);
		return ret;
	}
	
	
	///////////////////
	private final VfCalculatorTree calculatorTree;
	private final boolean allowingRootNodeMarked;
	private final boolean allowingToMarkMultipleNodes;
	/**
	 * whether node with single child can be selected
	 */
	private final boolean allowingSingleChildNodeMarked;
	private final boolean allowingLeafNodeMarked;
	
	/////////////////////////
	private DoubleProperty drawingAreaHeight;
	private DoubleProperty drawingAreaWidth;
	
	
	private DoubleProperty hRatio;
	private DoubleProperty vRatio;
	
	//////////////////////////
	/**
	 * canvass that holds all the shapes of the tree
	 */
	private AnchorPane canvass;
	
	/**
	 * map from node id to the x and y coordinate of the node
	 */
	private Map<Integer, CollectiveTreeNodeShapes> nodeIDCollectiveTreeNodeShapesMap;
	
	/**
	 * 
	 */
	private Set<Integer> currentlyMarkedNodeIDSet = new LinkedHashSet<>();
	
	
	private boolean bootstrapShowing = true;
	private boolean edgeLengthShowing = true;
	private boolean nodeNonMandatoryAdditionalFeaturesShowing = true;
	private boolean edgeNonMandatoryAdditionalFeaturesShowing = true;
	//
	private double currentNodeCircleRadius = 10d;
	
	
	private Runnable actionWhenCurrentlyMarkedNodeIDSetIsChanged;
	//////////////////////////
	/**
	 * 
	 * @param vfTree
	 */
	public InteractiveRectangleVfTreeGraphics(VfTree vfTree, 
			boolean allowingRootNodeMarked,
			boolean allowingToMarkMultipleNodes, 
			boolean allowingSingleChildNodeMarked,
			boolean allowingLeafNodeMarked){
		this.calculatorTree = new VfCalculatorTree(vfTree);
		this.allowingRootNodeMarked = allowingRootNodeMarked;
		this.allowingToMarkMultipleNodes = allowingToMarkMultipleNodes;
		this.allowingSingleChildNodeMarked = allowingSingleChildNodeMarked;
		this.allowingLeafNodeMarked = allowingLeafNodeMarked;
		
		this.initialize();
		this.build();
		this.addToCanvass();
	}
	
	/**
	 * build a InteractiveRectangleVfTreeGraphics with all non-mandatory additional features of node and edge included in the VfCalculatorTree
	 * @param hostVisProjectDBContext
	 * @param vfTreeMetadataID
	 * @param allowingRootNodeMarked
	 * @param allowingToMarkMultipleNodes
	 * @param allowingSingleChildNodeMarked
	 * @param allowingLeafNodeMarked
	 * @throws SQLException
	 */
	public InteractiveRectangleVfTreeGraphics(VisProjectDBContext hostVisProjectDBContext, MetadataID vfTreeMetadataID, 
			boolean allowingRootNodeMarked, boolean allowingToMarkMultipleNodes, boolean allowingSingleChildNodeMarked, boolean allowingLeafNodeMarked) throws SQLException {
		
		VfTreeDataMetadata vfTree = (VfTreeDataMetadata) hostVisProjectDBContext.getHasIDTypeManagerController().getMetadataManager().lookup(vfTreeMetadataID);
		
		LinkedHashSet<DataTableColumnName> nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
	    nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded.addAll(vfTree.getGraphVertexFeature().getNonMandatoryAdditionalColumnNameSet());
	    LinkedHashSet<DataTableColumnName> nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
	    nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded.addAll(vfTree.getGraphEdgeFeature().getNonMandatoryAdditionalColumnNameSet());
		
	    VfDataTreeReader reader = new VfDataTreeReader(
	    		hostVisProjectDBContext, 
	    		vfTreeMetadataID, 
	    		nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded, 
	    		nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded);
	    
	    reader.perform();
	    
	    this.calculatorTree = new VfCalculatorTree(reader);
	    this.allowingRootNodeMarked = allowingRootNodeMarked;
		this.allowingToMarkMultipleNodes = allowingToMarkMultipleNodes;
		this.allowingSingleChildNodeMarked = allowingSingleChildNodeMarked;
		this.allowingLeafNodeMarked = allowingLeafNodeMarked;
		
		this.initialize();
		this.build();
		this.addToCanvass();
	}
	
	
	
	/**
	 * @return the calculatorTree
	 */
	public VfCalculatorTree getCalculatorTree() {
		return calculatorTree;
	}
	

	public boolean isAllowingRootNodeMarked() {
		return allowingRootNodeMarked;
	}
	/**
	 * @return the allowingToMarkMultipleNodes
	 */
	public boolean isAllowingToMarkMultipleNodes() {
		return allowingToMarkMultipleNodes;
	}

	public boolean isAllowingSingleChildNodeMarked() {
		return allowingSingleChildNodeMarked;
	}

	
	public boolean isAllowingLeafNodeMarked() {
		return allowingLeafNodeMarked;
	}
	
	//////////////////
	/**
	 * 
	 * @param actionWhenCurrentlyMarkedNodeIDSetIsChanged
	 */
	public void setActionWhenCurrentlyMarkedNodeIDSetIsChanged(Runnable actionWhenCurrentlyMarkedNodeIDSetIsChanged) {
		this.actionWhenCurrentlyMarkedNodeIDSetIsChanged = actionWhenCurrentlyMarkedNodeIDSetIsChanged;
	}
	
	
	////////////////
	public void setDrawingAreaHeight(double height) {
		this.drawingAreaHeight.set(height);
	}
	public void setDrawingAreaWidth(double width) {
		this.drawingAreaWidth.set(width);
	}
	
	////////////////////////////
	public AnchorPane getCanvass() {
		return this.canvass;
	}
	
	////////////////////////////
	/**
	 * mark the node of the given id
	 * @param nodeID
	 */
	public void markTreeNode(int nodeID) {
		
		//if the given node is already marked, do nothing
		if(this.currentlyMarkedNodeIDSet.contains(nodeID)) {
			return;
		}
		
		if(!this.isAllowingRootNodeMarked()&&this.calculatorTree.getNodeIDMap().get(nodeID).getParentNodeID()==null) {
			AlertUtils.popAlert("warning", "Cannot select root node!");
			return;
		}
		
		if(!this.allowingSingleChildNodeMarked && this.calculatorTree.getNodeIDMap().get(nodeID).getChildrenNodeSiblingOrderIndexMap().size()==1) {
			AlertUtils.popAlert("warning", "Cannot select node with a single child!");
			return;
		}
		
		if(!this.allowingLeafNodeMarked && this.calculatorTree.getNodeIDMap().get(nodeID).isLeaf()) {
			AlertUtils.popAlert("warning", "Cannot select leaf node!");
			return;
		}
		
		
		//there is a currently marked node and not allowing multiple nodes
		//need to clear the mark of it
		if(!this.currentlyMarkedNodeIDSet.isEmpty()&&!this.isAllowingToMarkMultipleNodes()) {
			int id = this.currentlyMarkedNodeIDSet.iterator().next();
			this.nodeIDCollectiveTreeNodeShapesMap.get(id).setToUnMarkedStatus();
			this.currentlyMarkedNodeIDSet.remove(id);
		}
		
		//add the new node
		this.currentlyMarkedNodeIDSet.add(nodeID);
		this.nodeIDCollectiveTreeNodeShapesMap.get(nodeID).setToMarkedStatus();
		
		
		//update the UI if needed
		if(this.actionWhenCurrentlyMarkedNodeIDSetIsChanged!=null) {
			this.actionWhenCurrentlyMarkedNodeIDSetIsChanged.run();
		}
	}
	
	
	/**
	 * remove the mark of the given node id
	 * @param nodeID
	 */
	public void clearTreeNodeMark(int nodeID) {
		if(this.currentlyMarkedNodeIDSet.contains(nodeID)) {
			this.nodeIDCollectiveTreeNodeShapesMap.get(nodeID).setToUnMarkedStatus();
			
			this.currentlyMarkedNodeIDSet.remove(nodeID);
		}
		
		//update the UI if needed
		if(this.actionWhenCurrentlyMarkedNodeIDSetIsChanged!=null) {
			this.actionWhenCurrentlyMarkedNodeIDSetIsChanged.run();
		}
	}
	
	/**
	 * remove the mark of all nodes
	 */
	public void clearTreeNodeMarkSet() {
		this.currentlyMarkedNodeIDSet.forEach(e->{
			this.nodeIDCollectiveTreeNodeShapesMap.get(e).setToUnMarkedStatus();
		});
		
		//
		this.currentlyMarkedNodeIDSet.clear();
		
		//update the UI if needed
		if(this.actionWhenCurrentlyMarkedNodeIDSetIsChanged!=null) {
			this.actionWhenCurrentlyMarkedNodeIDSetIsChanged.run();
		}
	}
	

	/**
	 * @return the currentlyMarkedNodeID
	 */
	public Set<Integer> getCurrentlyMarkedNodeIDSet() {
		
		return this.currentlyMarkedNodeIDSet;
	}
	
	
	/////////////////////////////
	public void showBootstrap() {
		if(!this.bootstrapShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.bootstrapText!=null)
					this.canvass.getChildren().add(e.bootstrapText);
			});
			
			this.bootstrapShowing = true;
		}
	}
	
	public void hideBootstrap() {
		if(this.bootstrapShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.bootstrapText!=null)
					this.canvass.getChildren().remove(e.bootstrapText);
			});
			this.bootstrapShowing = false;
		}
	}
	
	public void showEdgeLength() {
		if(!this.edgeLengthShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.lengthText!=null)
					this.canvass.getChildren().add(e.lengthText);
			});
			
			this.edgeLengthShowing = true;
		}
	}
	
	public void hideEdgeLength() {
		if(this.edgeLengthShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.lengthText!=null)
					this.canvass.getChildren().remove(e.lengthText);
			});
			this.edgeLengthShowing = false;
		}
	}
	
	
	public void showNodeNonMandatoryAdditionalFeatures() {
		if(!this.nodeNonMandatoryAdditionalFeaturesShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.nodeNonMandatoryAdditionalFeatureValueText!=null)
					this.canvass.getChildren().add(e.nodeNonMandatoryAdditionalFeatureValueText);
			});
			
			this.nodeNonMandatoryAdditionalFeaturesShowing = true;
		}
	}
	public void hideNodeNonMandatoryAdditionalFeatures() {
		if(this.nodeNonMandatoryAdditionalFeaturesShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.nodeNonMandatoryAdditionalFeatureValueText!=null)
					this.canvass.getChildren().remove(e.nodeNonMandatoryAdditionalFeatureValueText);
			});
			this.nodeNonMandatoryAdditionalFeaturesShowing = false;
		}
	}
	
	public void showEdgeNonMandatoryAdditionalFeatures() {
		if(!this.edgeNonMandatoryAdditionalFeaturesShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.edgeNonMandatoryAdditionalFeatureValueText!=null)
					this.canvass.getChildren().add(e.edgeNonMandatoryAdditionalFeatureValueText);
			});
			
			this.edgeNonMandatoryAdditionalFeaturesShowing = true;
		}
	}
	public void hideEdgeNonMandatoryAdditionalFeatures() {
		if(this.edgeNonMandatoryAdditionalFeaturesShowing) {
			this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
				if(e.edgeNonMandatoryAdditionalFeatureValueText!=null)
					this.canvass.getChildren().remove(e.edgeNonMandatoryAdditionalFeatureValueText);
			});
			this.edgeNonMandatoryAdditionalFeaturesShowing = false;
		}
	}
	

	/**
	 * @return the bootstrapShowing
	 */
	public boolean isBootstrapShowing() {
		return bootstrapShowing;
	}


	/**
	 * @return the edgeLengthShowing
	 */
	public boolean isEdgeLengthShowing() {
		return edgeLengthShowing;
	}


	/**
	 * @return the nodeNonMandatoryAdditionalFeaturesShowing
	 */
	public boolean isNodeNonMandatoryAdditionalFeaturesShowing() {
		return nodeNonMandatoryAdditionalFeaturesShowing;
	}


	/**
	 * @return the edgeNonMandatoryAdditionalFeaturesShowing
	 */
	public boolean isEdgeNonMandatoryAdditionalFeaturesShowing() {
		return edgeNonMandatoryAdditionalFeaturesShowing;
	}

	//////////////////////////////
	public void zoomIn() {
		this.drawingAreaHeight.set(this.drawingAreaHeight.get()/(1+ZOOM_FACTOR));
		this.drawingAreaWidth.set(this.drawingAreaWidth.get()/(1+ZOOM_FACTOR));
	}
	
	public void zoomOut() {
		this.drawingAreaHeight.set(this.drawingAreaHeight.get()*(1+ZOOM_FACTOR));
		this.drawingAreaWidth.set(this.drawingAreaWidth.get()*(1+ZOOM_FACTOR));
	}
	
	public void zoomInX() {
		this.drawingAreaWidth.set(this.drawingAreaWidth.get()/(1+ZOOM_FACTOR));
	}
	
	public void zoomOutX() {
		this.drawingAreaWidth.set(this.drawingAreaWidth.get()*(1+ZOOM_FACTOR));
	}
	
	public void zoomInY() {
		this.drawingAreaHeight.set(this.drawingAreaHeight.get()/(1+ZOOM_FACTOR));
	}
	
	public void zoomOutY() {
		this.drawingAreaHeight.set(this.drawingAreaHeight.get()*(1+ZOOM_FACTOR));
	}
	
	public void setToDefaultSize() {
		this.drawingAreaHeight.set(DEFAULT_DRAWING_AREA_HEIGHT);
		this.drawingAreaWidth.set(DEFAULT_DRAWING_AREA_WIDTH);
	}
	//////////////////////////////////////////
	
	/**
	 * 
	 * @param radius
	 */
	public void setNodeEllipseRadius(double radius) {
		if(!NODE_CIRCLE_RADIUS_SET().contains(radius)) {
			throw new IllegalArgumentException("given node ellipse radius value is not supported!");
		}
		
		if(this.currentNodeCircleRadius==radius) {
			return;
		}
		
		this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
			e.nodeEllipse.setRadiusX(radius);
			e.nodeEllipse.setRadiusY(radius);
		});
		
		this.currentNodeCircleRadius=radius;
	}
	
	public void setToDefaultNodeEllipseRadius() {
		this.setNodeEllipseRadius(DEFAULT_NODE_CIRCLE_RADIUS);
	}
	
	


	/**
	 * @return the currentNodeCircleRadius
	 */
	public double getCurrentNodeCircleRadius() {
		return currentNodeCircleRadius;
	}

	//////////////////////////////////
	/**
	 * set whether the tree to be interactive or not
	 * @param interactive
	 */
	public void setTreeInteractive(boolean interactive) {
		this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
			e.setInterative(interactive);
		});
	}
	
	
	
	//////////////////////////////////
	/**
	 * set the tree graphics to default status;
	 * 
	 */
	public void setToDefaultStatus() {
		//no marked node
		this.clearTreeNodeMarkSet();
		
		//tree size
		this.setToDefaultSize();
		
		//node size
		this.setToDefaultNodeEllipseRadius();
		
		
		//
		this.showBootstrap();
		this.showEdgeLength();
		this.showEdgeNonMandatoryAdditionalFeatures();
		this.showNodeNonMandatoryAdditionalFeatures();
		
		//interactive
		this.setTreeInteractive(true);
	}
	
	///////////////////////////////////
	/**
	 * set up the binding between drawing area size and the length ratios;
	 */
	private void initialize() {
		this.drawingAreaHeight = new SimpleDoubleProperty();
		this.drawingAreaHeight.set(DEFAULT_DRAWING_AREA_HEIGHT);
		this.drawingAreaWidth = new SimpleDoubleProperty();
		this.drawingAreaWidth.set(DEFAULT_DRAWING_AREA_WIDTH);
		
		
		this.hRatio = new SimpleDoubleProperty();
		
		if(this.calculatorTree.containsNullValuedBranchLength()) {
			this.hRatio.bind(this.drawingAreaWidth.divide(this.calculatorTree.getLongestEdgeNumBetweenRootAndLeaf()));
		}else {
			this.hRatio.bind(this.drawingAreaWidth.divide(this.calculatorTree.getLongestDistBetweenRootAndLeaf()));
		}
		
		this.vRatio = new SimpleDoubleProperty();
		this.vRatio.bind(this.drawingAreaHeight.divide(this.calculatorTree.getLeafNum()));
		
		
	}
	
	private void build() {
		this.nodeIDCollectiveTreeNodeShapesMap = new HashMap<>();
		
		//initialize CollectiveTreeNodeShapes for each node
		for(int id:this.calculatorTree.getNodeIDMap().keySet()) {
			
			VfCalculatorTreeNode node = this.calculatorTree.getNodeIDMap().get(id);
			
			VfCalculatorTreeNode parent = node.getParentNodeID()==null?null: this.calculatorTree.getNodeIDMap().get(node.getParentNodeID());
			
			this.nodeIDCollectiveTreeNodeShapesMap.put(node.getID(), new CollectiveTreeNodeShapes(node, parent));
		}
		
		
		//build all shapes
		this.nodeIDCollectiveTreeNodeShapesMap.forEach((k,v)->{
			v.buildAll();
		});
		
	}
	
	private void addToCanvass() {
		this.canvass = new AnchorPane();
		this.nodeIDCollectiveTreeNodeShapesMap.values().forEach(e->{
			
			this.canvass.getChildren().addAll(e.getAllNonNullShape());
		});
	}
	///////////////////////////////////
	private class CollectiveTreeNodeShapes{
		private final VfCalculatorTreeNode node;
		private final VfCalculatorTreeNode parent;
		
		private final DoubleProperty x;
		private final DoubleProperty y;
		
		
		//////////////////////////////////
		private Line hLine;
		private Line vLine;
		private Ellipse nodeEllipse;
		
		private Text bootstrapText;
		private Text lengthText;
		
//		private Text leafText;
		
		private Text nodeNonMandatoryAdditionalFeatureValueText;
		private Text edgeNonMandatoryAdditionalFeatureValueText;
		
		
		/**
		 * initialize by set up the x and y coordinate
		 * @param child
		 * @param parent
		 */
		CollectiveTreeNodeShapes(VfCalculatorTreeNode child, VfCalculatorTreeNode parent){
			this.node = child;
			this.parent = parent;
			
			this.x = new SimpleDoubleProperty();
			if(calculatorTree.containsNullValuedBranchLength()) {
				x.bind(hRatio.multiply(node.getEdgeNumToRoot()).add(ORIGIN_X));
			}else {
				x.bind(hRatio.multiply(node.getDistanceToRootNode()).add(ORIGIN_X));
			}
			
			this.y = new SimpleDoubleProperty();
			y.bind(vRatio.multiply(node.getLeafIndex()).add(ORIGIN_Y));
			
		}
		
		/**
		 * build all other shapes
		 */
		void buildAll() {
			this.buildNodeEllipse();
			this.buildHLine();
			this.buildVLine();
			this.buildBootstrapText();
			this.buildLengthText();
//			this.buildLeafText();
			this.buildNodeNonMandatoryAdditionalFeatureValueText();
			this.buildEdgeOtherMandatoryFeatureValueText();
		}
		
		
		void buildNodeEllipse() {
			this.nodeEllipse = new Ellipse();
			this.nodeEllipse.centerXProperty().bind(this.x);
			this.nodeEllipse.centerYProperty().bind(this.y);
			
			this.nodeEllipse.setRadiusX(10);
			this.nodeEllipse.setRadiusY(10);
			
			if(this.parent==null) {//root node's stroke is blue
				this.nodeEllipse.setStroke(Color.BLUE);
			}else {
				this.nodeEllipse.setStroke(Color.BLACK);
			}
			
			this.nodeEllipse.setStyle("-fx-fill: transparent;");
			
		}
		
		
		void buildHLine() {
			if(this.parent!=null) {
				DoubleProperty hLineStartX = new SimpleDoubleProperty();
				hLineStartX.bind(nodeIDCollectiveTreeNodeShapesMap.get(parent.getID()).x); //start x is the same with parent node's x
				DoubleProperty hLineStartY = new SimpleDoubleProperty();
				hLineStartY.bind(y);//start y is the same with child node's y
				DoubleProperty hLineEndX = new SimpleDoubleProperty();
				hLineEndX.bind(x);//end x is the same with child node's x
				DoubleProperty hLineEndY = new SimpleDoubleProperty();
				hLineEndY.bind(hLineStartY); //end y is the same with start y;
				
				
				this.hLine = new Line();
				hLine.startXProperty().bind(hLineStartX);
				hLine.startYProperty().bind(hLineStartY);
				hLine.endXProperty().bind(hLineEndX);
				hLine.endYProperty().bind(hLineEndY);
				
			}
		}
		
		
		void buildVLine() {
			if(this.parent!=null) {
				DoubleProperty vLineStartX = new SimpleDoubleProperty();
				vLineStartX.bind(nodeIDCollectiveTreeNodeShapesMap.get(parent.getID()).x);//start x is the same with parent node's x
				DoubleProperty vLineStartY = new SimpleDoubleProperty();
				vLineStartY.bind(nodeIDCollectiveTreeNodeShapesMap.get(parent.getID()).y);//start y is the same with parent node's y
				DoubleProperty vLineEndX = new SimpleDoubleProperty();
				vLineEndX.bind(vLineStartX);////end x is the same with start x
				DoubleProperty vLineEndY = new SimpleDoubleProperty();
				vLineEndY.bind(y);//end y is the same with child node's y;
				
				this.vLine = new Line();
				vLine.startXProperty().bind(vLineStartX);
				vLine.startYProperty().bind(vLineStartY);
				vLine.endXProperty().bind(vLineEndX);
				vLine.endYProperty().bind(vLineEndY);
				
			}
		}
		
		
		void buildBootstrapText() {
			if(this.node.getBootstrapValueToParentNode()!=null) {
				this.bootstrapText = new Text(Double.toString(this.node.getBootstrapValueToParentNode()));
				
				////note that Text's layoutXProperty and layoutYProperty are already bound to a property, thus cannot be directly bound to another;
				////use xProperty and yProperty instead
				this.bootstrapText.xProperty().bind(x.add(this.nodeEllipse.radiusXProperty()));
				this.bootstrapText.yProperty().bind(y);
//				
			}
		}
		
		void buildLengthText() {
			if(this.node.getDistanceToParentNode()!=null) {
				this.lengthText = new Text(Double.toString(this.node.getDistanceToParentNode()));
				
				this.lengthText.xProperty().bind((nodeIDCollectiveTreeNodeShapesMap.get(parent.getID()).x.add(x)).divide(2));
				this.lengthText.yProperty().bind(y);
				
			}
		}
		
		
		void buildNodeNonMandatoryAdditionalFeatureValueText() {
			
			StringBuilder sb = new StringBuilder();
			this.node.getNonMandatoryAdditionalNodeFeatureColumnNameValueStringMap().forEach((k,v)->{
				if(sb.toString().isEmpty())
					sb.append(k.getStringValue()).append("=").append(v==null?"N/A":v);
				else
					sb.append("\\n").append(k.getStringValue()).append("=").append(v==null?"N/A":v);
			});
			
			this.nodeNonMandatoryAdditionalFeatureValueText = new Text(sb.toString());
			this.nodeNonMandatoryAdditionalFeatureValueText.xProperty().bind(x);
			this.nodeNonMandatoryAdditionalFeatureValueText.yProperty().bind(y);
		}
		
		void buildEdgeOtherMandatoryFeatureValueText() {
			if(this.node.getNonMandatoryAdditionalFeatureColumnNameValueStringMapOfEdgeToParent()!=null) {
				String content = "";
				this.node.getNonMandatoryAdditionalFeatureColumnNameValueStringMapOfEdgeToParent().forEach((k,v)->{
					if(content.isEmpty())
						content.concat(k.getStringValue()).concat("=").concat(v==null?"N/A":v);
					else
						content.concat("\\n").concat(k.getStringValue()).concat("=").concat(v==null?"N/A":v);
				});
				
				this.edgeNonMandatoryAdditionalFeatureValueText = new Text(content);
				this.edgeNonMandatoryAdditionalFeatureValueText.xProperty().bind(x);
				this.edgeNonMandatoryAdditionalFeatureValueText.yProperty().bind(y);
			}
		}
		
		/////////////////////////////
		Set<Shape> getAllNonNullShape(){
			
			Set<Shape> ret= new HashSet<>();
			
			ret.add(this.nodeEllipse);
			
			if(this.parent!=null) {
				ret.add(this.hLine);
				ret.add(this.vLine);
			}
			
			if(this.node.getBootstrapValueToParentNode()!=null) {
				ret.add(this.bootstrapText);
			}
			
			if(this.node.getDistanceToParentNode()!=null) {
				ret.add(this.lengthText);
			}
			
			ret.add(this.nodeNonMandatoryAdditionalFeatureValueText);
			
			if(this.node.getNonMandatoryAdditionalFeatureColumnNameValueStringMapOfEdgeToParent()!=null)
				ret.add(this.edgeNonMandatoryAdditionalFeatureValueText);
			
			
			return ret;
		}
		
		////////////////////////////////////////
		void setInterative(boolean interactive) {
			if(interactive) {
				//node
//				if(this.parent!=null) {//ellipse is only selectable for non-root node!!!!!!!!!!
					
					this.nodeEllipse.setOnMouseEntered(e->{
						this.nodeEllipse.setStyle("-fx-fill: red;");
					});
					
					this.nodeEllipse.setOnMouseExited(e->{
						if(!currentlyMarkedNodeIDSet.contains(this.node.getID()))//only change to black if the node is not the currently marked one
							this.nodeEllipse.setStyle("-fx-fill: transparent;");
					});
					
					this.nodeEllipse.setOnMouseClicked(e->{
						if(getCurrentlyMarkedNodeIDSet().contains(this.node.getID()))	
							clearTreeNodeMark(this.node.getID());
						else
							markTreeNode(this.node.getID());
					});
//				}else {
//					
//					
//				}
				
				
				
			}else {
				//node
				if(this.parent!=null) {//ellipse is only selectable for non-root node!!!!!!!!!!
					
					this.nodeEllipse.setOnMouseEntered(null);
					
					this.nodeEllipse.setOnMouseExited(null);
					
					this.nodeEllipse.setOnMouseClicked(null);
				}
				
			}
		}
		
		
		
		
		///////////////////////////////////////
		void setToMarkedStatus() {
			this.nodeEllipse.setStyle("-fx-fill:red");
			if(this.node.getParentNodeID()!=null) {
				this.hLine.setStrokeWidth(10);
				this.hLine.setStyle("-fx-stroke:red");
				this.vLine.setStrokeWidth(10);
				this.vLine.setStyle("-fx-stroke:red");
			}
		}
		
		void setToUnMarkedStatus() {
			this.nodeEllipse.setStyle("-fx-fill:transparent");
			if(this.node.getParentNodeID()!=null) {
				this.hLine.setStrokeWidth(1);
				this.hLine.setStyle("-fx-stroke:black");
				this.vLine.setStrokeWidth(1);
				this.vLine.setStyle("-fx-stroke:black");
			}
		}
		
		
	}


}
