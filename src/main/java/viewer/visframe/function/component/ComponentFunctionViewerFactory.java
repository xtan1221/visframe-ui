package viewer.visframe.function.component;

import function.component.ComponentFunction;
import function.component.PiecewiseFunction;
import function.component.SimpleFunction;
import viewer.visframe.function.composition.CompositionFunctionViewer;

public class ComponentFunctionViewerFactory {
	/**
	 * 
	 * @param cf
	 * @param hostCompositionFunctionViewer
	 * @return
	 */
	public static ComponentFunctionViewerBase<?,?> build(ComponentFunction cf, CompositionFunctionViewer hostCompositionFunctionViewer){
		if(cf instanceof SimpleFunction)
			return new SimpleFunctionViewer((SimpleFunction)cf, hostCompositionFunctionViewer);
		else
			return new PiecewiseFunctionViewer((PiecewiseFunction)cf, hostCompositionFunctionViewer);
	}
}
