package builder.basic.primitive;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;


/**
 * String type builder
 * 
 * 
 * @author tanxu
 *
 */
public class StringTypeBuilder extends LeafNodeBuilder<String, StringTypeBuilderEmbeddedUIContentController>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public StringTypeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(
//				Boolean.class, 
				name, description, canBeNull, parentNodeBuilder, StringTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * !!
	 * @return
	 */
	@Override
	public String getCurrentValue() {
		return this.getCurrentStatus().isSetToNull()?null:this.getCurrentStatus().isDefaultEmpty()?"":this.getCurrentStatus().getValue();
	}
}
