import java.util.*;

import javax.management.RuntimeErrorException;  


class Storage {

    Map<String,String>map = new HashMap<String, String>();
    private Integer capacity;

    public Storage(Integer capacity) {
        this.capacity=capacity;
    }

    public String get(String key) {
        return map.get(key);
    }

    public void add(String key, String value)
    {
        if(map.size() == capacity)  
        {
            throw new IllegalArgumentException("Capacity is full");
        }
        map.put(key,value);
    }

    public void remove(String key) {
        map.remove(key);
    }
}

class DLLNode {
    Integer element;
    DLLNode next;
    DLLNode prev;
    
    public DLLNode(Integer element) {
        this.element = element;
        this.prev = null;
        this.next = null;
    }
}

class DLL {
    private final DLLNode dummyHead;
    private final DLLNode dummyTail;

    public DLL() {
        this.dummyHead = new DLLNode(null);
        this.dummyTail = new DLLNode(null);
        this.dummyHead.next = dummyTail;
        this.dummyTail.prev = dummyHead;
    }

    public void detachNode(DLLNode dllNode) {

    }

    public void addNodeToLast(DLLNode dllNode) {

    }
}

class EvictionPolicy {
    private DLL dll;
    private Map<String, DLLNode>map;

    public EvictionPolicy(DLL dll) {
        this.dll=new DLL();
        this.map = new HashMap<>();
    }

    public void keyAccessed(String key) {
        if(map.containsKey(key)) {
            dll.detachNode(map.get(key));
            dll.addNodeToLast(map.get(key));
        }
        else {
            final DLLNode node = dll.insertElementAtEnd(key);
            map.put(key, node);
        }
    }
    
    public String evict() {
        final DLLNode lruNode = dll.getFirstNode();
        if(lruNode==null)
            return null;
        dll.detachNode(lruNode);
        map.remove(lruNode);
        return lruNode.getElement();
    }

}

class Cache {
    private final Storage storage;
    private final EvictionPolicy evictionPolicy;

    public Cache(Storage storage, EvictionPolicy evictionPolicy) {
        this.storage = storage;
        this.evictionPolicy = evictionPolicy;
    }


    public String get(String key) {
        try {
            String value = this.storage.get(key);
            this.evictionPolicy.keyAccessed(key);
            return value;
        }
        catch (Exception e) {
            System.out.println("Key not found ");
            return null;
        }
    }

    public void put(String key, String value) {
        try {
            this.storage.add(key,value);
            this.evictionPolicy.keyAccessed(key);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Caught an IllegalArgumentException: " + e.getMessage());
            String keyToRemove = evictionPolicy.evictKey();
            if(keyToRemove == null) {
                throw new RuntimeErrorException("No Key to evict");
            }
            this.storage.remove(keyToRemove);
            put(key,value);
        }
    }
}

class HelloWorld {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);  
        System.out.println("Input an integer ");
        int a= sc.nextInt(); 
        System.out.println("a is " + a);

    }
}