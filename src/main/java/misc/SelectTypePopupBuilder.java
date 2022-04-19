package misc;

import java.io.IOException;
import java.util.Set;
import java.util.function.Function;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SelectTypePopupBuilder<T> {
	private final Stage initWindow;
	private final Function<T,String> toStringFunction;
	private final Set<T> pool;
	
	
	/////////////////
	private Stage stage;
	
	private T selectedType;
	
	private SelectTypePopupController<T> controller;
	
	
	public SelectTypePopupBuilder(Stage initWindow, Function<T,String> toStringFunction, Set<T> pool){
		this.initWindow = initWindow;
		this.toStringFunction = toStringFunction;
		this.pool = pool;
	}
	
	void setSelectedType(T t) {
		this.selectedType = t;
	}
	
	@SuppressWarnings("unchecked")
	SelectTypePopupController<T> getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(SelectTypePopupController.FXML_FILE_DIR_STRING));
			
	    	try {
				loader.load();
		    	this.controller = (SelectTypePopupController<T>)loader.getController();
		    	this.controller.setOwnerBuilder(this);
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		return controller;
	}
	
	/**
	 * 
	 */
	public T showAndWait() {
		if(this.stage==null) {
			Scene scene = new Scene(this.getController().getRootContainerNode());
			this.stage = new Stage();
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(this.initWindow);
			
			this.stage.setScene(scene);
			this.stage.initStyle(StageStyle.UNDECORATED);
		}
		this.stage.showAndWait();
		
		
		return this.selectedType;
	}
	
	public T getSelectedType() {
		return selectedType;
	}
	
	Set<T> getPool() {
		return pool;
	}

	Function<T,String> getToStringFunction() {
		return toStringFunction;
	}

	Stage getPopupStage() {
		return this.stage;
	}
	
}