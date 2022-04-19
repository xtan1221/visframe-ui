package dependencygraph.cfd;

import java.util.Comparator;

import dependency.cfd.CFDNodeImpl;

/**
 * static factory class for methods that create Comparator to comparing two CFDNodeImpl to facilitate decide the order of a set of CFDNodeImpls;
 * @author tanxu
 *
 */
public class CFDNodeOrderingComparatorFactory {
	
	/**
	 * first sort by the host CompositionFunctionGroup's name;
	 * then sort by the copy index;
	 * @return
	 */
	public static Comparator<CFDNodeImpl> getComparator(){
		return (a,b)->{
			if(!a.getCFID().getHostCompositionFunctionGroupID().equals(b.getCFID().getHostCompositionFunctionGroupID())) {
				return a.getCFID().getHostCompositionFunctionGroupID().getName().compareTo(b.getCFID().getHostCompositionFunctionGroupID().getName());
			}else{
				return a.getCFID().getIndexID() - b.getCFID().getIndexID();
			}
		};
	}
	
	
	
}
