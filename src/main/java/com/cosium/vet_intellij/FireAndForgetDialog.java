package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.gerrit.Change;
import com.cosium.vet.gerrit.CodeReviewVote;
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
import static java.util.Optional.ofNullable;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class FireAndForgetDialog extends DialogWrapper {

  private final Project project;
  private final Vet vet;

  private final JPanel panel;
  private final JBTextField codeReviewVote;

  public FireAndForgetDialog(Project project, Vet vet) {
    super(project);
    this.project = project;
    this.vet = requireNonNull(vet);

    codeReviewVote = new JBTextField();

    panel = new JBPanel(new GridLayout(0, 2));
    panel.add(new JBLabel("Code review vote"));
    panel.add(codeReviewVote);

    this.init();
    this.setTitle("Vet Fire And Forget Options");
    this.setOKButtonText("Push");
    this.setResizable(false);
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();

    CodeReviewVote codeReviewVote =
        ofNullable(this.codeReviewVote.getText())
            .filter(StringUtils::isNotBlank)
            .map(CodeReviewVote::of)
            .orElse(null);

    try {
      Change change = vet.fireAndForgetCommandFactory().build(true, codeReviewVote).execute();

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
    if (StringUtils.isBlank(codeReviewVote.getText())) {
      return null;
    }
    try {
      CodeReviewVote.of(codeReviewVote.getText());
      return null;
    } catch (Exception e) {
      return new ValidationInfo(e.getMessage(), codeReviewVote);
    }
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return panel;
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return codeReviewVote;
  }
}
