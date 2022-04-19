package builder.visframe.visinstance;

import java.io.IOException;
import java.sql.SQLException;

import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import javafx.fxml.FXML;
import visinstance.VisInstance;

public abstract class VisInstanceBuilderEmbeddedUIContentControllerBase<T extends VisInstance> extends LeafNodeBuilderEmbeddedUIContentController<T>{
	
	///////////////////////////////////
	
	@Override
	public VisInstanceBuilderBase<T, ?> getOwnerNodeBuilder() {
		return (VisInstanceBuilderBase<T, ?>) this.ownerNodeBuilder;
	}

	
	@Override
	public abstract T build();
	
	
	/**
	 * set to the give value;
	 * 
	 * note that this could only occur when an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance is selected to be viewed for its details;
	 * 
	 * need to explicitly set up all the features
	 * 
	 * must be invoked together with {@link #setModifiable(boolean)} with parameter being false;
	 * 
	 * specifically
	 * 1. initialize a VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder and set the value of it with a non-null VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * 		{@link NativeVisInstanceBuilderFactory#build(VisSchemeAppliedArchiveReproducedAndInsertedInstance)}
	 * 		which will eventually invoke method {@link #setUIToNonNullValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance)}
	 * 2. set the returned VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder to un-modifiable
	 * 		{@link VisSchemeBasedVisInstanceBuilder#setModifiable(boolean)}
	 * 		which will eventually invoke {@link #setModifiable(boolean)} method
	 * 
	 * 
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public abstract void setUIToNonNullValue(T value) throws SQLException, IOException;
	
	//////////////////////////
	
	
	@FXML
	public abstract void initialize();
	
	
}
