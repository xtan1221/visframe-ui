package core.pipeline.visinstance.run.steps;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.visinstance.VisInstanceBuilderFactory;
import core.pipeline.AbstractProcessStepManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import visinstance.VisInstance;
import visinstance.VisInstanceID;
import visinstance.run.VisInstanceRun;

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
public class ASelectVisInstanceStepManager extends AbstractProcessStepManager<VisInstanceRun, ASelectVisInstanceStepSettings, ASelectVisInstanceStepController> {
	private final static String DESCRIPTION = "select an Existing VisSchemeAppliedArchiveReproducedAndInsertedInstance";
	private final static ASelectVisInstanceStepSettings DEFAULT_SETTINGS = new ASelectVisInstanceStepSettings(null);
	
	protected static ASelectVisInstanceStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 * @throws SQLException 
	 */
	public static ASelectVisInstanceStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectVisInstanceStepManager();
			
			SINGLETON.addNextStep(BMakeVisInstanceRunStepManager.singleton());
			
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
	protected ASelectVisInstanceStepManager() {
		super(VisInstanceRun.class, ASelectVisInstanceStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS,
				300d,
				700d);
		// TODO Auto-generated constructor stub
		
		
	}
	
	////////////////////
	private VisframeUDTTypeTableViewerManager<VisInstance, VisInstanceID> visInstanceTableViewManager;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	VisframeUDTTypeTableViewerManager<VisInstance, VisInstanceID> getVisInstanceTableViewManager() throws SQLException, IOException {
		if(this.visInstanceTableViewManager==null) {
			//whenever a VisSchemeAppliedArchiveReproducedAndInsertedInstance is selected, update the currentlyAssignedSettings
			Runnable operationAfterSelectionIsDone = ()->{
				try {
					if(visInstanceTableViewManager.getSelectedItem()==null) {
						this.currentlyAssignedSettings = this.getDefaultSettings();
					}else {
						this.currentlyAssignedSettings = 
								new ASelectVisInstanceStepSettings(visInstanceTableViewManager.getSelectedItem());
					}
					this.getController().setStepSettings(this.currentlyAssignedSettings);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
			
			this.visInstanceTableViewManager = new VisframeUDTTypeTableViewerManager<>(
					this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceManager(),
					VisInstanceBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()),
					null,
					null,
					TableViewerMode.SELECTION,
					null
					);
			
			this.visInstanceTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		
		return visInstanceTableViewManager;
	}
	
	//
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		return this.getPossibleNextStepList().indexOf(BMakeVisInstanceRunStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}


}
