package com.example.attendancemanager1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class DisplayAttendanceWidgetActivity extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        Intent intent = new Intent(context, AttendanceShowActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent intent2 = new Intent(context, CalenderActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.display_attendance_widget_activity);

        Context context_this = ContextRetriever.getContext();
        DatabaseHelper db = new DatabaseHelper(context_this);
        int[] att_arr = db.retureTotal();
        String widgetText = att_arr[1] + "/" + att_arr[0];
        int progress = Math.round((att_arr[1]/(float)att_arr[0])*100);

        views.setTextViewText(R.id.wattendanceatt, widgetText);
        views.setProgressBar(R.id.wprogressatt,100,progress,false);
        views.setTextViewText(R.id.wprogresstextatt,""+progress);
        views.setOnClickPendingIntent(R.id.view_attendance_widget, pendingIntent);
        views.setOnClickPendingIntent(R.id.view_calendar_widget,pendingIntent2);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

