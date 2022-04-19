package progressmanager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Future;

import context.project.VisProjectDBContext;
import context.project.process.SimpleProcessPerformer;
import context.project.process.logtable.StatusType;
import context.project.process.manager.SimpleProcessManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * note that even though this class is designed to run a single SimpleProcessPerformer, 
 * it can be used to run multiple SimpleProcessPerformers as a chain reaction 
 * by setting the {@link #afterSuccessfullyFinishedAction} properly;
 * 
 * @author tanxu
 *
 */
public class SingleSimpleProcessProgressManager {
	/**
	 * 
	 */
	private final SimpleProcessPerformer<?,?,?> simpleProcessPerformer;
	
	/**
	 * owner stage of the {@link #progressBarWindow};
	 */
	private final Stage primaryStage;
	
	/**
	 * whether or not to allow cancel and roll back during the running of the {@link #simpleProcessPerformer};
	 * if false, the cancel and roll back Button should be invisible;
	 */
	private final boolean allowingCancelAndRollback;
	
	/**
	 * action to take after the process is successfully finished;
	 * note that this will fully replace the {@link #makeDefaultAfterSuccessfullyFinishedAction()};
	 */
	private final Runnable afterSuccessfullyFinishedAction;
	/**
	 * any additional actions to take after the process is aborted and rolled back;
	 * 
	 * note that this action is in addition to the default one defined in {@link #abortAndRollback()};
	 */
	private final Runnable additionalAfterAbortAndRollbackAction;
	
	
	///////////////////
	private Future<StatusType> processFuture;
	
	private SingleSimpleProcessProgressController controller;
	
	private Stage progressBarWindow;
	
//	/**
//	 * whether or not the process of this SingleSimpleProcessProgressManager is done or not;
//	 * should be updated by the {@link #afterSuccessfullyFinishedAction}
//	 */
//	private boolean successfullyDone = false;
	
	/**
	 * 
	 * @param simpleProcessPerformer
	 * @param primaryStage
	 * @param allowingCancelAndRollback 
	 * @param afterSuccessfullyFinishedAction can be null
	 * @param additionalAfterAbortAndRollbackAction can be null
	 */
	public SingleSimpleProcessProgressManager(
			SimpleProcessPerformer<?,?,?> simpleProcessPerformer, 
			Stage primaryStage,
			boolean allowingCancelAndRollback,
			Runnable afterSuccessfullyFinishedAction,
			Runnable additionalAfterAbortAndRollbackAction
			
			){
		//TODO
		
		
		
		this.simpleProcessPerformer = simpleProcessPerformer;
		this.primaryStage = primaryStage;
		this.allowingCancelAndRollback = allowingCancelAndRollback;
		this.afterSuccessfullyFinishedAction = afterSuccessfullyFinishedAction;
		this.additionalAfterAbortAndRollbackAction = additionalAfterAbortAndRollbackAction;
	}
	
	/**
	 * 
	 * @return
	 */
	public SingleSimpleProcessProgressController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(SingleSimpleProcessProgressController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		
		return this.controller;
	}
	
	
	/**
	 * set the runnable
	 * 
	 * first start the simple process with {@link SimpleProcessManager#startNewProcess(SimpleProcessPerformer, Runnable, Runnable, Runnable)};
	 * 		the process will be inserted into process log table with RUNNING status;
	 * 
	 * submit to the executorService and get the future
	 * 		the {@link SimpleProcessPerformer#call()} method will be invoked inside the ExecutorService
	 * 
	 * show the window and wait;
	 * @param toShowProgressBar normally true for process type that may take a while such as DataImporter, Operation, VisInstanceRun (other?) to show the progress bar; false otherwise;
	 * @throws SQLException 
	 */
	public void start(boolean toShowProgressBar) throws SQLException {
		try {
			this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager()
			.getSimpleProcessManager().startNewProcess(this.simpleProcessPerformer);
		}catch(Exception e) {
			e.printStackTrace();
			//discard if the process has been inserted into the process log table
			if(this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager().getMostRecentRunningSimpleProcessUID()!=null &&
					this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager().getMostRecentlyRunningSimpleProcessStatus().equals(StatusType.RUNNING))
				this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager().discardCurrentRunningSimpleProcess();
			
			this.popupDiscardedMessageWindow(e.getMessage());
			return;
		}
		
		
		//****************testing without submit to ExecutorService and not showing the progress bar window******************
		
		if(!toShowProgressBar) {//not show progress bar and not submit to ExecutorService
			try {
				this.simpleProcessPerformer.call();
				
				//
				if(this.afterSuccessfullyFinishedAction!=null) {
					//if non-null afterSuccessfullyFinishedAction is given
					this.afterSuccessfullyFinishedAction.run();
				}else { //no non-null afterSuccessfullyFinishedAction is given, use the default one;
					this.popupSuccessfullyFinishedMessageWindow();
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				//roll back
				this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager().abortAndRollbackCurrentRunningSimpleProcess();
				this.popupSuccessfullyRolledbackMessageWindow();
			}
		}else {//show progress bar and submit to ExecutorService
			if(this.afterSuccessfullyFinishedAction!=null) {
				//if non-null afterSuccessfullyFinishedAction is given
				this.simpleProcessPerformer.setSuccessfullyFinishedAction(
						()->{
							this.closeWindow(); //close the progress window first
							this.afterSuccessfullyFinishedAction.run(); //run the afterSuccessfullyFinishedAction;
						});
			}else { //no non-null afterSuccessfullyFinishedAction is given, use the default one;
				this.simpleProcessPerformer.setSuccessfullyFinishedAction(this.makeDefaultAfterSuccessfullyFinishedAction());
			}
			
			this.processFuture = 
					this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().getExecutorService()
					.submit(this.simpleProcessPerformer);
			
			
			this.showProgressBarWindowAndWait();
		}
	}
	
	/**
	 * pop up window to show the message that caused the process to be discarded
	 * @param message
	 */
	private void popupDiscardedMessageWindow(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("DISCARDED!");
		alert.setContentText(message);
		
		alert.showAndWait();
	}
	
	
	/**
	 * default action to take after the process entity is successfully performed;
	 * 
	 * specifically, close the progress bar window and pop up a new window showing the message;
	 * @return
	 */
	private Runnable makeDefaultAfterSuccessfullyFinishedAction() {
		return ()->{
			System.out.print(this.getClass().getSimpleName()+" :finished, close window");
			this.closeWindow();
//			this.successfullyDone = true;
//			System.out.println("File Format Importing is successfully done");
			this.popupSuccessfullyFinishedMessageWindow();
		};
	}
	
	
	private void popupSuccessfullyFinishedMessageWindow() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("FINISHED!");
		alert.setContentText(simpleProcessPerformer.getProcessTypeManager().getManagedType().getSimpleName().concat(" is successfully finished!"));
		
		alert.showAndWait();
	}
	
	
	/**
	 * close the progress bar window
	 */
	private void closeWindow() {
		progressBarWindow.close();
	}
	
	/**
	 * show the progress bar window and wait until one of the following is met:
	 * 1. terminate and roll back button is clicked
	 * 2. process is successfully finished
	 */
	private void showProgressBarWindowAndWait() {
		progressBarWindow = new Stage();
		
		Scene scene = new Scene(this.getController().getRootNode());
		
		progressBarWindow.setScene(scene);
		progressBarWindow.initModality(Modality.WINDOW_MODAL);
		progressBarWindow.initStyle(StageStyle.UNDECORATED);
		
		progressBarWindow.initOwner(primaryStage);
		//must invoke the showAndWait() method rather than the show() method!!!!
		progressBarWindow.showAndWait();
	}
	
	
	/**
	 * cancel the running process and roll back
	 * then close the progress bar window;
	 * 
	 * note that after invoke the {@link Future#cancel(boolean)} method
	 * need to wait for sometime because 
	 * 		1. the {@link Future#cancel(boolean)} method take some time to terminate the running process and free the db connection from the running process;
	 * 		2. also it seems that after canceled, the db connection will be closed, thus need to reconnect to it if closed, 
	 * 					see {@link VisProjectDBContext#getDBConnection()};
	 * 
	 * @throws SQLException 
	 */
	public void abortAndRollback() throws SQLException {
		this.processFuture.cancel(true);
		
		//wait for some time for the {@link Future#cancel(boolean)} is finished TODO how long is needed???
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		////
		this.simpleProcessPerformer.getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager()
		.getSimpleProcessManager().abortAndRollbackCurrentRunningSimpleProcess();
		
		////run any additional actions 
		if(additionalAfterAbortAndRollbackAction!=null)
			additionalAfterAbortAndRollbackAction.run();
		
		
		///
		this.progressBarWindow.close();
		
		///
		this.popupSuccessfullyRolledbackMessageWindow();
	}
	
	private void popupSuccessfullyRolledbackMessageWindow() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Rolled back!");
		alert.setContentText(simpleProcessPerformer.getProcessTypeManager().getManagedType().getSimpleName().concat(" is successfully rolled bak!"));
		
		alert.showAndWait();
	}

	/**
	 * @return the allowingCancelAndRollback
	 */
	public boolean isAllowingCancelAndRollback() {
		return allowingCancelAndRollback;
	}
//
//	/**
//	 * @return the successfullyDone
//	 */
//	public boolean isSuccessfullyDone() {
//		return successfullyDone;
//	}
}
