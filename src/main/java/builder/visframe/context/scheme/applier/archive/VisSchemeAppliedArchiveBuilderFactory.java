package builder.visframe.context.scheme.applier.archive;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import context.scheme.VisScheme;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import function.composition.CompositionFunction;

/**
 * 
 * @author tanxu
 * 
 */
public class VisSchemeAppliedArchiveBuilderFactory implements VisframeUDTTypeBuilderFactory<VisSchemeAppliedArchive>{
	
	private static Map<VisProjectDBContext, VisSchemeAppliedArchiveBuilderFactory> SINGLETON_MAP;
	
	public static VisSchemeAppliedArchiveBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(SINGLETON_MAP==null) {
			SINGLETON_MAP = new HashMap<>();
		}
		
		if(!SINGLETON_MAP.containsKey(hostVisProjectDBContext)) {
			SINGLETON_MAP.put(hostVisProjectDBContext, new VisSchemeAppliedArchiveBuilderFactory(hostVisProjectDBContext));
		}
		
		return SINGLETON_MAP.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private VisSchemeAppliedArchiveBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		//
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * build and return a {@link VisSchemeAppliedArchiveBuilder} with the given {@link CompositionFunction} entity;
	 * used to view an existing VisSchemeAppliedArchive only;
	 * @throws SQLException 
	 */
	@Override
	public VisSchemeAppliedArchiveBuilder build(VisSchemeAppliedArchive entity) throws IOException, SQLException {
		VisScheme appliedVisScheme = this.hostVisProjectDBContext.getHasIDTypeManagerController().getVisSchemeManager().lookup(entity.getAppliedVisSchemeID());
		
		VisSchemeAppliedArchiveBuilder builder = new VisSchemeAppliedArchiveBuilder(
				this.hostVisProjectDBContext,
				appliedVisScheme,
				entity.getUID()
				);
		builder.setValue(entity, false);
		
		return builder;
	}
	
}
