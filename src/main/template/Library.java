package ch.usi.si.seart.treesitter.version;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility used for obtaining the current version of the {@code ${project.artifactId}} codebase.
 * It does this by fetching the "Implementation-Version" manifest attribute from the JAR file.
 * If the {@link ClassLoader} does not expose the manifest metadata,
 * it will fall back to using a hard-coded value injected from the POM.
 *
 * @author Ozren Dabić
 * @since 1.11.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Library {

    private static final String VERSION = "${project.version}";

    /**
     * Get the current version of {@code ${project.artifactId}}.
     *
     * @return the semantic version string
     */
    public static String getVersion() {
        Package pkg = Library.class.getPackage();
        return "v" + (pkg != null ? pkg.getImplementationVersion() : VERSION);
    }
}
