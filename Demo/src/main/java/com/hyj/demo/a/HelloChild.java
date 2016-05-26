package com.hyj.demo.a;

import com.hyj.demo.tools.LogUtils;

public class HelloChild extends HelloParent {
    HelloY y = new HelloY("Child");

    static {
        LogUtils.i("child static block");
    }

    public HelloChild() {
        LogUtils.i("child construct");
    }
}
