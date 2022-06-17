package com.example.testapp1.Callbacks;

import java.util.List;

public interface loadedListCallback<T> {
    void onComplete(List<T> list);
}
