package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import function.group.CompositionFunctionGroup;
import function.group.GraphicsPropertyCFG;
import function.group.IndependentGraphicsPropertyCFG;
import function.group.IndependentPrimitiveAttributeCFG;
import function.group.ShapeCFG;

/**
 * 
 * @author tanxu
 *
 */
public class CompositionFunctionGroupBuilderFactory implements VisframeUDTTypeBuilderFactory<CompositionFunctionGroup>{
	
	private static Map<VisProjectDBContext, CompositionFunctionGroupBuilderFactory> SINGLETON_MAP;
	
	public static CompositionFunctionGroupBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(SINGLETON_MAP==null) {
			SINGLETON_MAP = new HashMap<>();
		}
		
		if(!SINGLETON_MAP.containsKey(hostVisProjectDBContext)) {
			SINGLETON_MAP.put(hostVisProjectDBContext, new CompositionFunctionGroupBuilderFactory(hostVisProjectDBContext));
		}
		
		return SINGLETON_MAP.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private CompositionFunctionGroupBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		//
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	
	@Override
	public AbstractCompositionFunctionGroupBuilder<?> build(CompositionFunctionGroup entity) throws IOException, SQLException {
		
		AbstractCompositionFunctionGroupBuilder<?> builder = build(entity.getClass());
		builder.setValue(entity, false);
		return builder;
	}
	
	/**
	 * build and return a default empty specific type of CompositionFunctionGroup builder based on the given type;
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws SQLException 
	 */
	public AbstractCompositionFunctionGroupBuilder<?> build(Class<? extends CompositionFunctionGroup> type) throws IOException, SQLException {
		if(GraphicsPropertyCFG.class.isAssignableFrom(type)) {
			if(IndependentGraphicsPropertyCFG.class.isAssignableFrom(type)) {
				IndependentGraphicsPropertyCFGBuilder builder = new IndependentGraphicsPropertyCFGBuilder(this.hostVisProjectDBContext.getHasIDTypeManagerController().getMetadataManager());
				return builder;
			}else if(ShapeCFG.class.isAssignableFrom(type)) {
				ShapeCFGBuilder builder = new ShapeCFGBuilder(this.hostVisProjectDBContext.getHasIDTypeManagerController().getMetadataManager());
				return builder;
			}else {
				//
				throw new IllegalArgumentException("type of given CompositionFunctionGroup is not recognized!");
			}
			
		}else if(IndependentPrimitiveAttributeCFG.class.isAssignableFrom(type)){
			IndependentPrimitiveAttributeCFGBuilder builder = new IndependentPrimitiveAttributeCFGBuilder(this.hostVisProjectDBContext.getHasIDTypeManagerController().getMetadataManager());
			return builder;
		}else {
			throw new IllegalArgumentException("type of given CompositionFunctionGroup is not recognized!");
		}
		
	}
	
}
