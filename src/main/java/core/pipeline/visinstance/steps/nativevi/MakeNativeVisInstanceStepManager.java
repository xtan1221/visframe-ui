package core.pipeline.visinstance.steps.nativevi;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.nativevi.NativeVisInstanceBuilder;
import context.project.process.simple.VisInstanceInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.visinstance.steps.SelectTypeStepManager;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;
import visinstance.NativeVisInstance;
import visinstance.VisInstance;

/**
 * 
 * @author tanxu
 *
 */
public class MakeNativeVisInstanceStepManager extends AbstractProcessStepManager<VisInstance, MakeNativeVisInstanceStepSettings, MakeNativeVisInstanceStepController> {
	private final static String DESCRIPTION = "Build a native VisInstance;";
	private final static MakeNativeVisInstanceStepSettings DEFAULT_SETTINGS = new MakeNativeVisInstanceStepSettings();	
	
	protected static MakeNativeVisInstanceStepManager SINGLETON;
	
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
	public static MakeNativeVisInstanceStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new MakeNativeVisInstanceStepManager();
			
			SINGLETON.addPreviousStep(SelectTypeStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected MakeNativeVisInstanceStepManager() {
		super(VisInstance.class, MakeNativeVisInstanceStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1200d);
	}
	
	/////////////////
	private NativeVisInstanceBuilder nativeVisInstanceBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		
		this.nativeVisInstanceBuilder = new NativeVisInstanceBuilder(
				this.getProcessMainManager().getHostVisProjectDBContext(),
				this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceManager().findNextAvailableUID()
				);
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.nativeVisInstanceBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	
    	////////////
		return this.getController().getRootNode();
		
	}
	
	
	@Override
	public NodeBuilder<NativeVisInstance, ?> getNodeBuilder() {
		return (NodeBuilder<NativeVisInstance, ?>) nativeVisInstanceBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {

		NativeVisInstance nativeVisInstance = this.nativeVisInstanceBuilder.getCurrentValue();
		
		VisInstanceInserter inserter = 
				new VisInstanceInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						nativeVisInstance
						);
		
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
