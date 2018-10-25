// IOnNewBookArrivedListener.aidl
package io.github.aidlserverdemo;
import io.github.aidlserverdemo.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
