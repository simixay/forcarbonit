package com.forcarbonit.display;

public class Display extends AbstractDisplay{
    @Override
    public void display(String str, Object ...args) {
        System.out.printf(str, args);
    }
}
