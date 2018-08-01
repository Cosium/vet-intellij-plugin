package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.gerrit.AlterableChange;
import com.cosium.vet.gerrit.Change;
import com.intellij.openapi.project.Project;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class ChangePusher {

  private final Project project;
  private final Vet vet;

  public ChangePusher(Project project, Vet vet) {
    this.project = project;
    this.vet = requireNonNull(vet);
  }

  public void run() {
    Optional<AlterableChange> trackedChange = vet.getTrackedChange();
    if (trackedChange.isPresent()) {
      new PushDialog(project, vet, trackedChange.get()).show();
    } else {
      new CreateDialog(project, vet).show();
    }
  }
}
