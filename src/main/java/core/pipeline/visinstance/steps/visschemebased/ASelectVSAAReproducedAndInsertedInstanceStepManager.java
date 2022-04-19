package core.pipeline.visinstance.steps.visschemebased;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory;
import context.scheme.VisScheme;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceID;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.visinstance.steps.SelectTypeStepManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import visinstance.VisInstance;

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
public class ASelectVSAAReproducedAndInsertedInstanceStepManager extends AbstractProcessStepManager<VisInstance, ASelectVSAAReproducedAndInsertedInstanceStepSettings, ASelectVSAAReproducedAndInsertedInstanceStepController> {
	private final static String DESCRIPTION = "select an Existing VisSchemeAppliedArchiveReproducedAndInsertedInstance";
	private final static ASelectVSAAReproducedAndInsertedInstanceStepSettings DEFAULT_SETTINGS = new ASelectVSAAReproducedAndInsertedInstanceStepSettings(null);
	
	protected static ASelectVSAAReproducedAndInsertedInstanceStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 * @throws SQLException 
	 */
	public static ASelectVSAAReproducedAndInsertedInstanceStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectVSAAReproducedAndInsertedInstanceStepManager();
			
			SINGLETON.addPreviousStep(SelectTypeStepManager.SINGLETON);
			
			SINGLETON.addNextStep(BMakeVisSchemeBasedVisInstanceStepManager.singleton());
			
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
	protected ASelectVSAAReproducedAndInsertedInstanceStepManager() {
		super(VisInstance.class, ASelectVSAAReproducedAndInsertedInstanceStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS,
				300d,
				700d);
		// TODO Auto-generated constructor stub
	}
	

	//	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		return this.getPossibleNextStepList().indexOf(BMakeVisSchemeBasedVisInstanceStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}
	
	
	/////////////////////////////////////////
	private VisframeUDTTypeTableViewerManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance, VisSchemeAppliedArchiveReproducedAndInsertedInstanceID> VSAAReproducedAndInsertedInstanceTableViewManager;
	private VisScheme visScheme;
	private VisSchemeAppliedArchive visSchemeAppliedArchive;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	VisframeUDTTypeTableViewerManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance, VisSchemeAppliedArchiveReproducedAndInsertedInstanceID> getVSAAReproducedAndInsertedInstanceTableViewManager() throws SQLException, IOException {
		if(this.VSAAReproducedAndInsertedInstanceTableViewManager==null) {
			Runnable operationAfterSelectionIsDone = ()->{
				try {
					if(VSAAReproducedAndInsertedInstanceTableViewManager.getSelectedItem()==null) {
						this.currentlyAssignedSettings = this.getDefaultSettings();
						this.visScheme = null;
						this.visSchemeAppliedArchive = null;
					}else {
						this.currentlyAssignedSettings = 
								new ASelectVSAAReproducedAndInsertedInstanceStepSettings(VSAAReproducedAndInsertedInstanceTableViewManager.getSelectedItem());
						this.visScheme = 
								this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().lookup(this.currentlyAssignedSettings.getSelectedVSAAReproducedAndInsertedInstance().getAppliedVisSchemeID());
						this.visSchemeAppliedArchive = 
								this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveManager().lookup(this.currentlyAssignedSettings.getSelectedVSAAReproducedAndInsertedInstance().getApplierArchiveID());
					}
					//update the UI
					this.getController().setStepSettings(this.currentlyAssignedSettings);
					
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
			
			this.VSAAReproducedAndInsertedInstanceTableViewManager = new VisframeUDTTypeTableViewerManager<>(
					this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveReproducedAndInsertedInstanceManager(),
					VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()),
					null,
					null,
					TableViewerMode.SELECTION,
					null
					);
			
			this.VSAAReproducedAndInsertedInstanceTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		
		return VSAAReproducedAndInsertedInstanceTableViewManager;
	}
	

	/**
	 * @return the visScheme
	 */
	VisScheme getVisScheme() {
		return visScheme;
	}

	/**
	 * @return the visSchemeAppliedArchive
	 */
	VisSchemeAppliedArchive getVisSchemeAppliedArchive() {
		return visSchemeAppliedArchive;
	}

}
