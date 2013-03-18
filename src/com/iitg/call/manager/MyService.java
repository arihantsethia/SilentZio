/*************** Copyright 2012 AsyncTech ******************************

This file is part of SilentZio.

AsyncTech is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

AsyncTech is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with AsyncTech.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************/



package com.iitg.call.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyService extends Service {
	PhoneState phoneListener;
	TelephonyManager telephonyManager;

	@Override
	public void onCreate() {
		phoneListener = new PhoneState();
		phoneListener.contextCreator(getApplicationContext());
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		showNotification();
	}

	@SuppressWarnings("deprecation")
	public void showNotification() {

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification not = new Notification(R.drawable.shield,
				"SilentZio Activated", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Calendar.class),
				Notification.FLAG_ONGOING_EVENT);
		not.flags = Notification.FLAG_ONGOING_EVENT;
		not.setLatestEventInfo(this, "SilentZio", "SilentZio Running",
				contentIntent);
		mNotificationManager.notify(1, not);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification not = new Notification(R.drawable.shield,
				"SilentZio Dectivated", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Calendar.class),
				Notification.FLAG_ONGOING_EVENT);
		not.flags = Notification.FLAG_ONGOING_EVENT;
		not.setLatestEventInfo(this, "SilentZio", "SilentZio Stopped",
				contentIntent);

		mNotificationManager.notify(1, not);

		mNotificationManager.cancel(1);
		// stopSelf();
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
