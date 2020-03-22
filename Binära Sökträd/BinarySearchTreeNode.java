//Jesper Westin, jeve9726, jeve9726@student.su.se

/**
 * Detta är den enda av de tre klasserna ni ska göra några ändringar i. (Om ni
 * inte vill lägga till fler testfall.) Det är också den enda av klasserna ni
 * ska lämna in. Glöm inte att namn och användarnamn ska stå i en kommentar
 * högst upp, och att paketdeklarationen måste plockas bort vid inlämningen för
 * att koden ska gå igenom de automatiska testerna.
 * <p>
 * De ändringar som är tillåtna är begränsade av följande:
 * <ul>
 * <li>Ni får INTE byta namn på klassen.
 * <li>Ni får INTE lägga till några fler instansvariabler.
 * <li>Ni får INTE lägga till några statiska variabler.
 * <li>Ni får INTE använda några loopar någonstans. Detta gäller också alterntiv
 * till loopar, så som strömmar.
 * <li>Ni FÅR lägga till fler metoder, dessa ska då vara privata.
 * <li>Ni får INTE låta NÅGON metod ta en parameter av typen
 * BinarySearchTreeNode. Enbart den generiska typen (T eller vad ni väljer att
 * kalla den), String, StringBuilder, StringBuffer, samt primitiva typer är
 * tillåtna.
 * </ul>
 *
 * @param <T>
 * @author henrikbe
 */

public class BinarySearchTreeNode<T extends Comparable<T>>
{

    private T data;
    private BinarySearchTreeNode<T> left;
    private BinarySearchTreeNode<T> right;

    public BinarySearchTreeNode(T data)
    {
        this.data = data;
    }

    public boolean add(T data)
    {
        int resultOfComparison = data.compareTo(this.data);

        if (resultOfComparison == 0)
        {
            return false;
        } else if (resultOfComparison < 0)
        {
            if (left == null)
            {
                left = new BinarySearchTreeNode<>(data);
                return true;
            } else
            {
                return left.add(data);
            }
        } else
        {
            if (right == null)
            {
                right = new BinarySearchTreeNode<>(data);
                return true;
            } else
            {
                return right.add(data);
            }
        }
    }

    private T findMin()
    {
        if (left == null)
        {
            return data;
        }
        return left.findMin();
    }

    //Based on remove method in course book, modified to work with this structure and with null checks
    public BinarySearchTreeNode<T> remove(T data)
    {
        int resultOfComparison = data.compareTo(this.data);

        if (resultOfComparison < 0)
        {
            if (left != null)
            {
                left = left.remove(data);
            }
        } else if (resultOfComparison > 0)
        {
            if (right != null)
            {
                right = right.remove(data);
            }
        } else if (left != null && right != null) //two children
        {
            this.data = right.findMin();
            right = right.remove(this.data);
        } else if (left == null && right == null) //no children
        {
            return null;
        } else //one child
        {
            return (left == null) ? right : left;
        }
        return this;
    }

    public boolean contains(T data)
    {
        int resultOfComparison = data.compareTo(this.data);

        if (resultOfComparison == 0)
        {
            return true;
        } else if (resultOfComparison < 0)
        {
            if (left == null)
            {
                return false;
            } else
            {
                return left.contains(data);
            }
        } else
        {
            if (right == null)
            {
                return false;
            } else
            {
                return right.contains(data);
            }
        }
    }

    public int size()
    {
        //1 because we already checked if the root is null in the BinarySearchTree class
        int treeSize = 1;
        if (left != null)
        {
            treeSize += left.size();
        }
        if (right != null)
        {
            treeSize += right.size();
        }
        return treeSize;
    }

    public int depth()
    {
        int depthOfLeftSide = 0;
        int depthOfRightSide = 0;

        if (left == null && right == null)
        {
            return 0;
        } else
        {
            if (left != null)
            {
                depthOfLeftSide += left.depth();
            }
            if (right != null)
            {
                depthOfRightSide += right.depth();
            }
        }
        return Math.max(depthOfLeftSide, depthOfRightSide) + 1;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (left == null && right == null)
        {
            sb.append(data.toString());
            return sb.toString();
        }
        if (left != null)
        {
            sb.append(left.toString()).append(", ");
        }

        sb.append(data.toString());

        if (right != null)
        {
            sb.append(", ").append(right.toString());
        }
        return sb.toString();
    }
}