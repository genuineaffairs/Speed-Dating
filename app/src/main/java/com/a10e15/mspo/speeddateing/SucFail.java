package com.a10e15.mspo.speeddateing;

/**
 * Created by justking100 on 2/15/2018.
 */

public abstract class  SucFail {
    public abstract void onSuccess(Object ret);
    public abstract void onFailure(Exception ret);
}
