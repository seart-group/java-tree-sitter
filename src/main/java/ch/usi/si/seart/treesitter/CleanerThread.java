package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Objects;

/**
 * Java 8 implementation of {@link java.lang.ref.Cleaner Cleaner}.
 * Implementation is heavily inspired by said Java 9 feature,
 * as well as the {@link sun.misc.Cleaner Cleaner} from <em>sun</em>.
 * One minor difference with respect to these implementations is that
 * {@code CleanerThread} can be killed and respawned as needed.
 * Intended only for internal usage.
 *
 * @author Juan Lopes
 * @author Ozren Dabić
 * @see <a href="https://codereview.stackexchange.com/q/278276">Code Review Tread</a>
 * @since 1.8
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class CleanerThread {

    interface Cleaner {
        void clean();
    }

    interface Action {
        void run(boolean leak);
    }

    ReferenceQueue<Object> queue = new ReferenceQueue<>();

    long keepAlive;

    @NonFinal
    boolean threadRunning = false;

    @NonFinal
    PhantomNode first;

    CleanerThread(long keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Cleaner register(Object obj, Action action) {
        PhantomNode node = new PhantomNode(obj, action);
        add(node);
        return node;
    }

    private synchronized boolean isEmpty() {
        if (first == null) {
            threadRunning = false;
            return true;
        }
        return false;
    }

    private synchronized void add(PhantomNode node) {
        if (first != null) {
            node.nextNode = first;
            first.prevNode = node;
        }
        first = node;

        if (!threadRunning) {
            threadRunning = true;
            startThread();
        }
    }

    private void startThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    PhantomNode ref = (PhantomNode) queue.remove(keepAlive);
                    if (ref != null) {
                        ref.cleanInternal(true);
                    } else if (isEmpty()) {
                        break;
                    }
                } catch (Throwable ignored) {
                    // ignore exceptions from the cleanup action
                    // (including interruption of cleanup thread)
                }
            }
        }, "tree-sitter-cleaner");
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized boolean remove(PhantomNode node) {
        // If already removed, do nothing
        if (node.nextNode == node)
            return false;

        // Update list
        if (first == node)
            first = node.nextNode;
        if (node.nextNode != null)
            node.nextNode.prevNode = node.prevNode;
        if (node.prevNode != null)
            node.prevNode.nextNode = node.nextNode;

        // Indicate removal by pointing the cleaner to itself
        node.nextNode = node;
        node.prevNode = node;

        return true;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class PhantomNode extends PhantomReference<Object> implements Cleaner {

        final Action action;

        PhantomNode prevNode;
        PhantomNode nextNode;

        PhantomNode(Object referent, Action action) {
            super(referent, queue);
            this.action = action;
            Objects.requireNonNull(referent, "Referent must not be null!");
        }

        @Override
        public void clean() {
            cleanInternal(false);
        }

        private void cleanInternal(boolean leak) {
            if (!remove(this))
                return;
            try {
                action.run(leak);
            } catch (Throwable e) {
                // Should not happen if cleaners are well-behaved
                e.printStackTrace();
            }
        }
    }
}
