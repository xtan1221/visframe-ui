package core.pipeline.cfg.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.group.AbstractCompositionFunctionGroupBuilder;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import context.project.process.simple.CompositionFunctionGroupInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import function.group.AbstractCompositionFunctionGroup;
import function.group.CompositionFunctionGroup;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeCompositionFunctionGroupStepManager extends AbstractProcessStepManager<CompositionFunctionGroup, BMakeCompositionFunctionGroupStepManagerSettings, BMakeCompositionFunctionGroupStepController>{
	private final static String DESCRIPTION = "Build the CompositionFunctionGroup instance;";
	private final static BMakeCompositionFunctionGroupStepManagerSettings DEFAULT_SETTINGS = new BMakeCompositionFunctionGroupStepManagerSettings(null);	
	
	protected static BMakeCompositionFunctionGroupStepManager SINGLETON;
	
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
	public static BMakeCompositionFunctionGroupStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeCompositionFunctionGroupStepManager();
			
			SINGLETON.addPreviousStep(ASelectTypeAndTemplateStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeCompositionFunctionGroupStepManager() {
		super(CompositionFunctionGroup.class, BMakeCompositionFunctionGroupStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
	}
	
	/////////////////
	private AbstractCompositionFunctionGroupBuilder<? extends AbstractCompositionFunctionGroup> cfgBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		ASelectTypeAndTemplateStepManager prestep = (ASelectTypeAndTemplateStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		
    	//initialize the node builder and add it to the step content UI
		this.cfgBuilder = CompositionFunctionGroupBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext()).
				build(prestep.getCurrentAssignedSettings().getSelectedCompositionFunctionGroupType());
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.cfgBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	//if template is selected, set the step settings to the selected operation template
    	if(prestep.getCurrentAssignedSettings().getSelectedTemplateCompositionFunctionGroupID()!=null) {
    		System.out.println("template selected");
			try {
				CompositionFunctionGroup template = 
						this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(prestep.getCurrentAssignedSettings().getSelectedTemplateCompositionFunctionGroupID());
				
				//this will set the value of the operationBuilder to the selected template
				this.getController().setStepSettings(new BMakeCompositionFunctionGroupStepManagerSettings(template));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
    	}
    	
    	////////////
		return this.getController().getRootNode();
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public NodeBuilder<CompositionFunctionGroup, ?> getNodeBuilder() {
		return (NodeBuilder<CompositionFunctionGroup, ?>) cfgBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {
		CompositionFunctionGroupInserter inserter = 
				new CompositionFunctionGroupInserter(this.getProcessMainManager().getHostVisProjectDBContext(), this.getNodeBuilder().getCurrentValue());
		

		//set the init window of the progress bar to the stage of the visframe session;
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						inserter, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
		
		//
		progressManager.start(false);
		
		return true;
	}
	
	///////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
}
