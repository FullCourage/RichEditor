package com.text.richeditor.editor.interfaces;


import com.text.richeditor.editor.ActionType;

public interface OnActionPerformListener {
    void onActionPerform(ActionType type, Object... values);
}
