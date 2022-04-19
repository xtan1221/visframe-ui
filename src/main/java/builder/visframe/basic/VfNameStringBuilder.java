package builder.visframe.basic;

import basic.VfNameString;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

public class VfNameStringBuilder<T extends VfNameString> extends LeafNodeBuilder<T, VfNameStringBuilderEmbeddedUIContentController<T>> {
	private final Class<T> type;
	/**
	 * 
	 * @param type
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public VfNameStringBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Class<T> type) {
		super(name, description, canBeNull, parentNodeBuilder, VfNameStringBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.type = type;
	}
	
	
	public Class<T> getType() {
		return type;
	}
	
}
