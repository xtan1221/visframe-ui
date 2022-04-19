package builder.visframe.context.scheme.applier.archive.mapping;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Predicate;

import context.project.VisProjectDBContext;
import context.scheme.appliedarchive.mapping.MetadataMapping;
import core.builder.NonLeafNodeBuilder;
import metadata.DataType;
import metadata.Metadata;

public abstract class MetadataMappingBuilder<T extends MetadataMapping> extends NonLeafNodeBuilder<T>{
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @param recordMappingHelper
	 */
	protected MetadataMappingBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			VisProjectDBContext hostVisProjectDBContext
			) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	
	/**
	 * @return the hostViProjectDBContext
	 */
	protected VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	
	/**
	 * 
	 * @return
	 */
	abstract protected Set<DataType> getAllowedMetadataDataTypeSet();
	
	
	protected Predicate<Metadata> filteringCondition;
	
	/**
	 * build (if not already) and return the {@link #filteringCondition} that check if a Metadata can be used to create the wanted MetadataMapping;
	 * 
	 * @return
	 */
	abstract protected Predicate<Metadata> getMetadataFilteringCondition();
	
	
	@Override
	abstract protected T build() throws SQLException;
}
