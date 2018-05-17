package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class CreateDialog extends DialogWrapper {

  private final Vet vet;
  private final Project project;

  private final JPanel panel;
  private final ButtonGroup buttonGroup;

  private enum Command {
    fireAndForget((p, v) -> new FireAndForgetDialog(p, v).show()),
    checkoutNew((p, v) -> new CheckoutNewDialog(p, v).show()),
    newz((p, v) -> new NewDialog(p, v).show()),
    track((p, v) -> new TrackDialog(p, v).show());

    private final BiConsumer<Project, Vet> action;

    Command(BiConsumer<Project, Vet> action) {
      this.action = action;
    }
  }

  public CreateDialog(Project project, Vet vet) {
    super(project);
    this.project = project;
    this.vet = requireNonNull(vet);

    panel = new JBPanel(new GridLayout(0, 1));

    JRadioButton fireAndForget = new JRadioButton("Fire and forget");
    fireAndForget.setActionCommand(Command.fireAndForget.toString());

    JRadioButton checkoutNew = new JRadioButton("Checkout a new change");
    checkoutNew.setActionCommand(Command.checkoutNew.toString());

    JRadioButton newz = new JRadioButton("Track a new change");
    newz.setActionCommand(Command.newz.toString());

    JRadioButton track = new JRadioButton("Track an existing change");
    track.setActionCommand(Command.track.toString());

    buttonGroup = new ButtonGroup();

    buttonGroup.add(fireAndForget);
    panel.add(fireAndForget);

    buttonGroup.add(checkoutNew);
    panel.add(checkoutNew);

    buttonGroup.add(newz);
    panel.add(newz);

    buttonGroup.add(track);
    panel.add(track);

    buttonGroup.setSelected(fireAndForget.getModel(), true);

    this.init();
    this.setTitle("No Tracked Change");
    this.setResizable(false);
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();

    String actionCommand = buttonGroup.getSelection().getActionCommand();
    Command.valueOf(actionCommand).action.accept(project, vet);
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return panel;
  }
}
