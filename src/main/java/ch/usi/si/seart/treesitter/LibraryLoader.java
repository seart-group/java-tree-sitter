package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.TreeSitterException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

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

    private static final String LIBRARY_FILE_NAME = "libjava-tree-sitter";

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
        String filename = String.format("%s.%s", LIBRARY_FILE_NAME, getExtension());
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
        if (SystemUtils.IS_OS_LINUX) return "so";
        else if (SystemUtils.IS_OS_MAC) return "dylib";
        else throw new TreeSitterException(
                "The tree-sitter library was not compiled for this platform: " + SystemUtils.OS_NAME.toLowerCase()
            );
    }
}
