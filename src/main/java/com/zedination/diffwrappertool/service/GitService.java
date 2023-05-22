package com.zedination.diffwrappertool.service;

import com.zedination.diffwrappertool.model.GlobalState;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GitService {
    private GitService() {
    }

    public static GitService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final GitService INSTANCE = new GitService();
    }

    public void initGitState(String repositoryPath) throws IOException {
        GlobalState.selectedLocalRepository = repositoryPath;
        Repository repository = Git.open(new File(GlobalState.selectedLocalRepository)).getRepository();
        Ref currentBranch = repository.exactRef(repository.getFullBranch());
        GlobalState.selectedBranch = currentBranch.getName();
    }

    public void reInitGitState(String repositoryPath) throws IOException {
        GlobalState.selectedLocalRepository = repositoryPath;
        Repository repository = Git.open(new File(GlobalState.selectedLocalRepository)).getRepository();
        Ref currentBranch = repository.exactRef(repository.getFullBranch());
        GlobalState.selectedBranch = currentBranch.getName();
    }

    public List<RevCommit> getListCommit() throws IOException {
        // Mở repository
        Repository repository = Git.open(new File(GlobalState.selectedLocalRepository)).getRepository();
        // Lấy đối tượng Ref của nhánh
        Ref branchRef = repository.findRef(GlobalState.selectedBranch);

        // Lấy ObjectId của commit cuối cùng trên nhánh
        ObjectId branchObjectId = branchRef.getObjectId();

        // Tạo đối tượng RevWalk
        RevWalk revWalk = new RevWalk(repository);

        // Bắt đầu từ commit cuối cùng trên nhánh
        RevCommit headCommit = revWalk.parseCommit(branchObjectId);

        // Tạo danh sách các commit
        List<RevCommit> commits = new ArrayList<>();

        // Duyệt qua các commit theo thứ tự ngược
        revWalk.markStart(headCommit);
        for (RevCommit commit : revWalk) {
            commits.add(commit);
        }

        // Đóng RevWalk và repository
        revWalk.close();
        repository.close();
        return commits;
    }

    public void diffHtml(String leftCommit, String rightCommit) throws IOException {
        String template = "diff2html -s side -t DIFF_RESULT -f html -d word -i command -o preview -- -M %s %s";
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", String.format(template, leftCommit, rightCommit));
        builder.directory(new File(GlobalState.selectedLocalRepository));
        builder.start();
    }
    public void diffBeyondCompare(String leftCommit, String rightCommit) throws IOException {
        String template = "git difftool -d %s %s";
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", String.format(template, leftCommit, rightCommit));
        builder.directory(new File(GlobalState.selectedLocalRepository));
        builder.start();
    }

    public void changeConfigForBeyondCompare(String beyondComparePath) throws IOException {
        Repository repository = Git.open(new File(GlobalState.selectedLocalRepository)).getRepository();
        StoredConfig config = repository.getConfig();
        config.setString("difftool", "bc", "path", Optional.ofNullable(beyondComparePath).filter(x ->!x.isEmpty()).orElse("c:/Program Files/Beyond Compare 4/bcomp.exe"));
        config.setBoolean("difftool", null, "prompt", false);
        config.setString("diff", null, "tool", "bc");
        config.save();

    }
}
