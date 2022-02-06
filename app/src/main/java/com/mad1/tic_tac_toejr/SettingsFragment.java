package com.mad1.tic_tac_toejr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SettingsFragment extends Fragment implements View.OnCreateContextMenuListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_settings, container,false);
    }

    @Override
    public void onPause() {
        // save the instance variables

//        SharedPreferences.Editor editor = savedValues.edit();
//        editor.putInt("roundCount", roundCount);
//        editor.putInt("player1Points", player1Points);
//        editor.putInt("player2Points", player2Points);
//        editor.putBoolean("player1Turn", player1Turn);
//        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
//        new MainActivity();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//                    if(getFragmentManager().getBackStackEntryCount() > 0) {
//                        getFragmentManager().popBackStack();
//                    }
//
//                    return true;
//
//                }
//
//                return false;
//            }
//        });
    }

    // create menu items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        //inflater.inflate(R.menu.fragment_menu,menu);
    }




}
