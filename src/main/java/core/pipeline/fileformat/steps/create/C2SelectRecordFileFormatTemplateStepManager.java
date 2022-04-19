package core.pipeline.fileformat.steps.create;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.fileformat.FileFormatBuilderFactory;
import core.pipeline.AbstractProcessStepManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import fileformat.record.RecordDataFileFormat;
import metadata.DataType;
import sql.derby.TableContentSQLStringFactory;

/**
 * step to select an existing FileFormat as template to build the new FileFormat
 * 
 * previous step is {@link BSelectRecordBuildingModeStepManager};
 * 
 * next step is {@link DBuildRecordFileFormatWithTemplateStepManager}
 * 
 * @author tanxu
 * 
 */
public class C2SelectRecordFileFormatTemplateStepManager extends AbstractProcessStepManager<FileFormat, C2SelectRecordFileFormatTemplateStepSettings, C2SelectRecordFileFormatTemplateStepController> {
	private final static String DESCRIPTION = "select an Existing record File Format as template";
	private final static C2SelectRecordFileFormatTemplateStepSettings DEFAULT_SETTINGS = new C2SelectRecordFileFormatTemplateStepSettings(null);
	
	protected static C2SelectRecordFileFormatTemplateStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 * @throws SQLException 
	 */
	public static C2SelectRecordFileFormatTemplateStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new C2SelectRecordFileFormatTemplateStepManager();
			
			SINGLETON.addPreviousStep(BSelectRecordBuildingModeStepManager.SINGLETON);
			
			SINGLETON.addNextStep(DBuildRecordFileFormatWithTemplateStepManager.singleton());
			
			SINGLETON.finishInitialization();
		}
		
		return SINGLETON;
	}
	
	/////////////////////////////////////////////
	
	/**
	 * constructor
	 * @param possibleNextStepControllerIndexMap
	 * @param possiblePreviousStepControllerIndexMap
	 * @throws SQLException 
	 */
	protected C2SelectRecordFileFormatTemplateStepManager() {
		super(FileFormat.class, C2SelectRecordFileFormatTemplateStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
		
		
	}
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		return this.getPossibleNextStepList().indexOf(DBuildRecordFileFormatWithTemplateStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}

	/////////////////////////////
	private VisframeUDTTypeTableViewerManager<FileFormat,FileFormatID> fileFormatTableViewManager;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	VisframeUDTTypeTableViewerManager<FileFormat,FileFormatID> getFileFormatTableViewManager() throws SQLException, IOException {
		if(this.fileFormatTableViewManager==null) {
			//whenever a RecordDataFileFormat is selected, update the currentlyAssignedSettings
			Runnable operationAfterSelectionIsDone = ()->{
				if(fileFormatTableViewManager.getSelectedItem()==null) {
					this.currentlyAssignedSettings = this.getDefaultSettings();
				}else {
					this.currentlyAssignedSettings = new C2SelectRecordFileFormatTemplateStepSettings((RecordDataFileFormat) fileFormatTableViewManager.getSelectedItem());
				}
				//update UI
				try {
					this.getController().setStepSettings(this.currentlyAssignedSettings);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
			
			this.fileFormatTableViewManager = new VisframeUDTTypeTableViewerManager<>(
					this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getFileFormatManager(),
					FileFormatBuilderFactory.singleton(),
					TableContentSQLStringFactory.buildColumnValueEquityCondition(FileFormatID.TYPE_COLUMN.getName().getStringValue(), DataType.RECORD.toString(), true, true),
					null,
					TableViewerMode.SELECTION,
					null
					);
			
			this.fileFormatTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		return fileFormatTableViewManager;
	}
}
