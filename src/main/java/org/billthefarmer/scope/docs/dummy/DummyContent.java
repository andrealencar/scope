package org.billthefarmer.scope.docs.dummy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    private static final int COUNT = 10;

    static {
        addItem(new DummyItem("1","Texto número 1", "images/onda_sonora.jpg"));
        addItem(new DummyItem("2","Texto número 2", "images/onda_sonora.jpg"));
        addItem(new DummyItem("3","Texto número 3", "images/onda_sonora.jpg"));
        addItem(new DummyItem("4","Texto número 4", "images/onda_sonora.jpg"));
        addItem(new DummyItem("5","Texto número 5", "images/onda_sonora.jpg"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    public static class DummyItem {

        public final String id;
        public final String content;
        public final String image;

        public DummyItem(String id, String content,String image) {
            this.id = id;
            this.content = content;
            this.image = image;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
