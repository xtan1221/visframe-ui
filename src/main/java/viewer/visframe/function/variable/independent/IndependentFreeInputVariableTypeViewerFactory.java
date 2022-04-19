package viewer.visframe.function.variable.independent;

import java.util.HashMap;
import java.util.Map;

import context.VisframeContext;
import function.variable.independent.IndependentFreeInputVariableType;
import viewer.ViewerFactory;

public class IndependentFreeInputVariableTypeViewerFactory implements ViewerFactory<IndependentFreeInputVariableType>{
	private static Map<VisframeContext, IndependentFreeInputVariableTypeViewerFactory> projectSingletonMap;
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static IndependentFreeInputVariableTypeViewerFactory singleton(VisframeContext context) {
		if(projectSingletonMap==null) {
			projectSingletonMap = new HashMap<>();
		}
		
		if(!projectSingletonMap.containsKey(context)) {
			projectSingletonMap.put(context, new IndependentFreeInputVariableTypeViewerFactory(context));
		}
		
		return projectSingletonMap.get(context);
	}
	
	////////////////////////////
	private final VisframeContext hostVisframeContext;
	
	/**
	 * 
	 * @param hostVisframeContext
	 */
	public IndependentFreeInputVariableTypeViewerFactory(VisframeContext hostVisframeContext){
		this.hostVisframeContext = hostVisframeContext;
	}
	
	
	@Override
	public IndependentFreeInputVariableTypeViewer build(IndependentFreeInputVariableType u) {		
		return new IndependentFreeInputVariableTypeViewer(u, this.hostVisframeContext);
	}
	
	
}
