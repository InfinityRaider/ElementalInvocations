/*
 */
package com.infinityraider.elementalinvocations.api.souls;

/**
 * Interface for representing the souls of departed entities.
 */
public interface ISoul {
	
	String getId();
    
    default String getName() {
        return this.getId() + " Soul";
    }
	
}
