package com.cosium.vet_intellij.push;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.changes.actions.BaseCommitExecutorAction;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 15/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class CommitAndVetPushExecutorAction extends BaseCommitExecutorAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    super.actionPerformed(e);
  }

  @NotNull
  @Override
  protected String getExecutorId() {
    return CommitAndVetPushExecutor.ID;
  }
}
