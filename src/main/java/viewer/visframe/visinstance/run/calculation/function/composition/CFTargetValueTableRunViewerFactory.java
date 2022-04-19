package viewer.visframe.visinstance.run.calculation.function.composition;

import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import viewer.ViewerFactory;
import visinstance.run.calculation.function.composition.CFTargetValueTableRun;

public class CFTargetValueTableRunViewerFactory implements ViewerFactory<CFTargetValueTableRun>{
	private static Map<VisProjectDBContext, CFTargetValueTableRunViewerFactory> projectSingletonMap;
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static CFTargetValueTableRunViewerFactory singleton(VisProjectDBContext context) {
		if(projectSingletonMap==null) {
			projectSingletonMap = new HashMap<>();
		}
		
		if(!projectSingletonMap.containsKey(context)) {
			projectSingletonMap.put(context, new CFTargetValueTableRunViewerFactory(context));
		}
		
		return projectSingletonMap.get(context);
	}
	
	////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * 
	 * @param hostVisframeContext
	 */
	public CFTargetValueTableRunViewerFactory(VisProjectDBContext hostVisframeContext){
		this.hostVisProjectDBContext = hostVisframeContext;
	}
	
	
	@Override
	public CFTargetValueTableRunViewer build(CFTargetValueTableRun u) {		
		return new CFTargetValueTableRunViewer(u, this.hostVisProjectDBContext);
	}
	
	
}
