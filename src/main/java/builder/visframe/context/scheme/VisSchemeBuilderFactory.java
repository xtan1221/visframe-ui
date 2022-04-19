package builder.visframe.context.scheme;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import context.scheme.VisScheme;
import core.builder.factory.VisframeUDTTypeBuilderFactory;

public class VisSchemeBuilderFactory implements VisframeUDTTypeBuilderFactory<VisScheme>{
	private static Map<VisProjectDBContext, VisSchemeBuilderFactory> SINGLETON = new HashMap<>();;
	
	public static VisSchemeBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(!SINGLETON.containsKey(hostVisProjectDBContext)) {
			SINGLETON.put(hostVisProjectDBContext, new VisSchemeBuilderFactory(hostVisProjectDBContext));
		}
		return SINGLETON.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * private constructor
	 */
	private VisSchemeBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * 
	 */
	@Override
	public VisSchemeBuilder build(VisScheme entity) throws IOException, SQLException {
		VisSchemeBuilder builder = new VisSchemeBuilder(
				this.hostVisProjectDBContext);
		
		builder.setValue(entity, false);
			
		return builder;
	}

	
}
