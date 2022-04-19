package dependencygraph.vcd.pre;

import dependency.vcd.VCDNodeImpl;
import dependencygraph.DAGNodeController;
import dependencygraph.DAGNodeManager;
import dependencygraph.cfd.CFDNodeOrderingComparatorFactory;
import dependencygraph.cfd.SimpleCFDGraphViewerManager;
import dependencygraph.dos.DOSNodeOrderingComparatorFactory;
import dependencygraph.dos.SimpleDOSGraphViewerManager;
import dependencygraph.vcd.pre.utils.VCDNodeAssignedEntitiesManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VCDNodeController extends DAGNodeController<VCDNodeImpl> {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vcd/VCDNode.fxml";
	
	/////////////////////////////
	@Override
	protected void setManager(DAGNodeManager<VCDNodeImpl, ?> manager) {
		super.setManager(manager);
		//set labels
		this.precedenceIndexLabel.setText(Integer.toString(this.manager.getNode().getPrecedenceIndex()));
		
		this.precedenceIndexLabel.setMinWidth(Region.USE_PREF_SIZE);
		
		this.viewAssignedEntitiesButton.setMinWidth(Region.USE_PREF_SIZE);
		this.viewCFDGraphButton.setMinWidth(Region.USE_PREF_SIZE);
		this.viewDOSGraphButton.setMinWidth(Region.USE_PREF_SIZE);
	}
	
	@Override
	protected VCDNodeManager getManager() {
		return (VCDNodeManager) manager;
	}
	
	@Override
	protected Pane getRootNodeContainer() {
		return nodeRootStackPane;
	}
	
	
	////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@FXML
	private StackPane nodeRootStackPane;
	@FXML
	private Label precedenceIndexLabel;
	@FXML
	private Button viewAssignedEntitiesButton;
	@FXML
	private Button viewCFDGraphButton;
	@FXML
	private Button viewDOSGraphButton;
	
	// Event Listener on Button[#viewAssignedEntitiesButton].onAction
	@FXML
	public void viewAssignedEntitiesButtonOnAction(ActionEvent event) {
		VCDNodeAssignedEntitiesManager manager = new VCDNodeAssignedEntitiesManager(this.getManager().getNode());
		manager.showAndWait((Stage) this.getRootNodeContainer().getScene().getWindow());
	}
	// Event Listener on Button[#viewCFDGraphButton].onAction
	@FXML
	public void viewCFDGraphButtonOnAction(ActionEvent event) {
		SimpleCFDGraphViewerManager manager = new SimpleCFDGraphViewerManager(
				this.getManager().getCfdGraph().getUnderlyingGraph(),//SimpleDirectedGraph<CFDNodeImpl, CFDEdgeImpl> underlyingSimpleCFDGraph,
				CFDNodeOrderingComparatorFactory.getComparator(),//Comparator<CFDNodeImpl> nodeOrderComparator,
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return a.getCFID().toString();}//Function<CFDNodeImpl,String> dagNodeInforStringFunction
				);
		
		Stage stage = new Stage();
		Scene scene = new Scene(manager.getController().getRootContainerNode());
		stage.setScene(scene);
		
		stage.showAndWait();
		
	}
	// Event Listener on Button[#viewDOSGraphButton].onAction
	@FXML
	public void viewDOSGraphButtonOnAction(ActionEvent event) {
		SimpleDOSGraphViewerManager manager = new SimpleDOSGraphViewerManager(
				this.getManager().getDosGraph().getUnderlyingGraph(),//SimpleDirectedGraph<DOSNodeImpl, DOSEdgeImpl> underlyingSimpleDOSGraph,
				DOSNodeOrderingComparatorFactory.getComparator(),//Comparator<DOSNodeImpl> nodeOrderComparator,
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return a.getMetadataID().toString();}//Function<DOSNodeImpl,String> dagNodeInforStringFunction
				);
		
		Stage stage = new Stage();
		Scene scene = new Scene(manager.getController().getRootContainerNode());
		stage.setScene(scene);
		
		stage.showAndWait();
	}
}
