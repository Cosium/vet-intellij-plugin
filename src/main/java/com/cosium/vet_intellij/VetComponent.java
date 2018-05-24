package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.VetVersion;
import com.cosium.vet.command.DebugOptions;
import com.cosium.vet_intellij.commit_and_push.CommitAndVetPushExecutor;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;

import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 * Created on 15/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class VetComponent extends Vet implements ProjectComponent {

  public static final String DISPLAY_ID = "Vet";

  static {
    VetVersion.setValue("1.5_intellij-plugin");
  }

  public VetComponent(Project project) {
    super(false, DebugOptions.empty(), Paths.get(requireNonNull(project.getBasePath())));
    ChangeListManager.getInstance(project).registerCommitExecutor(new CommitAndVetPushExecutor());
  }
}
