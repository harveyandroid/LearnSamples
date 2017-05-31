// (c)2016 Flipboard Inc, All Rights Reserved.

package com.harvey.rxjava;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;

import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment {
    public final CompositeDisposable disposables = new CompositeDisposable();

    @OnClick(R.id.tipBt)
    void tip() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (disposables != null) {
            disposables.clear();
        }
    }

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
