package com.cosium.vet_intellij.push;

import com.cosium.vet_intellij.ChangePusher;
import com.cosium.vet_intellij.VetComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * Created on 17/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class PushAction extends AnAction {
  @Override
  public void actionPerformed(AnActionEvent e) {
    Project project = e.getProject();
    if (project == null) {
      throw new RuntimeException("No project found for event " + e);
    }

    new ChangePusher(project, project.getComponent(VetComponent.class)).run();
  }
}
