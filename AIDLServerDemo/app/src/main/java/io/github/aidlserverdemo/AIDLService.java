package io.github.aidlserverdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by codeczx on 2018/10/10 下午 12:05.
 * Class description:
 */
public class AIDLService extends Service {

	private static final String TAG = "AIDLService";

	private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

	// 处理客户端并发调用
	private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
	// 专门用来处理listener的类（序列化和反序列化之后客户端和服务端中的listener对象不是同一个，
	// 在底层通过binder对象来对应客户端注册的listener）
	private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		initData();
		new Thread(new ServiceWorker()).start();
	}

	private void initData() {
		for (int i = 0; i < 10; i++) {
			Book book = new Book("book#" + i);
			bookList.add(book);
		}
	}

	private final BookController.Stub stub = new BookController.Stub() {
		@Override
		public List<Book> getBookList() throws RemoteException {
			return bookList;
		}

		@Override
		public void addBookInOut(Book book) throws RemoteException {
			if (book != null) {
				bookList.add(book);
			}
		}

		@Override
		public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
			mListenerList.register(listener);
		}

		@Override
		public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
			mListenerList.unregister(listener);
		}
	};

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return stub;
	}

	private void onNewBookArrived(Book book) throws RemoteException {
		bookList.add(book);
		Log.i(TAG, "onNewBookArrived:" + book.getName());
		final int N = mListenerList.beginBroadcast();
		for (int i = 0; i < N; i++) {
			IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
			if (listener != null) {
				listener.onNewBookArrived(book);
			}
		}
		// 和beginBroadcast成对使用
		mListenerList.finishBroadcast();
	}

	private class ServiceWorker implements Runnable {

		@Override
		public void run() {
			while (!mIsServiceDestroyed.get()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Book newBook = new Book("newBook#" + bookList.size());
				try {
					onNewBookArrived(newBook);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mIsServiceDestroyed.set(false);
	}
}
