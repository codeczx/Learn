// BookController.aidl
package io.github.aidlserverdemo;

import io.github.aidlserverdemo.Book;
import io.github.aidlserverdemo.IOnNewBookArrivedListener;

interface BookController {
    List<Book> getBookList();
    void addBookInOut(inout Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
