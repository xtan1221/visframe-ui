package viewer.visframe.function.target;

import function.target.CFGTarget;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class CFGTargetViewerControllerBase<T extends CFGTarget<?>> extends AbstractViewerController<T>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public CFGTargetViewerBase<T,?> getViewer() {
		return (CFGTargetViewerBase<T,?>)this.viewer;
	}
	
	
	@Override
	public abstract Pane getRootContainerPane();
	///////////////////////////////////

	/**
	 * 
	 * @return
	 */
	public abstract TitledPane getDetailInforTitledPane();
}
