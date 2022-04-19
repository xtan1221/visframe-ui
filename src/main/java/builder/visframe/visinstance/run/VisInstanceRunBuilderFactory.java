package builder.visframe.visinstance.run;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import visinstance.run.VisInstanceRun;

public class VisInstanceRunBuilderFactory implements VisframeUDTTypeBuilderFactory<VisInstanceRun>{
	private static Map<VisProjectDBContext, VisInstanceRunBuilderFactory> SINGLETON = new HashMap<>();;
	
	public static VisInstanceRunBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(!SINGLETON.containsKey(hostVisProjectDBContext)) {
			SINGLETON.put(hostVisProjectDBContext, new VisInstanceRunBuilderFactory(hostVisProjectDBContext));
		}
		return SINGLETON.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private VisInstanceRunBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * build and return a VisInstanceRunBuilder with the given VisInstanceRun instance;
	 * 
	 * the builder is specifically used for viewing an existing VisInstanceRun;
	 * 
	 * to build a new VisInstanceRun, create an VisInstanceRunBuilder directly with its constructor;
	 * 
	 * TODO
	 */
	@Override
	public VisInstanceRunBuilder build(VisInstanceRun entity) throws IOException, SQLException {
		VisInstanceRunBuilder builder = 
				new VisInstanceRunBuilder(
					this.hostVisProjectDBContext, 
					entity.getVisInstanceID(), 
					entity.getRunUID()
					);
		
		builder.setValue(entity, false);
		
		return builder;
	}

}
