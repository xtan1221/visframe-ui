package dependencygraph.vcd;

import java.util.Comparator;

import dependency.vcd.VCDNodeImpl;

/**
 * static factory class for methods that create Comparator to comparing two CFDNodeImpl to facilitate decide the order of a set of CFDNodeImpls;
 * @author tanxu
 *
 */
public class VCDNodeOrderingComparatorFactory {
	
	/**
	 * first sort by the host CompositionFunctionGroup's name;
	 * then sort by the copy index;
	 * @return
	 */
	public static Comparator<VCDNodeImpl> getComparator(){
		return (a,b)->{return a.getPrecedenceIndex()-b.getPrecedenceIndex();};
	}
	
	
	
}
