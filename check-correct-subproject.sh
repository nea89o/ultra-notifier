#!/usr/bin/env bash
# git hook to make sure you check out the current core version before committing
# to install, place it into your git hook folder with the appropriate name:
#
# cp check-correct-subproject.sh .git/hooks/pre-commit
#
# OR (to check a bit earlier)
#
# cp check-correct-subproject.sh .git/hooks/prepare-commit-msg
#

currentMainProject="$(git show :versions/mainProject|tr -d '\n')"
currentMainVersion="$(git show :mainProject|tr -d '\n')"
if [ "x$currentMainVersion" != "x$currentMainProject" ]; then
  echo "Currently checked out version is $currentMainProject, but $currentMainVersion should be committed."
  echo "Run ./gradlew :${currentMainVersion}:setCoreVersion to fix"
  echo
  exit 1
else
  echo "Correct core version $currentMainVersion checked out"
fi

