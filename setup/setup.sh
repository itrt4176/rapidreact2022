#! /bin/sh
git branch -t main origin/main

git flow init -d

git flow config set master main
git flow config set release "comp/"
git flow config set hotfix "at-comp/"