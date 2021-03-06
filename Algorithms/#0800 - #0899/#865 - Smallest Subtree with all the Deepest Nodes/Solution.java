import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Given the root of a binary tree, the depth of each node is <b>the shortest distance to the root</b>.
 * <p>
 * Return <i>the smallest subtree</i> such that it contains <b>all the deepest nodes</b> in the original tree.
 * <p>
 * A node is called <b>the deepest</b> if it has the largest depth possible among any node in the entire tree.
 * <p>
 * The <b>subtree</b> of a node is tree consisting of that node, plus the set of all descendants of that node.
 * <p>
 * <b>Note:</b> This question is the same as <a href="https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/">1123. Lowest Common
 * Ancestor of Deepest Leaves</a>.
 * <p>
 * <b>Example 1:</b>
 * <p>
 * <img src="sketch1.png" />
 * <pre>
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4]
 * Output: [2,7,4]
 * Explanation: We return the node with value 2, colored in yellow in the diagram.
 * The nodes coloured in blue are the deepest nodes of the tree.
 * Notice that nodes 5, 3 and 2 contain the deepest nodes in the tree but node 2 is the smallest subtree among them, so we return it.
 * </pre>
 * <b>Example 2:</b>
 * <pre>
 * Input: root = [1]
 * Output: [1]
 * Explanation: The root is the deepest node in the tree.
 * </pre>
 * <b>Example 3:</b>
 * <pre>
 * Input: root = [0,1,3,null,2]
 * Output: [2]
 * Explanation: The deepest node in the tree is 2, the valid subtrees are the subtrees of nodes 2, 1 and 0 but the subtree of node 2 is the smallest.
 * </pre>
 * <ul>
 * <b>Constraints:</b>
 * <li>The number of nodes in the tree will be in the range <tt>[1, 500]</tt>.</li>
 * <li><tt>0 <= Node.val <= 500</tt></li>
 * <li>The values of the nodes in the tree are <b>unique</b>.</li>
 * </ul>
 *
 * @author Oleg Cherednik
 * @since 13.12.2020
 */
public class Solution {

    public static void main(String... args) {
        System.out.println(subtreeWithAllDeepest(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4));   // [2, 7, 4]
        System.out.println(subtreeWithAllDeepest(1));                                       // [1]
        System.out.println(subtreeWithAllDeepest(0, 1, 3, null, 2));                        // [2]
    }

    private static List<Integer> subtreeWithAllDeepest(Integer... vals) {
        TreeNode root = build(vals);
        root = subtreeWithAllDeepest(root);

        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.remove();
                res.add(node.val);

                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
            }
        }

        return res;
    }

    public static TreeNode subtreeWithAllDeepest(TreeNode root) {
        Queue<TreeNodeParent> queue = findDeepestNodes(root);

        while (queue.size() > 1) {
            TreeNodeParent parent = null;
            boolean same = true;

            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNodeParent node = queue.remove();
                queue.add(node.parent);
                parent = i == 0 ? node.parent : parent;
                same &= parent == node.parent;
            }

            if (same)
                break;
        }

        return queue.remove().node;
    }

    private static Queue<TreeNodeParent> findDeepestNodes(TreeNode root) {
        Queue<TreeNodeParent> queue = new LinkedList<>();
        queue.add(new TreeNodeParent(root, null));

        Queue<TreeNodeParent> nodes = new LinkedList<>();

        while (!queue.isEmpty()) {
            nodes = new LinkedList<>();

            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNodeParent node = queue.remove();
                nodes.add(node);

                if (node.node.left != null)
                    queue.add(new TreeNodeParent(node.node.left, node));
                if (node.node.right != null)
                    queue.add(new TreeNodeParent(node.node.right, node));
            }
        }

        return nodes;
    }

    private static TreeNode build(Integer... vals) {
        TreeNode root = null;
        Queue<TreeNode> nodes = new LinkedList<>();

        for (int i = 0; i < vals.length; ) {
            if (i == 0)
                nodes.add(root = new TreeNode(vals[i++]));
            else {
                if (nodes.isEmpty())
                    break;

                Integer left = vals[i++];
                Integer right = i < vals.length ? vals[i++] : null;
                TreeNode node = nodes.remove();

                if (left != null)
                    nodes.add(node.left = new TreeNode(left));
                if (right != null)
                    nodes.add(node.right = new TreeNode(right));
            }
        }

        return root;
    }

    private static class TreeNodeParent {

        private final TreeNode node;
        private final TreeNodeParent parent;

        public TreeNodeParent(TreeNode node, TreeNodeParent parent) {
            this.node = node;
            this.parent = parent;
        }
    }

    public static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
