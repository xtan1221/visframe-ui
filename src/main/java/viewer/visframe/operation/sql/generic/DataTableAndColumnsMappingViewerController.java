package viewer.visframe.operation.sql.generic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import operation.sql.generic.DataTableAndColumnsMapping;
import viewer.AbstractViewerController;
import viewer.visframe.metadata.MetadataIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class DataTableAndColumnsMappingViewerController extends AbstractViewerController<DataTableAndColumnsMapping>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/generic/DataTableAndColumnsMappingViewer.fxml";
	
	///////////////////////////
	@Override
	protected void setup() {
		this.tableAliasNameTextField.setText(this.getViewer().getValue().getTableAliasName());
		
		MetadataIDViewer ownerRecordMetadataIDViewer = new MetadataIDViewer(this.getViewer().getValue().getRecordMetadataID(), this.getViewer().getHostVisframeContext());
		this.ownerRecordMetadataIDViewerHBox.getChildren().add(ownerRecordMetadataIDViewer.getController().getRootContainerPane());
		
		int rowIndex = 1;
		for(String colAlias:this.getViewer().getValue().getColumnAliasNameDataTableColumnNameMap().keySet()) {
			TextField colAliasTf = new TextField();
			colAliasTf.setText(colAlias);
			colAliasTf.setEditable(false);
			
			TextField dataTableColNameTf = new TextField();
			dataTableColNameTf.setText(this.getViewer().getValue().getColumnAliasNameDataTableColumnNameMap().get(colAlias).getStringValue());
			dataTableColNameTf.setEditable(false);
			
			this.columnAliasNameDataTableColumnNameMapGridPane.add(colAliasTf, 0, rowIndex);
			this.columnAliasNameDataTableColumnNameMapGridPane.add(dataTableColNameTf, 1, rowIndex);
			
			rowIndex++;
		}
		
	}
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerVBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public DataTableAndColumnsMappingViewer getViewer() {
		return (DataTableAndColumnsMappingViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}

	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField tableAliasNameTextField;
	@FXML
	private HBox ownerRecordMetadataIDViewerHBox;
	@FXML
	private GridPane columnAliasNameDataTableColumnNameMapGridPane;
}
