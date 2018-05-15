package com.cosium.vet_intellij.push;

import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.CommitSession;
import com.intellij.openapi.vcs.changes.actions.BaseCommitExecutorAction;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 13/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class CommitAndVetPushExecutor implements CommitExecutor {

  static final String ID = "Vet.Commit.And.Push.Executor";

  @Nls
  @Override
  public String getActionText() {
    return "Commit and &Vet Push...";
  }

  @NotNull
  @Override
  public CommitSession createCommitSession() {
    return CommitSession.VCS_COMMIT;
  }

  @Override
  public boolean useDefaultAction() {
    return false;
  }

  @Nullable
  @Override
  public String getId() {
    return ID;
  }

}
