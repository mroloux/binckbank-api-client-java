package binck.api.collections;

import java.util.Iterator;
import java.util.function.Function;

public class PageIterator<T> implements Iterator<T> {

    private final Function<String, Page<T>> fetcher;
    private Page<T> currentPage;
    private Iterator<T> iteratorInPage;

    public PageIterator(Function<String, Page<T>> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public boolean hasNext() {
        if (currentPage == null) {
            return true;
        }
        if (iteratorInPage.hasNext()) {
            return true;
        }
        return currentPage.nextRange != null;
    }

    @Override
    public T next() {
        if (currentPage == null) {
            currentPage = fetcher.apply("");
            iteratorInPage = currentPage.items.iterator();
        } else if (!iteratorInPage.hasNext()) {
            currentPage = fetcher.apply(currentPage.nextRange);
            iteratorInPage = currentPage.items.iterator();
        }

        return iteratorInPage.next();
    }
}
