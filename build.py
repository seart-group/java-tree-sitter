#!/usr/bin/env python3

from argparse import ArgumentParser
from ctypes.util import find_library as find_cpp_library
from distutils.ccompiler import new_compiler as new_c_compiler
from distutils.log import set_verbosity as set_log_verbosity
from glob import glob as find
from os import environ
from os import system as cmd
from os.path import basename, dirname, exists, getmtime, realpath
from os.path import join as path
from os.path import split as split_path
from platform import system as os_name
from tempfile import TemporaryDirectory


# adapted from https://github.com/tree-sitter/py-tree-sitter
def build(repositories, output_path="libjava-tree-sitter", system=None, arch=None, verbose=False):
    here = dirname(realpath(__file__))

    if not repositories:
        repositories = sorted([basename(repository) for repository in find(path(here, "tree-sitter-*"))])

    if not repositories:
        raise ValueError("Library can not be compiled, no grammars were included!")

    if system is None:
        system = os_name()

    if arch and system != "Darwin":
        arch = "64" if "64" in arch else "32"
    if arch and system == "Darwin":
        arch = "arm64" if "aarch64" in arch else arch

    output_extension = 'dylib' if system == 'Darwin' else 'so'
    output_path = f"{output_path}.{output_extension}"
    env = ""
    if arch:
        env += (
            f"CFLAGS='-arch {arch} -mmacosx-version-min=11.0' LDFLAGS='-arch {arch}'"
            if system == "Darwin"
            else f"CFLAGS='-m{arch}' LDFLAGS='-m{arch}'"
        )

    tree_sitter = path(here, 'tree-sitter')
    redirect = '> /dev/null' if not verbose else ''
    cmd(f"make -C \"{tree_sitter}\" clean {redirect}")
    cmd(f"{env} make -C \"{tree_sitter}\" {redirect}")

    source_paths = find(path(here, "lib", "*.cc"))

    compiler = new_c_compiler()
    for repository in repositories:
        repository_name = split_path(repository.rstrip('/'))[1]
        repository_language = repository_name.split('tree-sitter-')[-1]
        repository_macro = f"TS_LANGUAGE_{repository_language.replace('-', '_').upper()}"
        compiler.define_macro(repository_macro, "1")
        match repository_name:
            case "tree-sitter-dtd" |\
                 "tree-sitter-markdown" |\
                 "tree-sitter-xml":
                src_path = path(repository, repository_name, "src")
            case "tree-sitter-ocaml" |\
                 "tree-sitter-tsx" |\
                 "tree-sitter-typescript":
                src_path = path(repository, repository_language, "src")
            case _:
                src_path = path(repository, "src")
        source_paths.append(path(src_path, "parser.c"))
        scanner_c = path(src_path, "scanner.c")
        scanner_cc = path(src_path, "scanner.cc")
        if exists(scanner_cc):
            source_paths.append(scanner_cc)
        elif exists(scanner_c):
            source_paths.append(scanner_c)

    source_mtimes = [getmtime(__file__)] + [getmtime(source_path) for source_path in source_paths]
    if find_cpp_library("stdc++"):
        compiler.add_library("stdc++")
    elif find_cpp_library("c++"):
        compiler.add_library("c++")

    output_mtime = getmtime(output_path) if exists(output_path) else 0
    if max(source_mtimes) <= output_mtime:
        return False

    with TemporaryDirectory(suffix="tree_sitter_language") as out_dir:
        object_paths = []
        for source_path in source_paths:
            flags = ["-O3"]

            if system == "Linux":
                flags.append("-fPIC")

            if source_path.endswith(".c"):
                flags.append("-std=c99")

            if arch:
                flags += ["-arch", arch] if system == "Darwin" else [f"-m{arch}"]

            include_dirs = [
                dirname(source_path),
                path("include"),
                path(environ["JAVA_HOME"], "include"),
                path(here, "tree-sitter", "lib", "include"),
            ]

            object_paths.append(
                compiler.compile(
                    [source_path],
                    output_dir=out_dir,
                    include_dirs=include_dirs,
                    extra_preargs=flags,
                )[0]
            )

        extra_preargs = []
        if system == "Darwin":
            extra_preargs.append("-dynamiclib")

        if arch:
            extra_preargs += ["-arch", arch] if system == "Darwin" else [f"-m{arch}"]

        compiler.link_shared_object(
            object_paths,
            output_path,
            extra_preargs=extra_preargs,
            extra_postargs=[path(here, "tree-sitter", "libtree-sitter.a")],
            library_dirs=[path(here, "tree-sitter")],
        )

    return True


if __name__ == "__main__":
    parser = ArgumentParser(description="Build a tree-sitter library.")
    parser.add_argument(
        "-s",
        "--system",
        help="Operating system to build for (Linux, Darwin, Windows)."
    )
    parser.add_argument(
        "-a",
        "--arch",
        help="Architecture to build for (x86, x86_64, arm64, aarch64).",
    )
    parser.add_argument(
        "-o",
        "--output",
        default="libjava-tree-sitter",
        help="Output file name.",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Print verbose output.",
    )
    parser.add_argument(
        "repositories",
        nargs="*",
        help="""
        tree-sitter repositories to include in build.
        If none are specified, all directories that
        match ./tree-sitter-* will be included.
        """,
    )

    args = parser.parse_args()
    set_log_verbosity(int(args.verbose))
    build(args.repositories, args.output, args.system, args.arch, args.verbose)
