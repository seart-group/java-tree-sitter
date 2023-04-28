package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.TreeSitterException;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@UtilityClass
public class LibraryLoader {

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static final class SystemResource {

        URL url;
        String name;

        private SystemResource(String name) {
            this.url = ClassLoader.getSystemResource(name);
            this.name = name;
        }
    }

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
                try {
                    @Cleanup InputStream input = systemResource.url.openStream();
                    String tmpdir = System.getProperty("java.io.tmpdir");
                    File temporary = new File(tmpdir, systemResource.name);
                    temporary.deleteOnExit();
                    @Cleanup OutputStream output = new FileOutputStream(temporary, false);
                    input.transferTo(output);
                    return temporary.getPath();
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
        if (platform.contains("nix") || platform.contains("nux") || platform.contains("aix"))
            return "so";
        else if (platform.contains("mac") || platform.contains("darwin"))
            return "dylib";
        else
            throw new TreeSitterException("The tree-sitter library was not compiled for this platform: " + platform);
    }
}
