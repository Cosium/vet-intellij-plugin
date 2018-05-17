package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.gerrit.Change;
import com.cosium.vet.gerrit.CodeReviewVote;
import com.cosium.vet.gerrit.PatchSubject;
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
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class PushDialog extends DialogWrapper {
  private final Project project;
  private final Vet vet;
  private final Change change;

  private final JPanel panel;

  private final JBTextField codeReviewVote;
  private final JBCheckBox publishDraftedComments;
  private final JBCheckBox workInProgress;
  private final JBTextField patchSetSubject;

  public PushDialog(@Nullable Project project, Vet vet, Change change) {
    super(project);
    this.project = project;
    this.vet = requireNonNull(vet);
    this.change = requireNonNull(change);

    publishDraftedComments = new JBCheckBox();
    workInProgress = new JBCheckBox();
    codeReviewVote = new JBTextField();
    patchSetSubject = new JBTextField();

    panel = new JBPanel(new GridLayout(0, 2));

    panel.add(new JLabel("Tracked change"));
    String changeWebUrl = change.getWebUrl();
    HyperlinkLabel changeWebUrlLabel = new HyperlinkLabel(changeWebUrl);
    changeWebUrlLabel.setHyperlinkTarget(changeWebUrl);
    panel.add(changeWebUrlLabel);

    panel.add(new JBLabel("Mark as work in progress"));
    panel.add(workInProgress);

    panel.add(new JBLabel("Publish drafted comments"));
    panel.add(publishDraftedComments);

    panel.add(new JBLabel("Code review vote"));
    panel.add(codeReviewVote);

    panel.add(new JBLabel("Patch set subject"));
    panel.add(patchSetSubject);

    this.init();
    this.setTitle("Vet Push Options");
    this.setOKButtonText("Push");
    this.setResizable(false);
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();

    PatchSubject patchSubject =
        ofNullable(patchSetSubject.getText())
            .filter(StringUtils::isNotBlank)
            .map(PatchSubject::of)
            .orElse(null);
    CodeReviewVote vote =
        ofNullable(codeReviewVote.getText())
            .filter(StringUtils::isNotBlank)
            .map(CodeReviewVote::of)
            .orElse(null);

    try {
      vet.pushCommandFactory()
          .build(
              publishDraftedComments.isSelected(),
              workInProgress.isSelected(),
              patchSubject,
              false,
              vote)
          .execute();

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

  @NotNull
  @Override
  protected List<ValidationInfo> doValidateAll() {
    List<ValidationInfo> validationInfos = new ArrayList<>();

    String codeReviewVoteTxt = codeReviewVote.getText();
    if (StringUtils.isNotBlank(codeReviewVoteTxt)) {
      try {
        CodeReviewVote.of(codeReviewVoteTxt);
      } catch (Exception e) {
        validationInfos.add(new ValidationInfo(e.getMessage(), codeReviewVote));
      }
    }

    return validationInfos;
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return panel;
  }
}
