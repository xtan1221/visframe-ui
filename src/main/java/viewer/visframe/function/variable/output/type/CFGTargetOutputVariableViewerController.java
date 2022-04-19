package viewer.visframe.function.variable.output.type;

import java.sql.SQLException;

import function.group.CompositionFunctionGroup;
import function.variable.output.type.CFGTargetOutputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import viewer.visframe.function.target.CFGTargetViewerBase;
import viewer.visframe.function.target.CFGTargetViewerFactory;
import viewer.visframe.function.variable.output.ValueTableColumnOutputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class CFGTargetOutputVariableViewerController extends ValueTableColumnOutputVariableViewerController<CFGTargetOutputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/output/type/CFGTargetOutputVariableViewer.fxml";
	//////////////////////////////////////////////
	
	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		
		try {
			CompositionFunctionGroup hostCompositionFunctionGroup = this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext().
			getCompositionFunctionGroupLookup().lookup(this.getViewer().getValue().getHostCompositionFunctionGroupID());
			
			CFGTargetViewerBase<?,?> targetViewer = 
					CFGTargetViewerFactory.build(
							this.getViewer().getValue().getTarget(), 
							hostCompositionFunctionGroup);
			
			this.targetViewerHBox.getChildren().add(targetViewer.getController().getRootContainerPane());
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public CFGTargetOutputVariableViewer getViewer() {
		return (CFGTargetOutputVariableViewer)this.viewer;
	}


	@Override
	public Parent getRootContainerPane() {
		return rootContainerGridPane;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////
	@FXML
	private GridPane rootContainerGridPane;
	@FXML
	private TextField aliasNameTextField;
	@FXML
	private Button notesButton;
	@FXML
	private HBox targetViewerHBox;
}
