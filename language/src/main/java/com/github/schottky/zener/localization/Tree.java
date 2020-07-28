package com.github.schottky.zener.localization;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

// Internal class for Language.class
// A tree with Strings as leaves and nodes
class Tree<V,E> {

    private final Node<V,E> root = Node.newBranchingNode();
    private int leafCounts = 0;

    public int size() { return leafCounts; }

    private SearchResult<E> traceLeafStartingFrom(@NotNull Node<V,E> node, @NotNull Queue<V> edges) {
        if (node.isLeaf()) {
            return edges.isEmpty() ? SearchResult.of(node.value) : SearchResult.notFound();
        } else {
            V edge = edges.poll();
            Node<V,E> nextNode = node.nextNodeForEdge(edge);
            return nextNode == null ? SearchResult.notFound() : traceLeafStartingFrom(nextNode, edges);
        }
    }

    public SearchResult<E> traceLeafStartingFromRoot(@NotNull Queue<V> searchQuarry) {
        return traceLeafStartingFrom(root, searchQuarry);
    }

    public void addLeafFor(@NotNull V[] branches, E leaf) {
        Preconditions.checkArgument(branches.length >= 1, "Branches must not be empty");
        Node<V,E> currentNode = root;
        for (int arrayIndex = 0; arrayIndex < branches.length - 1; arrayIndex++) {
            V currentBranch = branches[arrayIndex];
            Node<V,E> adjacentNode = currentNode.nextNodeForEdge(currentBranch);
            if (adjacentNode != null) {
                if (adjacentNode.isLeaf()) {
                    throw new RuntimeException("current branch " + Joiner.on(".").join(branches) +
                            " already has a mapping");
                }
                currentNode = currentNode.nextNodeForEdge(currentBranch);
            } else {
                Node<V,E> nextNode = Node.newBranchingNode();
                currentNode.append(currentBranch, nextNode);
                currentNode = nextNode;
            }
        }
        currentNode.append(branches[branches.length - 1], Node.newLeafNode(leaf));
        leafCounts += 1;
    }

    @Override
    public String toString() {
        return "Tree{" +
                traverse(10) +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Tree<?, ?> tree = (Tree<?, ?>) object;
        return root.equals(tree.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    private void traverse(
            final Set<Node<V,E>> visitedNodes,
            final @NotNull Node<V,E> current,
            final List<V> path,
            final Set<Path<V,E>> allPaths,
            final int limit)
    {
        if (current.isBranching()) {
            current.adjacentNodes.forEach((branch, node) -> {
                if (!visitedNodes.contains(node)) {
                    path.add(branch);
                    traverse(visitedNodes, node, path, allPaths, limit);
                }
            });
            if (current == root) return;
        } else {
            if (limit == 0) return;
            visitedNodes.add(current);
            allPaths.add(new Path<>(path, current.value));
            path.clear();
            traverse(visitedNodes, root, path, allPaths, limit - 1);
        }

        visitedNodes.add(current);
        path.clear();
        traverse(visitedNodes, root, path, allPaths, limit);
    }

    public static class Path<V,E> {
        private final List<V> edges;
        private final E leafValue;

        public Path(Iterable<V> edges, E leafValue) {
            this.edges = ImmutableList.copyOf(edges);
            this.leafValue = leafValue;
        }

        @Override
        public String toString() {
            return "Path{" +
                    "edges=" + edges +
                    ", leafValue=" + leafValue +
                    '}';
        }
    }

    public Set<Path<V,E>> traverse(int limit) {
        if (size() == 0) return Collections.emptySet();
        Set<Path<V,E>> protocol = new HashSet<>();
        traverse(new HashSet<>(), root, new ArrayList<>(), protocol, limit);
        return protocol;
    }

    public <X> Map<X,E> traverseAndFlatten(Function<List<V>,X> reducer, int limit) {
        Set<Path<V,E>> paths = traverse(limit);
        Map<X,E> map = new HashMap<>();
        for (Path<V,E> path: paths) {
            map.put(reducer.apply(path.edges), path.leafValue);
        }
        return map;
    }

    static class Node<V,E> {

        private final Map<V, Node<V,E>> adjacentNodes = new HashMap<>();
        private final E value;

        @NotNull
        @Contract(" -> new")
        public static <V,E> Node<V,E> newBranchingNode() {
            return new Node<>(null);
        }

        @NotNull
        @Contract("_ -> new")
        public static <V,E> Node<V,E> newLeafNode(E withValue) {
            return new Node<>(withValue);
        }

        private Node(E value) {
            this.value = value;
        }

        private void append(V edge, Node<V,E> node) {
            adjacentNodes.put(edge, node);
        }

        private Node<V,E> nextNodeForEdge(V edge) {
            return adjacentNodes.get(edge);
        }

        public boolean isLeaf() {
            return value != null;
        }

        public boolean isBranching() {
            return adjacentNodes.size() != 0;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder()
                    .append("BranchingNode{")
                    .append("adjacentNodes=");
            adjacentNodes.forEach((name, node) -> builder
                    .append(name)
                    .append("=")
                    .append(node.isBranching() ? "BranchingNode{...}" : node.value));
            builder.append('}');
            return builder.toString();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) object;
            return adjacentNodes.equals(node.adjacentNodes) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

}
