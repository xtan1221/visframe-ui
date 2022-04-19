package session;

import javafx.application.Application;
import javafx.stage.Stage;


/**
 * start point of a Visframe session;
 * 
 * @author tanxu
 * 
 */
public class VFSessionStarter extends Application{
	
	VFSessionManager sessionManager;
	
	@Override
	public void start(Stage vfStartStage) throws Exception {
		this.sessionManager = new VFSessionManager(vfStartStage);
		this.sessionManager.init();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
