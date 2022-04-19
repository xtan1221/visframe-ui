package dependencygraph.cfd2;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilderFactory;
import context.project.VisProjectDBContext;
import function.composition.CompositionFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SimpleCFDGraphNodeController{
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/cfd2/SimpleCFDGraphNode.fxml";
	
	/////////////////////////////
	private SimpleCFDGraphNodeManager manager;
	
	void setManager(SimpleCFDGraphNodeManager manager) {
		this.manager = manager;
		this.dagNodeInforLabel.setText(this.manager.getHostSimpleCFDGraphViewerManager().getDagNodeInforStringFunction().apply(this.manager.getNode()));
	}
	
	/**
	 * @return the manager
	 */
	public SimpleCFDGraphNodeManager getManager() {
		return manager;
	}

	
	public Pane getRootNodeContainer() {
		return this.dagNodeRootStackPane;
	}
	
	public Pane getCopyNodeContainerPane() {
		return this.copyNodesHBox;
	}
	
	
	////////////////////
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		
//		this.dagNodeRootStackPane.setOnMouseClicked(e->{
//			System.out.println("width:".concat(Double.toString(this.dagNodeRootStackPane.getWidth())));
//			System.out.println("height:".concat(Double.toString(this.dagNodeRootStackPane.getHeight())));
//			System.out.println("layoutx:".concat(Double.toString(this.dagNodeRootStackPane.getLayoutX())));
//			System.out.println("layouty:".concat(Double.toString(this.dagNodeRootStackPane.getLayoutY())));
//			
//		});
	}
	
	@FXML
	private StackPane dagNodeRootStackPane;
	@FXML
	private Label dagNodeInforLabel;
	@FXML
	private Button viewNodeDetailsButton;
	@FXML
	private HBox copyNodesHBox;
	
	
	/////////////////////////////////
	private Scene CFScene;
	private Stage CFStage;
	private CompositionFunctionBuilder CFBuilder;
	@FXML
	public void viewNodeDetailsButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.CFBuilder==null) {
			VisProjectDBContext vis = (VisProjectDBContext)this.getManager().getHostSimpleCFDGraphViewerManager().getHostVisframeContext();
			CompositionFunction cf = vis.getCompositionFunctionLookup().lookup(this.getManager().getNode().getCFID());
			CFBuilder = 
					CompositionFunctionBuilderFactory.singleton(vis).build(cf);
			
			CFBuilder.setModifiable(false);
			
			CFScene = new Scene(CFBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			CFStage = new Stage();
			
			CFStage.setScene(CFScene);
			
			CFStage.setWidth(1200);
			CFStage.setHeight(800);
			CFStage.initModality(Modality.NONE);
			String title = cf.getID().toString();
			CFStage.setTitle(title);
		}
		CFStage.showAndWait();
	}
}
