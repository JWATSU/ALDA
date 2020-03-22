//Jesper Westin, jeve9726, jeve9726@student.su.se

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyALDAList<E> implements ALDAList<E>
{
    private static class Node<E>
    {
        Node<E> next;
        E data;

        public Node(E data)
        {
            this.data = data;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int currentListSize;

    @Override
    public void add(E element)
    {
        if (head == null)
        {
            head = new Node<>(element);
            tail = head;
        } else
        {
            tail.next = new Node<>(element);
            tail = tail.next;
        }
        currentListSize++;
    }

    @Override
    public void add(int index, E element)
    {
        if (index < 0 || index > currentListSize)
        {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0)
        {
            Node<E> newNode = new Node<>(element);
            newNode.next = head;
            head = newNode;
            //For doing add(0, element) on an empty list, in which case tail would otherwise be null.
            if (currentListSize == 0)
            {
                tail = newNode;
            }
            currentListSize++;
        } else if (index == currentListSize)
        {
            add(element);
        } else
        {
            Node<E> newNode = new Node<>(element);
            Node<E> currentNode = head;
            Node<E> previousNode = null;

            for (int i = 0; i < index; i++)
            {
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = newNode;
            newNode.next = currentNode;
            currentListSize++;
        }
    }

    @Override
    public E remove(int index)
    {
        E removedDataToReturn = null;

        if (currentListSize == 0 || index < 0 || index >= currentListSize)
        {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0)
        {
            Node<E> nodeToRemove = head;
            removedDataToReturn = nodeToRemove.data;
            head = head.next;
        } else
        {
            int count = 0;
            for (Node<E> tmp = head; tmp != null; tmp = tmp.next)
            {
                if (count == index - 1)
                {
                    Node<E> removeNode = tmp.next;
                    tmp.next = removeNode.next;
                    if (tmp.next == null)
                    {
                        tail = tmp;
                    }
                    removedDataToReturn = removeNode.data;
                }
                count++;
            }
        }
        currentListSize--;
        return removedDataToReturn;
    }

    @Override
    public boolean remove(E element)
    {
        if (currentListSize == 0)
        {
            return false;
        }
        if (head.data == element || head.data.equals(element))
        {
            head = head.next;
            currentListSize--;
            return true;
        }
        for (Node<E> tmp = head; tmp.next != null; tmp = tmp.next)
        {
            if (tmp.next.data == element || tmp.next.data.equals(element))
            {
                if (tmp.next == tail)
                {
                    tail = tmp;
                    tmp.next = null;
                    currentListSize--;
                    return true;
                } else
                {
                    tmp.next = tmp.next.next;
                    currentListSize--;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E get(int index)
    {
        if (currentListSize == 0 || index < 0 || index >= currentListSize)
        {
            throw new IndexOutOfBoundsException();
        }
        if (index == size())
        {
            return tail.data;
        }
        Node<E> tmp = head;
        for (int i = 0; i < index; i++)
        {
            tmp = tmp.next;
        }
        return tmp.data;
    }

    @Override
    public boolean contains(E element)
    {
        for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next)
        {
            if (currentNode.data == element || currentNode.data.equals(element))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(E element)
    {
        int index = 0;
        for (Node<E> tmp = head; tmp != null; tmp = tmp.next)
        {
            if (tmp.data == element || tmp.data.equals(element))
            {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public void clear()
    {
        head = null;
        tail = null;
        currentListSize = 0;
    }

    @Override
    public int size()
    {
        return currentListSize;
    }

    @Override
    public String toString()
    {
        Node<E> tmp = head;
        StringBuilder sb = new StringBuilder();

        while (tmp != null)
        {
            sb.append(tmp.data);
            if (tmp.next != null)
            {
                sb.append(", ");
            }
            tmp = tmp.next;
        }
        return "[" + sb + "]";
    }

    @Override
    public Iterator<E> iterator()
    {
        return new Iterator<>()
        {
            Node<E> currentNode = null;
            Node<E> previousNode = null;
            boolean alreadyRemoved = true;

            @Override
            public boolean hasNext()
            {
                return currentListSize != 0 && currentNode != tail;
            }

            @Override
            public E next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                if (currentNode == null)
                {
                    currentNode = head;
                } else
                {
                    previousNode = currentNode;
                    currentNode = currentNode.next;
                }
                alreadyRemoved = false;
                return currentNode.data;
            }

            @Override
            public void remove()
            {
                if (alreadyRemoved)
                {
                    throw new IllegalStateException();
                }
                if (currentNode == head)
                {
                    //More elements left. We remove the first element and point head to the following element.
                    if (hasNext())
                    {
                        head = head.next;
                    } else
                    {
                        //No elements left. The list is now empty.
                        currentNode = null;
                        head = null;
                        tail = null;
                    }
                } else
                {
                    previousNode.next = currentNode.next;
                    currentNode = previousNode;
                    if (currentNode.next == null)
                    {
                        tail = currentNode;
                    }
                }
                currentListSize--;
                alreadyRemoved = true;
            }
        };
    }
}
