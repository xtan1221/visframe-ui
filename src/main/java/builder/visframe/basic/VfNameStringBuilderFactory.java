package builder.visframe.basic;

import basic.VfNameString;
import core.builder.factory.NodeBuilderFactoryBase;

public class VfNameStringBuilderFactory<T extends VfNameString> extends NodeBuilderFactoryBase<T, VfNameStringBuilderEmbeddedUIContentController<T>>{
	private final Class<T> type;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public VfNameStringBuilderFactory(
			String name, String description, boolean canBeNull,
			Class<T> type) {
		super(name, description, canBeNull, VfNameStringBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.type = type;
	}
	
	
	@Override
	public VfNameStringBuilder<T> build() {
		return new VfNameStringBuilder<>(
				this.getName(), this.getDescription(), this.canBeNull(),
				this.getParentNodeBuilder(), this.getType());
	}

	
	/**
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}

	
}
