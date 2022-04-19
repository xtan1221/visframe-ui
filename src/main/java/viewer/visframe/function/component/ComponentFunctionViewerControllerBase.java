package viewer.visframe.function.component;

import function.component.ComponentFunction;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class ComponentFunctionViewerControllerBase<F extends ComponentFunction> extends AbstractViewerController<F>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public ComponentFunctionViewerBase<F,?> getViewer() {
		return (ComponentFunctionViewerBase<F,?>)this.viewer;
	}
	
	/**
	 * return the center coordinate of the go-to-previous-Circle on the AnchorPane containing the CompositionFunction's tree;
	 * can only be invoked after the SimpleFunction's viewer is added to the AnchorPane;
	 * @return
	 */
	public abstract Point2D getGotoPreviousCircleCenterCoord();
	
	@Override
	public abstract Pane getRootContainerPane();
	///////////////////////////////////
	
}
