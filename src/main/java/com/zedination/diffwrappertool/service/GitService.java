package com.zedination.diffwrappertool.service;

import com.zedination.diffwrappertool.model.GlobalState;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(GlobalState.selectedBranch);
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
}
