package frc.irontigers.robot.utils;

public class Version {
    private String git_sha;
    private String git_date;
    private String git_branch;
    private String build_date;
    private int dirty;

    public Version() {}

    public Version(String git_sha, String git_date, String git_branch, String build_date, int dirty) {
        this.git_sha = git_sha;
        this.git_date = git_date;
        this.git_branch = git_branch;
        this.build_date = build_date;
        this.dirty = dirty;
    }

    /**
     * @return the git_sha
     */
    public String getGitSHA() {
        return git_sha;
    }

    /**
     * @return the git_date
     */
    public String getGitDate() {
        return git_date;
    }

    /**
     * @return the git_branch
     */
    public String getGitBranch() {
        return git_branch;
    }

    /**
     * @return the build_date
     */
    public String getBuildDate() {
        return build_date;
    }

    /**
     * @return the dirty
     */
    public int getDirty() {
        return dirty;
    }
    
    
}
