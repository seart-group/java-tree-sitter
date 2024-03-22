#!/usr/bin/env bash

# TODO: This script has not been tested yet.
#       If you run into an issue on the first run,
#       now you know why.

set -e

SUBMODULE=$1

if [ -z "$SUBMODULE" ]; then
  echo "Usage: $(basename "$0") <submodule>"
  exit 1
fi

git submodule deinit --force "$SUBMODULE"
git rm --cached "$SUBMODULE"
git config -f .gitmodules --remove-section "submodule.$SUBMODULE"
git add .gitmodules
rm -rf ".git/modules/$SUBMODULE" "$SUBMODULE"
git commit --message "Remove \`$SUBMODULE\` submodule"
