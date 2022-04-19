package builder.basic.primitive;

import core.builder.factory.NodeBuilderFactoryBase;

public class BooleanTypeBuilderFactory extends NodeBuilderFactoryBase<Boolean, BooleanTypeBuilderEmbeddedUIContentController>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 */
	public BooleanTypeBuilderFactory(
			String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, BooleanTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public BooleanTypeBuilder build() {
		return new BooleanTypeBuilder(
				this.getName(), this.getDescription(), this.canBeNull(),
				this.getParentNodeBuilder());
	}

	
}
