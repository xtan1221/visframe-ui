package builder.visframe.rdb.sqltype;

import java.util.function.Predicate;

import core.builder.factory.NodeBuilderFactoryBase;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public class VfDefinedPrimitiveSQLDataTypeBuilderFactory extends NodeBuilderFactoryBase<VfDefinedPrimitiveSQLDataType, SQLDataTypeBuilderEmbeddedUIContentController>{
	private final Predicate<VfDefinedPrimitiveSQLDataType> typeConstraints;
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public VfDefinedPrimitiveSQLDataTypeBuilderFactory(
			String name, String description, boolean canBeNull,
			Predicate<VfDefinedPrimitiveSQLDataType> typeConstraints
			) {
		super(name, description, canBeNull, SQLDataTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		this.typeConstraints = typeConstraints;
	}
	
	
	@Override
	public VfDefinedPrimitiveSQLDataTypeBuilder build() {
		return new VfDefinedPrimitiveSQLDataTypeBuilder(
				this.getName(), this.getDescription(), this.canBeNull(),
				this.getParentNodeBuilder(),
				this.typeConstraints);
	}

}
