package com.yogas.api.mv

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.yogas.api.model.Users
import com.yogas.api.remote.RetrofitClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<Users>>()
    var statusError = MutableLiveData<String?>()
    private val baseURL = "https://api.github.com/"
    private val authToken = "f5767d3e81fb3d94d634b9d3e8baa666c5190d4e"

    fun setListUsers(username: String) {
        RetrofitClient(baseURL).instanceListUsers.getListUsers(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonObject = JSONObject(response.body().toString())
                                val jsonArrayItem: JSONArray = jsonObject.getJSONArray("items")

                                val list = arrayListOf<Users>()
                                for (i in 0 until jsonArrayItem.length()) {
                                    val dataJSON = jsonArrayItem.getJSONObject(i)
                                    val user = Users()
                                    user.id = dataJSON.getInt("id")
                                    user.login = dataJSON.getString("login")
                                    user.avatar_url = dataJSON.getString("avatar_url")
                                    user.html_url = dataJSON.getString("html_url")
                                    list.add(user)
                                }
                                listUsers.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception", e.message.toString())
                                statusError.value = "Exception - " + e.message.toString()
                            }
                        } else {
                            Log.d("response.isFailed", "Response Not Successful")
                            statusError.value = "response.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure", t.message.toString())
                        statusError.value = "onFailure - " + t.message.toString()
                    }
                })
    }

    fun getListUsers(): LiveData<ArrayList<Users>> {
        return listUsers
    }
}