package com.jetpack.paging3;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.ArrayList;

public class TestSample {
    @Test
    public void test(){
        System.out.println("test for java");
        ArrayList list = null;
        System.out.println("list == null " + (list == null)  );//+ list.isEmpty()
        list = new ArrayList();
        System.out.println("list == null " + (list == null) + "   list.isEmpty() = " + list.isEmpty());
    }
}
