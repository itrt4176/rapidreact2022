# Git Flow FRC
## About
Git Flow FRC is a git workflow based on [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/) and adapted for FIRST Robotics Competition projects.

## Setup
If you do not have git-flow installed, follow the instructions below for your Operating System. Otherwise, skip to [After Cloning This Repository](#after-cloning-this-repository).

### Windows
git-flow is included in recent releases of Git for Windows and does not need to be separately installed.

### macOS
Homebrew
```
$ brew install git-flow-avh
```

Macports
```
$ port install git-flow-avh
```

### Debian/Ubuntu
```
$ apt install git-flow
```

### After Cloning This Repository
#### Windows
```
C:\repo\> setup\setup.bat
```

#### macOS/Linux
```
$ ./setup/setup.sh
```

## Features
### Start a new feature
Development of new features starting from the develop branch.

Start developing a new feature with
```
git flow feature start NEW_FEATURE
```
where `NEW_FEATURE` is the name of the feature being developed. This will create a new branch called `feature/NEW_FEATURE` based on develop and switches to it.

### Push to GitHub
Before you leave for the day, be sure to run
```
git flow feature publish NEW_FEATURE
```
to make your code available to the rest of the team.

### Pull from GitHub
To get the latest work on a feature, run
```
git flow feature pull NEW_FEATURE
```

### Finish a feature
When development on a feature is completed, run
```
git flow feature finish NEW_FEATURE
```
to merge your changes back into `develop` and delete the feature branch. Then run `git push` to push the changes to GitHub.

## Pre-Competition
When the software team lead decides no new features will be added prior to a competition (a feature freeze), they will create a new competition branch by running
```
git flow release start COMP_NAME
```
where `COMP_NAME` is the name of the upcoming competition. This will create a new branch called `comp/COMP_NAME` and switches to it. All testing and bugfixes are done on this branch. This branch is pushed and pulled the same way as a feature branch.

After the last pre-competition bugfix has been completed, the software team lead will run
```
git flow release finish COMP_NAME
```
to merge changes into `main` and `develop`. Be sure to run `git push --follow-tags` to push the changes to GitHub.

## At a Competition
Prior to, or at the competion, the software team lead will run
```
git flow hotfix start COMP_NAME-fixes
```
where `COMP_NAME` is the same name used pre-competition. This will create a new branch called `at-comp/COMP_NAME-fixes` and switches to it. All at competition development is done on this branch. It does not need to be pushed to GitHub.

## Post-Competition
At the end of a competition, the software team lead will run
```
git flow hotfix finish COMP_NAME-fixes
```
to merge the changes into `develop` and `main`. This will also delete the `at-comp/COMP_NAME-fixes` branch. Then run `git push --follow-tags` when there is internet access again.

## Credits
This guide was based in part on the [git-flow cheatsheet](https://danielkummer.github.io/git-flow-cheatsheet/) created by Daniel Kummer.