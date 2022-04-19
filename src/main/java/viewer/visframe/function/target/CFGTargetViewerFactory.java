package viewer.visframe.function.target;

import function.group.CompositionFunctionGroup;
import function.group.GraphicsPropertyCFG;
import function.target.CFGTarget;
import function.target.IndependentPrimitiveAttributeTarget;
import function.target.LeafGraphicsPropertyCFGTarget;

public class CFGTargetViewerFactory {

	/**
	 * 
	 * @param target
	 * @param hostCompositionFunctionGroup
	 * @return
	 */
	public static CFGTargetViewerBase<?,?> build(CFGTarget<?> target, CompositionFunctionGroup hostCompositionFunctionGroup){
		if(target instanceof IndependentPrimitiveAttributeTarget) {
			return new IndependentPrimitiveAttributeTargetViewer((IndependentPrimitiveAttributeTarget<?>)target);
		}else {
			GraphicsPropertyCFG graphicsPropertyCFG = (GraphicsPropertyCFG)hostCompositionFunctionGroup;
			
			return new LeafGraphicsPropertyCFGTargetViewer(
					(LeafGraphicsPropertyCFGTarget<?>)target,
					graphicsPropertyCFG.getGraphicsPropertyTree(target.getName())
					);
		}
	}
}
