package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance;

import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducerAndInserter;
import core.pipeline.AbstractProcessMainManager;
import core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps.ASelectVisSchemeAppliedArchiveStepManager;

public class VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager extends AbstractProcessMainManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance>{
	private final static String TITLE = "Make new VisSchemeAppliedArchiveReproducedAndInsertedInstance";
	
	/**
	 * contains the current VisSchemeAppliedArchiveReproducerAndInserter
	 * should be re-initialized whenever a new VisSchemeAppliedArchive is selected;
	 */
	private VisSchemeAppliedArchiveReproducerAndInserter visSchemeAppliedArchiveReproducerAndInserter;
	
	/**
	 * 
	 */
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager() {
		super(VisSchemeAppliedArchiveReproducedAndInsertedInstance.class, ASelectVisSchemeAppliedArchiveStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the visSchemeAppliedArchiveReproducerAndInserter
	 */
	public VisSchemeAppliedArchiveReproducerAndInserter getVisSchemeAppliedArchiveReproducerAndInserter() {
		return visSchemeAppliedArchiveReproducerAndInserter;
	}

	/**
	 * @param visSchemeAppliedArchiveReproducerAndInserter the visSchemeAppliedArchiveReproducerAndInserter to set
	 */
	public void setVisSchemeAppliedArchiveReproducerAndInserter(VisSchemeAppliedArchiveReproducerAndInserter visSchemeAppliedArchiveReproducerAndInserter) {
		this.visSchemeAppliedArchiveReproducerAndInserter = visSchemeAppliedArchiveReproducerAndInserter;
	}
	
}
