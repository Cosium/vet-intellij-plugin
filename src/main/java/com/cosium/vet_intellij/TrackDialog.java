package com.cosium.vet_intellij;

import com.cosium.vet.Vet;
import com.cosium.vet.gerrit.Change;
import com.cosium.vet.gerrit.ChangeNumericId;
import com.cosium.vet.git.BranchShortName;
import com.cosium.vet.thirdparty.apache_commons_lang3.StringUtils;
import com.cosium.vet.thirdparty.apache_commons_lang3.math.NumberUtils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
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

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class TrackDialog extends DialogWrapper {

  private final Project project;
  private final Vet vet;

  private final JPanel panel;
  private final JBTextField numericId;
  private final JBTextField targetBranch;

  public TrackDialog(@Nullable Project project, Vet vet) {
    super(project);
    this.project = project;
    this.vet = requireNonNull(vet);

    numericId = new JBTextField();
    targetBranch = new JBTextField("master");

    panel = new JBPanel(new GridLayout(0, 2));
    panel.add(new JBLabel("Change numeric ID"));
    panel.add(numericId);
    panel.add(new JBLabel("Target branch"));
    panel.add(targetBranch);

    this.init();
    this.setTitle("Vet Track Options");
    this.setOKButtonText("Track");
    this.setResizable(false);
  }

  @NotNull
  @Override
  protected List<ValidationInfo> doValidateAll() {
    List<ValidationInfo> validationInfos = new ArrayList<>();
    if (!NumberUtils.isDigits(numericId.getText())) {
      validationInfos.add(new ValidationInfo("Numeric ID must be a valid number", numericId));
    } else {
      ChangeNumericId changeNumericId = ChangeNumericId.of(Long.parseLong(numericId.getText()));
      if (!vet.isChangeExist(changeNumericId)) {
        validationInfos.add(
            new ValidationInfo(
                "Could not find change with numeric ID " + changeNumericId, numericId));
      }
    }
    if (StringUtils.isBlank(targetBranch.getText())) {
      validationInfos.add(new ValidationInfo("Target branch is mandatory", targetBranch));
    }
    return validationInfos;
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();

    try {
      Change change =
          vet.trackCommandFactory()
              .build(
                  true,
                  ChangeNumericId.of(Long.parseLong(numericId.getText())),
                  BranchShortName.of(targetBranch.getText()))
              .execute();
      new PushDialog(project, vet, change).show();
    } catch (Exception e) {
      Notifications.Bus.notify(
          new Notification(
              VetComponent.DISPLAY_ID, "Track failed", e.getMessage(), NotificationType.ERROR),
          project);
    }
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return panel;
  }
}
