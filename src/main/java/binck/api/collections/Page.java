package binck.api.collections;

import java.util.List;

public class Page<T> {

    public final List<T> items;
    public final String nextRange;

    public Page(List<T> items, String nextRange) {
        this.items = items;
        this.nextRange = nextRange;
    }
}
