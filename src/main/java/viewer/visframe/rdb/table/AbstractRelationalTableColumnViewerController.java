package viewer.visframe.rdb.table;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import rdb.table.AbstractRelationalTableColumn;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class AbstractRelationalTableColumnViewerController extends AbstractViewerController<AbstractRelationalTableColumn>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/rdb/table/AbstractRelationalTableColumnViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.indexTextField.setText(Integer.toString(this.getViewer().getIndex()));
		
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		
		this.dataTypeTextField.setText(this.getViewer().getValue().getSqlDataType().getSQLString());
		
		this.inPrimaryKeyCheckBox.setSelected(this.getViewer().getValue().isInPrimaryKey());
		
		if(this.getViewer().getValue().isUnique()==null) {
			this.uniqueCheckBox.setIndeterminate(true);
		}else {
			this.uniqueCheckBox.setSelected(this.getViewer().getValue().isUnique());
		}
		
		if(this.getViewer().getValue().isNotNull()==null) {
			this.notNullCheckBox.setIndeterminate(true);
		}else {
			this.notNullCheckBox.setSelected(this.getViewer().getValue().isNotNull());
		}
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public AbstractRelationalTableColumnViewer getViewer() {
		return (AbstractRelationalTableColumnViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField indexTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField dataTypeTextField;
	@FXML
	private CheckBox inPrimaryKeyCheckBox;
	@FXML
	private CheckBox uniqueCheckBox;
	@FXML
	private CheckBox notNullCheckBox;
}
