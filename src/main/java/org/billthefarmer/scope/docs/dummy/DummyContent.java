package org.billthefarmer.scope.docs.dummy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    private static final int COUNT = 10;


//    static {
//        addItem(new DummyItem("1","CONCEITOS FUNDAMENTAIS", "images/texto-01.png"));
//        addItem(new DummyItem("2","FENÔMENOS ONDULATÓRIOS", "images/texto-01.png"));
//        addItem(new DummyItem("3","ONDAS PERIÓDICAS", "images/texto-01.png"));
//        addItem(new DummyItem("4","FÍSICA ACÚSTICA", "images/texto-01.png"));
//        addItem(new DummyItem("5","O OUVIDO HUMANO", "images/texto-01.png"));
//        addItem(new DummyItem("6","FENÔMENOS ONDULATÓRIOS – ONDAS SONORAS", "images/texto-01.png"));
//        addItem(new DummyItem("7","INSTRUMENTOS MUSICAIS", "images/texto-01.png"));
//        addItem(new DummyItem("8","QUESTIONÁRIO", "images/texto-01.png"));
//        addItem(new DummyItem("9","LINKS E CURIOSIDADES SOBRE FÍSICA ACÚSTICA", "images/texto-01.png"));
//    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {

        public final String id;
        public final String content;
        public final String text;
        public final String image;

        public DummyItem(String id, String content,String text,String image) {
            this.id = id;
            this.text = text;
            this.content = content;
            this.image = image;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
