package com.example.milionerzy.interfaces;

public interface BaseService<T> {
    void saveResultInSharedPreferences(T toSave);
    void showToastWithMessage(String message, int lengthOfToast);
}
