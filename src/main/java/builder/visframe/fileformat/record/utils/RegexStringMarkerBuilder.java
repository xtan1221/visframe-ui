package builder.visframe.fileformat.record.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.utils.RegexStringMarker;


/**
 * 
 * @author tanxu
 *
 */
public class RegexStringMarkerBuilder extends LeafNodeBuilder<RegexStringMarker, RegexStringMarkerBuilderEmbeddedUIContentController> {
	
	protected RegexStringMarkerBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, RegexStringMarkerBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}

}
