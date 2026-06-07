package org.gamezone.model;

// Interface for formatting output to the UI
public interface IDisplayable {
    String getDisplayInfo();
    Object[] toTableRow();
}