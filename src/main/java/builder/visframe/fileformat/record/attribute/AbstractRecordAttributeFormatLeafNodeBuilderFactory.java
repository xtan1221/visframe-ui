package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.factory.NodeBuilderFactoryBase;
import fileformat.record.attribute.AbstractRecordAttributeFormat;

public class AbstractRecordAttributeFormatLeafNodeBuilderFactory extends NodeBuilderFactoryBase<AbstractRecordAttributeFormat, AbstractRecordAttributeFormatLeafNodeBuilderEmbeddedUIContentController>{
	
	
	public AbstractRecordAttributeFormatLeafNodeBuilderFactory(
			String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, AbstractRecordAttributeFormatLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public AbstractRecordAttributeFormatLeafNodeBuilder build() throws SQLException, IOException {
		return new AbstractRecordAttributeFormatLeafNodeBuilder(
				this.getName(), this.getDescription(), this.canBeNull(), 
				this.getParentNodeBuilder());
	}
	
}
