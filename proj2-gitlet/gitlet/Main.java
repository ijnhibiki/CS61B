package gitlet;

import java.io.IOException;
import java.util.Objects;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Haobo Chen
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Repository repo = Repository.readRepo();
        String firstArg = args[0];
        String file;
        String branch;
        String id;
        String remoteName;
        String remoteDirName;
        String remoteBranch;
        switch (firstArg) {
            case "init":
                validateNumArgs(args, 1);
                try {
                    repo.init();
                }
                catch (IOException ignored) {}
                break;

            case "add":
                validateNumArgs(args, 2);
                file = args[1];
                try {
                    repo.add(file);
                }
                catch (IOException ignored) {}
                break;

            case "commit":
                validateNumArgs(args,2);
                String message = args[1];
                repo.commit(message);
                break;

            case "rm":
                validateNumArgs(args,2);
                file = args[1];
                try {
                    repo.rm(file);
                }
                catch (IOException ignored) {}
                break;

            case "log":
                validateNumArgs(args, 1);
                repo.log();
                break;

            case "global-log":
                validateNumArgs(args, 1);
                repo.globalLog();
                break;

            case "find":
                validateNumArgs(args, 2);
                String commitMessage = args[1];
                repo.find(commitMessage);
                break;

            case "status":
                validateNumArgs(args, 1);
                repo.status();
                break;

            case "checkout":
                if(args.length == 2) {
                    branch = args[1];
                    try {
                        repo.checkoutBranch(branch);
                    }
                    catch (IOException ignored) {}
                } else if (args.length == 3 && Objects.equals(args[1], "--")) {
                    file = args[2];
                    try {
                        repo.checkout(file);
                    }
                    catch (IOException ignored) {}
                } else if (args.length == 4 && Objects.equals(args[2], "--")) {
                    id = args[1];
                    file = args[3];
                    try {
                        repo.checkout(id, file);
                    }
                    catch (IOException ignored) {}
                } else {
                    System.out.println("Incorrect operands.");
                }
                break;

            case "branch":
                validateNumArgs(args, 2);
                branch = args[1];
                repo.branch(branch);
                break;

            case "rm-branch":
                validateNumArgs(args, 2);
                branch = args[1];
                repo.rmBranch(branch);
                break;

            case "reset":
                validateNumArgs(args, 2);
                id = args[1];
                try {
                    repo.reset(id);
                }
                catch (IOException ignored) {}
                break;

            case "merge":
                validateNumArgs(args, 2);
                branch = args[1];
                try {
                    repo.merge(branch);
                }
                catch (IOException ignored) {}
                break;
            case "add-remote":
                validateNumArgs(args, 3);
                remoteName = args[1];
                remoteDirName = args[2];
                repo.addRemote(remoteName, remoteDirName);
                break;
            case "rm-remote":
                validateNumArgs(args, 2);
                remoteName = args[1];
                repo.rmRemote(remoteName);
                break;
            case "push":
                validateNumArgs(args, 3);
                remoteName = args[1];
                remoteBranch = args[2];
                try {
                    repo.push(remoteName, remoteBranch);
                }
                catch (IOException ignored) {}
                break;
            case "fetch":
                validateNumArgs(args, 3);
                remoteName = args[1];
                remoteBranch = args[2];
                try {
                    repo.fetch(remoteName, remoteBranch);
                }
                catch (IOException ignored) {}
                break;
            case "pull":
                validateNumArgs(args, 3);
                remoteName = args[1];
                remoteBranch = args[2];
                try {
                    repo.pull(remoteName, remoteBranch);
                }
                catch (IOException ignored) {}
                break;

            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
