package builder.visframe.visinstance.run.layoutconfiguration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

public class VisInstanceRunLayoutConfigurationBuilderFactory implements VisframeUDTTypeBuilderFactory<VisInstanceRunLayoutConfiguration>{
	private static Map<VisProjectDBContext, VisInstanceRunLayoutConfigurationBuilderFactory> SINGLETON = new HashMap<>();;
	
	public static VisInstanceRunLayoutConfigurationBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(!SINGLETON.containsKey(hostVisProjectDBContext)) {
			SINGLETON.put(hostVisProjectDBContext, new VisInstanceRunLayoutConfigurationBuilderFactory(hostVisProjectDBContext));
		}
		return SINGLETON.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private VisInstanceRunLayoutConfigurationBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	
	/**
	 * build and return a VisInstanceRunLayoutConfigurationBuilder with the given VisInstanceRunLayoutConfiguration;
	 * used to view an existing VisInstanceRunLayoutConfiguration only;
	 */
	@Override
	public VisInstanceRunLayoutConfigurationBuilder build(VisInstanceRunLayoutConfiguration entity) throws IOException, SQLException {
		VisInstanceRunLayoutConfigurationBuilder builder = 
				new VisInstanceRunLayoutConfigurationBuilder(
					this.hostVisProjectDBContext, 
					entity.getVisInstanceRunID(), 
					entity.getUID()
					);
		
		builder.setValue(entity, false);
		
		return builder;
	}

	
}
