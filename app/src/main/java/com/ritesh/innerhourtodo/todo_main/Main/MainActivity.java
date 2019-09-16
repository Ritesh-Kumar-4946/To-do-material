package com.ritesh.innerhourtodo.todo_main.Main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ritesh.innerhourtodo.R;
import com.ritesh.innerhourtodo.todo_main.AppDefault.AppDefaultActivity;

public class MainActivity extends AppDefaultActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.activity_main_todo;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        return MainFragment.newInstance();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.aboutMeMenuItem:
//                Intent i = new Intent(this, AboutActivity.class);
//                startActivity(i);
//                return true;
////            case R.id.switch_themes:
////                if(mTheme == R.style.CustomStyle_DarkTheme){
////                    addThemeToSharedPreferences(LIGHTTHEME);
////                }
////                else{
////                    addThemeToSharedPreferences(DARKTHEME);
////                }
////
//////                if(mTheme == R.style.CustomStyle_DarkTheme){
//////                    mTheme = R.style.CustomStyle_LightTheme;
//////                }
//////                else{
//////                    mTheme = R.style.CustomStyle_DarkTheme;
//////                }
////                this.recreate();
////                return true;
//            case R.id.preferences:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}


