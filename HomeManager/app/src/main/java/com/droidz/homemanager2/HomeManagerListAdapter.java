package com.droidz.homemanager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.List;

public class HomeManagerListAdapter extends ArrayAdapter<Launcher> {
    private Context context;
    private int layoutResourceId;
    private List<Launcher> data;

    private HomeManager homeManager;
    private ViewHolder selectedViewHolder;

    public HomeManagerListAdapter(Context context, int layoutResourceId, List<Launcher> data, HomeManager homeManager) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.homeManager = homeManager;
    }

    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        Launcher launcher = data.get(index);

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.defaultLauncher = (RadioButton)convertView.findViewById(R.id.default_set);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.launcher_logo);
            viewHolder.name = (Button) convertView.findViewById(R.id.home_launch_button);
            viewHolder.uninstallButton = (ImageButton) convertView.findViewById(R.id.uninstall_button);

            viewHolder.defaultLauncher.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(selectedViewHolder != null) {
                        ViewHolder currentViewHolder = (ViewHolder)((View)v.getParent()).getTag();
                        if(selectedViewHolder.index != currentViewHolder.index)
                            homeManager.showClearDefaultsDialog(selectedViewHolder.packageName);
                        else
                            return;
                    }
                    else {
                        Intent launchHome = new Intent(Intent.ACTION_MAIN);
                        launchHome.addCategory(Intent.CATEGORY_HOME);

                        context.startActivity(launchHome);
                    }

                    ((RadioButton) v).setChecked(false);
                }
            });

            viewHolder.name.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    View view = (View) v.getParent();
                    ViewHolder viewHolder = (ViewHolder) view.getTag();

                    homeManager.launchPackage(viewHolder.packageName);
                }
            });

            viewHolder.uninstallButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    View view = (View) v.getParent();
                    ViewHolder viewHolder = (ViewHolder) view.getTag();

                    homeManager.uninstallPackage(viewHolder.packageName);
                }

            });
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        viewHolder.icon.setImageDrawable(launcher.icon);
        viewHolder.name.setText(launcher.name);

        if((launcher.systemApp)) {
            viewHolder.uninstallButton.setEnabled(false);
            viewHolder.uninstallButton.setImageResource(R.drawable.ic_action_discard_disabled);
        }
        else {
            viewHolder.uninstallButton.setEnabled(true);
            viewHolder.uninstallButton.setImageResource(R.drawable.ic_action_discard);
        }

        viewHolder.packageName = launcher.packageName;
        viewHolder.index = index;

        if(launcher.defaultLauncher) {
            viewHolder.defaultLauncher.setChecked(true);

            try {
                selectedViewHolder = (ViewHolder) viewHolder.clone();
            } catch (CloneNotSupportedException e) {}

        }
        else
            viewHolder.defaultLauncher.setChecked(false);


        convertView.setTag(viewHolder);
        return convertView;
    }
}
