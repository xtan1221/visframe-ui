package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.attribute.AbstractRecordAttributeFormat;

/**
 * builder for a AbstractRecordAttributeFormat;
 * 
 * note that this class is a wrapper LeafNodeBuilder class for a PrimitiveRecordAttributeFormatBuilder and CompositeTagRecordAttributeFormatBuilder;
 * rather than a super class of them;
 * 
 * @author tanxu
 *
 */
public class AbstractRecordAttributeFormatLeafNodeBuilder extends LeafNodeBuilder<AbstractRecordAttributeFormat, AbstractRecordAttributeFormatLeafNodeBuilderEmbeddedUIContentController>{
	
	private final PrimitiveRecordAttributeFormatBuilder primitiveRecordAttributeFormatBuilder;
	private final CompositeTagRecordAttributeFormatBuilder compositeTagRecordAttributeFormatBuilder;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	protected AbstractRecordAttributeFormatLeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder
			) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, AbstractRecordAttributeFormatLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.primitiveRecordAttributeFormatBuilder = 
				new PrimitiveRecordAttributeFormatBuilder("","",false, null);
		this.compositeTagRecordAttributeFormatBuilder = 
				new CompositeTagRecordAttributeFormatBuilder("","",false, null);

	}

	public PrimitiveRecordAttributeFormatBuilder getPrimitiveRecordAttributeFormatBuilder() {
		return primitiveRecordAttributeFormatBuilder;
	}
	
	public CompositeTagRecordAttributeFormatBuilder getCompositeTagRecordAttributeFormatBuilder() {
		return compositeTagRecordAttributeFormatBuilder;
	}
	
	/**
	 * when this node is set to default empty status, also set the {@link #primitiveRecordAttributeFormatBuilder} and {@link #compositeTagRecordAttributeFormatBuilder} to default empty;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public boolean setToDefaultEmpty() throws SQLException, IOException {
		boolean changed = super.setToDefaultEmpty();
		
		this.getPrimitiveRecordAttributeFormatBuilder().setToDefaultEmpty();
		
		this.getCompositeTagRecordAttributeFormatBuilder().setToDefaultEmpty();
		
		return changed;
	}
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		super.setModifiable(modifiable);
		
		this.compositeTagRecordAttributeFormatBuilder.setModifiable(modifiable);
		this.primitiveRecordAttributeFormatBuilder.setModifiable(modifiable);
	}
}
