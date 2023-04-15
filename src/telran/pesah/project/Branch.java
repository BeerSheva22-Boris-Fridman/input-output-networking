package telran.pesah.project;

import java.io.Serializable;

public class Branch implements Serializable {
    private String name;
    private Commit head;

    public Branch(String name, Commit commit) {
        this.name = name;
        this.head = commit;
    }

    public String getName() {
        return name;
    }

    public Commit getHead() {
        return head;
    }
    public void setHead(Commit head) {
        this.head = head;
    }
}
