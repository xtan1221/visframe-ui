package builder.visframe.context.scheme.applier.reproduceandinserter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducerAndInserter;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import function.composition.CompositionFunction;

/**
 * 
 * @author tanxu
 * 
 */
public class VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory implements VisframeUDTTypeBuilderFactory<VisSchemeAppliedArchiveReproducedAndInsertedInstance>{
	
	private static Map<VisProjectDBContext, VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory> SINGLETON_MAP;
	
	public static VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory singleton(VisProjectDBContext hostVisProjectDBContext) {
		if(SINGLETON_MAP==null) {
			SINGLETON_MAP = new HashMap<>();
		}
		
		if(!SINGLETON_MAP.containsKey(hostVisProjectDBContext)) {
			SINGLETON_MAP.put(hostVisProjectDBContext, new VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory(hostVisProjectDBContext));
		}
		
		return SINGLETON_MAP.get(hostVisProjectDBContext);
	}
	
	//////////////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * private constructor
	 */
	private VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		//
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * build and return a {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder} with the given {@link CompositionFunction} entity
	 * 
	 * note that the built VisSchemeAppliedArchiveReproducerAndInserter should not be started, rather it is only used to facilitate show the information contained in it;
	 * 
	 * only be used to view an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance ??? TODO
	 */
	@Override
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder build(VisSchemeAppliedArchiveReproducedAndInsertedInstance entity) throws IOException {
		try {
			VisSchemeAppliedArchive applierArchive = this.hostVisProjectDBContext.getHasIDTypeManagerController().getVisSchemeAppliedArchiveManager().lookup(entity.getApplierArchiveID());
			VisSchemeAppliedArchiveReproducerAndInserter visSchemeAppliedArchiveReproducerAndInserter = 
					new VisSchemeAppliedArchiveReproducerAndInserter(
							this.hostVisProjectDBContext,
							applierArchive);
			
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder builder = new VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder(
					visSchemeAppliedArchiveReproducerAndInserter);
			
			//
			builder.setValue(entity, false);
			
			return builder;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);//debug
		}
		//never reached
		return null;
	}
	
}
