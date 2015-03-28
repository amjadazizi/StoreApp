package com.storeapp.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.storeapp.R;

public abstract class BaseFragment extends Fragment {



    protected abstract BaseFragment getPrevFragment();
	protected abstract BaseFragment getNextFragment();
    protected abstract void validate();
    protected  abstract void save();

    protected OnValidateFragmentListener validateFragmentListener;

    FragmentTransaction transaction;

    protected void setCurrentFragment(FragmentManager fragmentManager){
        setCurrentFragment(fragmentManager, true);
    }

    public void setValidateFragmentListener(OnValidateFragmentListener listener){
        this.validateFragmentListener = listener;
    }

    public void removeValidateFragmentListener(){
        this.validateFragmentListener = null;
    }

    protected void fireOnValidateFragmentListener(boolean validated){
        if(validateFragmentListener != null){
            validateFragmentListener.validateFragment(validated);
        }
    }


    protected void setCurrentFragment(FragmentManager fragmentManager, boolean addToStack){

         transaction = fragmentManager
                .beginTransaction();

            if(addToStack){
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            }
         //   SoundEffects.playActivityChangeSound();
            transaction.replace(R.id.fragmentsRegistCont, this);

          if(addToStack) {
               transaction.addToBackStack(null);
           }
        transaction.commit();

    }



}
