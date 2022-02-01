package frc.irontigers.robot.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {
    private String git_sha;
    private String git_date;
    private String git_branch;
    private String build_date;
    private int dirty;

    public Version() {
        git_sha = git_date = git_branch = build_date = "ERROR!";
        dirty = -1;
    }

    public Version(String git_sha, String git_date, String git_branch, String build_date, int dirty) {
        this.git_sha = git_sha;
        this.git_date = git_date;
        this.git_branch = git_branch;
        this.build_date = build_date;
        this.dirty = dirty;
    }

    /**
     * @param git_sha the git_sha to set
     */
    private void setGit_sha(String git_sha) {
        this.git_sha = git_sha;
    }

    /**
     * @param git_date the git_date to set
     */
    private void setGit_date(String git_date) {
        this.git_date = git_date;
    }

    /**
     * @param git_branch the git_branch to set
     */
    private void setGit_branch(String git_branch) {
        this.git_branch = git_branch;
    }

    /**
     * @param build_date the build_date to set
     */
    private void setBuild_date(String build_date) {
        this.build_date = build_date;
    }

    // /**
    //  * @param dirty the dirty to set
    //  */
    // private void setDirty(int dirty) {
    //     this.dirty = dirty;
    // }

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
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        String newline = System.lineSeparator();

        str.append(newline)
                .append("============================================")
                .append(newline)
                .append("Rapid React 2022")
                .append(newline)
                .append("  Branch: ")
                .append(git_branch);

        if (dirty == 1) {
            str.append("*")
                    .append(newline)
                    .append("  Build Date: ")
                    .append(build_date);
        } else {
            str.append(newline)
                    .append("  Build Date: ")
                    .append(build_date)
                    .append(newline)
                    .append("  Commit: ")
                    .append(git_sha.substring(0, 6))
                    .append(newline)
                    .append("  Commit Date: ")
                    .append(git_date);
        }

        str.append(newline).append("============================================").append(newline);
        
        return str.toString();
    }
    
}
