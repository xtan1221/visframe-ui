package builder.basic.primitive;

import java.util.function.Predicate;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

public class DoubleTypeBuilder extends LeafNodeBuilder<Double, DoubleTypeBuilderEmbeddedUIContentController>{
	private final Predicate<Double> domain;
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
	public DoubleTypeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, 
			
			Predicate<Double> domain,
			String domainDescription) {
		super(
//				Double.class, 
				name, description, canBeNull, parentNodeBuilder, DoubleTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		
		this.domain = domain;
		this.domainDescription = domainDescription;
	}

	
	public Predicate<Double> getDomain() {
		return domain;
	}
	
	
	public String getDomainDescription() {
		return domainDescription;
	}
}
