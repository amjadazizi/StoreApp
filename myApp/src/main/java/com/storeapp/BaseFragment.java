package com.storeapp;

import android.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public interface OnValidateFragmentListener {
        public void validated(boolean validated);
    }

    public abstract BaseFragment getNextFragment();
    public abstract void validate();

    private OnValidateFragmentListener validateFragmentListener;

    public void setValidateFragmentListener(OnValidateFragmentListener listener){
        this.validateFragmentListener = listener;
    }

    public void notifyValidated(boolean validated){
        if(validateFragmentListener != null){
            validateFragmentListener.validated(validated);
        }
    }


}
