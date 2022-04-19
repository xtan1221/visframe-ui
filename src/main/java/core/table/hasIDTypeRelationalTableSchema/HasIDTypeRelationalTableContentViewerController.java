package core.table.hasIDTypeRelationalTableSchema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.table.hasIDTypeRelationalTableSchema.column.ColumnSorterManager;
import core.table.hasIDTypeRelationalTableSchema.export.ExporterManager;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rdb.table.HasIDTypeRelationalTableColumn;
import sql.SQLStringUtils;
import sql.derby.TableContentSQLStringFactory;
import sql.derby.TableContentUtils;
import utils.AlertUtils;
import utils.HasIDTypeRelationalTableTableViewUtils;
import utils.FXUtils;
import utils.TableViewUtils;

/**
 * 
 * @author tanxu
 *
 */
public class HasIDTypeRelationalTableContentViewerController {
	public static final String FXML_FILE_DIR_STRING = "/core/table/hasIDTypeRelationalTableSchema/HasIDTypeRelationalTableContentViewer.fxml";
	
	private final static int DEFAULT_PAGE_LIMIT = 50;
	//////////////////////////////////
	private HasIDTypeRelationalTableContentViewer manager;
	
	private Map<String, Integer> colNameOriginalIndexMap;
	/**
	 * number of rows in the input data table;
	 * will not change once set throughout the whole session
	 */
	private int totalRowNum;
	/**
	 * @return the totalRowNum
	 */
	public int getTotalRowNum() {
		return totalRowNum;
	}

	/**
	 * selected number of rows to be viewed;
	 * 
	 */
	private int selectedRowNum;
	/**
	 * @return the selectedRowNum
	 */
	public int getSelectedRowNum() {
		return selectedRowNum;
	}

	private int totalPageNum;
	
	/**
	 * set manager;
	 * also initialization
	 * @param manager
	 * @throws SQLException 
	 */
	void setManager(HasIDTypeRelationalTableContentViewer manager) throws SQLException {
		this.manager = manager;
		
		//retrieve total row number;
		this.totalRowNum = 
				TableContentUtils.getTotalRowNum(
						this.manager.getHostVisProjectDBContext().getDBConnection(), 
						SQLStringUtils.buildTableFullPathString(this.manager.getHasIDTypeRelationalTableSchema().getID()));
		this.totalRecordNumTextField.setText(Integer.toString(this.totalRowNum));
		
		//initialize TableView
		this.tableView = HasIDTypeRelationalTableTableViewUtils.makeNewTableView(this.manager.getHasIDTypeRelationalTableSchema(), true);
		TableViewUtils.bindColWidthPropertyWithTableWidthProperty(this.tableView);
		this.tableViewContainerHBox.getChildren().add(this.tableView);
		this.tableView.prefWidthProperty().bind(this.tableViewContainerHBox.widthProperty());
		
		//
		this.colNameOriginalIndexMap = new HashMap<>();
		for(int i=0;i<this.manager.getHasIDTypeRelationalTableSchema().getOrderedListOfColumn().size();i++){
			HasIDTypeRelationalTableColumn col = this.manager.getHasIDTypeRelationalTableSchema().getOrderedListOfColumn().get(i);
			ColumnSorterManager colManager = new ColumnSorterManager(this.manager, col.getName().getStringValue());
			colManager.setOrderIndex(i);
			this.manager.getColumnSorterManagerList().add(colManager);
			
			this.colNameOriginalIndexMap.put(col.getName().getStringValue(), i);
		}
		this.updateColumnSorterVBox();
		double accordionWidth = this.manager.getColumnSorterManagerList().get(0).getRealWidth()*1.2;
		this.colSorterAccordion.setPrefWidth(accordionWidth);
//		this.colSorterAccordion.setMinWidth(this.manager.getColumnSorterManagerList().get(0).getRealWidth());
//		this.colSorterAccordion.setMaxWidth(this.manager.getColumnSorterManagerList().get(0).getRealWidth());
		
		//
		this.currentPageNumTextField.setText(Integer.toString(1));
		this.currentPageNumTextField.textProperty().addListener((obs,ov,nv)->{
			this.setDisabledNextPageButtonPreviousPageButtons();
			this.updateTableViewContent();
		});
		
		//
		this.includeAllRecordCheckBox.setSelected(true);
		FXUtils.set2Disable(this.alternativeRowRangeSelectionHBox, true);
		this.includeAllRecordCheckBox.selectedProperty().addListener((obs,ov,nv)->{
			if(nv) {
				FXUtils.set2Disable(this.alternativeRowRangeSelectionHBox, true);
				
				this.updateSelectedRowNumAndTotalPageNum();
			}else {
				FXUtils.set2Disable(this.alternativeRowRangeSelectionHBox, false);
			}
		});
		
		
		//
		this.pageRecordNumLimitChoiceBox.setValue(DEFAULT_PAGE_LIMIT);
		this.updateSelectedRowNumAndTotalPageNum();
		this.pageRecordNumLimitChoiceBox.valueProperty().addListener((obs,ov,nv)->{
			//whether the current page is 1 before any change is made
			boolean currentPageIs1 = this.currentPageNumTextField.getText().equals("1");
			
			//note that the TableView will only be updated if the current page is not 1 after this method invoked 
			this.updateSelectedRowNumAndTotalPageNum();
			
			//
			if(currentPageIs1)//need to explicitly update tableview content if the currentPageIs1 is true
				this.updateTableViewContent();
		});
		
		//
		this.totalPageNumTextField.textProperty().addListener((obs,ov,nv)->{
			this.updatePageIndexChoiceBox();
			
			this.currentPageNumTextField.setText(Integer.toString(1));
			this.setDisabledNextPageButtonPreviousPageButtons();
//			this.pageIndexChoiceBox.setValue(1);//always reset to 1 whenever page num limit changes
		});
		this.setDisabledNextPageButtonPreviousPageButtons();
		
		//
		this.updatePageIndexChoiceBox();
		
		this.pageIndexChoiceBox.valueProperty().addListener((obs,ov,nv)->{
			if(nv==null) {
				//
			}else {
				this.currentPageNumTextField.setText(Integer.toString(nv));
				this.pageIndexChoiceBox.setValue(null);
//					this.updateTableViewContent();
			}
		});
		
		//////
		this.updateTableViewContent();

	}
	
	private void updatePageIndexChoiceBox() {
		if(this.totalPageNum>100) {
			FXUtils.set2Disable(this.pageIndexChoiceBox, true);
			pageIndexChoiceBoxDisabledInforLabel.setVisible(true);
		}else {
			FXUtils.set2Disable(this.pageIndexChoiceBox, false);
			pageIndexChoiceBoxDisabledInforLabel.setVisible(false);
			
			List<Integer> pageIndexList = new ArrayList<>();
			for(int i=1;i<=this.totalPageNum;i++) {
				pageIndexList.add(i);
			}
			
			this.pageIndexChoiceBox.getItems().clear();
			this.pageIndexChoiceBox.getItems().addAll(pageIndexList);
		}
	}
	/**
	 * 
	 */
	private void updateSelectedRowNumAndTotalPageNum() {
		if(this.includeAllRecordCheckBox.isSelected()) {
			this.selectedRowNum = this.totalRowNum;
		}else {
			int start = Integer.parseInt(this.startRecordIndexTextField.getText());
			int end = Integer.parseInt(this.endRecordIndexTextField.getText());
			
			this.selectedRowNum = end-start+1;
		}
		
		int pageRowLimit = this.pageRecordNumLimitChoiceBox.getValue();
		int t = this.selectedRowNum/pageRowLimit;
		if(t*pageRowLimit<this.selectedRowNum)
			this.totalPageNum = t+1;
		else
			this.totalPageNum = t;
		
		this.totalPageNumTextField.setText(Integer.toString(this.totalPageNum));
		
		//tested
		this.updateTableViewContent();
	}
	
	private void setDisabledNextPageButtonPreviousPageButtons() {
		//
		int currentPage = Integer.parseInt(this.currentPageNumTextField.getText());
		
		FXUtils.set2Disable(this.previousPageButton, currentPage==1);
		FXUtils.set2Disable(this.nextPageButton, currentPage==this.totalPageNum);
	}
	
	
	private void updateColumnSorterVBox() {
		this.orderByColumnVBox.getChildren().clear();
		this.manager.getColumnSorterManagerList().forEach(colManager->{
			this.orderByColumnVBox.getChildren().add(colManager.getController().getRootContainerNode());
		});
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 */
	public void switchColSorter(int i, int j) {
		ColumnSorterManager col1 = this.manager.getColumnSorterManagerList().get(i);
		ColumnSorterManager col2 = this.manager.getColumnSorterManagerList().get(j);
		
		this.manager.getColumnSorterManagerList().set(i, col2);
		this.manager.getColumnSorterManagerList().set(j, col1);
		
		col1.setOrderIndex(j);
		col2.setOrderIndex(i);
		//
		this.updateColumnSorterVBox();
	}
		
	////////////////////////////
	private TableView<List<String>> tableView;
	
	
	/**
	 * update the shown content of the {@link #tableView}
	 * @param start
	 * @param end
	 * @throws SQLException
	 */
	private void updateTableViewContent() {
//		System.out.println(this.getClass().getName()+"========================");
//		System.out.println(this.getClass().getName()+":update TableView content!");
		
		try {
			HasIDTypeRelationalTableTableViewUtils.populateTableView(tableView, this.query(false));
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * create and return a ResultSet that queries the table based on current settings;
	 * note that the page 
	 * @param toExport whether the built ResultSet is used to export the table content to an external file or not (used to show content on a TableView (thus the page number limit is involved));
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet query(boolean toExport) throws SQLException {
		int currentPageIndex = Integer.parseInt(this.currentPageNumTextField.getText());
		int pageRecordLimitNum = this.pageRecordNumLimitChoiceBox.getValue();
		
		int start;
		int end;
		if(toExport) {
			if(this.includeAllRecordCheckBox.isSelected()) {
				start = 1;
				end = this.totalRowNum;
			}else {
				start = Integer.parseInt(this.startRecordIndexTextField.getText());
				end = Integer.parseInt(this.endRecordIndexTextField.getText());
			}
		}else {
			if(this.includeAllRecordCheckBox.isSelected()) {
				start = pageRecordLimitNum*(currentPageIndex-1)+1;
				end = pageRecordLimitNum*currentPageIndex>this.totalRowNum?this.totalRowNum:pageRecordLimitNum*currentPageIndex;
			}else {
				
				int startRowIndex = Integer.parseInt(this.startRecordIndexTextField.getText());
				int endRowIndex = Integer.parseInt(this.endRecordIndexTextField.getText());
				
				start = pageRecordLimitNum*(currentPageIndex-1)+startRowIndex;
//				end = pageRecordLimitNum*currentPageIndex>endRowIndex?endRowIndex:pageRecordLimitNum*currentPageIndex;
				end = currentPageIndex==this.totalPageNum?endRowIndex:start+pageRecordLimitNum;
			}
			
		}
		
		////////////////
		List<String> orderByColumnNameList = new ArrayList<>();
		List<Boolean> orderByASCList = new ArrayList<>();
		this.manager.getColumnSorterManagerList().forEach(colManager->{
			if(colManager.isSelected()) {
				orderByColumnNameList.add(colManager.getColumnName());
				orderByASCList.add(colManager.isSortingByASC());
			}
		});
		
		//////////////////
		List<String> selectedColumnList = new ArrayList<>();
		this.manager.getHasIDTypeRelationalTableSchema().getOrderedListOfColumn().forEach(c->{
			selectedColumnList.add(c.getName().getStringValue());
		});
		String query = 
				TableContentSQLStringFactory.buildSelectRowByRangeSQLString(
						SQLStringUtils.buildTableFullPathString(this.manager.getHasIDTypeRelationalTableSchema().getID()),
						start,//start, 
						end,//end, 
						selectedColumnList,//selectedColumnList, 
						null,//conditionString, 
						orderByColumnNameList.isEmpty()?null:orderByColumnNameList,//orderByColumnNameList, 
						orderByASCList.isEmpty()?null:orderByASCList//orderByASCList
						);
		
		Statement statement = this.manager.getHostVisProjectDBContext().getDBConnection().createStatement(); 
		ResultSet rs = statement.executeQuery(query);
			
		return rs;
	}
	
	//////////////////////////////////////////
	Pane getRootContainerNode() {
		return this.tableViwerRootContainerVBox;
	}
	
	Pane getTableViewContainer() {
		return tableViewContainerHBox;
	}
	
	////////////////////////
	@FXML
	public void initialize() {
		List<Integer> pageLimitNumList = new ArrayList<>();
		pageLimitNumList.add(DEFAULT_PAGE_LIMIT);
		pageLimitNumList.add(100);
		pageLimitNumList.add(150);
		pageLimitNumList.add(200);
		//
		this.pageRecordNumLimitChoiceBox.getItems().addAll(pageLimitNumList);
		
		
//		tableViewContainerHBox.setOnMouseClicked(e->{
//			System.out.println("===============");
//			System.out.println("width"+tableViewContainerHBox.getWidth());
//			System.out.println("pref width"+tableViewContainerHBox.getPrefWidth());
//			System.out.println("min width"+tableViewContainerHBox.getMinWidth());
//			System.out.println("max width"+tableViewContainerHBox.getMaxWidth());
//		});
		
		
	}
	
	@FXML
	private VBox tableViwerRootContainerVBox;
	@FXML
	private TextField totalRecordNumTextField;
	@FXML
	private HBox alternativeRowRangeSelectionHBox;
	@FXML
	TextField startRecordIndexTextField;
	@FXML
	TextField endRecordIndexTextField;
	@FXML
	private Button runSelectedRowRangeButton;
	@FXML
	CheckBox includeAllRecordCheckBox;
	@FXML
	private ChoiceBox<Integer> pageRecordNumLimitChoiceBox;
	@FXML
	private TextField totalPageNumTextField;
	@FXML
	private Button previousPageButton;
	@FXML
	private Button nextPageButton;
	@FXML
	private TextField goToPageIndexTextField;
	@FXML
	private Button goToPageButton;
	@FXML
	private TextField currentPageNumTextField;
	@FXML
	private ChoiceBox<Integer> pageIndexChoiceBox;
	@FXML
	private Label pageIndexChoiceBoxDisabledInforLabel;
	@FXML
	private Accordion colSorterAccordion;
	@FXML
	private VBox orderByColumnVBox;
	@FXML
	private Button rowOrderByButton;
	@FXML
	private Button resetOrderByButton;
	@FXML
	private Button exportButton;
	@FXML
	private HBox tableViewContainerHBox;
	
	// Event Listener on Button[#runSelectedRowRangeButton].onAction
	@FXML
	public void runSelectedRowRangeButtonOnAction(ActionEvent event) {
		try {
			int start = Integer.parseInt(this.startRecordIndexTextField.getText());
			int end = Integer.parseInt(this.endRecordIndexTextField.getText());
			
			if(end<start)
				throw new UnsupportedOperationException("row end index cannot be smaller than start index!");
			if(start<=0 || end<=0)
				throw new UnsupportedOperationException("row start and end index must be positive integer!");
			
			//
			updateSelectedRowNumAndTotalPageNum();
			
			//this will be triggered by above statement
//			this.updateTableViewContent();
		}catch(Exception e) {
			if(e instanceof NumberFormatException) {
				AlertUtils.popAlert("Error", "row index range must be positive integer!");
			}else if(e instanceof UnsupportedOperationException) {
				AlertUtils.popAlert("Error", e.getMessage());
			}else {
				throw new VisframeException(e.getMessage());
			}
		}
		
		
	}
	// Event Listener on Button[#previousPageButton].onAction
	@FXML
	public void previousPageButtonOnAction(ActionEvent event) {
		int pn = Integer.parseInt(this.currentPageNumTextField.getText());
		this.currentPageNumTextField.setText(Integer.toString(pn-1));
	}
	// Event Listener on Button[#nextPageButton].onAction
	@FXML
	public void nextPageButtonOnAction(ActionEvent event) {
		int pn = Integer.parseInt(this.currentPageNumTextField.getText());
		this.currentPageNumTextField.setText(Integer.toString(pn+1));
	}
	
	// Event Listener on Button[#goToPageButton].onAction
	@FXML
	public void goToPageButtonOnAction(ActionEvent event) {
		try {
			int pageIndex = Integer.parseInt(this.goToPageIndexTextField.getText());
			
			if(pageIndex<=0 || pageIndex>this.totalPageNum) {
				throw new UnsupportedOperationException("Page index must be between 1 and "+this.totalPageNum+"!");
			}
			
			this.currentPageNumTextField.setText(Integer.toString(pageIndex));
		}catch(Exception e) {
			if(e instanceof NumberFormatException) {
				AlertUtils.popAlert("Error", "Page index must be positive integer!");
			}else if(e instanceof UnsupportedOperationException) {
				AlertUtils.popAlert("Error", e.getMessage());
			}else {
				throw new VisframeException(e.getMessage());
			}
		}
	}
	
	// Event Listener on Button[#rowOderByButton].onAction
	@FXML
	public void rowOrderByButtonOnAction(ActionEvent event) {
		this.updateTableViewContent();
	}
	// Event Listener on Button[#resetOrderByButton].onAction
	@FXML
	public void resetOrderByButtonOnAction(ActionEvent event) {
		this.manager.getColumnSorterManagerList().forEach(colManager->{
			colManager.setOrderIndex(this.colNameOriginalIndexMap.get(colManager.getColumnName()));
			colManager.setSelected(false);
		});
		
		this.updateTableViewContent();
	}
	
	////////////////////////////////////////////////////
	private ExporterManager exporterManager;
	private Stage exporterStage;
	private Scene exporterScene;
	@FXML
	public void exportButtonOnAction(ActionEvent event) throws SQLException {
		if(this.exporterManager==null) {
			this.exporterManager = new ExporterManager(this.manager);
			
			exporterScene = new Scene(this.exporterManager.getController().getRootContainerPane());
			
			//////////////
			exporterStage = new Stage();
			
			exporterStage.setScene(exporterScene);
			
//			CFStage.setWidth(1200);
//			CFStage.setHeight(800);
			exporterStage.initModality(Modality.APPLICATION_MODAL);
			exporterStage.setTitle("Table content exporting");
		}
		
		this.exporterStage.showAndWait();
	}
}
