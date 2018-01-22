package org.dotlearn.lrncurriculum.fetcher

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

internal abstract class RemoteCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(response.isSuccessful) {
            val body = response.body()
            if (body == null) {
                onFailed(NullPointerException("ResponseBody is unexpectedly null"))
            } else {
                onSuccess(body)
            }
        }
        else {
            onFailed(HttpException(response))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailed(t)
    }

    abstract fun onSuccess(response: T)

    abstract fun onFailed(t: Throwable)

}