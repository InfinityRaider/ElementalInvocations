/*
 */
package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.souls.ISoul;

/**
 *
 */
public class BasicSoul implements ISoul {
    
    final String entityId;

    public BasicSoul(String entityId) {
        this.entityId = entityId;
    }

	@Override
	public String getId() {
		return entityId;
	}
	
}
