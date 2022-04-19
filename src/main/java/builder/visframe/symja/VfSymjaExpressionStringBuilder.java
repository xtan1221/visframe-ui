package builder.visframe.symja;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import symja.VfSymjaExpressionString;

public class VfSymjaExpressionStringBuilder extends LeafNodeBuilder<VfSymjaExpressionString, VfSymjaExpressionStringBuilderEmbeddedUIContentController>{
	
	protected VfSymjaExpressionStringBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, VfSymjaExpressionStringBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}

}
