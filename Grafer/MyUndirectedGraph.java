import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T>
{
    private final HashMap<T, Node<T>> nodes = new HashMap<>();

    private static class Node<T>
    {
        private T data;
        private boolean isVisited = false;
        private final HashMap<Node<T>, Integer> adjacentNodes = new HashMap<>();

        private Node(T data)
        {
            this.data = data;
        }

        private T getData()
        {
            return data;
        }

        private void addAdjacentNode(Node<T> nodeToAdd, Integer weight)
        {
            adjacentNodes.put(nodeToAdd, weight);
        }

        private Node<T> returnFirstUnvisitedAdjacentNode()
        {
            for (Node<T> node : adjacentNodes.keySet())
            {
                if (!node.isVisited())
                {
                    return node;
                }
            }
            return null;
        }

        private HashMap<Node<T>, Integer> getAdjacentNodes()
        {
            return adjacentNodes;
        }

        private boolean hasAdjacentNode(Node<T> node)
        {
            return adjacentNodes.containsKey(node);
        }

        private boolean isVisited()
        {
            return isVisited;
        }

        private void setVisited(boolean visited)
        {
            isVisited = visited;
        }
    }

    @Override
    public int getNumberOfNodes()
    {
        return nodes.size();
    }

    @Override
    public int getNumberOfEdges()
    {
        int sum = 0;

        for (Node n : nodes.values())
        {
            sum += n.getAdjacentNodes().size();
        }
        // Divided by 2 because the connection goes both ways
        return sum / 2;
    }

    @Override
    public boolean add(T newNode)
    {
        if (nodes.containsKey(newNode))
        {
            return false;
        }
        nodes.put(newNode, new Node<>(newNode));
        return true;
    }

    @Override
    public boolean connect(T firstNode, T secondNode, int weight)
    {
        Node<T> n1 = nodes.get(firstNode);
        Node<T> n2 = nodes.get(secondNode);

        if (weight <= 0 || n1 == null || n2 == null)
        {
            return false;
        }
        n1.addAdjacentNode(n2, weight);
        n2.addAdjacentNode(n1, weight);
        return true;
    }

    @Override
    public boolean isConnected(T firstNode, T secondNode)
    {
        Node<T> n1 = nodes.get(firstNode);
        Node<T> n2 = nodes.get(secondNode);

        if (n1 == null || n2 == null)
        {
            return false;
        }
        //Given that this is an undirected graph we don't have to check the other way (from n2 to n1).
        return n1.hasAdjacentNode(n2);
    }

    @Override
    public int getCost(T firstNode, T secondNode)
    {
        Node<T> n1 = nodes.get(firstNode);
        Node<T> n2 = nodes.get(secondNode);

        if (!isConnected(firstNode, secondNode))
        {
            return -1;
        }
        return n1.getAdjacentNodes().get(n2);
    }

    private void resetVisitedStatusForAllNodes()
    {
        for (Node<T> node : nodes.values())
        {
            node.setVisited(false);
        }
    }

    @Override
    public List<T> depthFirstSearch(T startNode, T endNode)
    {
        Node<T> startingNode = nodes.get(startNode);
        Node<T> endingNode = nodes.get(endNode);
        List<T> listToReturn = new ArrayList<>();
        ArrayDeque<Node<T>> stack = new ArrayDeque<>();

        if (startingNode == null || endingNode == null)
        {
            return listToReturn;
        }
        stack.push(startingNode);
        startingNode.setVisited(true);

        while (!stack.isEmpty() && !(stack.peek().equals(endingNode)))
        {
            Node<T> nextNode = stack.peek().returnFirstUnvisitedAdjacentNode();
            if (nextNode != null)
            {
                nextNode.setVisited(true);
                stack.push(nextNode);
            } else
            {
                stack.pop();
            }
        }
        Iterator<Node<T>> iterator = stack.descendingIterator();
        while (iterator.hasNext())
        {
            Node<T> node = iterator.next();
            listToReturn.add(node.getData());
        }
        resetVisitedStatusForAllNodes();
        return listToReturn;
    }

    @Override
    public List<T> breadthFirstSearch(T startNode, T endNode)
    {
        Node<T> startingNode = nodes.get(startNode);
        Node<T> endingNode = nodes.get(endNode);
        List<T> listToReturn = new ArrayList<>();
        HashMap<Node<T>, Node<T>> pathTakenByNode = new HashMap<>();
        Queue<Node<T>> queue = new LinkedList<>();

        if (startingNode == null || endingNode == null)
        {
            return listToReturn;
        }
        queue.add(startingNode);
        startingNode.setVisited(true);

        while (!queue.isEmpty())
        {
            Node<T> node = queue.poll();

            if (node.equals(endingNode))
            {
                break;
            }

            for (Node<T> adjacentNode : node.getAdjacentNodes().keySet())
            {
                if (!adjacentNode.isVisited())
                {
                    adjacentNode.setVisited(true);
                    queue.add(adjacentNode);
                    pathTakenByNode.put(adjacentNode, node);
                }
            }
        }
        //If going from node A -> B -> C -> D -> F, start at F and follow the path backwards. Finally reverse the list so that the first element is the starting node (A).
        while (endingNode != null)
        {
            listToReturn.add(endingNode.getData());
            endingNode = pathTakenByNode.get(endingNode);
        }
        Collections.reverse(listToReturn);

        resetVisitedStatusForAllNodes();
        return listToReturn;
    }

    /**
     * Returns a minimal spanning tree representation of the original weighted, undirected graph, using Prim's algorithm.
     *
     * @return A minimal spanning tree of the original graph. If the original graph is empty the MST will also be empty.
     */
    @Override
    public UndirectedGraph<T> minimumSpanningTree()
    {
        MyUndirectedGraph<T> minimumSpanningTree = new MyUndirectedGraph<>();

        //Situations with 0 or 1 nodes are handled separately and treated as special cases.
        if (nodes.size() == 0)
        {
            return minimumSpanningTree;
        }
        if (nodes.size() == 1)
        {
            minimumSpanningTree.add(nodes.entrySet().stream().findFirst().get().getValue().getData());
            return minimumSpanningTree;
        }
        //Nodes in this list are used when looking for the neighbor of all nodes with the minimum weight.
        ArrayList<Node<T>> nodesProcessedSoFar = new ArrayList<>();

        Node<T> node = nodes.entrySet().stream().findFirst().get().getValue();
        nodesProcessedSoFar.add(node);
        node.setVisited(true);

        //The number of nodes in the MST should/will not differ from our graph, only the number of edges. We continue until all nodes are processed.
        while (minimumSpanningTree.nodes.size() != nodes.size())
        {
            Node<T> sourceNode = null;
            Map.Entry<Node<T>, Integer> destinationNodeEntry = null;

            for (Node<T> currentNode : nodesProcessedSoFar)
            {
                List<Map.Entry<Node<T>, Integer>> unvisitedAdjacentNodes = new ArrayList<>();
                for (Map.Entry<Node<T>, Integer> entry : currentNode.getAdjacentNodes().entrySet())
                {
                    if (entry.getKey().isVisited())
                    {
                        continue;
                    }
                    unvisitedAdjacentNodes.add(entry);
                }
                Map.Entry<Node<T>, Integer> entry = null;

                if (unvisitedAdjacentNodes.size() != 0)
                {
                    //Sort so that the node with the least weight is at the top.
                    unvisitedAdjacentNodes.sort(Map.Entry.comparingByValue());
                    entry = unvisitedAdjacentNodes.get(0);
                }
                if (entry != null)
                {
                    //Here we check perform a check with the goal to finally get the edge with the lowest cost (node + cost) in minNodeEntry.
                    if (destinationNodeEntry == null || entry.getValue() < destinationNodeEntry.getValue())
                    {
                        sourceNode = currentNode;
                        destinationNodeEntry = entry;
                    }
                }
            }
            //We now connect the two nodes and add them to our MST. As we go further the nodes that already have been added to the MST will not be added again,
            // in that case we just add the new node (minNodeEntry) and connect them.
            minimumSpanningTree.add(sourceNode.getData());
            minimumSpanningTree.add(destinationNodeEntry.getKey().getData());
            minimumSpanningTree.connect(sourceNode.getData(), destinationNodeEntry.getKey().getData(), destinationNodeEntry.getValue());

            nodesProcessedSoFar.add(destinationNodeEntry.getKey());
            destinationNodeEntry.getKey().setVisited(true);
        }
        resetVisitedStatusForAllNodes();
        return minimumSpanningTree;
    }
}