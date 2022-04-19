package core.pipeline.visscheme.appliedarchive;

import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.AbstractProcessMainManager;
import core.pipeline.visscheme.appliedarchive.steps.ASelectAppliedVisSchemeStepManager;

public class VisSchemeAppliedArchiveProcessMainManager extends AbstractProcessMainManager<VisSchemeAppliedArchive>{
	private final static String TITLE = "Make new VisSchemeAppliedArchive";
	
	public VisSchemeAppliedArchiveProcessMainManager() {
		super(VisSchemeAppliedArchive.class, ASelectAppliedVisSchemeStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}
	
}
