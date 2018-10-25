package io.github.aidlserverdemo.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BinderPoolService extends Service {

	private static final String TAG = "BinderPoolService";

	private Binder mBinderPool = new BinderPool.BinderPoolImpl();
	private int pid = android.os.Process.myPid();

	public BinderPoolService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate: pid:"+pid);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinderPool;
	}
}
