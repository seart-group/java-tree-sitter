package ai.serenade.treesitter;

import java.io.FileNotFoundException;
import java.net.URL;

public class LibraryLoader {

    private LibraryLoader() {
    }

    public static void load() {
        String format = "libjava-tree-sitter.%s";
        String filename;
        switch (currentOS()) {
            case MACOS:
                filename = String.format(format, "dylib");
                break;
            case LINUX:
                filename = String.format(format, "so");
                break;
            default:
                throw new TreeSitterException("tree-sitter library was not compiled for this platform!");
        }
        URL libURL = ClassLoader.getSystemResource(filename);
        if (libURL != null) {
            String libPath = libURL.getPath();
            System.load(libPath);
        } else {
            Exception cause = new FileNotFoundException("Resource could not be found!");
            throw new TreeSitterException(cause);
        }
    }

    private static OS currentOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"))
            return OS.LINUX;
        else if (osName.contains("mac") || osName.contains("darwin"))
            return OS.MACOS;
        else
            return OS.UNSUPPORTED;
    }

    private enum OS {
        MACOS, LINUX, UNSUPPORTED
    }
}
