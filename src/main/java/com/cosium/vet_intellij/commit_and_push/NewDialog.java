package com.cosium.vet_intellij.commit_and_push;

import com.cosium.vet.Vet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static java.util.Objects.requireNonNull;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class NewDialog extends DialogWrapper {

  private final Vet vet;

  protected NewDialog(@Nullable Project project, Vet vet) {
    super(project);
    this.vet = requireNonNull(vet);
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {

    return null;
  }
}
