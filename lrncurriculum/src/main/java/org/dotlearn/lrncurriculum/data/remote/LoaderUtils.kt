package org.dotlearn.lrncurriculum.data.remote

import retrofit2.HttpException
import retrofit2.Response

internal object LoaderUtils {

    /**
     * Get the body of the response if the request was successful
     * @param response the response to get the body
     * @return The body of the response
     * @throws HttpException if the response was not successful
     */
    internal fun <T> getResponseBody(response: Response<T>): T {
        if(response.isSuccessful) {
            return response.body() ?: throw HttpException(response)
        }

        throw HttpException(response)
    }

}