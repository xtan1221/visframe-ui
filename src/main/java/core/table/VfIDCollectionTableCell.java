package core.table;

import context.project.process.logtable.VfIDCollection;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * TableCell for VfIDCollection based TableColumn;
 * 
 * if the cell contains a null VfIDCollection value, 
 * 		show 'N/A' on the cell text
 * 		click on the table cell will not popup any window
 * if the cell contains a non-null VfIDCollection value
 * 		the cell text will be 'click for more details'
 * 		and single click on the table cell will pop up a TextArea window showing the full details of the information;
 * 
 * @author tanxu
 * 
 * @param <T>
 * @param <I>
 */
public class VfIDCollectionTableCell<R,C> extends TableCell<R, VfIDCollection> {
//	private final boolean setToNotAvailable;
	
//	private TextArea textArea;

	private String title;

//	private String content;
	private Stage stage;
	private Scene scene;
	
	public VfIDCollectionTableCell() {
//		this.setToNotAvailable = setToNotAvailable;
		
//		this.textArea = new TextArea();
//		this.textArea.setEditable(false);
		
//		if(!this.setToNotAvailable) {
//		this.setOnMouseClicked();
//		}
//		content.addListener(new ChangeListener<String> () {
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if(isEditing())
//                    commitEdit(newValue == null ? "" : newValue);
//            }
//        });
		this.setOnMouseClicked(e -> {
//			System.out.println(this.getIndex());
//			System.out.println(this.getTableColumn().getCellData(this.getIndex()));
//        	System.out.println(this.getTableColumn().getTableView().getItems().get(this.getIndex()).getTableRow().getBaseProcessIDSet());
//			if (this.stage == null) {
			if(this.getTableColumn().getCellData(this.getIndex())!=null) {
				TextArea textArea = new TextArea();
				
				textArea.setText(this.getTableColumn().getCellData(this.getIndex()).toString());
				
				scene = new Scene(textArea);
				
				stage = new Stage();
				stage.setTitle(this.title);

				stage.setScene(scene);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(this.getScene().getWindow());
				
				stage.show();
			}
//			}
			
			
		});
	}
	
//	/**
//	 * set on mouse click event handler;
//	 * 
//	 * popup a window
//	 */
//	private void setOnMouseClicked() {
//		this.setOnMouseClicked(e -> {
////			System.out.println(this.getIndex());
////			System.out.println(this.getTableColumn().getCellData(this.getIndex()));
////        	System.out.println(this.getTableColumn().getTableView().getItems().get(this.getIndex()).getTableRow().getBaseProcessIDSet());
////			if (this.stage == null) {
//				if(this.getTableColumn().getCellData(this.getIndex())!=null) {
//					this.textArea.setText(this.getTableColumn().getCellData(this.getIndex()).toString());
//					
//					scene = new Scene(this.textArea);
//					
//					stage = new Stage();
//					stage.setTitle(this.title);
//	
//					stage.setScene(scene);
//					stage.initModality(Modality.WINDOW_MODAL);
//					stage.initOwner(this.getScene().getWindow());
//				}
////			}
//			
//			stage.show();
//		});
//		
////		System.out.println(this.getTableColumn());
////		System.out.println(getIndex());
////		if(this.getTableColumn().getCellData(this.getIndex())!=null) {
////		this.setText("click for details");
////		}
//		
//	}
	
	/**
	 * this will set the display text only for table cell of non-empty rows 
	 */
	@Override
	public void updateItem(VfIDCollection item, boolean empty) {
		if(empty) {//empty rows
			//
		}else {
			if(this.getTableColumn().getCellData(this.getIndex())==null) {//null
				this.setText("N/A");
			}else {
				this.setText("click for details");
			}
		}
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

//	/**
//	 * @return the content
//	 */
//	public String getContent() {
//		return content;
//	}
//
//	/**
//	 * @param content the content to set
//	 */
//	public void setContent(String content) {
//		this.content = content;
//	}

}
