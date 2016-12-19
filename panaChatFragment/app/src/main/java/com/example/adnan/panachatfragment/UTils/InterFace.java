package com.example.adnan.panachatfragment.UTils;

/**
 * Created by Adnan Ahmed on 2/24/2016.
 */
public interface InterFace<P ,Q> {

    public void sucess(P dataSnapshot);
    public void fail(Q obj);
}
