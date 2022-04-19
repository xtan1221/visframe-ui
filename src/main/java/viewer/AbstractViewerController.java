package viewer;

import javafx.fxml.FXML;
import javafx.scene.Parent;

public abstract class AbstractViewerController<T> {
	
	protected AbstractViewer<T,?> viewer;
	
	/**
	 * @param viewer
	 */
	void setViewer(AbstractViewer<T,?> viewer) {
		this.viewer = viewer;
		this.setup();
	}
	

	/**
	 * @return the viewer
	 */
	public abstract AbstractViewer<T, ?> getViewer();
	
	/**
	 * 
	 */
	protected abstract void setup();
	
	/**
	 * 
	 */
	public abstract Parent getRootContainerPane();
	
	
	//////////////////////////////////////
	@FXML
	public abstract void initialize();

	
}
