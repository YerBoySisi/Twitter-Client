package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


private const val TAG = "ComposeActivity"
private const val CHAR_LIMIT = 280

class ComposeActivity : AppCompatActivity() {

    private lateinit var tweetField: EditText
    private lateinit var tweetBtn: Button
    private lateinit var characterCountDisplay: TextView

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        tweetField = findViewById(R.id.field_tweet)
        tweetBtn = findViewById(R.id.btn_tweet)
        characterCountDisplay = findViewById(R.id.char_count)

        client = TwitterApplication.getRestClient(this)

        tweetBtn.setOnClickListener {

            // grab content of tweet text field
            val tweetContent = tweetField.text.toString()

            // check if tweet is empty
            if(tweetContent.isEmpty()) {
                Toast.makeText(this, "Your Tweet cannot be empty", Toast.LENGTH_SHORT).show()
            }

            // check if tweet is within character count
            if(tweetContent.length > 280) {
                Toast.makeText(
                    this, "Your Tweet should not exceed 280 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully published tweet: $tweetContent")
                        Toast.makeText(
                            baseContext, "Your Tweet was successfully posted",
                            Toast.LENGTH_SHORT
                        ).show()

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.i(TAG, "Failed to publish Tweet", throwable)
                    }

                })
            }

        }

        tweetField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                val tweetLength = tweetField.length()
                characterCountDisplay.text = "$tweetLength / $CHAR_LIMIT"
            }
        })

    }
}