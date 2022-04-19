package builder.visframe.fileformat.record.utils;

import core.builder.factory.NodeBuilderFactoryBase;
import fileformat.record.utils.StringMarker;

public class StringMarkerBuilderFactory extends NodeBuilderFactoryBase<StringMarker, StringMarkerBuilderEmbeddedUIContentController>{
	
	public StringMarkerBuilderFactory(String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, StringMarkerBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public StringMarkerBuilder build() {
		// TODO Auto-generated method stub
		return new StringMarkerBuilder(this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder());
	}
	
}
