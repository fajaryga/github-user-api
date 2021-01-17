package com.yogas.api.mv

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.yogas.api.model.DetailUser
import com.yogas.api.model.Users
import com.yogas.api.remote.RetrofitClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class DetailViewModel : ViewModel() {

    val detailUser = MutableLiveData<DetailUser>()
    val listFollowers = MutableLiveData<ArrayList<Users>>()
    val listFollowing = MutableLiveData<ArrayList<Users>>()
    var statusError = MutableLiveData<String?>()
    private val baseURL = "https://api.github.com/"
    private val authToken = "f5767d3e81fb3d94d634b9d3e8baa666c5190d4e"

    fun setDetailUser(username: String) {
        RetrofitClient(baseURL).instanceDetailUser.getDetailUser(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val dataJSON = JSONObject(response.body().toString())
                                val userDetail = DetailUser()
                                userDetail.id = dataJSON.getInt("id")
                                userDetail.login = dataJSON.getString("login")
                                userDetail.avatar_url = dataJSON.getString("avatar_url")
                                userDetail.html_url = dataJSON.getString("html_url")
                                userDetail.name = dataJSON.getString("name")
                                userDetail.followers = dataJSON.getInt("followers")
                                userDetail.following = dataJSON.getInt("following")
                                detailUser.postValue(userDetail)
                            } catch (e: JSONException) {
                                Log.d("Exception (Detail)", e.message.toString())
                                statusError.value = "Exception (Detail) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseDetail.isFailed", "Response Not Successful")
                            statusError.value = "responseDetail.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Detail)", t.message.toString())
                        statusError.value = "onFailure (Detail) - " + t.message.toString()
                    }
                })
    }

    fun setFollowerUser(username: String) {
        RetrofitClient(baseURL).instanceFollowers.getFollowers(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
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
                                listFollowers.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Followers)", e.message.toString())
                                statusError.value =
                                    "Exception (Followers) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseFlwers.isFailed", "Response Not Successful")
                            statusError.value = "responseFlwers.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Followers)", t.message.toString())
                        statusError.value = "onFailure (Followers) - " + t.message.toString()
                    }
                })
    }

    fun setFollowingUser(username: String) {
        RetrofitClient(baseURL).instanceFollowing.getFollowing(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
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
                                listFollowing.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Following)", e.message.toString())
                                statusError.value =
                                    "Exception (Following) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseFlwing.isFailed", "Response Not Successful")
                            statusError.value = "responseFlwing.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Following)", t.message.toString())
                        statusError.value = "onFailure (Following) - " + t.message.toString()
                    }
                })
    }


    fun getDetailUser(): LiveData<DetailUser> {
        return detailUser
    }

    fun getFollowerUser(): LiveData<ArrayList<Users>> {
        return listFollowers
    }

    fun getFollowingUser(): LiveData<ArrayList<Users>> {
        return listFollowing
    }


}