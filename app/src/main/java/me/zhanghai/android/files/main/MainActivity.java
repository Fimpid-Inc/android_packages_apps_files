/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.files.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java8.nio.file.Path;
import java8.nio.file.Paths;
import me.zhanghai.android.files.util.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_PREFIX = MainActivity.class.getName() + '.';

    private static final String EXTRA_PATH = KEY_PREFIX + "PATH";

    @Nullable
    private Path mExtraPath;

    @NonNull
    private MainFragment mMainFragment;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    @NonNull
    public static Intent makeIntent(@NonNull Path path, @NonNull Context context) {
        return makeIntent(context)
                .putExtra(EXTRA_PATH, (Parcelable) path);
    }

    public static void putFileExtra(@NonNull Intent intent, @NonNull Path path) {
        intent.putExtra(EXTRA_PATH, (Parcelable) path);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExtraPath = getIntent().getParcelableExtra(EXTRA_PATH);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            mMainFragment = MainFragment.newInstance(getPath());
            FragmentUtils.add(mMainFragment, this, android.R.id.content);
        } else {
            mMainFragment = FragmentUtils.findById(this, android.R.id.content);
        }
    }

    @Nullable
    private Path getPath() {

        if (mExtraPath != null) {
            return mExtraPath;
        }

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && Objects.equals(data.getScheme(), "file")) {
            String path = data.getPath();
            if (!TextUtils.isEmpty(path)) {
                return Paths.get(path);
            }
        }

        String path = intent.getStringExtra("org.openintents.extra.ABSOLUTE_PATH");
        if (path != null) {
            return Paths.get(path);
        }

        return null;
    }

    @Override
    public void onBackPressed() {
        if (mMainFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}