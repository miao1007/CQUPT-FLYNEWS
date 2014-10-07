package com.github.miao1007.wordpressclient.slidingmenu;

import android.app.Activity;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public abstract class SlidingMenuActivity extends Activity implements MenuAdapter.MenuListener,MenuDrawer.OnDrawerStateChangeListener {



    protected abstract int getDragMode();

    protected abstract Position getDrawerPosition();


}
