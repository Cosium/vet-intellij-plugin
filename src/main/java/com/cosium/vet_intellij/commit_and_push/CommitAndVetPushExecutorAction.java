package com.cosium.vet_intellij.commit_and_push;

import com.cosium.vet.Vet;
import com.cosium.vet.gerrit.Change;
import com.cosium.vet_intellij.CreateDialog;
import com.cosium.vet_intellij.PushDialog;
import com.cosium.vet_intellij.VetComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.actions.BaseCommitExecutorAction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

    Vet vet = project.getComponent(VetComponent.class);
    Optional<Change> trackedChange = vet.getTrackedChange();
    if (trackedChange.isPresent()) {
      new PushDialog(project, vet, trackedChange.get()).show();
    } else {
      new CreateDialog(project, vet).show();
    }
  }

  @NotNull
  @Override
  protected String getExecutorId() {
    return CommitAndVetPushExecutor.ID;
  }
}
