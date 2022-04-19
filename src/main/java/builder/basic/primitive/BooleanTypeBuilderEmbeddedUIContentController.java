package builder.basic.primitive;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class BooleanTypeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<Boolean> {
	public static final String FXML_FILE_DIR = "/builder/basic/primitive/BooleanTypeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		this.selectCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		//for boolean type builder, the default status is a valid value;
		//the following two lines are to trigger the above listener thus to transform the builder status to having a valid value in initial status
		this.selectCheckBox.setSelected(true);
//		this.selectCheckBox.setSelected(false);
	}


	@Override
	public BooleanTypeBuilder getOwnerNodeBuilder() {
		return (BooleanTypeBuilder) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return rootHBox;
	}
	
	
	@Override
	public Boolean build() {
		return this.selectCheckBox.isSelected();
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.selectCheckBox.setSelected(false);
	}
	
	@Override
	public void setUIToNonNullValue(Boolean value) {
		this.selectCheckBox.setSelected(value);
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	//////////////////////////
	@FXML
	private HBox rootHBox;
	@FXML
	private CheckBox selectCheckBox;
	
	@FXML
	public void initialize() {
		//
		
	}


}
