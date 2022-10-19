package com.example.mylingual.data;

public enum ButtonCase {
    Microphone, Camera, Keyboard;

    public Integer getButtonCase() {
        return  this.ordinal();
    }
}
