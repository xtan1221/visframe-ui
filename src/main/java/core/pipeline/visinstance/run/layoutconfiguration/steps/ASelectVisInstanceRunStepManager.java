package core.pipeline.visinstance.run.layoutconfiguration.steps;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.visinstance.run.VisInstanceRunBuilderFactory;
import core.pipeline.AbstractProcessStepManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import visinstance.run.VisInstanceRun;
import visinstance.run.VisInstanceRunID;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

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
public class ASelectVisInstanceRunStepManager extends AbstractProcessStepManager<VisInstanceRunLayoutConfiguration, ASelectVisInstanceRunStepSettings, ASelectVisInstanceRunStepController> {
	private final static String DESCRIPTION = "select an Existing VisSchemeAppliedArchiveReproducedAndInsertedInstance";
	private final static ASelectVisInstanceRunStepSettings DEFAULT_SETTINGS = new ASelectVisInstanceRunStepSettings(null);
	
	protected static ASelectVisInstanceRunStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 * @throws SQLException 
	 */
	public static ASelectVisInstanceRunStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectVisInstanceRunStepManager();
			
			SINGLETON.addNextStep(BMakeVisInstanceRunLayoutConfigurationStepManager.singleton());
			
			SINGLETON.finishInitialization();
		}
		
		return SINGLETON;
	}
	
	/////////////////////////////////////////////
	private VisframeUDTTypeTableViewerManager<VisInstanceRun, VisInstanceRunID> visInstanceRunTableViewManager;
	
	/**
	 * constructor
	 * @param possibleNextStepControllerIndexMap
	 * @param possiblePreviousStepControllerIndexMap
	 * @throws SQLException 
	 */
	protected ASelectVisInstanceRunStepManager() {
		super(VisInstanceRunLayoutConfiguration.class, ASelectVisInstanceRunStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS,
				300d,
				700d);
		// TODO Auto-generated constructor stub
		
	}
	
	public VisframeUDTTypeTableViewerManager<VisInstanceRun, VisInstanceRunID> getVisInstanceRunTableViewManager() throws SQLException, IOException {
		if(this.visInstanceRunTableViewManager==null) {
			//whenever a VisSchemeAppliedArchiveReproducedAndInsertedInstance is selected, update the currentlyAssignedSettings
			Runnable operationAfterSelectionIsDone = ()->{
				try {
					if(visInstanceRunTableViewManager.getSelectedItem()==null) {
						this.currentlyAssignedSettings = this.getDefaultSettings();
					}else {
						this.currentlyAssignedSettings = 
								new ASelectVisInstanceRunStepSettings(visInstanceRunTableViewManager.getSelectedItem());
					}
					//update UI
					this.getController().setStepSettings(this.currentlyAssignedSettings);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
			
			this.visInstanceRunTableViewManager = new VisframeUDTTypeTableViewerManager<>(
					this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceRunManager(),
					VisInstanceRunBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()),
					null,
					null,
					TableViewerMode.SELECTION,
					null
					);
			
			this.visInstanceRunTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		
		return visInstanceRunTableViewManager;
	}
	
	//
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		return this.getPossibleNextStepList().indexOf(BMakeVisInstanceRunLayoutConfigurationStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}


}
