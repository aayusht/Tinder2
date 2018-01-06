package com.aayush.tinder2

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.json.JSONArray
import ru.noties.markwon.Markwon
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class MainActivity : AppCompatActivity() {
    val url = URL("https://s3-us-west-2.amazonaws.com/udacity-mobile-interview/CardData.json")
    lateinit var json : JSONArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        object : AsyncTask<Unit, Unit, Unit>() { /*Your code(e.g. doInBackground )*/
            override fun doInBackground(vararg p0: Unit?) {
                val conn = url.openConnection() as HttpsURLConnection
                conn.requestMethod = "GET"
                val inputStream = BufferedInputStream(conn.inputStream)
                json = JSONArray(convertStreamToString(inputStream))
                Log.d("json output", json.toString())
            }

            override fun onPostExecute(result: Unit?) {
                findViewById<ViewPager>(R.id.viewPager).adapter = CardPagerAdapter()
            }
        }.execute()

//        val conn = url.openConnection() as HttpsURLConnection
//        conn.requestMethod = "GET"
//        val inputStream = BufferedInputStream(conn.inputStream)
//        val json = JSONObject(convertStreamToString(inputStream))
//        Log.d("json output", json.toString())
//
//        findViewById<ViewPager>(R.id.viewPager).adapter = CardPagerAdapter()
    }

    //Credit to https://stackoverflow.com/a/5445161
    fun convertStreamToString(`is`: java.io.InputStream): String {
        val s = java.util.Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    fun ISO8601toString(iso8601: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        return SimpleDateFormat("MMM yyyy").format(df.parse(iso8601))
    }

    inner class CardPagerAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return json.length()
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val layout = LayoutInflater.from(this@MainActivity).inflate(R.layout.person_card, container, false)
            val data = json.getJSONObject(position)
            layout.findViewById<TextView>(R.id.name).text = "${data.getString("firstName")} ${data.getString("lastName")}"
            layout.findViewById<TextView>(R.id.email).text = "Contact at ${data.getString("email")}"
            layout.findViewById<TextView>(R.id.company).text = "Working at ${data.getString("company")} since ${ISO8601toString(data.getString("startDate"))}"
            Markwon.setMarkdown(layout.findViewById<TextView>(R.id.body), data.getString("bio"))
            container?.addView(layout)
            val imageView = layout.findViewById<ImageView>(R.id.avatar)
            Glide.with(this@MainActivity).load(data.getString("avatar").replace("\\\\", "")).into(imageView)

            return layout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

    }
}
