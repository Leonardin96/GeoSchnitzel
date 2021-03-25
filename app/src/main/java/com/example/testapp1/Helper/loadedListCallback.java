package com.example.testapp1.Helper;

import java.util.List;

public interface loadedListCallback<T> {
    void onComplete(List<T> list);
}
