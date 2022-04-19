package builder.basic.primitive;

import core.builder.factory.NodeBuilderFactoryBase;

public class StringTypeBuilderFactory extends NodeBuilderFactoryBase<String, StringTypeBuilderEmbeddedUIContentController>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public StringTypeBuilderFactory(
			String name, String description, boolean canBeNull
			) {
		super(name, description, canBeNull, StringTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public StringTypeBuilder build() {
		return new StringTypeBuilder(
				this.getName(), this.getDescription(), this.canBeNull(),
				this.getParentNodeBuilder());
	}

}
