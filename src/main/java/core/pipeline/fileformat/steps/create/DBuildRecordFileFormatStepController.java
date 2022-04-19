package core.pipeline.fileformat.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import fileformat.FileFormat;
import fileformat.record.RecordDataFileFormat;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DBuildRecordFileFormatStepController extends AbstractProcessStepController<FileFormat,DBuildRecordFileFormatStepSettings>{

	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/create/DBuildRecordFileFormatStep.fxml";
	
	@Override
	public void setStepSettings(DBuildRecordFileFormatStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getRecordDataFileFormat(),settings.getRecordDataFileFormat()==null?true:false);
	}
	
	@Override
	public DBuildRecordFileFormatStepSettings getStepSettings() throws IOException {
		return new DBuildRecordFileFormatStepSettings((RecordDataFileFormat)this.getManager().getNodeBuilder().getCurrentValue());
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		return builderEmbeddedRootNodeVBox;
	}

	@Override
	public Pane getRootNode() {
		return rootVBox;
	}
	///////////////////////
	@FXML
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private VBox builderEmbeddedRootNodeVBox;



}
