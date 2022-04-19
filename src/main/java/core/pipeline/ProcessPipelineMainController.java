package core.pipeline;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.ButtonUtils;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;

public class ProcessPipelineMainController {
	public static final String FXML_FILE_DIR_STRING = "/core/pipeline/ProcessPipelineMain.fxml";
	private ProcessPipelineMainManager<?> processManager;
	
	/**
	 * 
	 * @param processManager
	 */
	public void setProcessManager(ProcessPipelineMainManager<?> processManager) {
		this.processManager = processManager;
		
	}
	
	
	/**
	 * @param step
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void setStep(ProcessStepManager<?,?,?> step) throws IOException, SQLException {
		this.getStage().setOnCloseRequest(e->{
			step.getPipelineMainWindowCloseRequestEventHandler().run();
		});
		
		
		///////////////////////
		if(step.isFinalStep()) {
			ButtonUtils.setUnclickable(this.nextButton);
		}else {
			ButtonUtils.setClickable(this.nextButton);
		}
		
		if(step.isFirstStep()) {
			ButtonUtils.setUnclickable(this.backButton);
		}else {
			ButtonUtils.setClickable(this.backButton);
		}
		
		//other
		if(step.isFinalStep()) {
			ButtonUtils.setClickable(this.finishButton);
		}else {
			ButtonUtils.setUnclickable(this.finishButton);
		}
		
		
		///////////////
		this.getStepDescriptionLabel().setText(step.getDescription());
		this.getStepMainContentPane().getChildren().clear();
		this.getStepMainContentPane().getChildren().add(step.getStepRootNode());
		
		
		//set size of the window and the main root pane
		double height = step.getPrefHeight()==null?step.getDefaultHeight():step.getPrefHeight();
		double width = step.getPrefWidth()==null?step.getDefaultWidth():step.getPrefWidth();
		//set the root pane size
		this.rootVBox.setPrefHeight(height);
		this.rootVBox.setPrefWidth(width);
		
		//set the stage size
		this.getStage().setHeight(height);
		this.getStage().setWidth(width);
		
		//set the layout position of the stage on the screen;
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		this.getStage().setX(bounds.getWidth()/2-width/2);
		this.getStage().setY(bounds.getHeight()/2-height/2);
		
//		System.out.println(this.getClass().getSimpleName().concat(": step is set!"));
	}
	
	public VBox getProcessMainRootVBox() {
		return this.rootVBox;
	}
	
	public Pane getStepMainContentPane() {
		return this.stepMainContentVBox;
	}
	
	public Label getStepDescriptionLabel() {
		return this.stepDescriptionLabel;
	}
	
	public Stage getStage() {
		return (Stage) this.stepDescriptionHBox.getScene().getWindow();
	}
	
	////////////////////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	protected VBox rootVBox;
	@FXML
	protected HBox stepDescriptionHBox;
	@FXML
	protected Label stepDescriptionLabel;
	
	@FXML
	protected VBox stepMainContentVBox;
	@FXML
	protected Button backButton;
	@FXML
	protected Button nextButton;
	@FXML
	protected Button finishButton;
	@FXML
	protected Button cancelButton;
	@FXML
	protected Button resetButton;
	
	
	// Event Listener on Button[#backButton].onAction
	/**
	 * 1. save current settings
	 * 2. go to the previous step
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	public void backButtonOnAction(ActionEvent event) throws IOException, SQLException {
//		System.out.println(this.getClass().getSimpleName()+": back");
		
		this.processManager.getCurrentlyOpenedStepManager().back();
	}
	
	
	/**
	 * 1. save the current settings
	 * 2. go to the next step based on current settings
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	public void nextButtonOnAction(ActionEvent event) throws IOException, SQLException {
//		System.out.println(this.getClass().getSimpleName()+": next");
		this.processManager.getCurrentlyOpenedStepManager().next();
	}
	
	/**
	 * finish the process;
	 * 
	 * @param event
	 * @throws Throwable 
	 * @throws IOException 
	 */
	@FXML
	public void finishButtonOnAction(ActionEvent event) throws Throwable {
//		System.out.println(this.getClass().getSimpleName()+": finish");
		
		if(this.processManager.getCurrentlyOpenedStepManager().finish()) {
			this.processManager.closeWindow();
		}
	}
	
	/**
	 * end the whole process
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	public void cancelButtonOnAction(ActionEvent event) throws IOException, SQLException {
//		System.out.println(this.getClass().getSimpleName()+": cancel");
		
		this.processManager.getCurrentlyOpenedStepManager().cancel();
	}
	
	// Event Listener on Button[#resetButton].onAction
	/**
	 * reset the settings of current step;
	 * also reset the previous step index of the selected next step;
	 * 
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	public void resetButtonOnAction(ActionEvent event) throws IOException, SQLException {
//		System.out.println(this.getClass().getSimpleName()+": reset");
		
		this.processManager.getCurrentlyOpenedStepManager().reset();
	}
	
	
}
