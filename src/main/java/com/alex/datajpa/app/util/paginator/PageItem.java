package com.alex.datajpa.app.util.paginator;

public class PageItem {
    
    private int number;
    private boolean current;

    
    public PageItem(int number, boolean current) {
        this.number = number;
        this.current = current;
    }


    public int getNumber() {
        return number;
    }
    public boolean isCurrent() {
        return current;
    }

}
