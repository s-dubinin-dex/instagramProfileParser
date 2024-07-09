package com.instagram.instagramProfileParser;

public class Timer {
    public static int getRandomTimeForScrolling(){
        return (int) (Math.random() * 500) + 2000;
    }

    public static int getRandomTimeToGoToNextPhoto(){
        return (int) (Math.random() * 500) + 500;
    }
    public static int getRandomTimeToWaitProfileLoading(){
        return (int) (Math.random() * 500) + 5000;
    }
    public static int getRandomTimeForLogIn(){
        return (int) (Math.random() * 1000) + 5 * 60 * 1000;
    }

}
