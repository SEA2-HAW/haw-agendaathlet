/**
 * HAWAgendaAthlet Android client application
 *
 * @author Tobias Kröger
 * @author Leon Schardin
 * @author Kaleb Pohl
 * @author Erfan Akhondi
 * @author Taalaibek Mateev
 * @author Ansgar Leonard Kock
 * Copyright (C) 2022.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3,
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.haw.agendaathlet;

import static de.haw.agendaathlet.MainActivity.datenverwaltung;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import de.haw.agendaathlet.datamanagement.DatenverwaltungImpl;
import de.haw.agendaathlet.eventManager.CalendarUtils;
import de.haw.agendaathlet.eventVisual.Event;
import de.haw.agendaathlet.eventVisual.EventZeitComparator;

public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        InjectorManager j = new InjectorManager();
        InjectorManager.IM.setDatenverwaltung(new DatenverwaltungImpl(context));

        try {
            ArrayList<Event> eventsList = datenverwaltung.ladeEvents();
            ArrayList<Event> result = new ArrayList<>();

            for (Event ev : eventsList) {
                if (ev.getDate().compareTo(LocalDate.now()) >= 0) {
                    if (ev.getDate().equals(LocalDate.now()) && ((ev.getstarTime().getHour() - (LocalTime.now().getHour())) >= 0))
                        result.add(ev);
                } else result.add(ev);
            }

            Collections.sort(result, new EventZeitComparator());

            Event e = result.get(0);

            views.setTextViewText(R.id.wiidgeteventNameAnzeige, e.getName() + "\n");
            views.setTextViewText(R.id.widgeteventZeitAnzeige, CalendarUtils.DayMonthFromDate(e.getDate()) + "\n" + e.getstarTime() + "\n" + e.getendTime());
            views.setTextViewText(R.id.widgeteventDescriptionAnzeige, e.getDescription());
        } catch (Exception e) {
            views.setTextViewText(R.id.wiidgeteventNameAnzeige, "Keine Events");
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}