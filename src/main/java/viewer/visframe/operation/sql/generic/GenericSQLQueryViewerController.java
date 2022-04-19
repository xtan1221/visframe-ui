package viewer.visframe.operation.sql.generic;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import operation.sql.generic.GenericSQLQuery;
import rdb.table.data.DataTableColumn;
import viewer.AbstractViewerController;
import viewer.visframe.rdb.table.AbstractRelationalTableColumnViewer;

/**
 * 
 * @author tanxu
 *
 */
public class GenericSQLQueryViewerController extends AbstractViewerController<GenericSQLQuery>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/generic/GenericSQLQueryViewer.fxml";
	
	///////////////////////////
	@Override
	protected void setup() {
		this.aliasedSQLQueryStringTextArea.setText(this.getViewer().getValue().getSqlStringWithAliasedIDs());
		
		this.getViewer().getValue().getTableAliasNameMappingMap().forEach((tableAlias, mapping)->{
			DataTableAndColumnsMappingViewer viewer = new DataTableAndColumnsMappingViewer(mapping, this.getViewer().getHostVisframeContext());
			
			this.tableAliasNameMappingsViewerVBox.getChildren().add(viewer.getController().getRootContainerPane());
			
		});
		
		int i = 0;
		for(DataTableColumn col:this.getViewer().getValue().getOrderedListOfColumnInOutputDataTable()) {
			AbstractRelationalTableColumnViewer viewer = new AbstractRelationalTableColumnViewer(col, i);
			this.orderedListOfColumnInOutputDataTableViewersVBox.getChildren().add(viewer.getController().getRootContainerPane());
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
	public GenericSQLQueryViewer getViewer() {
		return (GenericSQLQueryViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextArea aliasedSQLQueryStringTextArea;
	@FXML
	private VBox tableAliasNameMappingsViewerVBox;
	@FXML
	private VBox orderedListOfColumnInOutputDataTableViewersVBox;
	
}
