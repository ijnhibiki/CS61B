package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *
 *  does at a high level.
 *
 *  @author Haobo Chen
 */
public class Repository implements Serializable {
    /**
     *
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The stage area directory. */
    public static final File STAGE_AREA = join(GITLET_DIR, "stage_area");

    /** The Objects directory. */
    public static final File OBJECTS = join(GITLET_DIR, "objects");

    /** The header file. */
    private static final File HEADER = join(GITLET_DIR, "header");
    /** The repo information file. */
    private static final File REPO_INFO = join(GITLET_DIR, "repo");
    /** The hashmap keep track of blobs. */
    private HashMap<String, File> blobs;
    private TreeMap<String, String> adds;
    private TreeMap<String, String> rms;
    private TreeMap<String, Commit> commitList;
    private TreeMap<String, Commit> shortCommitList;
    private TreeMap<String, Commit> branch;
    
    private Commit header;
    private Commit secondHeader;
    private Commit splitPoint;
    private String currentBranch;

    private CommitGraph commitGraph;

    private HashMap<String, File> remoteInfo;
    private Commit initalComit;
    //private HashMap<String, Repository> remoteRepo;







    public Repository() {
        this.blobs = new HashMap<>();
        this.adds = new TreeMap<>();
        this.rms = new TreeMap<>();
        this.header = null;
        this.secondHeader = null;
        this.splitPoint = null;
        this.branch = new TreeMap<>();
        this.currentBranch = "master";
        this.commitList = new TreeMap<>();
        this.shortCommitList = new TreeMap<>();
        this.commitGraph = new CommitGraph();
        this.remoteInfo = new HashMap<>();
        this.initalComit = null;
        //this.remoteRepo = new HashMap<>();
    }

    public void setupPersistence() throws IOException {
        if (!GITLET_DIR.mkdir()) {
            throw new IOException();
        }
        if (!STAGE_AREA.mkdir()) {
            throw new IOException();
        }
        if (!OBJECTS.mkdir()) {
            throw new IOException();
        }
        if (!HEADER.createNewFile()) {
            throw new IOException();
        }
        if (!REPO_INFO.createNewFile()) {
            throw new IOException();
        }
    }


    public void init() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
        } else {
            setupPersistence();
            Commit initial = new Commit("initial commit", null, null, null);
            this.header = initial;
            this.initalComit = initial;
            if (initial.getCommitSHA().length() <= UID_LENGTH) {
                String shortId = initial.getCommitSHA().substring(0, 6);
                shortCommitList.put(shortId, initial);
            }
            commitList.put(initial.getCommitSHA(), initial);
            commitGraph.addNode(initial.getCommitSHA());
            writeInfo();
        }
    }

    public void add(String file) throws IOException {
        checkRepo();
        File addFile = join(CWD, file);

        if (!addFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            String addSHA1 = sha1(readContentsAsString(addFile) + addFile);
            if (rms.containsKey(file)) {
                this.rms.remove(file);
            }
            String headerSha = "0";
            String addSha = "1";
            if (this.header.getFileMap().containsKey(file)) {
                File headerfile = blobs.get(this.header.getFileMap().get(file));
                headerSha = sha1(readContentsAsString(headerfile) + addFile);
                addSha = sha1(readContentsAsString(addFile) + addFile);
            }
            if (this.header.getFileMap().containsKey(file) && addSha.equals(headerSha)) {
                adds.remove(file);
                File delete = join(STAGE_AREA, file);
                if (delete.exists()) {
                    delete.delete();
                }
            } else {
                if (adds.containsKey(file)) {
                    adds.put(file, addSHA1);
                    File current = join(STAGE_AREA, file);
                    String updated = readContentsAsString(addFile);
                    writeContents(current, updated);
                } else {
                    File target = join(OBJECTS, sha1(readContentsAsString(addFile) + addFile));
                    File addArea = join(STAGE_AREA, file);
                    if (!target.exists()) {
                        Files.copy(addFile.toPath(), target.toPath());
                    }

                    Files.copy(addFile.toPath(), addArea.toPath());
                    adds.put(file, addSHA1);
                    blobs.put(sha1(readContentsAsString(addFile) + addFile), target);
                }
            }
            writeInfo();
        }
    }

    public void commit(String message) {
        checkRepo();
        if (message.length() == 0) {
            System.out.println("Please enter a commit message.");
        } else if (adds.isEmpty() && rms.isEmpty()) {
            System.out.println("No changes added to the commit.");
        } else {
            for (Map.Entry<String, String> prevFile: this.header.getFileMap().entrySet()) {
                if (!adds.containsKey(prevFile.getKey()) && !rms.containsKey(prevFile.getKey())) {
                    adds.put(prevFile.getKey(), prevFile.getValue());
                }
            }

            Commit newCommit = new Commit(message, this.header, this.secondHeader, adds);
            if (newCommit.getCommitSHA().length() <= UID_LENGTH) {
                String shortId = newCommit.getCommitSHA().substring(0, 6);
                shortCommitList.put(shortId, newCommit);

            }
            commitGraph.addNode(newCommit.getCommitSHA());
            commitGraph.addEdge(this.header.getCommitSHA(), newCommit.getCommitSHA());
            commitList.put(newCommit.getCommitSHA(), newCommit);
            if (secondHeader != null) {
                commitGraph.addEdge(this.secondHeader.getCommitSHA(), newCommit.getCommitSHA());
            }
            cleanStage();
            this.header = newCommit;
            writeInfo();
        }
    }

    public void rm(String filename) throws IOException {
        checkRepo();
        if (!adds.containsKey(filename) && !header.getFileMap().containsKey(filename)) {
            System.out.println("No reason to remove the file.");
        }
        if (adds.containsKey(filename)) {
            File delete = join(STAGE_AREA, filename);
            delete.delete();
            adds.remove(filename);
        }
        if (this.header.getFileMap().containsKey(filename)) {
            File delete = join(CWD, filename);
            if (delete.exists()) {
                delete.delete();
            }
            String target = this.header.getFileMap().get(filename);
            rms.put(filename, target);
            //fileTracked.remove(filename);
        }
        writeInfo();
    }

    public void log() {
        checkRepo();
        Commit printNode = this.header;
        while (printNode != null) {
            System.out.println("===");
            System.out.println("commit " + printNode.getCommitSHA());
            System.out.println("Date: " + printNode.getTimestamp());
            System.out.println(printNode.getMessage());
            System.out.println();
            printNode = printNode.getParent();
        }
    }
    public void globalLog() {
        checkRepo();
        for (Map.Entry<String, Commit> commit: commitList.entrySet()) {
            System.out.println("===");
            System.out.println("commit " + commit.getValue().getCommitSHA());
            System.out.println("Date: " + commit.getValue().getTimestamp());
            System.out.println(commit.getValue().getMessage());
            System.out.println();
        }
    }

    public void find(String commitMessage) {
        checkRepo();
        int targetCommit = 0;
        for (Map.Entry<String, Commit> commit: commitList.entrySet()) {
            if (commit.getValue().getMessage().equals(commitMessage)) {
                System.out.println(commit.getValue().getCommitSHA());
                targetCommit += 1;
            }
        }
        if (targetCommit == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void status() {
        checkRepo();
        System.out.println("=== Branches ===");
        for (Map.Entry<String, Commit> branchPrint: branch.entrySet()) {
            if (branchPrint.getKey().equals(this.currentBranch)) {
                System.out.println("*" + branchPrint.getKey());
            } else {
                System.out.println(branchPrint.getKey());
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String staged: adds.keySet()) {
            System.out.println(staged);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String staged: rms.keySet()) {
            System.out.println(staged);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        TreeMap<String, String> printList = new TreeMap<>();
        stagedForAdd(printList);
        stagedCommit(printList);
        for (Map.Entry<String, String> print: printList.entrySet()) {
            System.out.println(print.getKey() + " " + print.getValue());
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String currentFile: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if (isUntrackedStatus(currentFile)) {
                System.out.println(currentFile);
            }
        }
        System.out.println();


    }

    public void checkout(String filename) throws IOException {
        checkout(null, filename, null, 1);
    }

    public void checkout(String commitID, String filename) throws IOException {
        checkout(commitID, filename, null, 2);
    }

    public void checkoutBranch(String branchName) throws IOException {
        checkout(null, null, branchName, 3);
    }




    public void checkout(String commitID, String filename, String branchName, int selector)
            throws IOException {
        checkRepo();
        if (selector == 1) {
            if (!this.header.getFileMap().containsKey(filename)) {
                System.out.println("File does not exist in that commit.");
            } else {
                File current = join(CWD, filename);
                File toCheck = blobs.get(this.header.getFileMap().get(filename));
                String checked = readContentsAsString(toCheck);
                writeContents(current, checked);
                writeInfo();
            }
        } else if (selector == 2) {
            String shortId = commitID.substring(0, 6);
            boolean inShort = this.shortCommitList.containsKey(shortId);
            boolean inLong = this.commitList.containsKey(commitID);
            if (!inLong && !inShort) {
                System.out.println("No commit with that id exists.");
            } else {
                if (idLen(commitID) && !inCommit(commitID, filename)) {
                    System.out.println("File does not exist in that commit.");
                    System.exit(0);
                } else if (!this.shortCommitList.get(shortId).getFileMap().containsKey(filename)) {
                    System.out.println("File does not exist in that commit.");
                    System.exit(0);
                } else {
                    File current = join(CWD, filename);
                    String checked = "";
                    if (idLen(commitID)) {
                        if (this.commitList.get(commitID).getFileMap().containsKey(filename)) {
                            Commit toCheck = this.commitList.get(commitID);
                            File checkfile = blobs.get(toCheck.getFileMap().get(filename));
                            checked = readContentsAsString(checkfile);
                        }
                    } else {
                        Commit checkCommit = this.shortCommitList.get(shortId);
                        File checkfile = blobs.get(checkCommit.getFileMap().get(filename));
                        checked = readContentsAsString(checkfile);
                    }
                    writeContents(current, checked);
                    writeInfo();
                }
            }
        } else if (selector == 3) {
            checkoutBranchChecker(branchName);
            Commit writer = branch.get(branchName);
            for (Map.Entry<String, String> toWrite: writer.getFileMap().entrySet()) {
                File toWriteFile = join(CWD, toWrite.getKey());
                if (toWriteFile.exists()) {
                    String content = readContentsAsString(blobs.get(toWrite.getValue()));
                    writeContents(toWriteFile, content);
                } else {
                    toWriteFile.createNewFile();
                    String content = readContentsAsString(blobs.get(toWrite.getValue()));
                    writeContents(toWriteFile, content);
                }
            }
            this.currentBranch = branchName;
            this.header = writer;
            for (String cwdFile : Objects.requireNonNull(plainFilenamesIn(CWD))) {
                if (!writer.getFileMap().containsKey(cwdFile)) {
                    File delete = join(CWD, cwdFile);
                    delete.delete();
                }
            }
            cleanStage();
            writeInfo();
        }
    }


    public void branch(String branchName) {
        checkRepo();
        if (branch.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branch.put(branchName, this.header);
        writeInfo();
    }

    public void rmBranch(String branchName) {
        checkRepo();
        if (!branch.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (this.currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        }
        branch.remove(branchName);
        writeInfo();
    }

    public void reset(String commitID) throws IOException {
        checkRepo();
        String shortId = commitID.substring(0, 6);
        if (!commitList.containsKey(commitID) && !shortCommitList.containsKey(shortId)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit target;
        if (idLen(commitID)) {
            target = commitList.get(commitID);
        } else {
            target = shortCommitList.get(commitID);
        }
        for (String cwdFile : Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if (isUntracked(cwdFile) && target.getFileMap().containsKey(cwdFile)) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        for (Map.Entry<String, String> fileToTake : target.getFileMap().entrySet()) {
            checkout(commitID, fileToTake.getKey());
        }

        for (String cwdFile: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if (!isUntracked(cwdFile) && !target.getFileMap().containsKey(cwdFile)) {
                File delete = join(CWD, cwdFile);
                delete.delete();
            }
        }
        this.header = target;
        cleanStage();
        writeInfo();
    }

    public void merge(String branchName) throws IOException {
        checkRepo();
        mergeBranchChecker(branchName);
        setSplitPoint(branchName);
        splitPointChecker(branchName);

        HashSet<String> filesTocheck = new HashSet<>(Objects.requireNonNull(plainFilenamesIn(CWD)));
        for (String fileInOther: branch.get(branchName).getFileMap().keySet()) {
            if (!filesTocheck.contains(fileInOther)) {
                filesTocheck.add(fileInOther);
            }
        }
        boolean conflicts = false;
        for (String fileToCheck : filesTocheck) {
            if (branchChecker1(fileToCheck, branchName)) {
                checkout(branch.get(branchName).getCommitSHA(), fileToCheck);
                add(fileToCheck);
            }
            if (branchChecker2(fileToCheck, branchName)) {
                continue;
            }
            if (branchChecker3(fileToCheck, branchName)) {
                continue;
            }
            if (branchChecker4(fileToCheck, branchName)) {
                continue;
            }
            if (branchChecker5(fileToCheck, branchName)) {
                checkout(branch.get(branchName).getCommitSHA(), fileToCheck);
                add(fileToCheck);
            }

            if (branchChecker6(fileToCheck, branchName)) {
                File delete = join(CWD, fileToCheck);
                delete.delete();
                rms.put(fileToCheck, this.header.getFileMap().get(fileToCheck));
                this.header.getFileMap().remove(fileToCheck);
            }

            if (branchChecker7(fileToCheck, branchName)) {
                continue;
            }

            if (branchChecker8(fileToCheck, branchName)) {
                conflicts = true;
                Commit targetCommit = branch.get(branchName);
                String a = "<<<<<<< HEAD\n";
                String b = "=======\n";
                String c = ">>>>>>>\n";
                String currentBranchString = "";
                String givenBranchString = "";
                if (fileExistOn(fileToCheck, this.header)) {
                    File toCheck = blobs.get(header.getFileMap().get(fileToCheck));
                    currentBranchString = readContentsAsString(toCheck);
                }
                if (fileExistOn(fileToCheck, targetCommit)) {
                    File toCheck = blobs.get(targetCommit.getFileMap().get(fileToCheck));
                    givenBranchString = readContentsAsString(toCheck);
                }
                String output = a + currentBranchString + b + givenBranchString + c;
                File toWrite = join(CWD, fileToCheck);

                writeContents(toWrite, output);
                add(fileToCheck);
            }
        }

        String message = "Merged " + branchName + " into " + currentBranch + ".";
        this.secondHeader = branch.get(branchName);
        commit(message);

        this.secondHeader = null;



        if (conflicts) {
            System.out.println("Encountered a merge conflict.");
        }
        writeInfo();
    }

    public static Repository readRepo() {
        if (GITLET_DIR.exists()) {
            return readObject(REPO_INFO, Repository.class);
        }
        return new Repository();
    }

    public void writeInfo() {
        this.branch.put(this.currentBranch, header);
        if (GITLET_DIR.exists()) {
            writeObject(REPO_INFO, this);
        }
    }

    public void checkRepo() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public void checkoutBranchChecker(String branchName) {
        if (!this.branch.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (this.currentBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        if (!adds.isEmpty()) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            System.exit(0);
        }
        for (String currentFile: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            boolean branchChecker = branch.get(branchName).getFileMap().containsKey(currentFile);
            if (isUntracked(currentFile) && branchChecker) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    public boolean isUntracked(String fileName) {
        return !this.header.getFileMap().containsKey(fileName);
    }

    public boolean isUntrackedStatus(String fileName) {
        return !adds.containsKey(fileName) && !this.header.getFileMap().containsKey(fileName);
    }

    public boolean isUntrackedMerge(String fileName, String branchName) {
        boolean headerChecker = header.getFileMap().containsKey(fileName);
        boolean branchChecker = branch.get(branchName).getFileMap().containsKey(fileName);
        if (!headerChecker && !branchChecker) {
            return false;

        }
        return isUntracked(fileName);
    }

    public void stagedForAdd(TreeMap<String, String> input) {
        for (Map.Entry<String, String> addItem: adds.entrySet()) {
            if (!Objects.requireNonNull(plainFilenamesIn(CWD)).contains(addItem.getKey())) {
                input.put(addItem.getKey(), "(deleted)");
            } else {
                File add = blobs.get(addItem.getValue());
                File cwdFile = join(CWD, addItem.getKey());
                if (!sha1(readContentsAsString(add)).equals(sha1(readContentsAsString(cwdFile)))) {
                    input.put(addItem.getKey(), "(modified)");
                }
            }
        }
    }

    public void stagedCommit(TreeMap<String, String> input) {
        for (Map.Entry<String, String> commitItem: this.header.getFileMap().entrySet()) {
            if (Objects.requireNonNull(plainFilenamesIn(CWD)).contains(commitItem.getKey())) {
                File commit = blobs.get(commitItem.getValue());
                File cwdFile = join(CWD, commitItem.getKey());
                String commitC = sha1(readContentsAsString(commit));
                String cwdFileC = sha1(readContentsAsString(cwdFile));
                if (!commitC.equals(cwdFileC)) {
                    input.put(commitItem.getKey(), "(modified)");
                }
            }
            if (!Objects.requireNonNull(plainFilenamesIn(CWD)).contains(commitItem.getKey())) {
                if (!rms.containsKey(commitItem.getKey())) {
                    input.put(commitItem.getKey(), "(deleted)");
                }
            }
        }
    }

    public void cleanStage() {
        List<String> fileInAdd = plainFilenamesIn(STAGE_AREA.toString());
        assert fileInAdd != null;
        for (String f: fileInAdd) {
            File delete = join(STAGE_AREA, f);
            delete.delete();
        }
        this.adds = new TreeMap<>();
        this.rms = new TreeMap<>();
    }

    public void mergeBranchChecker(String branchName) {
        if (!adds.isEmpty() || !rms.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!branch.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (this.currentBranch.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    public void setSplitPoint(String branchName) {
        String commitIndex1 = this.header.getCommitSHA();
        String commitIndex2 = branch.get(branchName).getCommitSHA();
        /**
        ArrayList<String> commitPath = new ArrayList<>();
        HashSet<String> commitCompare = new HashSet<>();
        while (commitIndex1 != null) {
            commitPath.add(commitIndex1.getCommitSHA());
            commitIndex1 = commitIndex1.getParent();
        }
        while (commitIndex2 != null) {
            commitCompare.add(commitIndex2.getCommitSHA());
            commitIndex2 = commitIndex2.getParent();
        }
        for (String commitSha1 : commitPath) {
            if (commitCompare.contains(commitSha1)) {
                this.splitPoint = commitList.get(commitSha1);
                return;
            }
        }
         */

        String lcaSHA1 = this.commitGraph.findLCA(commitIndex1, commitIndex2);
        this.splitPoint = commitList.get(lcaSHA1);
    }

    public void splitPointChecker(String branchName) throws IOException {
        if (branch.get(branchName).getCommitSHA().equals(this.splitPoint.getCommitSHA())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (branch.get(this.currentBranch).getCommitSHA().equals(this.splitPoint.getCommitSHA())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        for (String fileName : Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if (isUntrackedMerge(fileName, branchName)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    public boolean branchChecker1(String fileName, String branchName) {
        Commit targetCommit = branch.get(branchName);
        boolean splitChecker = fileExistOn(fileName, splitPoint);
        boolean headerChecker = fileExistOn(fileName, header);
        boolean targetChecker = fileExistOn(fileName, targetCommit);
        if (splitChecker && headerChecker && targetChecker) {
            boolean mSinceB = modifiedSinceSplit(fileName, branchName);
            boolean mSinceC = modifiedSinceSplit(fileName, this.currentBranch);
            return !mSinceC && mSinceB;
        }
        return false;
    }

    public boolean branchChecker2(String fileName, String branchName) {
        Commit targetCommit = branch.get(branchName);
        boolean splitChecker = fileExistOn(fileName, splitPoint);
        boolean headerChecker = fileExistOn(fileName, header);
        boolean targetChecker = fileExistOn(fileName, targetCommit);
        if (splitChecker && headerChecker && targetChecker) {
            boolean mSinceB = modifiedSinceSplit(fileName, branchName);
            boolean mSinceC = modifiedSinceSplit(fileName, this.currentBranch);
            return !mSinceB && mSinceC;
        }
        return false;
    }

    /** 3. Any files that have been modified in both the
     * current and given branch in the same way (i.e., both
     * files now have the same content or were both removed)
     * are left unchanged by the merge. If a file was removed
     * from both the current and given branch, but a file of the
     * same name is present in the working directory, it is left
     * alone and continues to be absent (not tracked nor staged)
     * in the merge.*/
    public boolean branchChecker3(String fileName, String branchName) {
        if (fileExistOn(fileName, splitPoint)) {
            boolean hContainF = header.getFileMap().containsKey(fileName);
            boolean bContainF = branch.get(branchName).getFileMap().containsKey(fileName);
            if (!hContainF && !bContainF) {
                return true;
            }
            if (hContainF && bContainF) {
                String givenBranch = branch.get(branchName).getFileMap().get(fileName);
                String current = this.branch.get(currentBranch).getFileMap().get(fileName);
                if (givenBranch.equals(current)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** 4. Any files that were not present at the split point and are
     * present only in the current branch should remain as they are.*/
    public boolean branchChecker4(String fileName, String branchName) {
        Commit targetCommit = branch.get(branchName);
        boolean splitChecker = fileExistOn(fileName, splitPoint);
        boolean headerChecker = fileExistOn(fileName, header);
        boolean targetChecker = fileExistOn(fileName, targetCommit);
        if (!splitChecker && !targetChecker && headerChecker) {
            return true;
        }
        return false;

    }

    /** 5. Any files that were not present at the split point
     * and are present only in the given branch should be checked
     * out and staged. */
    public boolean branchChecker5(String fileName, String branchName) {
        Commit targetCommit = branch.get(branchName);
        boolean splitChecker = fileExistOn(fileName, splitPoint);
        boolean headerChecker = fileExistOn(fileName, header);
        boolean targetChecker = fileExistOn(fileName, targetCommit);
        if (!splitChecker && targetChecker && !headerChecker) {
            return true;
        }
        return false;

    }

    /** 6. Any files present at the split point, unmodified in
     * the current branch, and absent in the given branch should
     * be removed (and untracked). */

    public boolean branchChecker6(String fileName, String branchName) {
        if (fileExistOn(fileName, splitPoint)) {
            boolean modifiedC = modifiedSinceSplit(fileName, currentBranch);
            boolean bContainsF = branch.get(branchName).getFileMap().containsKey(fileName);

            if (!modifiedC && !bContainsF) {
                return true;
            }
        }
        return false;
    }


    /** 7. Any files present at the split point, unmodified in the
     * given branch, and absent in the current branch should remain
     * absent. */

    public boolean branchChecker7(String fileName, String branchName) {
        if (fileExistOn(fileName, splitPoint)) {
            boolean modifiedB = !modifiedSinceSplit(fileName, branchName);
            if (!modifiedB && !header.getFileMap().containsKey(fileName)) {
                return true;
            }
        }
        return false;
    }

    public boolean branchChecker8(String fileName, String branchName) {
        //absent at the split point and has different contents in the given and current branches
        Commit targetCommit = branch.get(branchName);
        boolean splitChecker = fileExistOn(fileName, splitPoint);
        boolean headerChecker = fileExistOn(fileName, header);
        boolean targetChecker = fileExistOn(fileName, targetCommit);
        if (!splitChecker && headerChecker && targetChecker) {
            String givenBranch = branch.get(branchName).getFileMap().get(fileName);
            String current = this.branch.get(branchName).getFileMap().get(fileName);
            if (!givenBranch.equals(current)) {
                return true;
            }
        }

        //contents of both are changed and different from other
        if (splitChecker && headerChecker && targetChecker) {
            String givenBranch = branch.get(branchName).getFileMap().get(fileName);
            String current = branch.get(currentBranch).getFileMap().get(fileName);
            boolean modifiedB = modifiedSinceSplit(fileName, branchName);
            boolean modifiedC = modifiedSinceSplit(fileName, currentBranch);
            if (!givenBranch.equals(current) && modifiedB && modifiedC) {
                return true;
            }
        }
        //contents of target branch are changed and the other file is deleted
        if (splitChecker && !headerChecker && targetChecker) {
            if (modifiedSinceSplit(fileName, branchName)) {
                return true;
            }
        }
        //contents of current are changed and the other file is deleted


        if (splitChecker && headerChecker && !targetChecker) {
            if (modifiedSinceSplit(fileName, currentBranch)) {
                return true;
            }
        }
        return false;
    }


    public boolean modifiedSinceSplit(String fileName, String branchName) {
        String splitFileSha1 = this.splitPoint.getFileMap().get(fileName);
        String current = this.branch.get(branchName).getFileMap().get(fileName);
        return !splitFileSha1.equals(current);
    }

    public boolean fileExistOn(String fileName, Commit commit) {
        return commit.getFileMap().containsKey(fileName);
    }

    public boolean idLen(String commit) {
        return commit.length() == UID_LENGTH;
    }

    public boolean inCommit(String commitID, String filename) {
        return this.commitList.get(commitID).getFileMap().containsKey(filename);
    }




    public void addRemote(String name, String remoteDirName) {
        if (remoteInfo.containsKey(name)) {
            System.out.println("A remote with that name already exists.");
            System.exit(0);
        }

        remoteDirName = remoteDirName.replace('/', java.io.File.separatorChar);

        File remoteDir = new File(remoteDirName);

        remoteInfo.put(name, remoteDir);
        Repository remote = readRemoteRepo(remoteInfo.get(name));
        //remoteRepo.put(name, remote);
        writeInfo();
    }

    public void rmRemote(String name) {
        if (!remoteInfo.containsKey(name)) {
            System.out.println("A remote with that name does not exist.");
            System.exit(0);
        }
        remoteInfo.remove(name);
        writeInfo();
    }

    public void push(String name, String remoteBranch) throws IOException {
        if (!remoteInfo.get(name).exists()) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        Repository remote = readRemoteRepo(remoteInfo.get(name));
        if (!remote.branch.containsKey(remoteBranch)) {
            remote.branch(remoteBranch);
        }
        Commit intersection = inHistory(remote.header);
        if (intersection == null) {
            System.out.println("Please pull down remote changes before pushing.");
            System.exit(0);
        } else {
            ArrayList<Commit> append = toAppend(intersection);
            String finalCommitId = "";
            for (Commit item : append) {
                item.changeParent(remote.header);
                remote.commitGraph.addEdge(remote.header.getCommitSHA(), item.getCommitSHA());
                remote.commitList.put(item.getCommitSHA(), item);
                remote.shortCommitList.put(item.getCommitSHA().substring(0, 6), item);
                remote.header = item;
                finalCommitId = item.getCommitSHA();
                writeRemoteInfo(name, remote);
            }
            remote.blobs.putAll(this.blobs);
            remote.reset(finalCommitId);
        }
        writeInfo();
    }


    public void fetch(String name, String remoteBranch) throws IOException {
        if (!remoteInfo.get(name).exists()) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        Repository remote = readRemoteRepo(remoteInfo.get(name));
        if (!remote.branch.containsKey(remoteBranch)) {
            System.out.println("That remote does not have that branch.");
            System.exit(0);
        }
        if (!GITLET_DIR.exists()) {
            init();
        }
        for (Map.Entry<String, File> file : remote.blobs.entrySet()) {
            if (!this.blobs.containsKey(file.getKey())) {
                blobs.put(file.getKey(), file.getValue());
                File toPut = join(OBJECTS, file.getKey());
                toPut.createNewFile();
                String content = readContentsAsString(file.getValue());
                writeContents(toPut, content);
            }
        }
        ArrayList<Commit> toAppend = sortCommit(remote.header);
        this.commitList.putAll(remote.commitList);
        this.shortCommitList.putAll(remote.shortCommitList);
        String commitShaTemp = initalComit.getCommitSHA();

        for (Commit app : toAppend) {
            this.commitGraph.addNode(app.getCommitSHA());
            this.branch.put(name + "/" + remoteBranch, app);
            if (app.getParent() == null) {
                commitShaTemp = app.getCommitSHA();
            } else {
                this.commitGraph.addEdge(commitShaTemp, app.getCommitSHA());
            }
        }

        writeInfo();
    }


    public void pull(String name, String remoteBranch) throws IOException {
        fetch(name, remoteBranch);
        merge(name + "/" + remoteBranch);
        writeInfo();
    }

    public Commit inHistory(Commit reHead) {
        Commit commitCheck = header.getParent();
        while (commitCheck != null) {
            if (commitCheck.getCommitSHA().equals(reHead.getCommitSHA())) {
                return commitCheck;
            }
            commitCheck = commitCheck.getParent();
        }
        return null;
    }

    public ArrayList<Commit> toAppend(Commit intersection) {
        ArrayList<Commit> result = new ArrayList<>();
        Commit temp = header;
        while (!temp.equals(intersection)) {
            result.add(temp);
            temp = temp.getParent();
        }
        return result;
    }

    public ArrayList<Commit> sortCommit(Commit head) {
        ArrayList<Commit> result = new ArrayList<>();
        Commit temp = head;
        while (temp != null) {
            result.add(temp);
            temp = temp.getParent();
        }
        Collections.reverse(result);
        return result;
    }


    public static Repository readRemoteRepo(File remoteDirName) {
        if (remoteDirName.exists()) {
            File info = join(remoteDirName, "repo");
            return readObject(info, Repository.class);
        }
        return new Repository();
    }

    public void writeRemoteInfo(String remoteName, Repository remote) {
        if (this.remoteInfo.containsKey(remoteName)) {
            File remoteGitletDir = remoteInfo.get(remoteName);
            if (remoteGitletDir.exists()) {
                File remoteInfoFile = join(remoteGitletDir, "repo");
                writeObject(remoteInfoFile, remote);
            }
        }
    }
}
