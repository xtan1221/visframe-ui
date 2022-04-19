package builder.visframe.basic;

import basic.VfNotes;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

public class VfNotesBuilder extends LeafNodeBuilder<VfNotes, VfNotesBuilderEmbeddedUIContentController> {
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public VfNotesBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, VfNotesBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
}
