package builder.visframe.graphics.property.node;

import basic.SimpleName;
import basic.VfNotes;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import graphics.property.node.GraphicsPropertyLeafNode;
import graphics.property.node.GraphicsPropertyNode;
import graphics.property.node.GraphicsPropertyNonLeafNode;
import graphics.property.shape2D.factory.VfBasicGraphicsPropertyNodeFactory;
import graphics.property.shape2D.factory.VfDefinedGraphicsPropertyNodeLookup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GraphicsPropertyNodeSelectorEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<GraphicsPropertyNode> {
	public static final String FXML_FILE_DIR = "/builder/visframe/graphics/property/node/GraphicsPropertyNodeSelectorEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		this.majorTypeComboBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
			if(this.majorTypeComboBox.getSelectionModel().getSelectedItem()==null) {
				this.propertyNodeTreeView.setVisible(false);
				this.propertyNodeTreeView.setRoot(null);
			}else {//reset the tree view
				this.propertyNodeTreeView.setVisible(true);
				
				TreeItem<GraphicsPropertyNode> rootNode = new TreeItem<>(makeDummyRootNode());
				this.propertyNodeTreeView.setRoot(rootNode);
				
				for(GraphicsPropertyNode node:VfDefinedGraphicsPropertyNodeLookup.getTypeRootNodeSetMap().get(this.majorTypeComboBox.getSelectionModel().getSelectedItem())) {
					rootNode.getChildren().add(makeTreeItem(node));
				}
			}
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		this.propertyNodeTreeView.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			
		});
	}
	
	@Override
	public GraphicsPropertyNodeSelector getOwnerNodeBuilder() {
		return (GraphicsPropertyNodeSelector) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return this.rootVBox;
	}
	
	
	@Override
	public GraphicsPropertyNode build() {
		if(this.majorTypeComboBox.getSelectionModel().getSelectedItem()==null) {
			throw new VisframeException("no major type is selected!");
		}
		
		if(this.propertyNodeTreeView.getSelectionModel().getSelectedItem()==null) {
			throw new VisframeException("no property Node TreeItem is selected!");
		}
		
		return this.propertyNodeTreeView.getSelectionModel().getSelectedItem().getValue();
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.majorTypeComboBox.setValue(null);
	}
	
	/**
	 * the major type is set to a random one if the given node is included in multiple major types
	 */
	@Override
	public void setUIToNonNullValue(GraphicsPropertyNode value) {
		
		for(String type:VfDefinedGraphicsPropertyNodeLookup.PROPERTY_TYPE_SET_MAP.keySet()) {
			if(VfDefinedGraphicsPropertyNodeLookup.PROPERTY_TYPE_SET_MAP.get(type).contains(value)) {
				this.majorTypeComboBox.setValue(type);
				break;
			}
		}
		
		
		this.propertyNodeTreeView.getSelectionModel().select(new TreeItem<>(value));
		
	}
	
	//////////////////////////

	@FXML
	public void initialize() {
		this.majorTypeComboBox.getItems().addAll(VfDefinedGraphicsPropertyNodeLookup.PROPERTY_TYPE_SET_MAP.keySet());
		
		//tree cell shows a Label with the simple name of the class contained by the tree item;
		propertyNodeTreeView.setCellFactory(e->{
			return new TreeCell<GraphicsPropertyNode>() {
	            @Override
	            protected void updateItem(GraphicsPropertyNode node, boolean empty) {
	                super.updateItem(node, empty);
	                if (node == null || empty) {
	                    setGraphic(null);
	                } else {
	                	//set the Graphics for the tree cell here with a specific operationType
	                	Label label = new Label(node.getName().getStringValue());
	                	
//	                	label.setOnMouseClicked(e->{
//	                		if(Modifier.isFinal(operationType.getModifiers())){
////		                		System.out.println(operationType.getSimpleName());
//		                		selectedTypeTextField.setText(operationType.getSimpleName());
//	                		}else {
//	                			selectedTypeTextField.setText("");
//	                		}
//	                	});
	                	
	                	Tooltip tp = new Tooltip(makeTipString(node));
	                	
	                	Tooltip.install(label, tp);
	                	
	                    setGraphic(label);
	                }
	            }
	        };
		});
		
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private ComboBox<String> majorTypeComboBox;
	@FXML
	private TreeView<GraphicsPropertyNode> propertyNodeTreeView;
	@FXML
	private Button clearSelectionButton;
	
	
	// Event Listener on Button[#clearSelectionButton].onAction
	@FXML
	public void clearSelectionButtonOnAction(ActionEvent event) {
		this.majorTypeComboBox.setValue(null);
	}
	
	
	
	
	////////////////////////////////
	static GraphicsPropertyNonLeafNode makeDummyRootNode() {
		return new GraphicsPropertyNonLeafNode(
				new SimpleName("ROOT"), 
				VfNotes.makeVisframeDefinedVfNotes(), 
				VfBasicGraphicsPropertyNodeFactory.LAYOUT_CART_2D.getChildrenNodeNameMap(), 
				VfBasicGraphicsPropertyNodeFactory.LAYOUT_CART_2D.getChildrenNodeNameDescriptionStringMap());
	}
	
	static String makeTipString(GraphicsPropertyNode node) {
		StringBuilder sb = new StringBuilder();
		
		if(node instanceof GraphicsPropertyLeafNode) {
			GraphicsPropertyLeafNode<?> leaf = (GraphicsPropertyLeafNode<?>)node;
			sb.append(leaf.getName().getStringValue()).append(leaf.getPropertyAttribute());
		}else {
			GraphicsPropertyNonLeafNode nonLeaf = (GraphicsPropertyNonLeafNode)node;
		}
		
		
		return sb.toString();
	}
	
	/**
	 * make and return a TreeItem that contains the full set of descendant nodes of the given node;
	 * @param node
	 * @return
	 */
	static TreeItem<GraphicsPropertyNode> makeTreeItem(GraphicsPropertyNode node){
		TreeItem<GraphicsPropertyNode> ret;
		if(node instanceof GraphicsPropertyLeafNode) {
			GraphicsPropertyLeafNode<?> leaf = (GraphicsPropertyLeafNode<?>)node;
			
			ret = new TreeItem<>(leaf);
		}else {
			GraphicsPropertyNonLeafNode nonLeaf = (GraphicsPropertyNonLeafNode)node;
			
			ret = new TreeItem<>(nonLeaf);
			
			for(GraphicsPropertyNode child:nonLeaf.getChildrenNodeNameMap().values()) {
				ret.getChildren().add(makeTreeItem(child));
			}
		}
		
		
		return ret;
	}
	
	
}
