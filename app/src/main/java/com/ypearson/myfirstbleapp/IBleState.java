package com.ypearson.myfirstbleapp;


public interface IBleState {

    int bleGetState();
    void bleSetState(int state);
    void bleUpdateUI();
}
