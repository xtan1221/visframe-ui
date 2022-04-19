package core.pipeline.cf.steps;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import core.pipeline.AbstractProcessStepManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import function.composition.CompositionFunction;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;

public class ASelectOwnerCFGStepManager extends AbstractProcessStepManager<CompositionFunction, ASelectOwnerCFGStepSettings, ASelectOwnerCFGStepController> {
	private final static String DESCRIPTION = "Select owner CompositionFunctionGroup for the CompositionFunction to be created";
	private final static ASelectOwnerCFGStepSettings DEFAULT_SETTINGS = new ASelectOwnerCFGStepSettings(null);
	
	
	////////////////////
	public static ASelectOwnerCFGStepManager SINGLETON;
	
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
	public static ASelectOwnerCFGStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectOwnerCFGStepManager();
			
			SINGLETON.addNextStep(BMakeCompositionFunctionStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	////////////////////////////////////
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected ASelectOwnerCFGStepManager() {
		super(CompositionFunction.class, ASelectOwnerCFGStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		return this.getPossibleNextStepList().indexOf(BMakeCompositionFunctionStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		throw new UnsupportedOperationException();
	}
	
	
	////////////////////////////////
	private VisframeUDTTypeTableViewerManager<CompositionFunctionGroup,CompositionFunctionGroupID> cfgTableViewManager;
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	VisframeUDTTypeTableViewerManager<CompositionFunctionGroup,CompositionFunctionGroupID> getCFGTableViewManager() throws SQLException{
		if(this.cfgTableViewManager == null) {
			//only allow those CompositionFunctionGroups whose targets are NOT all assigned to existing CompositionFunctions to be selected for the new CompositionFunction;
			Predicate<CompositionFunctionGroup> cfgFilteringCondition = (cfg)->{
				try {
					return this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().
							getCompositionFunctionManager().getTargetNameAssignedCFIDMap(cfg.getID()).size()
							!=cfg.getTargetNameMap().size();
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(1);
				}
				//should never be reached
				return true;
			};
			
			this.cfgTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager(),
							CompositionFunctionGroupBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()),
							null,//sqlFilterConditionString
							cfgFilteringCondition,
							TableViewerMode.SELECTION,
							null
							);
			
			Runnable operationAfterSelectionIsDone = ()->{
				if(cfgTableViewManager.getSelectedItem()==null) {
					this.currentlyAssignedSettings = this.getDefaultSettings();
				}else {
					this.currentlyAssignedSettings = new ASelectOwnerCFGStepSettings(cfgTableViewManager.getSelectedItem());
				}
				try {
					this.getController().setStepSettings(this.currentlyAssignedSettings);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
			
			cfgTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		}
		
		return this.cfgTableViewManager;
	}
}
