package multiverse.androidapp.multiverse.util.functions;

import java.util.List;

public class ListUtil {

    private ListUtil() {

    }

    public static <T> List<T> mergeList(List<T> list, List<T> itemToMerge, int offset) {
        int itemIndex = offset;
        for(T t : itemToMerge) {
            if(list.size() < itemIndex) {
                list.set(itemIndex, t);
            } else if(list.size() == itemIndex) {
                list.add(t);
            } else {
                // Pad the list with null items
                int itemToAdd = itemIndex - list.size();
                for(int i = 0; i < itemIndex; i++) {
                    list.add(null);
                }
                list.add(t);
            }
            itemIndex++;
        }
        return list;
    }
}
