package com.tw.util;

public class RelativeIndex {
    public static int get(int currentIndex, int relativeIndex, int totalSize) {
        int index = (currentIndex + relativeIndex) % totalSize;
        return index < 0 ? index + totalSize : index;
    }
}
