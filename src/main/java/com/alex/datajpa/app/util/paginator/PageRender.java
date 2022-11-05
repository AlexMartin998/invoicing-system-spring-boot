package com.alex.datajpa.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

    private String url;
    private Page<T> page;

    private int totalPages;
    private int numItemsPerPage;
    private int currentPage;

    private List<PageItem> pages;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<PageItem>();

        // vienen del controller paginador
        numItemsPerPage = page.getSize();
        totalPages = page.getTotalPages();
        currentPage = page.getNumber() + 1;


        int from, to;
        if (totalPages <= numItemsPerPage) {
            from = 1;
            to = totalPages;
        } else {
            if (currentPage <= numItemsPerPage / 2) {
                from = 1;
                to = numItemsPerPage;
            } else if (currentPage >= totalPages - numItemsPerPage / 2) {
                from = totalPages - numItemsPerPage + 1;
                to = numItemsPerPage;
            } else {
                from = currentPage - numItemsPerPage / 2;
                to = numItemsPerPage;
            }
        }

        for (int i = 0; i < to; i++) {
            pages.add(new PageItem(from + i, currentPage == from + i));
        }

    }

    public String getUrl() {
        return url;
    }

    public List<PageItem> getPages() {
        return pages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    //
    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }

}
