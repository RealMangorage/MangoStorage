package org.mangorage.mangostorage.util;

public class ChangedStatus {
    public static ChangedStatus of(Runnable changed) {
        return new ChangedStatus(changed);
    }

    private boolean changed = false;
    private final Runnable runnable;

    private ChangedStatus(Runnable runnable) {
        this.runnable = runnable;
    }
    private void setChanged() {
        changed = true;
    }

    public void setChanged(boolean value) {
        if (value) setChanged();
    }

    public boolean hasChanged() {
        return changed;
    }

    public void finalizeChanged() {
        if (hasChanged()) runnable.run();
        changed = false;
    }
}
