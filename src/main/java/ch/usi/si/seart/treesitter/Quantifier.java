package ch.usi.si.seart.treesitter;

/**
 * Represents the {@link Capture} quantifier in a {@link Pattern},
 * i.e. the number of nodes that a capture should contain. Within a
 * query, a capture can have different quantifiers for each pattern.
 *
 * @since 1.12.0
 * @author Ozren Dabić
 */
public enum Quantifier {

    /**
     * The capture will not match any nodes,
     * as said capture is not present in a
     * specific pattern.
     */
    ZERO,
    /**
     * The capture will match at most one node.
     * Example:
     * <pre>{@code
     * ((_)? @capture)
     * }</pre>
     */
    ZERO_OR_ONE,
    /**
     * The capture will match any number of nodes.
     * Example:
     * <pre>{@code
     * ((_)* @capture)
     * }</pre>
     */
    ZERO_OR_MORE,
    /**
     * The capture will match exactly one node.
     * Example:
     * <pre>{@code
     * ((_) @capture)
     * }</pre>
     */
    ONE,
    /**
     * The capture will match at least one node.
     * Example:
     * <pre>{@code
     * ((_)+ @capture)
     * }</pre>
     */
    ONE_OR_MORE
}
