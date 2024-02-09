#!/usr/bin/env python3

from argparse import ArgumentParser
from os import getcwd as cwd
from os.path import dirname, realpath
from os.path import join as path
from re import search as match
from subprocess import run

# https://www.debuggex.com/r/6FsTee7fWKlzfqVb
pattern = r"\s([0-9a-fA-F]+)\s[^\s]+\s\(([^)]+)\)"
__location__ = realpath(path(cwd(), dirname(__file__)))


if __name__ == "__main__":
    parser = ArgumentParser(description="Generate tree-sitter API version class.")
    parser.add_argument(
        "-o",
        "--output",
        default=path(__location__, "TreeSitter.java"),
        help="Output file path.",
    )
    args = parser.parse_args()
    output = args.output
    submodule = path(__location__, "tree-sitter")
    cmd = ["git", "submodule", "status", submodule]
    status = run(cmd, capture_output=True, text=True)
    sha, tag = match(pattern, status.stdout).groups()
    content = f"""/*
 * MIT License
 *
 * Copyright (c) 2022-present SEART Research Group and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.usi.si.seart.treesitter.version;

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

    public static final String SHA = \"{sha}\";

    public static final String TAG = \"{tag}\";

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
    with open(output, "w") as file:
        file.write(content)
