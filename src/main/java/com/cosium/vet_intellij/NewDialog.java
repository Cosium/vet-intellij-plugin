package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.gerrit.Change;
import com.cosium.vet.git.BranchShortName;
import com.cosium.vet.thirdparty.apache_commons_lang3.StringUtils;
import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static java.util.Objects.requireNonNull;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class NewDialog extends DialogWrapper {

  private final Project project;
  private final Vet vet;

  private final JPanel panel;
  private final JBTextField targetBranch;

  public NewDialog(@Nullable Project project, Vet vet) {
    super(project);
    this.project = project;
    this.vet = requireNonNull(vet);

    targetBranch = new JBTextField("master");

    panel = new JBPanel(new GridLayout(0, 2));
    panel.add(new JBLabel("Target branch"));
    panel.add(targetBranch);

    this.init();
    this.setTitle("Vet New Options");
    this.setOKButtonText("Push");
    this.setResizable(false);
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();

    try {
      Change change =
          vet.newCommandFactory().build(true, BranchShortName.of(targetBranch.getText())).execute();

      Notification okMessage =
          new Notification(
              VetComponent.DISPLAY_ID,
              "Push successful",
              "Pushed to " + change.getWebUrl(),
              NotificationType.INFORMATION);
      okMessage.addAction(
          new AnAction("Open change") {
            @Override
            public void actionPerformed(AnActionEvent e) {
              BrowserUtil.browse(change.getWebUrl());
            }
          });
      Notifications.Bus.notify(okMessage, project);
    } catch (Exception e) {
      Notifications.Bus.notify(
          new Notification(
              VetComponent.DISPLAY_ID, "Push failed", e.getMessage(), NotificationType.ERROR),
          project);
    }
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    if (!StringUtils.isBlank(targetBranch.getText())) {
      return null;
    }
    return new ValidationInfo("The target branch is mandatory", targetBranch);
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return panel;
  }
}
