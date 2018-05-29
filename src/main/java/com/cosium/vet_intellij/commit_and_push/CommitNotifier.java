package com.cosium.vet_intellij.commit_and_push;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created on 29/05/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class CommitNotifier extends CheckinHandlerFactory {

  private static AtomicReference<Runnable> nextCommitSuccessListener = new AtomicReference<>();

  /**
   * Registers the provided listener and triggers it later on the next commit success
   *
   * @param listener The listener to execute on commit success
   */
  public static void onNextCommitSuccess(Runnable listener) {
    nextCommitSuccessListener.set(listener);
  }

  @NotNull
  @Override
  public CheckinHandler createHandler(
      @NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
    return new Handler();
  }

  private static class Handler extends CheckinHandler {

    @Override
    public void checkinFailed(List<VcsException> exception) {
      nextCommitSuccessListener.set(null);
    }

    @Override
    public void checkinSuccessful() {
      if (nextCommitSuccessListener.get() == null) {
        return;
      }
      nextCommitSuccessListener.getAndSet(null).run();
    }
  }
}
