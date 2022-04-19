package viewer.visframe.function.group;

import context.VisframeContext;
import function.group.CompositionFunctionGroup;
import function.group.IndependentGraphicsPropertyCFG;
import function.group.IndependentPrimitiveAttributeCFG;
import function.group.ShapeCFG;

public class CompositionFunctionGroupViewerFactory {
	
	/**
	 * 
	 * @param cfg
	 * @param hostVisframeContext
	 * @return
	 */
	public static CompositionFunctionGroupViewerBase<?,?> build(CompositionFunctionGroup cfg, VisframeContext hostVisframeContext){
		if(cfg instanceof ShapeCFG)
			return new ShapeCFGViewer((ShapeCFG)cfg, hostVisframeContext);
		else if(cfg instanceof IndependentGraphicsPropertyCFG)
			return new IndependentGraphicsPropertyCFGViewer((IndependentGraphicsPropertyCFG)cfg, hostVisframeContext);
		else
			return new IndependentPrimitiveAttributeCFGViewer((IndependentPrimitiveAttributeCFG)cfg, hostVisframeContext);
	}
}
