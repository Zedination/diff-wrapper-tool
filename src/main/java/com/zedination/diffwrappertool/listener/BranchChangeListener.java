package com.zedination.diffwrappertool.listener;

import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.Repository;

public class BranchChangeListener implements RefsChangedListener {
    @Override
    public void onRefsChanged(RefsChangedEvent refsChangedEvent) {
        System.out.println(refsChangedEvent);
    }
}