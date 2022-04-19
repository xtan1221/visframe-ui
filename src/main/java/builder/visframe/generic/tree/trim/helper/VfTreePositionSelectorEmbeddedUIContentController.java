package builder.visframe.generic.tree.trim.helper;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import generic.tree.calculation.VfCalculatorTreeNode;
import generic.tree.trim.helper.PositionOnTree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import rdb.table.data.DataTableColumnName;
import utils.AlertUtils;
import utils.FXUtils;

public class VfTreePositionSelectorEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<PositionOnTree> {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/generic/tree/trim/helper/VfTreePositionSelectorEmbeddedUIContent.fxml";
	
	/**
	 * tree which contains the information for currently selected position
	 */
	private InteractiveRectangleVfTreeGraphics tree;
	
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
		
		
		if(this.tree!=null){//set runnable after selected node is changed
			this.tree.setActionWhenCurrentlyMarkedNodeIDSetIsChanged(()->{
				if(!this.tree.getCurrentlyMarkedNodeIDSet().isEmpty()) {
				
					this.selectedPositionIncidentChildNodeIDTextField.setText(Integer.toString(this.tree.getCurrentlyMarkedNodeIDSet().iterator().next()));
					
				}else {
					this.selectedPositionIncidentChildNodeIDTextField.setText("");
				}
				this.positionExactlyOnNodeCheckBox.setSelected(false);
				this.positionBetweenChildAndParentNodeTextField.setText("");
			});
		}
		
		this.setUIToDefaultEmptyStatus();
		
		this.updateNodeFeatureChoiceBoxStatus();
		this.updateTreeNodeListViewStatus();
		this.updateSelectedPositionVBoxStatus();
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
	
	
	private void updateSelectedPositionVBoxStatus(){
		if(this.tree!=null) {
			this.selectedPositionIncidentChildNodeIDTextField.setText(this.tree.getCurrentlyMarkedNodeIDSet().isEmpty()?"":Integer.toString(this.tree.getCurrentlyMarkedNodeIDSet().iterator().next()));
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
		else {
			this.interactiveTreeScrollPane.setContent(null);
		}
		
	}
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		this.setupTreeNodeListView();
		
		this.setupSelectedPositionVBox();
		
		
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
				
//				this.selectedPositionIncidentChildNodeIDTextField.setText(Integer.toString(
//						this.treeNodeListView.getSelectionModel().getSelectedItem().getID()));
//				
//				this.positionExactlyOnNodeCheckBox.setSelected(false);
//				this.positionBetweenChildAndParentNodeTextField.setText("");
			}
		});
		
		
	}
	
	
	/**
	 * 
	 */
	private void setupSelectedPositionVBox() {
		this.selectedPositionIncidentChildNodeIDTextField.textProperty().addListener((o,oldValue,newValue)->{
//			try{
//				PositionOnTree type = this.build();
//				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(type);
//			}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//				//skip
//				System.out.println("exception thrown, skip update!");
//			}
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		this.positionExactlyOnNodeCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
//			try{
//				PositionOnTree type = this.build();
//				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(type);
//			}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//				//skip
//				System.out.println("exception thrown, skip update!");
//			}
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		this.positionBetweenChildAndParentNodeTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.positionBetweenChildAndParentNodeTextField.focusedProperty().get()) {//focus is lost
//				if(this.positionBetweenChildAndParentNodeTextField.getText().isEmpty()) {//?TODO
//					
//					return;
//				}
//				
//				//non-empty
//				try {
//					double value = Double.parseDouble(this.positionBetweenChildAndParentNodeTextField.getText());
//					
//					if(value<=0||value>=1) {//check domain constraint
//						Alert alert = new Alert(AlertType.ERROR);
//						alert.setTitle("Text Content Error");
//						alert.setHeaderText("Error!");
//						alert.setContentText("position value must be in (0,1)!");
//						
//						alert.showAndWait();
//						
//						this.positionBetweenChildAndParentNodeTextField.setText("");
//						this.positionBetweenChildAndParentNodeTextField.requestFocus();
//						
//						
//					}else {//valid non null
//						this.getOwnerNodeBuilder().setValue(this.build(), false);
//					}
//					
//				}catch(Exception ex) {
//					Alert alert = new Alert(AlertType.ERROR);
//					alert.setTitle("Text Content Error");
//					alert.setHeaderText("Error!");
//					alert.setContentText(ex.getClass().getSimpleName()+":"+ex.getMessage());
//					alert.showAndWait();
//					
//					this.positionBetweenChildAndParentNodeTextField.setText("");
//					this.positionBetweenChildAndParentNodeTextField.requestFocus();
//				}
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
			}
		});
		
		this.positionBetweenChildAndParentNodeTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				this.positionBetweenChildAndParentNodeTextField.getParent().requestFocus();
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
	public VfTreePositionSelector getOwnerNodeBuilder() {
		return (VfTreePositionSelector) this.ownerNodeBuilder;
	}
	
	@Override
	public Parent getRootParentNode() {
		return rootTiltledPane;
	}
	
	/**
	 * try to build a {@link PositionOnTree} from the current UI;
	 * exception may be thrown;
	 */
	@Override
	public PositionOnTree build() {
		int childNodeID = Integer.parseInt(this.selectedPositionIncidentChildNodeIDTextField.getText());
		
		Integer parentNodeID;
		Double pos;
		boolean onRootNode = this.tree.getCalculatorTree().getNodeIDMap().get(childNodeID).getParentNodeID()==null;
		
		if(this.positionExactlyOnNodeCheckBox.isSelected()) {
			parentNodeID = null;
			pos = null;
		}else {//
			parentNodeID = this.tree.getCalculatorTree().getNodeIDMap().get(childNodeID).getParentNodeID();
			
			try {
				pos = Double.parseDouble(this.positionBetweenChildAndParentNodeTextField.getText());
			}catch(NumberFormatException e) {
				this.positionBetweenChildAndParentNodeTextField.setText("");
				throw new VisframeException("position Between Child And Parent node must be a value in (0,1)");
			}
			
			if(pos<=0||pos>=1) {
				this.positionBetweenChildAndParentNodeTextField.setText("");
				throw new VisframeException("position Between Child And Parent node must be a value in (0,1)");
			}
		}
		
		return new PositionOnTree(
				childNodeID, parentNodeID, pos, onRootNode
				);
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
			this.selectedPositionIncidentChildNodeIDTextField.setText("");
			this.positionExactlyOnNodeCheckBox.setSelected(false);
			this.positionBetweenChildAndParentNodeTextField.setText("");
			
			//
			this.nodeCircleSizeChoiceBox.setValue(InteractiveRectangleVfTreeGraphics.DEFAULT_NODE_CIRCLE_RADIUS);
			
			this.showBootstrapCheckBox.setSelected(true);
			this.showEdgeLengthCheckBox.setSelected(true);
			this.showEdgeNonMandatoryAdditionalFeatureCheckBox.setSelected(true);
			this.showNodeNonMandatoryAdditionalFeatureCheckBox.setSelected(true);
			
			this.interactiveTreeScrollPane.setContent(null);
		}else {
			//
			this.tree.setToDefaultStatus();
			
			
			//
			nodeFeatureChoiceBox.setValue(null);
			featureContentTextField.setText("");
			this.treeNodeListView.getItems().clear();
			this.treeNodeListView.getItems().addAll(this.tree.getCalculatorTree().getNodeIDMap().values());
			
			
			//
//			this.selectedPositionIncidentChildNodeIDTextField.setText(
//					this.tree.getCurrentlyMarkedNodeIDSet().isEmpty()?"":Integer.toString(this.tree.getCurrentlyMarkedNodeIDSet().iterator().next()));
//			this.positionExactlyOnNodeCheckBox.setSelected(false);
//			this.positionBetweenChildAndParentNodeTextField.setText("");
			
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
	public void setUIToNonNullValue(PositionOnTree value) {
		this.tree.markTreeNode(value.getChildNodeID());
		
//		this.selectedPositionIncidentChildNodeIDTextField.setText(Integer.toString(value.getChildNodeID()));
		
		if(value.getParentNodeID()==null) {
			this.positionExactlyOnNodeCheckBox.setSelected(true);
		}else {
			this.positionExactlyOnNodeCheckBox.setSelected(false);
			this.positionBetweenChildAndParentNodeTextField.setText(Double.toString(value.getPos()));
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
		this.positionExactlyOnNodeCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
			if(this.positionExactlyOnNodeCheckBox.isSelected()) {
				positionBetweenChildAndParentNodeTextField.setText("");
				FXUtils.set2Disable(this.positionBetweenChildAndParentNodeTextField, true);
			}else {
				FXUtils.set2Disable(this.positionBetweenChildAndParentNodeTextField, false);
			}
		});
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
	private VBox selectedPositionVBox;
	@FXML
	private TextField selectedPositionIncidentChildNodeIDTextField;
	@FXML
	private CheckBox positionExactlyOnNodeCheckBox;
	@FXML
	private TextField positionBetweenChildAndParentNodeTextField;
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
//			this.selectedPositionIncidentChildNodeIDTextField.setText("");
//			this.positionExactlyOnNodeCheckBox.setSelected(false);
//			this.positionBetweenChildAndParentNodeTextField.setText("");
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
}
