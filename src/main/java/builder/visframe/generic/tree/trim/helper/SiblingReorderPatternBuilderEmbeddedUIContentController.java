package builder.visframe.generic.tree.trim.helper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import generic.tree.calculation.VfCalculatorTreeNode;
import generic.tree.trim.helper.PositionOnTree;
import generic.tree.trim.helper.SiblingReorderPattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import rdb.table.data.DataTableColumnName;
import utils.AlertUtils;

public class SiblingReorderPatternBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<SiblingReorderPattern> {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/generic/tree/trim/helper/SiblingReorderPatternBuilderEmbeddedUIContent.fxml";
	
	/**
	 * tree which contains the information for currently selected position
	 */
	private InteractiveRectangleVfTreeGraphics tree;
	
	
	private LinkedHashMap<Integer, ReorderPatternVBox> selectedParentNodeIDChildrenNodeReorderPatternVBoxMap;
	
	/**
	 * set the tree;
	 * 
	 * need to first clear any existing tree and set UI to default empty;
	 * 
	 * then set up involved UI components
	 * @param tree
	 */
	public void setTree(InteractiveRectangleVfTreeGraphics tree) {
		//if there is a current tree, reset the UI to default empty
		this.tree = tree;
		
		
		if(this.tree!=null) {
			this.tree.setActionWhenCurrentlyMarkedNodeIDSetIsChanged(()->{
				//first remove those not marked
				Set<Integer> parentNodeIDSetToBeRemoved = new HashSet<>();
				for(int parentNodeID:this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.keySet()) {
					if(!this.tree.getCurrentlyMarkedNodeIDSet().contains(parentNodeID)) {
						parentNodeIDSetToBeRemoved.add(parentNodeID);
					}
				}
				parentNodeIDSetToBeRemoved.forEach(e->{
					this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.remove(e);
				});
				
				
				//then add those newly marked
				for(int nodeID:this.tree.getCurrentlyMarkedNodeIDSet()) {
					if(!this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.containsKey(nodeID)) {
						
						Map<Integer, Integer> childrenNodeIDOriginalOrderIndexMap = new LinkedHashMap<>();
						
						Map<Integer, VfCalculatorTreeNode> childrenNodeSiblingOrderIndexMap = 
								this.tree.getCalculatorTree().getNodeIDMap().get(nodeID).getChildrenNodeSiblingOrderIndexMap();
						
						
						for(int childNodeOrderIndex:childrenNodeSiblingOrderIndexMap.keySet()) {
							
							childrenNodeIDOriginalOrderIndexMap.put(
									childrenNodeSiblingOrderIndexMap.get(childNodeOrderIndex).getID(), 
									childNodeOrderIndex);
						}
						
						this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.put(nodeID, new ReorderPatternVBox(nodeID, childrenNodeIDOriginalOrderIndexMap));
					}
				}
				
				this.updateNodeReorderVBoxStatus();
				
				//try to build and update the value
//				try{
//					SiblingReorderPattern type = this.build();
//					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(type);
//					this.getOwnerNodeBuilder().setUIVisualEffect(true);
//				}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//					//skip
//					System.out.println("exception thrown, skip update:"+ex.getMessage());
//					this.getOwnerNodeBuilder().setUIVisualEffect(false);
//				}
				
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			});
		}
		
		this.setUIToDefaultEmptyStatus();
		
		this.updateNodeFeatureChoiceBoxStatus();
		this.updateTreeNodeListViewStatus();
		this.updateNodeReorderVBoxStatus();
		this.updateDisplayOptionHBoxStatus();
		
		this.updateNodeCircleSizeChoiceBoxStatus();
		this.updateInteractiveTreeScrollPaneStatus();
	}
	
	/**
	 * set the {@link #nodeFeatureChoiceBox} to show the node features of current tree
	 */
	private void updateNodeFeatureChoiceBoxStatus() {
		
		if(this.tree!=null) {
			this.nodeFeatureChoiceBox.getItems().addAll(
					this.tree.getCalculatorTree().getVfTreeNodeFeature().getAllFeaturesColumnNameSet());
		}
	}
	
	private void updateTreeNodeListViewStatus() {
		
		if(this.tree!=null) {
			this.treeNodeListView.getItems().addAll(this.tree.getCalculatorTree().getNodeIDMap().values());
		}
	}
	
	/**
	 * clear the nodeReorderVBox, then add all in selectedParentNodeIDChildrenNodeReorderPatternVBoxMap
	 */
	private void updateNodeReorderVBoxStatus(){
		if(this.tree!=null) {
			this.nodeReorderVBox.getChildren().clear();
			
			this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.forEach((k,v)->{
				this.nodeReorderVBox.getChildren().add(v.containerVBox);
			});
		}
	}
	
	private void updateNodeCircleSizeChoiceBoxStatus() {
		if(this.tree!=null) 
			this.nodeCircleSizeChoiceBox.setValue(this.tree.getCurrentNodeCircleRadius());
	}
	
	private void updateDisplayOptionHBoxStatus() {
		if(this.tree!=null) {
			showBootstrapCheckBox.setSelected(this.tree.isBootstrapShowing());
			
		}
		
	}
	
	
	private void updateInteractiveTreeScrollPaneStatus() {
		if(this.tree!=null)
			this.interactiveTreeScrollPane.setContent(this.tree.getCanvass());
		else
			this.interactiveTreeScrollPane.setContent(null);
	}
	
	
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		this.setupTreeNodeListView();
		
//		this.setupSelectedPositionVBox();
		
		
		this.setupNodeCircleSizeChoiceBox();
		
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	/**
	 * set up the cell factory and event listeners of {@link #treeNodeListView};
	 * 
	 */
	private void setupTreeNodeListView() {
		this.treeNodeListView.setCellFactory(new Callback<ListView<VfCalculatorTreeNode>, ListCell<VfCalculatorTreeNode>>() {

		@Override
		public ListCell<VfCalculatorTreeNode> call(ListView<VfCalculatorTreeNode> param) {
		    ListCell<VfCalculatorTreeNode> cell = new ListCell<>() {
		
		        @Override
		        protected void updateItem(VfCalculatorTreeNode item, boolean empty) {
		            super.updateItem(item, empty);
		            if (item != null) {
		                setText(Integer.toString(item.getID()));
		            } else {
		                setText("");
		                }
		            }
		        };
		        return cell;
		    }
		});
		
		this.treeNodeListView.setOnMouseClicked(e->{
			if(this.treeNodeListView.getSelectionModel().getSelectedItem()!=null) {
				this.tree.markTreeNode(this.treeNodeListView.getSelectionModel().getSelectedItem().getID());
			}
		});
		
	}
	
	
	
	private void setupNodeCircleSizeChoiceBox() {
		this.nodeCircleSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
			if(this.tree!=null) {
				if(this.nodeCircleSizeChoiceBox.getSelectionModel().getSelectedItem()!=null) {
					this.tree.setNodeEllipseRadius(this.nodeCircleSizeChoiceBox.getSelectionModel().getSelectedItem());
				}
			}else {
				AlertUtils.popAlert("Warning", "No tree is currently showing!");
			}
		});
	}
	
	
	///////////////////////////////////////////
	@Override
	public SiblingReorderPatternBuilder getOwnerNodeBuilder() {
		return (SiblingReorderPatternBuilder) this.ownerNodeBuilder;
	}
	
	@Override
	public Parent getRootParentNode() {
		return rootTiltledPane;
	}
	
	/**
	 * try to build a {@link PositionOnTree} from the current UI;
	 * exception will be thrown if not buildable;
	 */
	@Override
	public SiblingReorderPattern build() {
		
		Map<Integer,Map<Integer, Integer>> parentNodeIDToOriginalSwappedIndexMapMap = new LinkedHashMap<>();
		
		
		this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.forEach((k,v)->{
			parentNodeIDToOriginalSwappedIndexMapMap.put(k, v.getOriginalSwappedOrderIndexMap());
		});
		
		
		if(parentNodeIDToOriginalSwappedIndexMapMap.isEmpty()) {
			throw new VisframeException("reorder pattern cannot be empty!");
		}
		
		return new SiblingReorderPattern(parentNodeIDToOriginalSwappedIndexMapMap);
	}
	
	
	/**
	 * the default empty status of this UI is 
	 * 
	 * 1. selection is cleared;
	 * 2. 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		if(this.tree==null) {
			nodeFeatureChoiceBox.setValue(null);
			featureContentTextField.setText("");
			
			this.treeNodeListView.getItems().clear();
			//
			this.nodeReorderVBox.getChildren().clear();
			
			//
			this.nodeCircleSizeChoiceBox.setValue(InteractiveRectangleVfTreeGraphics.DEFAULT_NODE_CIRCLE_RADIUS);
			
			this.showBootstrapCheckBox.setSelected(true);
			this.showEdgeLengthCheckBox.setSelected(true);
			this.showEdgeNonMandatoryAdditionalFeatureCheckBox.setSelected(true);
			this.showNodeNonMandatoryAdditionalFeatureCheckBox.setSelected(true);
			
			this.interactiveTreeScrollPane.setContent(null);
		}else {
			//
			this.tree.setToDefaultStatus();//this will trigger the runnable action to clear the nodeReorderVBox
			
			
			//
			nodeFeatureChoiceBox.setValue(null);
			featureContentTextField.setText("");
			this.treeNodeListView.getItems().clear();
			this.treeNodeListView.getItems().addAll(this.tree.getCalculatorTree().getNodeIDMap().values());
			
			
			//
			this.nodeCircleSizeChoiceBox.setValue(this.tree.getCurrentNodeCircleRadius());
			
			this.showBootstrapCheckBox.setSelected(this.tree.isBootstrapShowing());
			this.showEdgeLengthCheckBox.setSelected(this.tree.isEdgeLengthShowing());
			this.showEdgeNonMandatoryAdditionalFeatureCheckBox.setSelected(this.tree.isEdgeNonMandatoryAdditionalFeaturesShowing());
			this.showNodeNonMandatoryAdditionalFeatureCheckBox.setSelected(this.tree.isNodeNonMandatoryAdditionalFeaturesShowing());
			
		}
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(SiblingReorderPattern value) {
		this.tree.setToDefaultStatus();
		
		//this will add the ReorderPatternVBox for each parent node to the selectedParentNodeIDChildrenNodeReorderPatternVBoxMap
		for(int parentNodeID:value.getParentNodeIDToOriginalSwappedIndexMapMap().keySet()) {
			this.tree.markTreeNode(parentNodeID);
		}
		
		
		//then set the swapped order index
		for(int parentNodeID:value.getParentNodeIDToOriginalSwappedIndexMapMap().keySet()) {
			VfCalculatorTreeNode parentNode = this.tree.getCalculatorTree().getNodeIDMap().get(parentNodeID);
			
			Map<Integer,Integer> originalOrderIndexChildNodeIDMap = new HashMap<>();
			parentNode.getChildrenNodeSiblingOrderIndexMap().forEach((k,v)->{
				originalOrderIndexChildNodeIDMap.put(k,v.getID());
			});
			
			
//			System.out.println(originalOrderIndexChildNodeIDMap);
			
			Map<Integer, Integer> originalSwappedIndexMap = value.getParentNodeIDToOriginalSwappedIndexMapMap().get(parentNodeID);
			
//			System.out.println(originalSwappedIndexMap);
			
			ReorderPatternVBox parentNodeReorderPatternVBox = this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.get(parentNodeID);
			
//			parentNodeReorderPatternVBox.childNodeIDHBoxMap.forEach((k,v)->{
//				System.out.println(k);
//			});
			
			
			if(originalSwappedIndexMap.size()>2) {
				originalSwappedIndexMap.forEach((o,s)->{
//					System.out.println(o+"=="+s);
					
//					this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap.get(parentNodeID).childNodeIDHBoxMap.get(originalOrderIndexChildNodeIDMap.get(o))
//					.orderIndexComboBox.setValue(s);
					int childNodeID = originalOrderIndexChildNodeIDMap.get(o);
//					System.out.println("child node id:"+childNodeID);
					ChildNodeHBox childNodeHBox = parentNodeReorderPatternVBox.childNodeIDHBoxMap.get(childNodeID);
//					System.out.println(childNodeHBox);
//					System.out.println("order index:"+childNodeHBox.siblingOrderIndexList);
					childNodeHBox.orderIndexComboBox.setValue(s);
				});
			}
		}
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	/**
	 * if not modifiable
	 * 1. the {@link #sideVBox} should be non-interactive
	 * 2. the {@link #interactiveTreeScrollPane} should be mostly interactive except that
	 * 		tree node cannot be selected or deselected;
	 * 
	 */
	@Override
	public void setModifiable(boolean modifiable) {
		//
		this.sideVBox.setMouseTransparent(!modifiable);
		
		//
		if(this.tree!=null)
			this.tree.setTreeInteractive(modifiable);
	}
	//////////////////////////
	
	/**
	 * perform initialization that is independent of the tree and the owner node builder
	 */
	@FXML
	public void initialize() {
		this.nodeCircleSizeChoiceBox.getItems().addAll(InteractiveRectangleVfTreeGraphics.NODE_CIRCLE_RADIUS_SET());
		
		//
		this.selectedParentNodeIDChildrenNodeReorderPatternVBoxMap = new LinkedHashMap<>();
	}
	
	@FXML
	private TitledPane rootTiltledPane;
	@FXML
	private VBox sideVBox;
	@FXML
	private HBox searchNodeHBox;
	@FXML
	private ChoiceBox<DataTableColumnName> nodeFeatureChoiceBox;
	@FXML
	private TextField featureContentTextField;
	@FXML
	private Button searchButton;
	@FXML
	private Button clearSearchButton;
	@FXML
	private ListView<VfCalculatorTreeNode> treeNodeListView;
	@FXML
	private VBox selectedParentNodesVBox;
	@FXML
	private VBox nodeReorderVBox;
	@FXML
	private Button clearSelectionButton;
	@FXML
	private VBox mainVBox;
	@FXML
	private VBox operationVBox;
	@FXML
	private HBox treeZoomOperationHBox;
	@FXML
	private Button xZoomInButton;
	@FXML
	private Button xZoomOutButton;
	@FXML
	private Button yZoomInButton;
	@FXML
	private Button yZoomOutButton;
	@FXML
	private Button zoomInButton;
	@FXML
	private Button zoomOutButton;
	@FXML
	private Button defaultSizeButton;
	@FXML
	private ChoiceBox<Double> nodeCircleSizeChoiceBox;
	@FXML
	private HBox displayOptionHBox;
	@FXML
	private CheckBox showBootstrapCheckBox;
	@FXML
	private CheckBox showEdgeLengthCheckBox;
	@FXML
	private CheckBox showNodeNonMandatoryAdditionalFeatureCheckBox;
	@FXML
	private CheckBox showEdgeNonMandatoryAdditionalFeatureCheckBox;
	@FXML
	private ScrollPane interactiveTreeScrollPane;

	// Event Listener on Button[#searchButton].onAction
	@FXML
	public void searchButtonOnAction(ActionEvent event) {
		
		if(this.tree!=null) {
			DataTableColumnName featureName = this.nodeFeatureChoiceBox.getSelectionModel().getSelectedItem();
			if(featureName!=null) {
				this.treeNodeListView.getItems().clear();
				
				this.tree.getCalculatorTree().getNodeIDMap().values().forEach(e->{
					if(e.getNonMandatoryAdditionalNodeFeatureColumnNameValueStringMap().containsKey(featureName)) {
						if(e.getNonMandatoryAdditionalNodeFeatureColumnNameValueStringMap().get(featureName)!=null) {
							if(e.getNonMandatoryAdditionalNodeFeatureColumnNameValueStringMap().get(featureName).contains(this.featureContentTextField.getText())) {
								this.treeNodeListView.getItems().add(e);
							}
						}
					}
					
				});
				
				
				
			}else {
				AlertUtils.popAlert("Warning", "No node feature is currently showing!");
			}
			
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
		
		
	}
	
	// Event Listener on Button[#clearSearchButton].onAction
	@FXML
	public void clearSearchButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.nodeFeatureChoiceBox.setValue(null);
			this.featureContentTextField.setText("");
			this.treeNodeListView.getItems().clear();
			
			this.treeNodeListView.getItems().addAll(this.tree.getCalculatorTree().getNodeIDMap().values());
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#clearSelectionButton].onAction
	@FXML
	public void clearSelectionButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.clearTreeNodeMarkSet();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	
	///================zoom============================
	// Event Listener on Button[#xZoomInButton].onAction
	@FXML
	public void xZoomInButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.zoomInX();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#xZoomOutButton].onAction
	@FXML
	public void xZoomOutButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.zoomOutX();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#yZoomInButton].onAction
	@FXML
	public void yZoomInButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.zoomInY();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#yZoomOutButton].onAction
	@FXML
	public void yZoomOutButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.zoomOutY();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#zoomInButton].onAction
	@FXML
	public void zoomInButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.zoomIn();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#zoomOutButton].onAction
	@FXML
	public void zoomOutButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.zoomOut();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on Button[#defaultSizeButton].onAction
	@FXML
	public void defaultSizeButtonOnAction(ActionEvent event) {
		if(this.tree!=null) {
			this.tree.setToDefaultSize();
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	///================zoom============================
	
	
	
	///================display============================
	// Event Listener on ChoiceBox[#nodeCircleSizeChoiceBox].onDragDetected
	@FXML
	public void nodeCircleSizeChoiceBoxOnAction(MouseEvent event) {
		if(this.tree!=null) {
			this.tree.setNodeEllipseRadius(this.nodeCircleSizeChoiceBox.getSelectionModel().getSelectedItem());
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on CheckBox[#showBootstrapCheckBox].onAction
	@FXML
	public void showBootstrapCheckBoxOnAction(ActionEvent event) {
		if(this.tree!=null) {
			if(this.showBootstrapCheckBox.isSelected()) {
				this.tree.showBootstrap();
			}else {
				this.tree.hideBootstrap();
			}
			
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on CheckBox[#showEdgeLengthCheckBox].onAction
	@FXML
	public void showEdgeLengthCheckBoxOnAction(ActionEvent event) {
		if(this.tree!=null) {
			if(this.showEdgeLengthCheckBox.isSelected()) {
				this.tree.showEdgeLength();
			}else {
				this.tree.hideEdgeLength();
			}
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on CheckBox[#showNodeNonMandatoryAdditionalFeatureCheckBox].onAction
	@FXML
	public void showNodeNonMandatoryAdditionalFeatureCheckBoxOnAction(ActionEvent event) {
		if(this.tree!=null) {
			if(this.showNodeNonMandatoryAdditionalFeatureCheckBox.isSelected()) {
				this.tree.showNodeNonMandatoryAdditionalFeatures();
			}else {
				this.tree.hideNodeNonMandatoryAdditionalFeatures();
			}
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	// Event Listener on CheckBox[#showEdgeNonMandatoryAdditionalFeatureCheckBox].onAction
	@FXML
	public void showEdgeNonMandatoryAdditionalFeatureCheckBoxOnAction(ActionEvent event) {
		if(this.tree!=null) {
			if(this.showEdgeNonMandatoryAdditionalFeatureCheckBox.isSelected()) {
				this.tree.showEdgeNonMandatoryAdditionalFeatures();
			}else {
				this.tree.hideEdgeNonMandatoryAdditionalFeatures();
			}
		}else {
			AlertUtils.popAlert("Warning", "No tree is currently showing!");
		}
	}
	
	
	//////////////////////////////////
	private class ReorderPatternVBox{
		
		private final int parentNodeID;
		private final Map<Integer, Integer> childrenNodeIDOriginalOrderIndexMap;
		
		
		//////////////////
		private VBox containerVBox;
		private TextField parentNodeIDTextField;
		private TextField numberOfChildrenNodeTextField;
		private Map<Integer, ChildNodeHBox> childNodeIDHBoxMap;
		
		private Button removeButton;
		
		
		ReorderPatternVBox(int parentNodeID, Map<Integer, Integer> childrenNodeIDOriginalOrderIndexMap){
			this.parentNodeID = parentNodeID;
			this.childrenNodeIDOriginalOrderIndexMap = childrenNodeIDOriginalOrderIndexMap;
			
			/////////////////////
			this.containerVBox = new VBox();
			this.parentNodeIDTextField = new TextField(Integer.toString(this.parentNodeID));
			this.numberOfChildrenNodeTextField = new TextField(Integer.toString(this.childrenNodeIDOriginalOrderIndexMap.size()));
			
			if(this.childrenNodeIDOriginalOrderIndexMap.size()>2) {
				
				List<Integer> siblingOrderIndexList = new ArrayList<>();
				siblingOrderIndexList.addAll(this.childrenNodeIDOriginalOrderIndexMap.values());
				
				this.childNodeIDHBoxMap = new LinkedHashMap<>();
				
				this.childrenNodeIDOriginalOrderIndexMap.forEach((k,v)->{
					this.childNodeIDHBoxMap.put(k, new ChildNodeHBox(this, k,v,siblingOrderIndexList));
				});
				
			}
			

			this.removeButton = new Button("remove");
			
			
			/////////////////////////
			this.containerVBox.getChildren().add(this.removeButton);
			
			this.containerVBox.getChildren().add(this.parentNodeIDTextField);
			this.containerVBox.getChildren().add(this.numberOfChildrenNodeTextField);
			
			if(this.childrenNodeIDOriginalOrderIndexMap.size()>2) {
				this.childNodeIDHBoxMap.forEach((k,v)->{
					this.containerVBox.getChildren().add(v.containerHBox);
				});
			}
			
			
			////////////////////////////

			this.removeButton.setOnAction(e->{
				tree.clearTreeNodeMark(this.parentNodeID);
			});
		}
		
		
		/**
		 * try to get the map from original order index to swapped one;
		 * 
		 * if not valid, throw VisframeException;
		 * @return
		 */
		Map<Integer, Integer> getOriginalSwappedOrderIndexMap(){
			Map<Integer, Integer> ret = new LinkedHashMap<>();
			
			if(this.childrenNodeIDOriginalOrderIndexMap.size()==1) {
				//should never happen because single child node is not allowed to be selected
			}else if(this.childrenNodeIDOriginalOrderIndexMap.size()==2) {
				Iterator<Integer> iterator = this.childrenNodeIDOriginalOrderIndexMap.keySet().iterator();
				
				int node1ID = iterator.next();
				int node2ID = iterator.next();
				
				ret.put(childrenNodeIDOriginalOrderIndexMap.get(node1ID), childrenNodeIDOriginalOrderIndexMap.get(node2ID));
				ret.put(childrenNodeIDOriginalOrderIndexMap.get(node2ID), childrenNodeIDOriginalOrderIndexMap.get(node1ID));
				
			}else {
				//check if all children nodes has a selected new order index
				//if not, throw exception;
				
				//also check if the selected new order index are all the same with original one;
				//if yes, throw exception;
				
				boolean allChildNodeNewIndexSameWithOriginal = true;
				Set<Integer> assignedOrderIndexSet = new HashSet<>();
				
				for(ChildNodeHBox child:this.childNodeIDHBoxMap.values()) {
					Integer newOrderIndex = child.orderIndexComboBox.getSelectionModel().getSelectedItem();
					if(newOrderIndex==null) {
						throw new VisframeException("at least one child node's new sibling order index is unselected!");
					}

					if(assignedOrderIndexSet.contains(newOrderIndex)) {
						throw new VisframeException("at least one order index is assigned to multiple nodes!");
					}
					
					if(newOrderIndex!=child.originalOrderIndex) {
						allChildNodeNewIndexSameWithOriginal = false;
					}
					
					ret.put(child.originalOrderIndex, newOrderIndex);
				}
				
				if(allChildNodeNewIndexSameWithOriginal) {
					throw new VisframeException("all nodes' sibling order index are the same with original one, no swap is detected!");
				}
				
			}
			
			
			return ret;
		}
		
	}
	
	private class ChildNodeHBox{
		private final ReorderPatternVBox ownerReorderPatternVBox;
		private final int childNodeID;
		private final int originalOrderIndex;
		private final List<Integer> siblingOrderIndexList;
		
		////
		private HBox containerHBox;
		
		private TextField childNodeIDTextField;
		private TextField originalOrderIndexTextField;
		
		private ComboBox<Integer> orderIndexComboBox;
		
		ChildNodeHBox(ReorderPatternVBox ownerReorderPatternVBox, int childNodeID, int originalOrderIndex, List<Integer> siblingOrderIndexList){
			this.ownerReorderPatternVBox = ownerReorderPatternVBox;
			this.childNodeID = childNodeID;
			this.originalOrderIndex = originalOrderIndex;
			
			this.siblingOrderIndexList = siblingOrderIndexList;
			
			this.containerHBox = new HBox();
			this.childNodeIDTextField = new TextField(Integer.toString(this.childNodeID));
			this.originalOrderIndexTextField = new TextField(Integer.toString(this.originalOrderIndex));
			
			
			this.orderIndexComboBox = new ComboBox<>();
			this.orderIndexComboBox.getItems().addAll(this.siblingOrderIndexList);
			
			this.containerHBox.getChildren().add(this.childNodeIDTextField);
			this.containerHBox.getChildren().add(this.originalOrderIndexTextField);
			this.containerHBox.getChildren().add(this.orderIndexComboBox);
			
			
			this.setEventHandler();
		}
		
		private void setEventHandler() {
			//when a change is detected
			this.orderIndexComboBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
				//try to build and update the value
//				try{
//					SiblingReorderPattern type = build();
//					getOwnerNodeBuilder().updateNonNullValueFromContentController(type);
//					getOwnerNodeBuilder().setUIVisualEffect(true);
//				}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//					//skip
//					System.out.println("exception thrown, skip update:"+ex.getMessage());
//					getOwnerNodeBuilder().setUIVisualEffect(false);
//				}
				
				boolean allSelected = true;
				for(ChildNodeHBox child:this.ownerReorderPatternVBox.childNodeIDHBoxMap.values()) {
					if(child.orderIndexComboBox.getSelectionModel().getSelectedItem()==null) {
						allSelected = false;
						break;
					}
				}
				//only popup alert when all silbing node's new order index are selected;
				getOwnerNodeBuilder().updateNonNullValueFromContentController(allSelected);
			});
			
			
		}
		
	}
	
	
	
	
}
