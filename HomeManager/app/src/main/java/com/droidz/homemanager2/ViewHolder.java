package com.droidz.homemanager2;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

public class ViewHolder implements Cloneable{
    protected String packageName;
    protected RadioButton defaultLauncher;
    protected ImageView icon;
    protected Button name;
    protected ImageButton uninstallButton;
    protected int index;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


