package com.cosium.vet_intellij.commit_and_push;

import com.cosium.vet_intellij.ChangePusher;
import com.cosium.vet_intellij.VetComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
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

    Project project = e.getProject();
    if (project == null) {
      throw new RuntimeException("No project found for event " + e);
    }

    new ChangePusher(project, project.getComponent(VetComponent.class)).run();
  }

  @NotNull
  @Override
  protected String getExecutorId() {
    return CommitAndVetPushExecutor.ID;
  }
}
