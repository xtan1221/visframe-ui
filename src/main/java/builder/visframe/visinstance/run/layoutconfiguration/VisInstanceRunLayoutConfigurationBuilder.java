package builder.visframe.visinstance.run.layoutconfiguration;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.run.layoutconfiguration.display.VisInstanceRunLayoutDisplayManager;
import builder.visframe.visinstance.run.layoutconfiguration.previewconfig.VisInstanceRunLayoutConfigurationWrapper;
import builder.visframe.visinstance.run.layoutconfiguration.previewconfig.VisInstanceRunLayoutPreviewAndConfigManager;
import context.project.VisProjectDBContext;
import core.builder.LeafNodeBuilder;
import visinstance.VisInstance;
import visinstance.run.VisInstanceRun;
import visinstance.run.VisInstanceRunID;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;
import image.save.FXNodeSaveAsImageManager;
import utils.FXUtils;

/**
 * 
 * @author tanxu
 * 
 */
public class VisInstanceRunLayoutConfigurationBuilder extends LeafNodeBuilder<VisInstanceRunLayoutConfiguration, VisInstanceRunLayoutConfigurationBuilderEmbeddedUIContentController>{
	public static final String NODE_NAME = "VisInstanceRunLayoutConfiguration";
	public static final String NODE_DESCRIPTION = "VisInstanceRunLayoutConfiguration";
	
	////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	private final VisInstanceRunID visInstanceRunID;
	private final int UID; //for the VisInstanceRunLayoutConfiguration
	/////////////////////////
	private VisInstance visInstance;
	private VisInstanceRun visInstanceRun;
	
	//////////////////////////////////
	///step 1
	private VisInstanceRunLayoutPreviewAndConfigManager previewAndConfigManager;
	private Runnable previewAndConfigManagerAfterChangeMadeAction;
	private final String previewAndConfigManagerBeforeChangeMadeWarningInforText = "perform this will clear any laid out graphical visualization in step 2 (if already finished)!";
	
	///step 2
	private VisInstanceRunLayoutDisplayManager displayManager;
	private Runnable afterLayoutDisplayDoneAction;
	
	///step 3
	private FXNodeSaveAsImageManager FXNodeSaveAsImageManager;
	
	/////////////////
	/**
	 * whether the target VisSchemeAppliedArchive is finishable to be inserted into the host VisProjectDBContext;
	 */
	private boolean finishable;
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public VisInstanceRunLayoutConfigurationBuilder(
			VisProjectDBContext hostVisProjectDBContext, 
			VisInstanceRunID visInstanceRunID, 
			int UID
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, VisInstanceRunLayoutConfigurationBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		if(visInstanceRunID==null)
			throw new IllegalArgumentException("given visInstanceRunID cannot be null!");
		
		
		/////////////////////
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.visInstanceRunID = visInstanceRunID;
		this.UID = UID;
		
		////////////////////
		this.initialize();
	}
	
	/**
	 * 
	 * initialize the first step
	 * 		{@link #previewAndConfigManager}
	 * 
	 * 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void initialize() throws SQLException, IOException {
		this.visInstanceRun = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceRunManager().lookup(this.visInstanceRunID);
		
		this.visInstance = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceManager().lookup(this.visInstanceRun.getVisInstanceID());
				
		//
		this.setToNewAndActivateUIOfPrevoiusAndConfigurationStep();
	}
	
	/**
	 * 
	 * @return
	 */
	Runnable getPreviewAndConfigManagerAfterChangeMadeAction() {
		if(this.previewAndConfigManagerAfterChangeMadeAction==null) {
			this.previewAndConfigManagerAfterChangeMadeAction = ()->{
				try {
					if(this.previewAndConfigManager.currentConfigurationIsFinishable()) {
						//FINISH button clicked;
						this.getEmbeddedUIContentController().step1StatusLabel.setText("FINISHED");
						
						this.setToNewAndActivateUIOfLayoutDisplayStep();
						
						//set to finishable
						this.setToFinishable();
						
					}else {//this should never occur
						throw new UnsupportedOperationException("not implemented yet; should never occur?");
					}
					
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					System.exit(1);//debug
				}
			};
		}
		return this.previewAndConfigManagerAfterChangeMadeAction;
	}
	
	/**
	 * activate the save as image step manager;
	 * @return
	 */
	Runnable getAfterLayoutDisplayDoneAction() {
		if(this.afterLayoutDisplayDoneAction==null) {
			this.afterLayoutDisplayDoneAction = ()->{
				//
				this.getEmbeddedUIContentController().step2StatusLabel.setText("FINISHED");
				
				try {
					this.setToNewAndActivateUIOfSaveAsImageStep();
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(1);
				}
			};
		}
		
		return this.afterLayoutDisplayDoneAction;
	}
	
	//////////////////////////////chains of reactions
	///////////step 1
	void setToNewAndActivateUIOfPrevoiusAndConfigurationStep() throws SQLException, IOException {
		this.previewAndConfigManager = 
				new VisInstanceRunLayoutPreviewAndConfigManager(
						this.getHostVisProjectDBContext(),
						this.visInstance,
						this.visInstanceRun,
						this.previewAndConfigManagerBeforeChangeMadeWarningInforText,
						this.getPreviewAndConfigManagerAfterChangeMadeAction());
		
		//set the UI to this step
		this.getEmbeddedUIContentController().showStep1ButtonOnAction(null);
		
		this.setToNullAndDeactivateUIOfLayoutDisplayStep();
	}
	
	///////////step 2
	private void setToNewAndActivateUIOfLayoutDisplayStep() throws SQLException, IOException {
		VisInstanceRunLayoutConfigurationWrapper configurationWrapper = this.getPreviewAndConfigManager().getCurrentVisInstanceRunLayoutConfigurationWrapper();
		
		this.displayManager = new VisInstanceRunLayoutDisplayManager(
				this.getHostVisProjectDBContext(),
				this.visInstanceRun,
				this.getAfterLayoutDisplayDoneAction(),
				configurationWrapper.getSelectedCoreShapeCFGIDListInLayoutOrder(),
				configurationWrapper.getLayoutRegionUpperLeftCoord(),//Point2D layoutRegionUpperLeftCoord,
				configurationWrapper.getLayoutRegionBottomRightCoord()//Point2D layoutRegionBottomRightCoord
				);
		
		//2
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step2RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep2Button, false);
		this.getEmbeddedUIContentController().step2StatusLabel.setText("UNFINISHED");
		
		//3
		this.setToNullAndDeactivateUIOfSaveAsImageStep();
	}
	
	//
	private void setToNullAndDeactivateUIOfLayoutDisplayStep() {
		if(this.displayManager!=null) {
			this.displayManager = null;
			//
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step2RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep2Button, true);
			//
			this.setToNullAndDeactivateUIOfSaveAsImageStep();
		}
	}
	
	
	//////////step 3
	private void setToNewAndActivateUIOfSaveAsImageStep() throws SQLException {
		this.FXNodeSaveAsImageManager = 
				new FXNodeSaveAsImageManager(
						this.displayManager.getController().getCanvassAnchorPane(),
						this.displayManager.getLayoutRegionWidth(),
						this.displayManager.getLayoutRegionHeight());
		
		//2
		FXUtils.set2Disable(this.getEmbeddedUIContentController().step3RootContainerVBox, false);
		FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep3Button, false);
		
	}
	
	private void setToNullAndDeactivateUIOfSaveAsImageStep() {
		if(this.FXNodeSaveAsImageManager!=null) {
			this.FXNodeSaveAsImageManager = null;
			//
			FXUtils.set2Disable(this.getEmbeddedUIContentController().step3RootContainerVBox, true);
			FXUtils.set2Disable(this.getEmbeddedUIContentController().showStep3Button, true);
			//
		}
	}
	
	/////////////////////////////////////
	///////////////////////
	private void setToFinishable() {
		this.finishable = true;
		this.getEmbeddedUIContentController().finishableLabel.setText("FINISHABLE");
	}
	
	
	/**
	 * @return the finishable
	 */
	public boolean isFinishable() {
		return finishable;
	}
	
	////////////////////////////
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}


	/**
	 * @return the visInstanceRunID
	 */
	public VisInstanceRunID getVisInstanceRunID() {
		return visInstanceRunID;
	}

	/**
	 * @return the uID
	 */
	public int getUID() {
		return UID;
	}

	/**
	 * @return the visInstance
	 */
	public VisInstance getVisInstance() {
		return visInstance;
	}

	/**
	 * @return the visInstanceRun
	 */
	public VisInstanceRun getVisInstanceRun() {
		return visInstanceRun;
	}

	/**
	 * @return the previewAndConfigManager
	 */
	public VisInstanceRunLayoutPreviewAndConfigManager getPreviewAndConfigManager() {
		return previewAndConfigManager;
	}

	/**
	 * @return the displayManager
	 */
	public VisInstanceRunLayoutDisplayManager getDisplayManager() {
		return displayManager;
	}

	/**
	 * @return the fXNodeSaveAsImageManager
	 */
	public FXNodeSaveAsImageManager getFXNodeSaveAsImageManager() {
		return FXNodeSaveAsImageManager;
	}

}
