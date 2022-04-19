package viewer;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * base class for Viewer of an instance of type T;
 * @author tanxu
 *
 * @param <T>
 * @param <C> type of controller class AbstractViewerController;
 */
public abstract class AbstractViewer<T, C extends AbstractViewerController<T>> {
	private final T value;
	private final  String FXMLFileDirString;
	////////////////////////////////
	private C controller;
	private Stage stage;
	private Scene scene;
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 */
	protected AbstractViewer(T value, String FXMLFileDirString){
		if(value == null)
			throw new IllegalArgumentException("given value cannot be null!");
		if(FXMLFileDirString == null||FXMLFileDirString.isEmpty())
			throw new IllegalArgumentException("given FXMLFileDirString cannot be null or empty!");
		
		//
		
		this.value = value;
		this.FXMLFileDirString = FXMLFileDirString;
	}
	
	
	//////////////////////////////////////////
	/**
	 * build and return the controller;
	 * @return
	 * @throws IOException 
	 */
	public C getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(this.getFXMLFileDirString()));
			
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setViewer(this);
		}
		return this.controller;
	}
	
	/**
	 * show the viewer in a pop up independent window;
	 * 
	 * @param owner the owner Window of the pop-up one.
	 */
	public void show(Window owner) {
		if(this.stage == null) {
			
			this.scene = new Scene(this.getController().getRootContainerPane());
			this.stage = new Stage();
			this.stage.setScene(this.scene);
			
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(owner);
		}
		
		this.stage.show();
	}
	
	///////////////////////////////////
	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return the fXMLFileDirString
	 */
	public String getFXMLFileDirString() {
		return FXMLFileDirString;
	}
}
