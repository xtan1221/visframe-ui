package builder.visframe.fileformat.record.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.utils.StringMarker;


/**
 * builder for a {@link StringMarker};
 * 
 * whether the built instance is {@link PlainStringMarker} or {@link RegexStringMarker} is selected on the builder UI;
 * 
 * @author tanxu
 *
 */
public class StringMarkerBuilder extends LeafNodeBuilder<StringMarker, StringMarkerBuilderEmbeddedUIContentController> {
	
	public StringMarkerBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, StringMarkerBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
}
