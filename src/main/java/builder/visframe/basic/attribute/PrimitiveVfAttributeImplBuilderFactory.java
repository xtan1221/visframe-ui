package builder.visframe.basic.attribute;

import java.io.IOException;
import java.sql.SQLException;

import basic.attribute.VfAttribute;
import core.builder.factory.NodeBuilderFactoryBase;
import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;

public class PrimitiveVfAttributeImplBuilderFactory extends NodeBuilderFactoryBase<VfAttribute<?>, NonLeafNodeBuilderEmbeddedUIContentController<VfAttribute<?>>>{
	
	public PrimitiveVfAttributeImplBuilderFactory(String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PrimitiveVfAttributeImplBuilder build() throws SQLException, IOException {
		return new PrimitiveVfAttributeImplBuilder(this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder());
	}

}
