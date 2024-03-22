#!/usr/bin/env python

from argparse import ArgumentParser
from git import Repo as GitRepository, Tag as GitTag
from glob import glob as find
from os import PathLike
from os import getcwd as cwd
from os.path import basename, dirname, realpath
from os.path import join as path
from typing import AnyStr, TextIO

__location__ = realpath(path(cwd(), dirname(__file__)))


class PropertyWriter:

    def __init__(self, submodule: AnyStr | PathLike):
        self.submodule = submodule
        self.language = basename(submodule)[12:]

    @staticmethod
    def is_semver(tag: GitTag) -> bool:
        first = next(iter(tag.name), "")
        return first.isdigit() or first == "v"

    def write(self, outfile: TextIO):
        with GitRepository(self.submodule) as repository:
            commit = repository.head.commit
            tags = [tag.name for tag in repository.tags if tag.commit == commit and self.is_semver(tag)]
            outfile.write(
                f"""\
url.{self.language}={next(repository.remotes.origin.urls)}
sha.{self.language}={commit.hexsha}
tag.{self.language}={next(iter(tags), "")}
""")


if __name__ == "__main__":
    parser = ArgumentParser(description="Generate tree-sitter properties file.")
    parser.add_argument(
        "-o",
        "--output",
        default=path(__location__, "language.properties"),
        help="Output file path.",
    )
    args = parser.parse_args()
    with open(args.output, "w") as properties_file:
        for directory in sorted(find(f"{__location__}/tree-sitter-*")):
            PropertyWriter(directory).write(properties_file)
