package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.TreeSitterException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Utility for loading the native system library.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@UtilityClass
public class LibraryLoader {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static final class SystemResource {

        URL url;
        String name;

        private SystemResource(String name) {
            this(LibraryLoader.class.getClassLoader().getResource(name), name);
        }
    }

    /**
     * Load the native library.
     * Call this once prior to using any of the APIs.
     */
    public void load() {
        String name = "libjava-tree-sitter";
        String extension = getExtension();
        String filename = name + "." + extension;
        SystemResource systemResource = new SystemResource(filename);
        String libPath = getLibPath(systemResource);
        System.load(libPath);
    }

    private String getLibPath(SystemResource systemResource) {
        String protocol = systemResource.url.getProtocol();
        switch (protocol) {
            case "file":
                return systemResource.url.getPath();
            case "jar":
                File tmpdir = FileUtils.getTempDirectory();
                File tmpfile = new File(tmpdir, systemResource.name);
                tmpfile.deleteOnExit();
                try (
                    InputStream input = systemResource.url.openStream();
                    OutputStream output = new FileOutputStream(tmpfile, false)
                ) {
                    IOUtils.copy(input, output);
                    return tmpfile.getPath();
                } catch (IOException cause) {
                    throw new TreeSitterException(cause);
                }
            default:
                Exception cause = new UnsupportedOperationException("Unsupported protocol: " + protocol);
                throw new TreeSitterException(cause);
        }
    }

    private String getExtension() {
        String platform = System.getProperty("os.name").toLowerCase();
        if (platform.contains("nix") || platform.contains("nux") || platform.contains("aix")) {
            return "so";
        } else if (platform.contains("mac") || platform.contains("darwin")) {
            return "dylib";
        } else {
            throw new TreeSitterException(
                    "The tree-sitter library was not compiled for this platform: " + platform
            );
        }
    }
}
