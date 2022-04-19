package core.table.hasIDTypeRelationalTableSchema.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import export.CommentSection;
import export.HasIDRelationTableContentExporter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.FileIOUtils;

public final class ExporterController {
	public static final String FXML_FILE_DIR_STRING = "/core/table/hasIDTypeRelationalTableSchema/export/Exporter.fxml";
	
	///////////////////////
	private ExporterManager manager;
	
	/**
	 * 
	 * @param manager
	 * @throws SQLException
	 */
	void setManager(ExporterManager manager) throws SQLException {
		this.manager = manager;
	}
	
	
	/////////////////////////
	ExporterManager getManager() {
		return this.manager;
	}
	
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	///////////////////////////////////////////////////	
	@FXML
	public void initialize() {

	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField outputFilePathStringTextField;
	@FXML
	private Button chooseOutputPathButton;
	@FXML
	private TextField outputFileNameTextField;
	@FXML
	private CheckBox toIncludeInforSectionCheckBox;
	@FXML
	private CheckBox toIncludeHeaderLineCheckBox;
	@FXML
	private CheckBox toIncludeRUIDColumnCheckBox;
	@FXML
	private Button exportButton;

	// Event Listener on Button[#chooseOutputPathButton].onAction
	@FXML
	public void chooseOutputPathButtonOnAction(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		
		File selectedDirectory = directoryChooser.showDialog(this.getRootContainerPane().getScene().getWindow());
		
		this.outputFilePathStringTextField.setText(selectedDirectory.toString());
	}
	

	/**
	 * build a HasIDRelationTableContentExporter and invoke its run method
	 * 
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	public void exportButtonOnAction(ActionEvent event) throws SQLException, IOException {
		//////first check if the file name is valid or not;
		try {
			FileIOUtils.validateFileName(this.outputFileNameTextField.getText());
		}catch(InvalidPathException e){
			AlertUtils.popAlert("invalid file name", e.getMessage());
			
			return;
		}
		
		//////////////////
		Path outputFilePath = 
				FileIOUtils.buildFilePath(
						this.outputFilePathStringTextField.getText(), 
						this.outputFileNameTextField.getText(), 
						HasIDRelationTableContentExporter.OUTPUT_FILE_FORMAT);
		
		if(outputFilePath==null)
			return;
		////////////////////////////////////		
		CommentSection commentSection = new CommentSection(
				this.getManager().getHasIDTypeRelationalTableContentViewer().getHostVisProjectDBContext().getName().getStringValue(),//String projectName,
				this.getManager().getHasIDTypeRelationalTableContentViewer().getTableSourceVisframeUDTObject().toString(),//String dataSource,
				this.getManager().getHasIDTypeRelationalTableContentViewer().getColumnNameASCSortedMap(),//LinkedHashMap<String, Boolean> columnNameASCSortedMap,
				this.getManager().getHasIDTypeRelationalTableContentViewer().allRowSelected(),//boolean allRowsIncluded,
				this.getManager().getHasIDTypeRelationalTableContentViewer().getSelectedRowNum(),//int totalRowNum,
				this.getManager().getHasIDTypeRelationalTableContentViewer().getStartRowIndex(), //int startRowIndex,
				this.getManager().getHasIDTypeRelationalTableContentViewer().getEndRowIndex() //int endRowIndex
				);
		
		//////////////////////////
		List<String> nonRUIDHeaderColumnNameList = new ArrayList<>();
		this.getManager().getHasIDTypeRelationalTableContentViewer().getHasIDTypeRelationalTableSchema().getOrderedListOfNonRUIDColumn().forEach(col->{
			nonRUIDHeaderColumnNameList.add(col.getName().getStringValue());
		});
		
		///////////////////////////
		HasIDRelationTableContentExporter exporter = new HasIDRelationTableContentExporter(
				this.getManager().getHasIDTypeRelationalTableContentViewer().getController().query(true),//ResultSet resultSet,
				nonRUIDHeaderColumnNameList,//List<String> headerColumnNameList,
				this.toIncludeInforSectionCheckBox.isSelected(),//boolean toAddInforSection
				commentSection,//CommentSection CommentSection,
				this.toIncludeHeaderLineCheckBox.isSelected(),//boolean toAddHeaderLine,
				this.toIncludeRUIDColumnCheckBox.isSelected(),//boolean toIncludeRUIDColumn 
				outputFilePath//Path outputFilePath
				);
		
		
		/////////////////
		exporter.run();
		
		//
		AlertUtils.popConfirmationDialog(
				"Finished", 
				"",  //TODO
				"Table content is successfully exported to file!"); //TODO
		
		Stage stage = ((Stage)this.getRootContainerPane().getScene().getWindow());
		stage.close();
	}
	
}
