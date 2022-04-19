package dependencygraph.dos;

import java.util.Comparator;

import dependency.dos.DOSNodeImpl;

/**
 * static factory class for methods that create Comparator to comparing two CFDNodeImpl to facilitate decide the order of a set of CFDNodeImpls;
 * @author tanxu
 *
 */
public class DOSNodeOrderingComparatorFactory {
	
	/**
	 * first sort by the contained MetadataID's data type;
	 * then sort by the MetadataID's name;;
	 * @return
	 */
	public static Comparator<DOSNodeImpl> getComparator(){
		return (a,b)->{
			if(!a.getMetadataID().getDataType().equals(b.getMetadataID().getDataType())) {
				return a.getMetadataID().getDataType().compareTo(b.getMetadataID().getDataType());
			}else{
				return a.getMetadataID().getName().compareTo(b.getMetadataID().getName());
			}
		};
	}
	
	
	
}
