package builder.visframe.function.component;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import function.component.ComponentFunction;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import utils.PropertyBindingUtils;

public abstract class ComponentFunctionBuilderEmbeddedUIContentController<T extends ComponentFunction> extends LeafNodeBuilderEmbeddedUIContentController<T>{
	
	private CubicCurve curveToPrevious = null;

	/**
	 * @return the curveToPrevious
	 */
	public CubicCurve getCurveToPrevious() {
		return curveToPrevious;
	}
	
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		
	}
	
	/**
	 * invoked after this componentFunction builder UI is added to the anchorpane;
	 */
	public void buildCurveToPrevious() {
		
		if(this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder()!=null) {//there is a previous compoennt function
			this.curveToPrevious = new CubicCurve();
			
			NumberBinding gotoPreviousCircleLayoutXBindings = 
					PropertyBindingUtils.buildInnestLayoutXPropertyBinding(
							this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane, 
							this.getToPreviousCircle());
			NumberBinding gotoPreviousCircleLayoutYBindings = 
					PropertyBindingUtils.buildInnestLayoutYPropertyBinding(
							this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane, 
							this.getToPreviousCircle());
			
			NumberBinding previousFunctionGoToThisCircleLayoutXBindings;
			NumberBinding previousFunctionGoToThisCircleLayoutYBindings;
			
			if(this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder() instanceof SimpleFunctionBuilder) {
				SimpleFunctionBuilder previous = (SimpleFunctionBuilder)this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder();
				
				previousFunctionGoToThisCircleLayoutXBindings = 
						PropertyBindingUtils.buildInnestLayoutXPropertyBinding(
								this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane, 
								previous.getEmbeddedUIContentController().goToNextFunctionCircle);
				
				previousFunctionGoToThisCircleLayoutYBindings = 
						PropertyBindingUtils.buildInnestLayoutYPropertyBinding(
								this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane, 
								previous.getEmbeddedUIContentController().goToNextFunctionCircle);
			}else {
				PiecewiseFunctionBuilder previous = (PiecewiseFunctionBuilder)this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder();
				previousFunctionGoToThisCircleLayoutXBindings = 
						PropertyBindingUtils.buildInnestLayoutXPropertyBinding(
								this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane, 
								previous.getEmbeddedUIContentController().getGoToNextCircle(this.getOwnerNodeBuilder()));
				
				previousFunctionGoToThisCircleLayoutYBindings = 
						PropertyBindingUtils.buildInnestLayoutYPropertyBinding(
								this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane, 
								previous.getEmbeddedUIContentController().getGoToNextCircle(this.getOwnerNodeBuilder()));
			}
			
			this.curveToPrevious.startXProperty().bind(previousFunctionGoToThisCircleLayoutXBindings);
			this.curveToPrevious.startYProperty().bind(previousFunctionGoToThisCircleLayoutYBindings);
			this.curveToPrevious.endXProperty().bind(gotoPreviousCircleLayoutXBindings);
			this.curveToPrevious.endYProperty().bind(gotoPreviousCircleLayoutYBindings);
			
			this.curveToPrevious.controlX1Property().bind(previousFunctionGoToThisCircleLayoutXBindings.add(100));
			this.curveToPrevious.controlY1Property().bind(previousFunctionGoToThisCircleLayoutYBindings);
			this.curveToPrevious.controlX2Property().bind(gotoPreviousCircleLayoutXBindings.subtract(100));
			this.curveToPrevious.controlY2Property().bind(gotoPreviousCircleLayoutYBindings);
			
			this.curveToPrevious.setFill(null);
			this.curveToPrevious.setStroke(Color.BLACK);
		}
	}
	
	
	@Override
	public ComponentFunctionBuilder<T,?> getOwnerNodeBuilder() {
		return (ComponentFunctionBuilder<T,?>) this.ownerNodeBuilder;
	}
	
	
	@Override
	public abstract Pane getRootParentNode();
	
	public abstract Pane getMainContentPane();
	
	public abstract Circle getToPreviousCircle();
	
	
	
	/**
	 * build and return a ComponentFunction;
	 */
	@Override
	public abstract T build();
	
	
	@FXML 
	public void initialize() {
//		this.getRootParentNode().setOnMouseClicked(e->{
//			System.out.println("======================");
//			System.out.println("layoutX:"+this.getRootParentNode().getLayoutX());
//			System.out.println("layoutY:"+this.getRootParentNode().getLayoutY());
//			System.out.println("height:"+this.getMainContentPane().getHeight());
//		});
	}
}
