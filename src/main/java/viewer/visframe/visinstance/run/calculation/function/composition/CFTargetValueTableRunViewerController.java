package viewer.visframe.visinstance.run.calculation.function.composition;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import context.project.VisProjectDBContext;
import core.table.hasIDTypeRelationalTableSchema.HasIDTypeRelationalTableContentViewer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import viewer.AbstractViewerController;
import viewer.visframe.function.composition.CompositionFunctionIDViewer;
import viewer.visframe.rdb.table.AbstractRelationalTableColumnViewer;
import viewer.visframe.visinstance.run.calculation.IndependentFIVTypeIDStringValueMapViewer;
import visinstance.run.calculation.function.composition.CFTargetValueTableRun;

/**
 * 
 * @author tanxu
 *
 */
public class CFTargetValueTableRunViewerController extends AbstractViewerController<CFTargetValueTableRun>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/visinstance/run/calculation/function/composition/CFTargetValueTableRunViewer.fxml";
	
	
	@Override
	protected void setup() {
		//
		CompositionFunctionIDViewer compositionFunctionIDViewer = new CompositionFunctionIDViewer(this.getViewer().getValue().getTargetCompositionFunctionID(), this.getViewer().getHostVisProjectDBContext());
		this.CFIDViewerVBox.getChildren().add(compositionFunctionIDViewer.getController().getRootContainerPane());
		
		//
		this.runUIDTextField.setText(Integer.toString(this.getViewer().getValue().getRunUID()));
		
		//
		IndependentFIVTypeIDStringValueMapViewer independentFIVTypeIDStringValueMapViewer = 
				new IndependentFIVTypeIDStringValueMapViewer(this.getViewer().getValue().getCFDGraphIndependetFIVStringValueMap(), this.getViewer().getHostVisProjectDBContext());
		this.independentFreeInputVariableTypesValueMapViewerVBox.getChildren().add(independentFIVTypeIDStringValueMapViewer.getController().getRootContainerPane());
		
		//
		int i = 1;
		for(SimpleName targetName:this.getViewer().getValue().getTargetNameColumnNameMap().keySet()) {
			SimpleName colName = this.getViewer().getValue().getTargetNameColumnNameMap().get(targetName);
			
			TextField targetTF = new TextField(targetName.getStringValue());
			targetTF.setEditable(false);
			TextField colTF = new TextField(colName.getStringValue());
			colTF.setEditable(false);
			
			this.targetNameColumnNameMapGridPane.add(targetTF, 0, i); //col, row
			this.targetNameColumnNameMapGridPane.add(colTF, 1, i);
			
			i++;
		}
		
		//
		this.setColumnVBox();
	}
	
	
	/**
	 * add viewer UI of each column of data table schema to the columnViewerVBox
	 */
	private void setColumnVBox() {
		for(int i=0;i<this.getViewer().getValue().getValueTableSchema().getOrderedListOfNonRUIDColumn().size();i++) {
			AbstractRelationalTableColumnViewer colViewer = new AbstractRelationalTableColumnViewer(this.getViewer().getValue().getValueTableSchema().getOrderedListOfNonRUIDColumn().get(i),i);
			this.targetValueTableColumnVBox.getChildren().add(colViewer.getController().getRootContainerPane());
		}
	}
	
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public CFTargetValueTableRunViewer getViewer() {
		return (CFTargetValueTableRunViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private VBox CFIDViewerVBox;
	@FXML
	private TextField runUIDTextField;
	@FXML
	private VBox independentFreeInputVariableTypesValueMapViewerVBox;
	@FXML
	private GridPane targetNameColumnNameMapGridPane;
	@FXML
	private VBox targetValueTableColumnVBox;
	@FXML
	private Button viewTargetValueTableContentButton;


	//////////////////////////////
	private HasIDTypeRelationalTableContentViewer CFTargetValueTableViewerManager;
	@FXML
	public void viewTargetValueTableContentButtonOnAction(ActionEvent event) throws IOException, SQLException {
		if(this.CFTargetValueTableViewerManager==null)
			this.CFTargetValueTableViewerManager = 
					new HasIDTypeRelationalTableContentViewer(
							(VisProjectDBContext) this.getViewer().getHostVisProjectDBContext(),
							this.getViewer().getValue(),
							this.getViewer().getValue().getValueTableSchema());
		
		this.CFTargetValueTableViewerManager.showWindow((Stage)this.getRootContainerPane().getScene().getWindow());
	}
}
