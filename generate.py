#!/usr/bin/env python3

from argparse import ArgumentParser
from os import getcwd as cwd
from os.path import dirname, realpath
from os.path import join as path
from re import search as match
from subprocess import run


__location__ = realpath(path(cwd(), dirname(__file__)))

# https://www.debuggex.com/r/6FsTee7fWKlzfqVb
pattern = r"\s([0-9a-fA-F]+)\s[^\s]+\s\(([^)]+)\)"


if __name__ == "__main__":
    parser = ArgumentParser(description="Generate tree-sitter API version class.")
    parser.add_argument(
        "-o",
        "--output",
        default=f"{__location__}/TreeSitter.java",
        help="Output file path.",
    )
    args = parser.parse_args()
    path = args.output
    cmd = ["git", "submodule", "status", f"{__location__}/tree-sitter"]
    status = run(cmd, capture_output=True, text=True)
    sha, tag = match(pattern, status.stdout).groups()
    content = f"""package ch.usi.si.seart.treesitter.version;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility used for obtaining the current version of the {{@code tree-sitter}} API.
 *
 * @author Ozren Dabić
 * @since 1.11.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TreeSitter {{

    private static final String SHA = \"{sha}\";

    private static final String TAG = \"{tag}\";

    /**
     * Get the current version of {{@code tree-sitter}}.
     *
     * @return the semantic version string, along with a commit SHA
     */
    public static String getVersion() {{
        return TAG + \" (\" + SHA + \")\";
    }}
}}
"""
    with open(path, "w") as f:
        f.write(content)
