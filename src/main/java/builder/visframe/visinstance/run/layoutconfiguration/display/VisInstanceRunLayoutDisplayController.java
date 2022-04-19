package builder.visframe.visinstance.run.layoutconfiguration.display;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utils.FXUtils;
import utils.Pair;
import visinstance.run.extractor.CoreShapeCFGCalculatedTargetExtractor;
import visinstance.run.extractor.LayoutUtils;

public final class VisInstanceRunLayoutDisplayController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/run/layoutconfiguration/display/VisInstanceRunLayoutDisplay.fxml";
	
	///////////////////////
	private VisInstanceRunLayoutDisplayManager manager;
	
	/**
	 * 
	 * @param manager
	 * @throws SQLException
	 */
	void setManager(VisInstanceRunLayoutDisplayManager manager) throws SQLException {
		this.manager = manager;
		
		this.setCanvassAnchorPaneSize();
	}
	
	/**
	 * set the size of the canvass AnchorPane based on the layout region of the core ShapeCFGs
	 */
	private void setCanvassAnchorPaneSize() {
		double width = this.getManager().getLayoutRegionBottomRightCoord().getX()-this.getManager().getLayoutRegionUpperLeftCoord().getX();
		double height = this.getManager().getLayoutRegionBottomRightCoord().getY()-this.getManager().getLayoutRegionUpperLeftCoord().getY();
		this.canvassAnchorPane.setMinSize(width, height);
		this.canvassAnchorPane.setPrefSize(width, height);
		this.canvassAnchorPane.setMaxSize(width, height);
	}
	
	
	/**
	 * only invoked when showing an existing VisInstanceRunLayoutConfiguration;
	 * @param modifiable
	 * @throws IOException 
	 */
	void setModifiable(boolean modifiable) throws IOException {
		if(modifiable) {
			throw new UnsupportedOperationException("not supported?");
		}else {
			FXUtils.set2Disable(this.startButton, true);
		}
	}
	
	
	/////////////////////////
	VisInstanceRunLayoutDisplayManager getManager() {
		return this.manager;
	}
	
	public Parent getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	public AnchorPane getCanvassAnchorPane() {
		return this.canvassAnchorPane;
	}

//	/**
//	 * re-add the {@link #canvassAnchorPane} back to the {@link #canvassScrollPane}
//	 * 
//	 * this is needed after the {@link #canvassAnchorPane} is used to save the image by {@link FXNodeSaveAsImageManager};
//	 * 
//	 * should be invoked whenever step 2 of VisInstanceRunLayoutConfigurationBuilder is to be shown on the UI;
//	 * 
//	 * note that 
//	 * 1. even after the {@link #canvassAnchorPane} is used to save image (thus added to another Scene), the {@link #canvassScrollPane} cannot recognize the change and still have it as content (bug or javafx design deficiency?)
//	 * 2. thus need to explicitly set the content of the {@link #canvassScrollPane} to null, then set it to {@link #canvassAnchorPane}
//	 * 
//	 * no re-adding the shape instances is needed!
//	 * 
//	 * @throws SQLException 
//	 */
//	public void reAddCanvassAnchorPaneToScrollPane() throws SQLException {
//		this.canvassScrollPane.setContent(null);
//		this.canvassScrollPane.setContent(this.canvassAnchorPane);
//	}
	
	///////////////////////////////////////////////////
	@FXML
	public void initialize() {
		//
		this.canvassAnchorPane.setOnMouseClicked(e->{
			System.out.println(this.getClass().getName()+":testtest");
			this.canvassAnchorPane.getChildren().forEach(c->{
				System.out.println(c);
			});
		});
		
		this.canvassScrollPane.setOnMouseClicked(e->{
			System.out.println(this.canvassScrollPane.getContent());
		});
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button startButton;
	@FXML
	private ScrollPane canvassScrollPane;
	@FXML
	private AnchorPane canvassAnchorPane;

	
	// Event Listener on Button[#layoutButton].onAction
	@FXML
	public void startButtonOnAction(ActionEvent event) throws SQLException {
		//
		CoreShapeCFGCalculatedTargetExtractor extractor;
		
		while((extractor=this.getManager().getVisInstanceRunCoreShapeCFGCalculatedTargetValueExtractor().nextCoreShapeCFGExtractor())!=null) {
			
			Pair<Point2D, Map<SimpleName, String>> record;
			
			while((record=extractor.nextRecord())!=null) {
				if(LayoutUtils.isInRegion(this.getManager().getLayoutRegionUpperLeftCoord(),  this.getManager().getLayoutRegionBottomRightCoord(), record.getFirst())) {
					Node node = extractor.getVfShapeTypeFXNodeFactory().makeFXNode(record.getSecond());
					//since the canvassAnchorPane starts at (0,0), thus need to translate each shape accordingly based on the upper left corner of the selected layout region;
					//note that by default the layout x and y of a node is at (0,0) for javafx shapes??
					node.setLayoutX(-this.getManager().getLayoutRegionUpperLeftCoord().getX());
					node.setLayoutY(-this.getManager().getLayoutRegionUpperLeftCoord().getY());
					
					this.canvassAnchorPane.getChildren().add(node);
				}
			}
		}
		
		FXUtils.set2Disable(this.startButton, true);
		
		//////////
		if(this.getManager().getAfterLayoutDoneAction()!=null)
			this.getManager().getAfterLayoutDoneAction().run();
	}
	
}
