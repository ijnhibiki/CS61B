package gitlet;
import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

/** Represents a gitlet commit object.
 *
 *  does at a high level.
 *
 *  @author Haobo Chen
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Commit firstParent;
    private Commit secondParent;
    private String timestamp;
    private String UID;
    private TreeMap<String, String> filelist;




    public Commit(String message, Commit firstParent, Commit secondParent,
                  TreeMap<String, String> files) {
        this.message = message;
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        String formate = "E MMM d HH:mm:ss yyyy Z";
        if (this.firstParent == null) {
            Date date = new Date(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat(formate, Locale.US);
            this.timestamp = dateFormat.format(date);
            this.filelist = new TreeMap<>();
        } else {
            //Setup times
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(formate, Locale.US);
            timestamp = dateFormat.format(date);
            //Setup file tree for commit
            this.filelist = files;
        }
        this.UID = Utils.sha1((Object) Utils.serialize(this));

    }

    public String getMessage() {
        return this.message;
    }
    public Commit getParent() {
        return this.firstParent;
    }
    public String getTimestamp() {
        return this.timestamp;
    }
    public String getCommitSHA() {
        return this.UID;
    }
    public TreeMap<String, String> getFileMap() {
        return this.filelist;
    }
    public void changeParent(Commit commit) {
        this.firstParent = commit;
    }
}
