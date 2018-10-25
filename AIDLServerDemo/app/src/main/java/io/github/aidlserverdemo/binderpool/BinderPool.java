package io.github.aidlserverdemo.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

import io.github.aidlserverdemo.IBinderPool;

/**
 * Created by codeczx on 2018/10/24 下午 10:06.
 * Class description:把多个模块的远程调用集中到一个服务中
 */
public class BinderPool {

	private static final String TAG = "BinderPool";
	public static final int BINDER_NONE = -1;
	public static final int BINDER_COMPUTE = 0;
	public static final int BINDER_SECURITY = 1;

	private static volatile BinderPool sInstance;
	private Context mContext;
	private CountDownLatch mConnectBinderPoolCountDownLatch;
	private IBinderPool mBinderPool;

	private BinderPool(Context context) {
		mContext = context.getApplicationContext();
		connectBinderPoolService();
	}

	private void connectBinderPoolService() {
		mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
		Intent intent = new Intent(mContext, BinderPoolService.class);
		// bind的时候自动创建service
		mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		try {
			mConnectBinderPoolCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinderPool = IBinderPool.Stub.asInterface(service);

			// 注册一个通知，远程的binder对象死亡会回调deathRecipient#binderDied
			try {
				mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			mConnectBinderPoolCountDownLatch.countDown();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
		@Override
		public void binderDied() {
			Log.i(TAG, "binder died.");
			// 已经得到远程binder死亡的回调了，取消通知的注册
			mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
			mBinderPool = null;
			// 直接重新连接
			connectBinderPoolService();
		}
	};

	public static BinderPool getInstance(Context context) {
		if (sInstance == null) {
			synchronized (BinderPool.class) {
				if (sInstance == null) {
					return new BinderPool(context);
				} else {
					return sInstance;
				}
			}
		} else {
			return sInstance;
		}
	}

	public IBinder queryBinder(int binderCode) {
		IBinder binder = null;
		try {
			if (mBinderPool != null) {
				binder = mBinderPool.queryBinder(binderCode);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return binder;
	}

	public static class BinderPoolImpl extends IBinderPool.Stub {

		@Override
		public IBinder queryBinder(int binderCode) throws RemoteException {
			IBinder binder = null;
			switch (binderCode) {
				case BINDER_NONE:
					break;
				case BINDER_COMPUTE:
					binder = new ComputeImpl();
					break;
				case BINDER_SECURITY:
					binder = new SecurityCenterImpl();
					break;
			}
			return binder;
		}
	}
}
