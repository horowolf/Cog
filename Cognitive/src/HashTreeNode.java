import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HashTreeNode {

    static int HASHSIZE = 3;
    static int MAXITEMS = 3;
    int hashIndex = 0;
    int itemCount = 0;

    HashTreeNode parent;
    HashTreeNode[] childList = new HashTreeNode[HASHSIZE + 1];
    ArrayList<List<String>> transactionList = new ArrayList<List<String>>();


    public int hash(String item) {
        int hashcode = item.hashCode();
        int bucket = hashcode % HASHSIZE;
        //System.out.println(item + " : " + bucket + "(" + hashcode + ")");
        return bucket;
    }

    public void insert(List<String> transaction) {
        int bucket = hash(transaction.get(hashIndex));
        HashTreeNode child;

        if (this.childList[bucket] != null) {
            child = this.childList[bucket];
        }
        else {
            child = new HashTreeNode();
            this.childList[bucket] = child;
            child.parent = this;
        }

        if (itemCount == MAXITEMS) {
            System.out.println("Adding another level");
            child.hashIndex++;
            for (int i = 0; i < child.transactionList.size(); i++){
                child.insert(child.transactionList.get(i));
            }
            child.transactionList.clear();
        }
        else {
            child.transactionList.add(transaction);
        }
        this.itemCount++;

    }

    public static void main(String[] args) {
        List<String> test1 = Arrays.asList("1", "4", "5");
        List<String> test2 = Arrays.asList("1", "4", "5");
        List<String> test3 = Arrays.asList("1", "2", "3");
        List<String> test4 = Arrays.asList("1", "2", "4");
        List<String> test5 = Arrays.asList("5", "2", "4");


        HashTreeNode root = new HashTreeNode();
        root.insert(test1);
        root.insert(test2);
        root.insert(test3);
        root.insert(test4);
        root.insert(test5);

        for (int i = 0; i < 3; i++) {
            if (root.childList[i] != null) {
                for (int j = 0; j < root.childList[i].transactionList.size(); j++) {
                    System.out.println("Child " + i + " Item " + j + " : " + root.childList[i].transactionList.get(j));
                }
            }
        }

    }
}