package com.teaminfinity.elementalinvocations.item;

import com.teaminfinity.elementalinvocations.utility.IToggleable;

import java.util.List;

public interface IInfinityItem extends IToggleable {
    String getInternalName();

    List<String> getOreTags();
}
