#!/usr/bin/env python3

from os import getcwd as cwd
from os.path import dirname, realpath
from os.path import join as path
from re import search as match
from subprocess import run


__location__ = realpath(path(cwd(), dirname(__file__)))
__java_dir__ = path(__location__, "src/main/java")
__base_dir__ = path(__java_dir__, "ch/usi/si/seart/treesitter")

# https://www.debuggex.com/r/6FsTee7fWKlzfqVb
pattern = r"\s([0-9a-fA-F]+)\s[^\s]+\s\(([^)]+)\)"


if __name__ == "__main__":
    path = f"{__base_dir__}/version/TreeSitter.java"
    args = ["git", "submodule", "status", f"{__location__}/tree-sitter"]
    status = run(args, capture_output=True, text=True)
    sha, tag = match(pattern, status.stdout).groups()
    content = f"""package ch.usi.si.seart.treesitter.version;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains version information pertaining to the {{@code tree-sitter}} API.
 *
 * @author Ozren Dabić
 * @since 1.11.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TreeSitter {{

    public static final String SHA = \"{sha}\";

    public static final String TAG = \"{tag}\";
}}
"""
    with open(path, "w") as f:
        f.write(content)
