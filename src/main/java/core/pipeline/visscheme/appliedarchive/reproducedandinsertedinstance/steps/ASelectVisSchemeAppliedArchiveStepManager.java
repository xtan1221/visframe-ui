package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilderFactory;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.VisSchemeAppliedArchiveID;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducerAndInserter;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.ProcessStepController;
import core.pipeline.ProcessStepManager;
import core.pipeline.ProcessStepSettings;
import core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;

public class ASelectVisSchemeAppliedArchiveStepManager 
	extends AbstractProcessStepManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance, ASelectVisSchemeAppliedArchiveStepSettings, ASelectVisSchemeAppliedArchiveStepController> {
	
	private final static String DESCRIPTION = "Select VisSchemeAppliedArchive";
	private final static ASelectVisSchemeAppliedArchiveStepSettings DEFAULT_SETTINGS = new ASelectVisSchemeAppliedArchiveStepSettings();
	
	
	////////////////////
	public static ASelectVisSchemeAppliedArchiveStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this step;
	 * if not built yet;
	 * 
	 * 1. initialize the SINGLETON instance;
	 * 2. add next step (if any) by invoke their singleton() methods which will trigger the initialization chain starting from the root step to every final step;
	 * 3. add previous step (if any) with the SINGLETON field to avoid circular reference
	 * @return
	 * @throws SQLException 
	 */
	public static ASelectVisSchemeAppliedArchiveStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectVisSchemeAppliedArchiveStepManager();
			
			SINGLETON.addNextStep(BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	////////////////////////////////////
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected ASelectVisSchemeAppliedArchiveStepManager() {
		super(VisSchemeAppliedArchiveReproducedAndInsertedInstance.class, ASelectVisSchemeAppliedArchiveStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		return this.getPossibleNextStepList().indexOf(BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager.singleton());
	}
	
	
	/**
	 * need to 
	 * 1. pre-process
	 * 		check if a VisSchemeAppliedArchive is selected
	 * 		save the selected VisSchemeAppliedArchive
	 * 
	 * 2. main
	 * 		1. initialize the VisSchemeAppliedArchiveReproducerAndInserter in the VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager with the selected VisSchemeAppliedArchive;
	 * 		2. start the VisSchemeAppliedArchiveReproducerAndInserter;
	 * 
	 * 3. set current step to the next step
	 */
	@Override
	public void next() throws IOException, SQLException {
		//1 preprocess
		if(!this.getController().validateSettingsToGoToNextStep()) {
			return;
		}
		
		this.saveSettings();
		
		Integer selectedIndex = this.getCurrentlySelectedNextStepIndex();
		
		this.updateSelectedNextStepIndex(selectedIndex);
		
		ProcessStepManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance, ? extends ProcessStepSettings, ? extends ProcessStepController<VisSchemeAppliedArchiveReproducedAndInsertedInstance,?>> nextStep = 
				this.getPossibleNextStepList().get(selectedIndex);
		
		nextStep.setSelectedPreviousStepIndex(this);
		
		/////////////////////2
		//initialize a new VisSchemeAppliedArchiveReproducerAndInserter with the selected VisSchemeAppliedArchive
		VisSchemeAppliedArchiveReproducerAndInserter visSchemeAppliedArchiveReproducerAndInserter = 
				new VisSchemeAppliedArchiveReproducerAndInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(),
						this.getCurrentAssignedSettings().getVisSchemeAppliedArchive()
						);
		
		//start the VisSchemeAppliedArchiveReproducerAndInserter process
		this.getProcessMainManager().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager()
		.getVSAArchiveReproducerAndInserterManager().startNewVSAArchiveReproducerAndInserter(visSchemeAppliedArchiveReproducerAndInserter);
		
		//save the new VisSchemeAppliedArchiveReproducerAndInserter to the VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager
		VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager mainManager = 
				(VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager)this.getProcessMainManager();
		
		mainManager.setVisSchemeAppliedArchiveReproducerAndInserter(visSchemeAppliedArchiveReproducerAndInserter);
		
		//////3
		this.getProcessMainManager().setCurrentlyOpenedStep(nextStep);
	}
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}
	
	///////////////////////////////////////
	private VisframeUDTTypeTableViewerManager<VisSchemeAppliedArchive,VisSchemeAppliedArchiveID> visSchemeAppliedArchiveTableViewManager;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	VisframeUDTTypeTableViewerManager<VisSchemeAppliedArchive,VisSchemeAppliedArchiveID> getVisSchemeAppliedArchiveTableViewManager() throws SQLException{
		if(this.visSchemeAppliedArchiveTableViewManager==null) {
			this.visSchemeAppliedArchiveTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
					this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveManager(),
					VisSchemeAppliedArchiveBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()),
					null,//sqlFilterConditionString
					null,
					TableViewerMode.SELECTION,
					null
					);
			
			Runnable operationAfterSelectionIsDone = ()->{
				if(visSchemeAppliedArchiveTableViewManager.getSelectedItem()==null) {
					this.currentlyAssignedSettings = this.getDefaultSettings();
				}else {
					this.currentlyAssignedSettings = 
							new ASelectVisSchemeAppliedArchiveStepSettings(visSchemeAppliedArchiveTableViewManager.getSelectedItem());
				}
				try {
					this.getController().setStepSettings(this.currentlyAssignedSettings);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
			
			visSchemeAppliedArchiveTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		return this.visSchemeAppliedArchiveTableViewManager;
	}
}
