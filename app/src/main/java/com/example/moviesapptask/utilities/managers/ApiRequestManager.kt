package com.example.moviesapptask.utilities.managers

import android.content.res.Resources
import android.util.Log
import com.google.gson.Gson
import com.example.moviesapptask.models.response.Message
import com.example.moviesapptask.utilities.ResponseResult
import kotlinx.coroutines.*
import okhttp3.Headers
import retrofit2.Response

interface ApiRequestManagerInterface {
    fun <T : Any> execute(
        request: suspend () -> Response<T>,
        onSuccess: ((T, Headers,Int) -> Unit)? = null,
        onFailure: ((Message, Int) -> Unit)? = null,
        finally: (() -> Unit)? = null
    ): Job
}

class ApiRequestManager(private val resources: Resources) : ApiRequestManagerInterface {

    override fun <T : Any> execute(
        request: suspend () -> Response<T>,
        onSuccess: ((T, Headers,Int) -> Unit)?,
        onFailure: ((Message,Int) -> Unit)?,
        finally: (() -> Unit)?
    ): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = request.invoke()
                val result = verifyResponse(response)

                withContext(Dispatchers.Main) {
                    when (result) {
                        is ResponseResult.Success -> onSuccess?.invoke(result.data, response.headers(),response.code())
                        is ResponseResult.Failure -> onFailure?.invoke(result.message,response.code())
                    }
                }
            } catch (e: Exception) {
                Log.e("hhh", e.message!!)
                withContext(Dispatchers.Main) {
                    onFailure?.invoke(Message("Some error happened"),0)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    finally?.invoke()
                }
            }
        }
    }

    private fun <T : Any> verifyResponse(response: Response<T>): ResponseResult<T> {
        return try {
            val isResponseSuccessful = response.isSuccessful
            val statusCode = response.code()
            val responseBody = response.body()
            val rawMessage = response.raw().message()
            val errorBodyMessage = response.errorBody()?.string()

            if (isResponseSuccessful) {
                if (response.code() == 204) {
                    @Suppress("UNCHECKED_CAST")
                    (ResponseResult.Success(rawMessage as T))
                }
                else {
                    ResponseResult.Success(responseBody!!)
                }
            } else {
                val message = Gson().fromJson(errorBodyMessage, Message::class.java)
                message.statusCode = statusCode
                ResponseResult.Failure(message)
            }
        } catch (ex: Exception) {
            ResponseResult.Failure(Message(ex.localizedMessage!!))
        }
    }
}