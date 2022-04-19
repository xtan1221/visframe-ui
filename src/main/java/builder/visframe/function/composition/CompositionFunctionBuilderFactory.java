package builder.visframe.function.composition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import function.composition.CompositionFunction;
import function.group.CompositionFunctionGroup;

/**
 * 
 * @author tanxu
 *
 */
public class CompositionFunctionBuilderFactory implements VisframeUDTTypeBuilderFactory<CompositionFunction>{
	
	private static Map<VisProjectDBContext, CompositionFunctionBuilderFactory> SINGLETON_MAP;
	
	
	/**
	 * TODO take in a 
	 * @param hostVisProjectDBContext
	 * @return
	 */
	public static CompositionFunctionBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(SINGLETON_MAP==null) {
			SINGLETON_MAP = new HashMap<>();
		}
		
		if(!SINGLETON_MAP.containsKey(hostVisProjectDBContext)) {
			SINGLETON_MAP.put(hostVisProjectDBContext, new CompositionFunctionBuilderFactory(hostVisProjectDBContext));
		}
		
		return SINGLETON_MAP.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private CompositionFunctionBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		//
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * build and return a {@link CompositionFunctionBuilder} with the given {@link CompositionFunction} entity
	 */
	@Override
	public CompositionFunctionBuilder build(CompositionFunction entity) throws IOException {
		try {
			CompositionFunctionGroup ownerCompositionFunctionGroup = this.hostVisProjectDBContext.getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(entity.getHostCompositionFunctionGroupID());
			int indexID = entity.getIndexID();
			CompositionFunctionBuilder builder = new CompositionFunctionBuilder(
					this.hostVisProjectDBContext,
					ownerCompositionFunctionGroup,
					indexID
					);
			builder.setValue(entity, false);
			return builder;
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//never reached
		return null;
	}
	
}
