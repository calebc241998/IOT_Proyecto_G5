package com.example.proyecto_g5.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BienvenidoNombreViewModel extends ViewModel {
    private final MutableLiveData<String> bienvenido = new MutableLiveData<>();
    public MutableLiveData<String> getBienvenido() {return bienvenido;}
}
