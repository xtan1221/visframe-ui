package builder.basic.primitive;

import java.util.function.Predicate;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

public class IntegerTypeBuilder extends LeafNodeBuilder<Integer, IntegerTypeBuilderEmbeddedUIContentController>{
	private final Predicate<Integer> domain;
	private final String domainDescription;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param domain
	 * @param domainDescription
	 */
	public IntegerTypeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, 
			
			Predicate<Integer> domain,
			String domainDescription) {
		super(name, description, canBeNull, parentNodeBuilder, IntegerTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		
		this.domain = domain;
		this.domainDescription = domainDescription;
	}

	
	public Predicate<Integer> getDomain() {
		return domain;
	}
	
	
	public String getDomainDescription() {
		return domainDescription;
	}
}
