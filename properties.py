#!/usr/bin/env python3

from argparse import ArgumentParser
from glob import glob as find
from os import getcwd as cwd
from os.path import basename, dirname, realpath
from os.path import join as path
from subprocess import run

__location__ = realpath(path(cwd(), dirname(__file__)))


class PropertyWriter:

    commands = {
        "url": ["config", "--get", "remote.origin.url"],
        "sha": ["rev-parse", "HEAD"],
        "tag": ["describe", "--tags", "--abbrev=0"],
    }

    def __init__(self, workdir):
        self.workdir = workdir
        self.language = basename(workdir)[12:]

    def write(self, outfile):
        for key, cmd in self.commands.items():
            result = run(["git", *cmd], cwd=self.workdir, capture_output=True, text=True)
            value = "" if result.returncode else result.stdout.strip()
            outfile.write(f"tree-sitter.language.{self.language}.{key}={value}\n")


if __name__ == "__main__":
    parser = ArgumentParser(description="Generate tree-sitter properties file.")
    parser.add_argument(
        "-o",
        "--output",
        default=f"{__location__}/language.properties",
        help="Output file path.",
    )
    args = parser.parse_args()
    path = args.output
    directories = find(f"{__location__}/tree-sitter-*")
    with open(path, "w") as f:
        for directory in sorted(directories):
            PropertyWriter(directory).write(f)
