package core.pipeline.dataimporter.steps.graph;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.importer.graph.GraphDataImporterBaseBuilder;
import builder.visframe.importer.graph.GraphDataImporterBaseBuilderFactory;
import context.project.process.simple.DataImporterPerformer;
import core.pipeline.AbstractProcessStepManager;
import importer.DataImporter;
import importer.graph.GraphDataImporterBase;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;

/**
 * step to build a sub-type instance of {@link GraphDataImporterBase} based on the selected {@link GraphDataFileFormatType} in previous step;
 * 
 * @author tanxu
 * 
 */
public class BMakeGraphDataImporterStepManager extends AbstractProcessStepManager<DataImporter, BMakeGraphDataImporterStepSettings, BMakeGraphDataImporterStepController>{
	private final static String DESCRIPTION = "Build the GraphDataImporter;";
	private final static BMakeGraphDataImporterStepSettings DEFAULT_SETTINGS = new BMakeGraphDataImporterStepSettings(null);	
	
	protected static BMakeGraphDataImporterStepManager SINGLETON;
	
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
	public static BMakeGraphDataImporterStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeGraphDataImporterStepManager();
			
			SINGLETON.addPreviousStep(ASelectGraphFileFormatTypeStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	/**
	 * constructor
	 */
	protected BMakeGraphDataImporterStepManager() {
		super(DataImporter.class, BMakeGraphDataImporterStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	
	/////////////////////GraphDataImporterBaseBuilder specific
	private GraphDataImporterBaseBuilder<? extends GraphDataImporterBase> graphDataImporterBaseBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		
//		if(this.stepRootNode==null) {//first time invoked
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getFXMLFileDirString()));
//	    	//this must be invoked before the getController() method!!!!!!!
//	    	this.stepRootNode = loader.load();
//	    	
//	    	this.controller = (BMakeGraphDataImporterStepController)loader.getController();
	    	
	    	//
	    	
//	    	this.controller.setManager(this);
//		}
		
		
		//first time invoked
		if(this.controller==null) {
			this.graphDataImporterBaseBuilder = GraphDataImporterBaseBuilderFactory.make(
	    			((ASelectGraphFileFormatTypeStepSettings)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex()).getCurrentAssignedSettings()).getSelectedType(), 
	    			this.getProcessMainManager().getHostVisProjectDBContext());
			
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	    	
		}
		
		
		this.getController().setStepSettings(this.getCurrentAssignedSettings());
		
		return this.getController().getRootNode();
		
	}
	
	
	@Override
	public GraphDataImporterBaseBuilder<? extends GraphDataImporterBase> getNodeBuilder() {
		return graphDataImporterBaseBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {
		DataImporterPerformer performer = new DataImporterPerformer(this.getProcessMainManager().getHostVisProjectDBContext(), this.getNodeBuilder().getCurrentValue());
		
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						performer, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
				
		progressManager.start(true);
		
		return true;
	}
	
	///////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}

}
