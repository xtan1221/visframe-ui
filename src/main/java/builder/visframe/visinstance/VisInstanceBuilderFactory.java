package builder.visframe.visinstance;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import builder.visframe.visinstance.nativevi.NativeVisInstanceBuilder;
import builder.visframe.visinstance.visschemebased.VisSchemeBasedVisInstanceBuilder;
import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import visinstance.NativeVisInstance;
import visinstance.VisInstance;
import visinstance.VisSchemeBasedVisInstance;

/**
 * 
 * @author tanxu
 * 
 */
public class VisInstanceBuilderFactory implements VisframeUDTTypeBuilderFactory<VisInstance>{
	
	private static Map<VisProjectDBContext, VisInstanceBuilderFactory> SINGLETON_MAP;
	
	public static VisInstanceBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(SINGLETON_MAP==null) {
			SINGLETON_MAP = new HashMap<>();
		}
		
		if(!SINGLETON_MAP.containsKey(hostVisProjectDBContext)) {
			SINGLETON_MAP.put(hostVisProjectDBContext, new VisInstanceBuilderFactory(hostVisProjectDBContext));
		}
		
		return SINGLETON_MAP.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private VisInstanceBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * build and return a VisInstanceBuilderBase with the given VisInstance;
	 * used to view an existing VisInstance only;
	 * @throws SQLException 
	 * 
	 */
	@Override
	public VisInstanceBuilderBase<?,?> build(VisInstance entity) throws IOException, SQLException {
		if(entity instanceof NativeVisInstance) {
			//return a NativeVisInstanceBuilder
			NativeVisInstanceBuilder builder = new NativeVisInstanceBuilder(this.hostVisProjectDBContext, entity.getUID());
			
			builder.setValue((NativeVisInstance)entity, false);
			
			return builder;
		}else {
			VisSchemeBasedVisInstance visSchemeBasedVisInstance = (VisSchemeBasedVisInstance)entity;
			VisSchemeBasedVisInstanceBuilder builder = 
					new VisSchemeBasedVisInstanceBuilder(
							this.hostVisProjectDBContext, 
							entity.getUID(),
							visSchemeBasedVisInstance.getVisSchemeID(),
							visSchemeBasedVisInstance.getVisSchemeAppliedArchiveID(),
							visSchemeBasedVisInstance.getVisSchemeAppliedArchiveReproducedAndInsertedInstanceID()
							);
			
			builder.setValue((VisSchemeBasedVisInstance)entity, false);
			
			return builder;
		}
	}
	
}
