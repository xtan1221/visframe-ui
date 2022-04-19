package builder.basic.primitive;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;


/**
 * boolean type builder
 * 
 * 
 * note that there is no empty value for boolean type;
 * 
 * when the node builder is set to default empty, its value is false (checkbox unselected);
 * @author tanxu
 *
 */
public class BooleanTypeBuilder extends LeafNodeBuilder<Boolean, BooleanTypeBuilderEmbeddedUIContentController>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public BooleanTypeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(
//				Boolean.class, 
				name, description, canBeNull, parentNodeBuilder, BooleanTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * !!
	 * @return
	 */
	@Override
	public Boolean getCurrentValue() {
		return this.getCurrentStatus().isSetToNull()?null:this.getCurrentStatus().isDefaultEmpty()?false:this.getCurrentStatus().getValue();
	}
}
