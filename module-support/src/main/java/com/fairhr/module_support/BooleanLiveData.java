package com.fairhr.module_support;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class BooleanLiveData extends MutableLiveData<Boolean> {

    public BooleanLiveData(Boolean value) {
        super(value);
    }

    @Nullable
    @Override
    public Boolean getValue() {
        return super.getValue();
    }

}
