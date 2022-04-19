package builder.visframe.fileformat.record.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.utils.PlainStringMarker;

public class PlainStringMarkerBuilder extends LeafNodeBuilder<PlainStringMarker, PlainStringMarkerBuilderEmbeddedUIContentController> {
	
	public PlainStringMarkerBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, PlainStringMarkerBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}

}
