package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.command.DebugOptions;
import com.cosium.vet_intellij.push.CommitAndVetPushExecutor;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 * Created on 15/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class VetComponent implements ProjectComponent {

  private final Vet vet;

  public VetComponent(Project project) {
    Path path = Paths.get(requireNonNull(project.getBasePath()));
    vet = new Vet(false, DebugOptions.empty(), path);

    ChangeListManager.getInstance(project).registerCommitExecutor(new CommitAndVetPushExecutor());
  }

  public Vet vet() {
    return vet;
  }
}
