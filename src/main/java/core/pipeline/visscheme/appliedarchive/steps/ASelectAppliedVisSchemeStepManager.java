package core.pipeline.visscheme.appliedarchive.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import context.scheme.VisScheme;
import context.scheme.VisSchemeID;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.AbstractProcessStepManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;

public class ASelectAppliedVisSchemeStepManager extends AbstractProcessStepManager<VisSchemeAppliedArchive, ASelectAppliedVisSchemeStepSettings, ASelectAppliedVisSchemeStepController> {
	private final static String DESCRIPTION = "Select applied VisScheme";
	private final static ASelectAppliedVisSchemeStepSettings DEFAULT_SETTINGS = new ASelectAppliedVisSchemeStepSettings();
	
	
	////////////////////
	public static ASelectAppliedVisSchemeStepManager SINGLETON;
	
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
	public static ASelectAppliedVisSchemeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectAppliedVisSchemeStepManager();
			
			SINGLETON.addNextStep(BMakeVisSchemeAppliedArchiveStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	////////////////////////////////////
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected ASelectAppliedVisSchemeStepManager() {
		super(VisSchemeAppliedArchive.class, ASelectAppliedVisSchemeStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		return this.getPossibleNextStepList().indexOf(BMakeVisSchemeAppliedArchiveStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}
	
	////////////////////////////////////////////
	/**
	 * 
	 */
	private VisframeUDTTypeTableViewerManager<VisScheme,VisSchemeID> visSchemeTableViewManager;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	VisframeUDTTypeTableViewerManager<VisScheme,VisSchemeID> getVisSchemeTableViewManager() throws SQLException, IOException {
		if(this.visSchemeTableViewManager==null) {
			this.visSchemeTableViewManager = 
				new VisframeUDTTypeTableViewerManager<>(
						this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager(),
						VisSchemeBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()),
						null,//sqlFilterConditionString
						null,
						TableViewerMode.SELECTION,
						null
						);
		
			Runnable operationAfterSelectionIsDone = ()->{
				if(visSchemeTableViewManager.getSelectedItem()==null) {
					this.currentlyAssignedSettings = this.getDefaultSettings();
				}else {
					this.currentlyAssignedSettings = new ASelectAppliedVisSchemeStepSettings(visSchemeTableViewManager.getSelectedItem());
				}
				try {
					this.getController().setStepSettings(this.currentlyAssignedSettings);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			};
			
			visSchemeTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		return this.visSchemeTableViewManager;
	}
	
}
