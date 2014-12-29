package com.droidz.homemanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class HomeManager {
    private Context context;
    private ResolveInfo preferredActivity;
    private List<ResolveInfo> resolveInfoList;
    private PackageManager packageManager;
    protected boolean defaultFound = false;

    public HomeManager(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
    }

    private boolean isDefault(String packageName) {
        if(preferredActivity.activityInfo.packageName.equals(packageName)) {
            defaultFound = true;
            return true;
        }

        return false;
    }

    /** Checks if a launcher is a system app. */
    private boolean isSystemApp(ResolveInfo resolveInfo) {
        ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;

        if((appInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0)
            return true;

        return false;
    }

    /** Gathers the necessary information about all the launchers installed on the device. */
    protected List<Launcher> getLaunchers() {
        List<Launcher> launchers = new ArrayList<Launcher>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        //Gets all the launchers installed on the device.
        resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        //Gets the current default launcher.
        preferredActivity = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for(int i = 0; i < resolveInfoList.size(); i++) {
            ResolveInfo resolveInfo = resolveInfoList.get(i);

            String packageName = resolveInfo.activityInfo.packageName;
            String name = resolveInfo.loadLabel(packageManager).toString();
            Drawable icon = resolveInfo.loadIcon(packageManager);

            if(!defaultFound)
                launchers.add(new Launcher(packageName, isDefault(packageName), icon, name, isSystemApp(resolveInfo)));
            else
                launchers.add(new Launcher(packageName, false, icon, name, isSystemApp(resolveInfo)));
        }
        return launchers;
    }

    /** Launches a launcher when given its package name. */
    protected void launchPackage(String packageName) {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    /** Uninstalls a launcher when given its package name. */
    protected void uninstallPackage(String packageName) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);

        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);

        context.startActivity(intent);
    }

    /** Opens application info settings of a launcher */
    private void showApplicationInfo(String selectedPackageName) {
        Intent showAppInfo = new Intent();
        showAppInfo.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

        Uri uri = Uri.fromParts("package", selectedPackageName, null);
        showAppInfo.setData(uri);

        context.startActivity(showAppInfo);
    }

    protected void showClearDefaultsDialog(final String selectedPackageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.clear_defaults);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showApplicationInfo(selectedPackageName);
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /** Returns the number of launcher installed on the device. */
    protected int getNumberOfLaunchersInstalled() { return resolveInfoList.size(); }
}
