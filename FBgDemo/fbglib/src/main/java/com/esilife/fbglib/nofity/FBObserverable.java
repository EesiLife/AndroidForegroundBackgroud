package com.esilife.fbglib.nofity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siy on 18-4-27.
 */
public class FBObserverable implements Observerable {
    private List<Observer> mList = new ArrayList<>();
    private Observer mObserver = null;
    private boolean fg = false;

    public static class SingleHolder{
        public static FBObserverable  mInstance = new FBObserverable();
    }

    public static FBObserverable getInstance() {
        return SingleHolder.mInstance;
    }

    private FBObserverable(){

    }

    @Override
    public void registerOnlyOneObserver(Observer o) {
        this.mObserver = o;
    }

    @Override
    public void removeOnlyOneObserver() {
        this.mObserver = null;
    }

    @Override
    public void registerObserver(Observer o) {
        if(!mList.contains(o)) {
            mList.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        if(mList.contains(o)) {
            mList.remove(o);
        }
    }

    public void setStatus(boolean fg) {
        this.fg = fg;
        notifyObserver();
    }

    @Override
    public void notifyObserver() {
        if (null != mObserver) mObserver.update(fg);
        for (Observer ob : mList)
            ob.update(fg);
    }
}
