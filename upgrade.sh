#!/usr/bin/env bash

function get_default_branch() {
  TARGET="$1"
  HEAD=refs/remotes/origin/HEAD
  RESULT=$(git -C "$TARGET" symbolic-ref "$HEAD" --short)
  echo "$RESULT"
}

set -e

SUBMODULE=$1

if [ -z "$SUBMODULE" ]; then
  echo "Usage: $(basename "$0") <submodule> [checkout_target]"
  exit 1
fi

CHECKOUT_TARGET="${2:-$(get_default_branch "$SUBMODULE")}"

git -C "$SUBMODULE" fetch --all
git -C "$SUBMODULE" checkout "$CHECKOUT_TARGET"
git add "$SUBMODULE"
