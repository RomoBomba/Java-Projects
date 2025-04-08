package com.petapp;

import com.petapp.util.AppController;
import com.petapp.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        AppController controller = new AppController();
        controller.run();
        HibernateUtil.shutdown();
    }
}