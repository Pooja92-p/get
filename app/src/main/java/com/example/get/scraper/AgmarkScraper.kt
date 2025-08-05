package com.example.get.scraper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.FormBody
import okhttp3.RequestBody
import org.jsoup.Jsoup
import java.io.IOException

class AgmarkScraper {

    private val client = OkHttpClient()

    suspend fun fetchTomatoMysore(): List<List<String>> = withContext(Dispatchers.IO) {
        val url = "https://agmarknet.gov.in/"

        // 1. GET for hidden fields
        val getRequest = Request.Builder().url(url).get().build()
        val getResponse = client.newCall(getRequest).execute()
        val html = getResponse.body?.string() ?: ""
        val doc = Jsoup.parse(html)

        val viewState = doc.select("input#__VIEWSTATE").attr("value")
        val eventValidation = doc.select("input#__EVENTVALIDATION").attr("value")
        val viewStateGen = doc.select("input#__VIEWSTATEGENERATOR").attr("value")

        // 2. POST for Tomato Mysore May → Aug
        val body: RequestBody = FormBody.Builder()
            .add("__VIEWSTATE", viewState)
            .add("__EVENTVALIDATION", eventValidation)
            .add("__VIEWSTATEGENERATOR", viewStateGen)
            .add("__EVENTTARGET", "")
            .add("__EVENTARGUMENT", "")
//
            .add("btnGetData", "Get Data")
            .build()
        val url1 =
            "https://www.agmarknet.gov.in/SearchCmmMkt.aspx?Tx_Commodity=78&Tx_State=KK&Tx_District=12&Tx_Market=123&DateFrom=21-Oct-2024&DateTo=04-Aug-2025&Fr_Date=21-Oct-2024&To_Date=04-Aug-2025&Tx_Trend=0&Tx_CommodityHead=Tomato&Tx_StateHead=Karnataka&Tx_DistrictHead=Mysore&Tx_MarketHead=Mysore+(Bandipalya)"
        val postRequest = Request.Builder().url(url1).post(body).build()
        val postResponse = client.newCall(postRequest).execute()
        val postHtml = postResponse.body?.string() ?: ""
        val postDoc = Jsoup.parse(postHtml)

        val table = postDoc.select("table#cphBody_GridPriceData").first()
        val rows = mutableListOf<List<String>>()

        if (table != null) {
            val rowElements = table.select("tr").drop(1) // skip header
            for (row in rowElements) {
                val cols = row.select("td span").map { it.text().trim() }
                rows.add(cols)
            }
        } else {
            println("No table found! HTML length: ${postHtml.length}")
        }

        return@withContext rows
    }
}