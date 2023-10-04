package com.example.contact_management_app;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private MutableLiveData<String> clickedValue;

    public DataViewModel(){
        clickedValue = new MediatorLiveData<String>();
        clickedValue.setValue("contact");
    }

    public String getClickedValue(){
        return clickedValue.getValue();
    }
    public MutableLiveData<String> getClicked() {
        return clickedValue;
    }
    public void setClickedValue(String value){
        clickedValue.setValue(value);
    }

}
